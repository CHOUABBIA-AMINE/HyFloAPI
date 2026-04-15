package dz.sh.trc.hyflo.platform.ai.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Owns the canonical mapping of session ID → session owner (userId).
 *
 * <p>This is the only place where new session IDs may be created.
 * No code outside of this class calls {@code UUID.randomUUID()} for sessions.
 * Controllers, use-cases, and agent executors all receive session IDs
 * from this registry — they never generate them directly.</p>
 *
 * <h3>Session lifecycle:</h3>
 * <ol>
 *   <li>{@link #createSession(String)} — called when the user starts a new
 *       conversation. Returns a new session ID bound to the userId.</li>
 *   <li>{@link #extendSession(String)} — called on every successful turn to
 *       push the session's last-active timestamp forward.</li>
 *   <li>Session expires when idle for more than {@code hyflo.ai.memory.session-ttl-minutes}
 *       (default 60). The {@link #evictExpiredSessions()} scheduler runs every
 *       10 minutes and removes expired entries, then calls
 *       {@link ConversationMemory#evict(String)}.</li>
 *   <li>{@link #closeSession(String, String)} — explicitly closes and evicts a session.
 *       Called when the user ends the conversation or logs out.</li>
 * </ol>
 *
 * <h3>Session ownership:</h3>
 * <p>Each session is bound to exactly one {@code userId} at creation time.
 * The binding cannot be changed. {@link #ownerOf(String)} is the single
 * source of truth used by {@link SessionIsolationEnforcer}.</p>
 *
 * <h3>Design rule:</h3>
 * <p>This class does NOT perform authorization checks — it only manages
 * session lifecycle and ownership lookup. Authorization (whether a user
 * may read data X) is the security layer's responsibility.</p>
 */
@Component
@ConfigurationProperties(prefix = "hyflo.ai.memory")
public class SessionRegistry {

    private static final Logger log = LoggerFactory.getLogger(SessionRegistry.class);

    // -------------------------------------------------------------------------
    // Session entry
    // -------------------------------------------------------------------------

    private static final class SessionEntry {
        final String  sessionId;
        final String  userId;
        final Instant createdAt;
        volatile Instant lastActiveAt;

        SessionEntry(String sessionId, String userId) {
            this.sessionId    = sessionId;
            this.userId       = userId;
            this.createdAt    = Instant.now();
            this.lastActiveAt = this.createdAt;
        }
    }

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private final ConcurrentHashMap<String, SessionEntry> sessions =
            new ConcurrentHashMap<>();

    private final ConversationMemory memory;

    /** Maximum idle duration before a session is considered expired. */
    private int sessionTtlMinutes = 60;

    public SessionRegistry(ConversationMemory memory) {
        this.memory = Objects.requireNonNull(memory, "ConversationMemory");
    }

    // -------------------------------------------------------------------------
    // Lifecycle API
    // -------------------------------------------------------------------------

    /**
     * Creates a new conversation session for the given user.
     *
     * @param userId the authenticated user ID (must not be null or blank)
     * @return the new session ID
     * @throws IllegalArgumentException if userId is null or blank
     */
    public String createSession(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be null or blank");
        }

        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, new SessionEntry(sessionId, userId));

        log.info("[SessionRegistry] created sessionId={} userId={}", sessionId, userId);
        return sessionId;
    }

    /**
     * Updates the last-active timestamp of the given session to now.
     * Called after every successful agent turn.
     *
     * @param sessionId the session to extend
     */
    public void extendSession(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId");

        SessionEntry entry = sessions.get(sessionId);
        if (entry != null) {
            entry.lastActiveAt = Instant.now();
            log.debug("[SessionRegistry] extended sessionId={}", sessionId);
        }
    }

    /**
     * Explicitly closes and evicts a session.
     * The session must be owned by the given userId — closing another user's
     * session is silently rejected (logged as WARN).
     *
     * @param sessionId the session to close
     * @param userId    the requesting user
     */
    public void closeSession(String sessionId, String userId) {
        Objects.requireNonNull(sessionId, "sessionId");
        Objects.requireNonNull(userId,    "userId");

        SessionEntry entry = sessions.get(sessionId);
        if (entry == null) {
            log.debug("[SessionRegistry] closeSession: session not found sessionId={}", sessionId);
            return;
        }
        if (!entry.userId.equals(userId)) {
            log.warn("[SessionRegistry] closeSession: userId='{}' attempted to close session "
                    + "owned by '{}' sessionId={}", userId, entry.userId, sessionId);
            return;
        }

        sessions.remove(sessionId);
        memory.evict(sessionId);
        log.info("[SessionRegistry] closed sessionId={} userId={}", sessionId, userId);
    }

    // -------------------------------------------------------------------------
    // Lookup API (used by SessionIsolationEnforcer)
    // -------------------------------------------------------------------------

    /**
     * Returns the userId that owns the given session.
     * Returns {@link Optional#empty()} if the session does not exist or is expired.
     *
     * @param sessionId the session ID
     * @return the owner's userId, or empty
     */
    public Optional<String> ownerOf(String sessionId) {
        if (sessionId == null) return Optional.empty();

        SessionEntry entry = sessions.get(sessionId);
        if (entry == null) return Optional.empty();
        if (isExpired(entry)) {
            evictEntry(sessionId, entry);
            return Optional.empty();
        }
        return Optional.of(entry.userId);
    }

    /**
     * Returns true if the session exists and has not expired.
     *
     * @param sessionId the session ID
     * @return true if alive
     */
    public boolean isAlive(String sessionId) {
        if (sessionId == null) return false;

        SessionEntry entry = sessions.get(sessionId);
        if (entry == null) return false;
        if (isExpired(entry)) {
            evictEntry(sessionId, entry);
            return false;
        }
        return true;
    }

    /**
     * Returns the number of active sessions owned by the given userId.
     * Useful for rate-limiting session creation.
     *
     * @param userId the user ID
     * @return the count of live sessions
     */
    public long countSessionsFor(String userId) {
        Objects.requireNonNull(userId, "userId");
        return sessions.values().stream()
                .filter(e -> userId.equals(e.userId) && !isExpired(e))
                .count();
    }

    /**
     * Returns all session IDs currently owned by the given userId.
     *
     * @param userId the user ID
     * @return a snapshot set of session IDs
     */
    public Set<String> sessionsFor(String userId) {
        Objects.requireNonNull(userId, "userId");
        return sessions.entrySet().stream()
                .filter(e -> userId.equals(e.getValue().userId) && !isExpired(e.getValue()))
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    // -------------------------------------------------------------------------
    // Scheduled eviction
    // -------------------------------------------------------------------------

    /**
     * Evicts all expired sessions every 10 minutes.
     * This is a background task; it does not affect correctness of isolation
     * (expired sessions are checked inline in {@link #ownerOf(String)}).
     */
    @Scheduled(fixedDelayString = "PT10M")
    public void evictExpiredSessions() {
        long before = sessions.size();

        sessions.entrySet().removeIf(e -> {
            if (isExpired(e.getValue())) {
                memory.evict(e.getKey());
                log.debug("[SessionRegistry] evicted expired sessionId={} userId={}",
                        e.getKey(), e.getValue().userId);
                return true;
            }
            return false;
        });

        long evicted = before - sessions.size();
        if (evicted > 0) {
            log.info("[SessionRegistry] scheduled eviction: removed {} expired session(s). "
                    + "Active sessions: {}", evicted, sessions.size());
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private boolean isExpired(SessionEntry entry) {
        return Duration.between(entry.lastActiveAt, Instant.now())
                .toMinutes() >= sessionTtlMinutes;
    }

    private void evictEntry(String sessionId, SessionEntry entry) {
        sessions.remove(sessionId);
        memory.evict(sessionId);
        log.info("[SessionRegistry] lazily evicted expired session sessionId={} userId={}",
                sessionId, entry.userId);
    }

    // -------------------------------------------------------------------------
    // @ConfigurationProperties setters
    // -------------------------------------------------------------------------

    public int  getSessionTtlMinutes()       { return sessionTtlMinutes; }
    public void setSessionTtlMinutes(int v)  { this.sessionTtlMinutes = v; }
}