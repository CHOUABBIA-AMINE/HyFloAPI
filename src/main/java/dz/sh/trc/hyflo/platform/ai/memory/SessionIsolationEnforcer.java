package dz.sh.trc.hyflo.platform.ai.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * The single authoritative gate for all conversation memory access.
 *
 * <h3>Contract:</h3>
 * <p>Every read or write to {@link ConversationMemory} MUST be preceded
 * by a successful call to {@link #enforce(String, String, String)}.
 * Any call site that accesses memory without going through this enforcer
 * is a correctness bug and must be rejected in code review.</p>
 *
 * <h3>Fail-closed guarantee:</h3>
 * <p>This enforcer always fails closed — on any ambiguity, unknown session,
 * expired session, or ownership mismatch, it throws
 * {@link SessionIsolationException}. There is no "graceful" fallback that
 * silently allows access.</p>
 *
 * <h3>What this class enforces:</h3>
 * <ol>
 *   <li>Session ID is not null or blank.</li>
 *   <li>Session exists in {@link SessionRegistry} (not expired).</li>
 *   <li>The requesting {@code userId} matches the session's registered owner.</li>
 * </ol>
 *
 * <h3>What this class does NOT enforce:</h3>
 * <ul>
 *   <li>Whether the userId is allowed to access a specific business entity
 *       — that is the security layer ({@code @PreAuthorize}).</li>
 *   <li>Whether the session has exceeded a message quota — that is
 *       {@link dz.sh.trc.hyflo.platform.ai.resilience.ToolExecutionGuard}.</li>
 * </ul>
 *
 * <h3>Stateless design:</h3>
 * <p>This component holds no state of its own. It delegates all session
 * lookups to {@link SessionRegistry}. It is therefore thread-safe,
 * reusable across concurrent requests, and trivially testable.</p>
 *
 * <h3>Usage pattern:</h3>
 * <pre>{@code
 * // In AgentExecutor or use-case:
 * isolationEnforcer.enforce(sessionId, userId, correlationId);
 * // ← throws SessionIsolationException if access is denied
 *
 * List<ConversationMessage> history = memory.getHistory(sessionId);
 * // safe to read/write now
 * }</pre>
 */
@Component
public class SessionIsolationEnforcer {

    private static final Logger log = LoggerFactory.getLogger(SessionIsolationEnforcer.class);

    private final SessionRegistry sessionRegistry;

    public SessionIsolationEnforcer(SessionRegistry sessionRegistry) {
        this.sessionRegistry = Objects.requireNonNull(sessionRegistry, "SessionRegistry");
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Enforces session isolation: verifies that the given session exists,
     * is not expired, and is owned by the given userId.
     *
     * <p>This method is the single gate. Call it before every memory operation.</p>
     *
     * @param sessionId     the session ID being accessed
     * @param userId        the requesting user's ID (from authentication context)
     * @param correlationId the current request correlation ID (for audit logging)
     * @throws SessionIsolationException if access is denied for any reason
     */
    public void enforce(String sessionId, String userId, String correlationId) {
        Objects.requireNonNull(userId, "userId must not be null");

        // Check 1: session ID is not null/blank
        if (sessionId == null || sessionId.isBlank()) {
            log.warn("[SessionIsolationEnforcer] NULL/blank sessionId userId='{}' "
                    + "correlationId={}", userId, correlationId);
            throw new SessionIsolationException(
                    SessionIsolationException.ViolationType.NULL_SESSION_ID,
                    sessionId, userId, correlationId);
        }

        // Check 2 + 3: session existence and ownership (combined: ownerOf is fail-closed)
        String registeredOwner = sessionRegistry.ownerOf(sessionId)
                .orElseThrow(() -> {
                    log.warn("[SessionIsolationEnforcer] session not found sessionId='{}' "
                            + "userId='{}' correlationId={}", sessionId, userId, correlationId);
                    return new SessionIsolationException(
                            SessionIsolationException.ViolationType.SESSION_NOT_FOUND,
                            sessionId, userId, correlationId);
                });

        if (!registeredOwner.equals(userId)) {
            log.warn("[SessionIsolationEnforcer] OWNERSHIP MISMATCH sessionId='{}' "
                    + "registeredOwner='{}' requestingUserId='{}' correlationId={}",
                    sessionId, registeredOwner, userId, correlationId);
            throw new SessionIsolationException(
                    SessionIsolationException.ViolationType.OWNERSHIP_MISMATCH,
                    sessionId, userId, correlationId);
        }

        log.debug("[SessionIsolationEnforcer] access granted sessionId='{}' userId='{}' "
                + "correlationId={}", sessionId, userId, correlationId);
    }

    /**
     * Enforces session isolation for a stateless call (no session required).
     *
     * <p>When {@code sessionId} is null, the call is treated as a stateless
     * single-shot invocation and no isolation check is performed. This is
     * the correct behavior for flow scoring or crisis assessment calls
     * that do not maintain conversation state.</p>
     *
     * <p>When {@code sessionId} is non-null, this delegates to
     * {@link #enforce(String, String, String)} with full validation.</p>
     *
     * @param sessionId     null for stateless; non-null to enforce isolation
     * @param userId        the requesting user's ID
     * @param correlationId the current request correlation ID
     */
    public void enforceOrSkipIfStateless(String sessionId,
                                          String userId,
                                          String correlationId) {
        if (sessionId == null) {
            // Stateless call — no session to validate
            log.debug("[SessionIsolationEnforcer] stateless call, skipping isolation "
                    + "userId='{}' correlationId={}", userId, correlationId);
            return;
        }
        enforce(sessionId, userId, correlationId);
    }
}