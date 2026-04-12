package dz.sh.trc.hyflo.platform.ai.cost;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Mutable, thread-safe budget aggregate for a single userId.
 *
 * <p>Tracks three counters, all updated atomically:</p>
 * <ul>
 *   <li>{@link #dailyCommitted} — tokens committed (settled) today.</li>
 *   <li>{@link #dailyReserved}  — tokens currently reserved but not yet committed
 *       (in-flight LLM calls). Combined with {@link #dailyCommitted}, this gives
 *       the effective daily consumption: {@code committed + reserved}.</li>
 *   <li>{@link #monthlyCommitted} — tokens committed this calendar month.</li>
 * </ul>
 *
 * <h3>Reserve-then-commit protocol:</h3>
 * <pre>
 * 1. reserve(n)     → atomically increments dailyReserved by n
 *                     → returns false if it would exceed the daily limit
 * 2. commit(actual) → decrements dailyReserved by reserved amount
 *                     → increments dailyCommitted by actual
 *                     → increments monthlyCommitted by actual
 * 3. release()      → decrements dailyReserved by reserved amount (on LLM error)
 * </pre>
 *
 * <h3>Daily reset:</h3>
 * <p>{@link #resetIfNewDay()} is called by {@link TokenUsageTracker} on every
 * read. It checks whether the record's {@link #lastResetDate} is before today
 * (UTC) and atomically resets {@link #dailyCommitted} and {@link #dailyReserved}
 * if so. Monthly reset follows the same pattern on the first day of each month.</p>
 */
public final class UserBudgetRecord {

    private final String  userId;

    // Daily counters — reset at midnight UTC
    private final AtomicInteger dailyCommitted = new AtomicInteger(0);
    private final AtomicInteger dailyReserved  = new AtomicInteger(0);

    // Monthly counter — reset on the 1st of each month UTC
    private final AtomicLong    monthlyCommitted = new AtomicLong(0L);

    // For daily/monthly reset detection
    private volatile LocalDate  lastResetDate;
    private volatile int        lastResetMonth;   // 1-12

    public UserBudgetRecord(String userId) {
        this.userId         = Objects.requireNonNull(userId, "userId");
        LocalDate today     = LocalDate.now(ZoneOffset.UTC);
        this.lastResetDate  = today;
        this.lastResetMonth = today.getMonthValue();
    }

    // -------------------------------------------------------------------------
    // Reserve-then-commit
    // -------------------------------------------------------------------------

    /**
     * Attempts to reserve {@code tokens} against the daily limit.
     *
     * @param tokens     the number of tokens to reserve (must be > 0)
     * @param dailyLimit the applicable daily limit from {@link CostPolicy}
     * @return true if the reservation succeeded; false if it would exceed the limit
     */
    public boolean reserve(int tokens, int dailyLimit) {
        resetIfNewDay();
        if (tokens <= 0) throw new IllegalArgumentException("tokens must be > 0");

        // Spin-loop CAS to atomically reserve only if headroom exists
        while (true) {
            int current = dailyReserved.get();
            int committed = dailyCommitted.get();
            int projected = committed + current + tokens;

            if (projected > dailyLimit) {
                return false; // would exceed — deny reservation
            }

            if (dailyReserved.compareAndSet(current, current + tokens)) {
                return true;  // reservation secured
            }
            // Another thread changed dailyReserved between read and CAS — retry
        }
    }

    /**
     * Commits actual token usage after a successful LLM call.
     *
     * @param reserved the amount that was reserved for this call
     * @param actual   the actual {@link TokenUsage} returned by the model
     */
    public void commit(int reserved, TokenUsage actual) {
        Objects.requireNonNull(actual, "actual TokenUsage");

        dailyReserved .addAndGet(-reserved);
        dailyCommitted.addAndGet(actual.totalTokens());
        monthlyCommitted.addAndGet(actual.totalTokens());
    }

    /**
     * Releases a reservation without committing (e.g. on LLM call failure).
     *
     * @param reserved the amount that was reserved
     */
    public void release(int reserved) {
        dailyReserved.addAndGet(-reserved);
    }

    // -------------------------------------------------------------------------
    // Read accessors
    // -------------------------------------------------------------------------

    public String userId()              { resetIfNewDay(); return userId; }

    /** Effective daily consumption = committed + in-flight reserved. */
    public int effectiveDailyUsage()    { resetIfNewDay(); return dailyCommitted.get() + dailyReserved.get(); }
    public int dailyCommitted()         { resetIfNewDay(); return dailyCommitted.get(); }
    public int dailyReserved()          { resetIfNewDay(); return dailyReserved.get(); }
    public long monthlyCommitted()      { resetIfNewDay(); return monthlyCommitted.get(); }

    // -------------------------------------------------------------------------
    // Reset logic
    // -------------------------------------------------------------------------

    /**
     * Resets daily (and monthly if applicable) counters when a new UTC day begins.
     * Called on every read path — no external scheduler needed.
     */
    public void resetIfNewDay() {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);

        if (!today.equals(lastResetDate)) {
            synchronized (this) {
                if (!today.equals(lastResetDate)) {
                    dailyCommitted.set(0);
                    dailyReserved.set(0);
                    lastResetDate = today;

                    // Monthly reset on the 1st of each month
                    if (today.getMonthValue() != lastResetMonth) {
                        monthlyCommitted.set(0L);
                        lastResetMonth = today.getMonthValue();
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "UserBudgetRecord{userId=" + userId
                + ", dailyCommitted=" + dailyCommitted.get()
                + ", dailyReserved=" + dailyReserved.get()
                + ", monthlyCommitted=" + monthlyCommitted.get() + "}";
    }
}