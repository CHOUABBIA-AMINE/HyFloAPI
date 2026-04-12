package dz.sh.trc.hyflo.platform.ai.cost;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The single entry point for all token budget operations.
 *
 * <p>Orchestrates the full reserve-then-commit lifecycle using
 * {@link TokenUsageTracker}, {@link CostPolicy}, and
 * {@link BudgetExceededHandler}. Agent executors and use-case orchestrators
 * call only this class — they never touch the tracker or policy directly.</p>
 *
 * <h3>Call sequence in the agent executor:</h3>
 * <pre>{@code
 * // 1. Check budget before the LLM call
 * BudgetDecision decision = budgetManager.checkAndReserve(userId, role,
 *         estimatedTokens, correlationId);
 * // → ALLOW / WARN → proceed normally
 * // → ALLOW_DEGRADED → downgrade model/context before calling
 * // → throws BudgetExhaustedException if blockOnExceeded=true
 *
 * String reservationId = budgetManager.lastReservationId(userId);
 *
 * try {
 *     TokenUsage actual = callLlm(...);
 *     // 2. Settle after success
 *     budgetManager.commit(reservationId, userId, actual);
 * } catch (Exception e) {
 *     // 3. Release on failure
 *     budgetManager.release(reservationId, userId);
 *     throw e;
 * }
 * }</pre>
 *
 * <h3>Reservation tracking:</h3>
 * <p>Each {@link #checkAndReserve} call generates a UUID reservation ID
 * stored in an in-memory map keyed by reservation ID → (userId, amount).
 * The map is bounded: reservations older than 5 minutes are purged by
 * {@link #pruneStaleReservations()}, called on each commit/release.</p>
 *
 * <h3>Per-request cap:</h3>
 * <p>If {@code estimatedTokens} exceeds {@link CostPolicy#perRequestCap()},
 * {@link #checkAndReserve} caps the reservation at the max cap and logs a
 * warning — it does not throw. The actual usage is settled in full at commit time.</p>
 */
@Component
public class TokenBudgetManager {

    private static final Logger log = LoggerFactory.getLogger(TokenBudgetManager.class);

    /** Reservation entries expire after 5 minutes if never committed/released. */
    private static final long RESERVATION_TTL_MS = 5 * 60 * 1_000L;

    private final TokenUsageTracker   tracker;
    private final CostPolicy          policy;
    private final BudgetExceededHandler handler;

    // -------------------------------------------------------------------------
    // Reservation ledger
    // -------------------------------------------------------------------------

    private record Reservation(String userId, int amount, long createdAtMs) {}

    private final ConcurrentHashMap<String, Reservation> reservations =
            new ConcurrentHashMap<>();

    public TokenBudgetManager(TokenUsageTracker tracker,
                               CostPolicy policy,
                               BudgetExceededHandler handler) {
        this.tracker  = Objects.requireNonNull(tracker,  "TokenUsageTracker");
        this.policy   = Objects.requireNonNull(policy,   "CostPolicy");
        this.handler  = Objects.requireNonNull(handler,  "BudgetExceededHandler");
    }

    // -------------------------------------------------------------------------
    // Primary API
    // -------------------------------------------------------------------------

    /**
     * Checks budget health, enforces the per-request cap, and atomically reserves
     * tokens for the upcoming LLM call.
     *
     * <p>Returns a reservation ID that must be passed to {@link #commit} or
     * {@link #release} after the LLM call resolves.</p>
     *
     * @param userId          the authenticated user ID
     * @param roleName        the user's primary role (for limit resolution)
     * @param estimatedTokens the expected total tokens for this call
     * @param correlationId   for log and audit tracing
     * @return a {@link ReservationResult} carrying the reservation ID and
     *         the {@link BudgetExceededHandler.BudgetDecision}
     * @throws BudgetExhaustedException if the budget is exceeded and
     *         {@link CostPolicy#isBlockOnExceeded()} is true
     */
    public ReservationResult checkAndReserve(String userId,
                                              String roleName,
                                              int estimatedTokens,
                                              String correlationId) {
        Objects.requireNonNull(userId, "userId");
        if (estimatedTokens <= 0) throw new IllegalArgumentException("estimatedTokens must be > 0");

        UserBudgetRecord record = tracker.getOrCreate(userId);
        int dailyLimit = policy.resolveDailyLimit(userId, roleName);

        // Step 1: Check budget health (may throw BudgetExhaustedException)
        BudgetExceededHandler.BudgetDecision decision =
                handler.handle(record, dailyLimit, correlationId);

        // Step 2: Cap the reservation at the per-request maximum
        int toReserve = estimatedTokens;
        if (!handler.isWithinPerRequestCap(estimatedTokens, correlationId)) {
            toReserve = policy.perRequestCap();
            log.warn("[TokenBudgetManager] capped reservation userId='{}' requested={} cap={}",
                    userId, estimatedTokens, toReserve);
        }

        // Step 3: Atomically reserve
        boolean reserved = record.reserve(toReserve, dailyLimit);
        if (!reserved) {
            // Race condition: budget was fine at step 1 but filled up by concurrent request
            log.warn("[TokenBudgetManager] reservation denied (concurrent fill) userId='{}' "
                    + "correlationId={}", userId, correlationId);
            if (policy.isBlockOnExceeded()) {
                throw new BudgetExhaustedException(
                        userId, record.effectiveDailyUsage(), dailyLimit, correlationId);
            }
            // Degrade: return a zero-reservation result
            return new ReservationResult(null, 0,
                    BudgetExceededHandler.BudgetDecision.ALLOW_DEGRADED);
        }

        // Step 4: Register reservation in ledger
        String reservationId = UUID.randomUUID().toString();
        reservations.put(reservationId,
                new Reservation(userId, toReserve, System.currentTimeMillis()));

        log.debug("[TokenBudgetManager] reserved reservationId={} userId='{}' amount={} "
                + "decision={} correlationId={}",
                reservationId, userId, toReserve, decision, correlationId);

        return new ReservationResult(reservationId, toReserve, decision);
    }

    /**
     * Settles actual token usage after a successful LLM call.
     *
     * @param reservationId the ID returned by {@link #checkAndReserve}
     * @param userId        the authenticated user ID (verified against reservation)
     * @param actual        the actual token usage from the model response
     */
    public void commit(String reservationId, String userId, TokenUsage actual) {
        Objects.requireNonNull(actual, "actual TokenUsage");

        Reservation res = removeAndValidateReservation(reservationId, userId, "commit");
        if (res == null) return; // null reservation = degraded call with no reservation

        tracker.recordUsage(userId, res.amount(), actual);
        pruneStaleReservations();

        log.info("[TokenBudgetManager] committed reservationId={} userId='{}' "
                + "reserved={} actual={}", reservationId, userId, res.amount(), actual);
    }

    /**
     * Releases a reservation without committing usage (called on LLM failure).
     *
     * @param reservationId the ID returned by {@link #checkAndReserve}
     * @param userId        the authenticated user ID
     */
    public void release(String reservationId, String userId) {
        Reservation res = removeAndValidateReservation(reservationId, userId, "release");
        if (res == null) return;

        tracker.releaseReservation(userId, res.amount());
        pruneStaleReservations();

        log.debug("[TokenBudgetManager] released reservationId={} userId='{}' amount={}",
                reservationId, userId, res.amount());
    }

    /**
     * Convenience read: returns the current effective daily usage for a user.
     * For observability and operator queries only — not for budget decisions.
     *
     * @param userId the user ID
     * @return effective daily token usage (0 if never tracked)
     */
    public int dailyUsage(String userId) {
        Objects.requireNonNull(userId, "userId");
        UserBudgetRecord record = tracker.snapshotFor(userId);
        return record == null ? 0 : record.effectiveDailyUsage();
    }

    // -------------------------------------------------------------------------
    // Reservation result
    // -------------------------------------------------------------------------

    /**
     * Carries the output of a {@link #checkAndReserve} call.
     *
     * @param reservationId the reservation ID (null if degraded with no reservation)
     * @param reservedAmount the number of tokens reserved (0 if degraded)
     * @param decision      the budget decision
     */
    public record ReservationResult(
            String reservationId,
            int    reservedAmount,
            BudgetExceededHandler.BudgetDecision decision
    ) {
        public boolean isDegraded() {
            return decision == BudgetExceededHandler.BudgetDecision.ALLOW_DEGRADED;
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Reservation removeAndValidateReservation(String reservationId,
                                                      String userId,
                                                      String operation) {
        if (reservationId == null) return null; // degraded call

        Reservation res = reservations.remove(reservationId);
        if (res == null) {
            log.warn("[TokenBudgetManager] {} called with unknown reservationId={} userId='{}'",
                    operation, reservationId, userId);
            return null;
        }
        if (!res.userId().equals(userId)) {
            log.error("[TokenBudgetManager] {} userId mismatch: reservationId={} "
                    + "expected='{}' actual='{}'", operation, reservationId, res.userId(), userId);
            // Return to tracker to avoid leak
            reservations.put(reservationId, res);
            return null;
        }
        return res;
    }

    private void pruneStaleReservations() {
        long now = System.currentTimeMillis();
        reservations.entrySet().removeIf(e -> {
            if (now - e.getValue().createdAtMs() > RESERVATION_TTL_MS) {
                log.warn("[TokenBudgetManager] pruning stale reservation id={} userId='{}'",
                        e.getKey(), e.getValue().userId());
                tracker.releaseReservation(e.getValue().userId(), e.getValue().amount());
                return true;
            }
            return false;
        });
    }
}