package dz.sh.trc.hyflo.platform.ai.cost;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory token usage tracker — active by default.
 *
 * <p>Manages one {@link UserBudgetRecord} per userId in a
 * {@link ConcurrentHashMap}. Records are created lazily on first access
 * and survive the lifetime of the JVM process.</p>
 *
 * <h3>Persistence note:</h3>
 * <p>Usage data is lost on JVM restart in this implementation. A JDBC-backed
 * implementation can replace it by declaring a bean of the same type. When
 * {@code hyflo.ai.cost.jdbc-tracking.enabled=true}, a {@code JdbcTokenUsageTracker}
 * (see stub at end of class) activates via {@code @ConditionalOnProperty} and this
 * bean does not register.</p>
 *
 * <h3>Thread safety:</h3>
 * <p>{@link ConcurrentHashMap#computeIfAbsent} guarantees exactly-once record
 * creation. Each {@link UserBudgetRecord} uses atomic operations internally.</p>
 *
 * <h3>How to use:</h3>
 * <p>All calls go through {@link TokenBudgetManager}. This tracker is never
 * called directly by agent or use-case code.</p>
 */
@Component
@ConditionalOnMissingBean(name = "jdbcTokenUsageTracker")
public class TokenUsageTracker {

    private static final Logger log = LoggerFactory.getLogger(TokenUsageTracker.class);

    private final ConcurrentHashMap<String, UserBudgetRecord> records =
            new ConcurrentHashMap<>();

    // -------------------------------------------------------------------------
    // Public API (called only by TokenBudgetManager)
    // -------------------------------------------------------------------------

    /**
     * Returns the budget record for the given userId,
     * creating it lazily if it does not yet exist.
     *
     * @param userId the authenticated user ID
     * @return the user's current budget record
     */
    public UserBudgetRecord getOrCreate(String userId) {
        Objects.requireNonNull(userId, "userId");
        return records.computeIfAbsent(userId, UserBudgetRecord::new);
    }

    /**
     * Records committed token usage after a successful LLM call.
     *
     * <p>This is the settlement step of the reserve-then-commit protocol.
     * The reservation is released and the actual usage is committed atomically
     * inside {@link UserBudgetRecord#commit}.</p>
     *
     * @param userId   the authenticated user ID
     * @param reserved the number of tokens that were reserved before the call
     * @param actual   the actual token usage returned by the model
     */
    public void recordUsage(String userId, int reserved, TokenUsage actual) {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(actual, "actual");

        UserBudgetRecord record = getOrCreate(userId);
        record.commit(reserved, actual);

        log.info("[TokenUsageTracker] committed userId='{}' reserved={} actual={} daily={}",
                userId, reserved, actual.totalTokens(), record.dailyCommitted());
    }

    /**
     * Releases a reservation without committing usage (on LLM call failure).
     *
     * @param userId   the authenticated user ID
     * @param reserved the number of tokens that were reserved
     */
    public void releaseReservation(String userId, int reserved) {
        Objects.requireNonNull(userId, "userId");

        UserBudgetRecord record = records.get(userId);
        if (record != null) {
            record.release(reserved);
            log.debug("[TokenUsageTracker] released reservation userId='{}' reserved={}",
                    userId, reserved);
        }
    }

    /**
     * Returns a snapshot of all tracked users' budget records.
     * For operator dashboard / admin inspection only.
     *
     * @return an unmodifiable list of all current budget records
     */
    public List<UserBudgetRecord> snapshot() {
        return List.copyOf(records.values());
    }

    /**
     * Returns the budget record for a specific user, or null if not found.
     * For operator dashboard use.
     *
     * @param userId the user ID to look up
     * @return the budget record, or null
     */
    public UserBudgetRecord snapshotFor(String userId) {
        return records.get(userId);
    }
}