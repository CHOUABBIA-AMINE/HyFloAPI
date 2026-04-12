package dz.sh.trc.hyflo.platform.ai.cost;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Decides what to do when a user's token budget is exhausted or approaching its limit.
 *
 * <h3>Two modes (configured via {@link CostPolicy#isBlockOnExceeded()}):</h3>
 * <ul>
 *   <li>{@code blockOnExceeded=false} (default) — returns
 *       {@link BudgetDecision#ALLOW_DEGRADED}. The agent executor is expected to
 *       downgrade the request: use a shorter context window, pick a cheaper model
 *       via {@code FallbackModelResolver}, or truncate history. The user sees a
 *       degraded but functional response.</li>
 *   <li>{@code blockOnExceeded=true} — throws {@link BudgetExhaustedException},
 *       which propagates to the API layer as a 429 response.</li>
 * </ul>
 *
 * <h3>Warn-threshold handling:</h3>
 * <p>When usage is above the warn threshold but below the daily limit,
 * {@link #handle} returns {@link BudgetDecision#WARN}. The agent executor
 * continues normally but logs a structured warning for operator dashboards.</p>
 *
 * <h3>Separate from guardrails:</h3>
 * <p>This handler is not part of the {@code guardrail} package. Budget
 * enforcement is an infrastructure concern (rate limiting), not an AI safety
 * concern. The guardrail chain checks <em>content</em>; this handler checks
 * <em>capacity</em>.</p>
 */
@Component
public class BudgetExceededHandler {

    private static final Logger log = LoggerFactory.getLogger(BudgetExceededHandler.class);

    /**
     * Decision returned by {@link #handle}, telling the agent executor
     * how to proceed.
     */
    public enum BudgetDecision {
        /** Budget is healthy — proceed normally. */
        ALLOW,
        /** Budget is approaching the warn threshold — proceed but log a structured warning. */
        WARN,
        /** Budget is exceeded but blocking is disabled — proceed with degraded settings. */
        ALLOW_DEGRADED,
        /** Budget is exceeded and blocking is enabled — this decision is never returned;
         *  {@link BudgetExhaustedException} is thrown instead. */
        BLOCK
    }

    private final CostPolicy policy;

    public BudgetExceededHandler(CostPolicy policy) {
        this.policy = Objects.requireNonNull(policy, "CostPolicy");
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Evaluates the current budget state for a user and returns a
     * {@link BudgetDecision} telling the executor how to proceed.
     *
     * <p>This method is called <em>before</em> the reservation step.
     * It uses the current effective daily usage (committed + reserved) to decide.</p>
     *
     * @param record         the user's current budget record
     * @param dailyLimit     the applicable daily limit from {@link CostPolicy}
     * @param correlationId  for log tracing
     * @return the decision (never null)
     * @throws BudgetExhaustedException if budget is exceeded and
     *         {@link CostPolicy#isBlockOnExceeded()} is true
     */
    public BudgetDecision handle(UserBudgetRecord record,
                                  int dailyLimit,
                                  String correlationId) {
        Objects.requireNonNull(record, "UserBudgetRecord");

        int effective = record.effectiveDailyUsage();
        String userId = record.userId();

        // Case 1: Budget exceeded
        if (effective >= dailyLimit) {
            if (policy.isBlockOnExceeded()) {
                log.warn("[BudgetExceededHandler] BLOCK userId='{}' effective={} limit={} "
                        + "correlationId={}", userId, effective, dailyLimit, correlationId);
                throw new BudgetExhaustedException(userId, effective, dailyLimit, correlationId);
            }

            log.warn("[BudgetExceededHandler] DEGRADED userId='{}' effective={} limit={} "
                    + "correlationId={}", userId, effective, dailyLimit, correlationId);
            return BudgetDecision.ALLOW_DEGRADED;
        }

        // Case 2: Above warn threshold
        double usageFraction = (double) effective / dailyLimit;
        if (usageFraction >= policy.warnThreshold()) {
            log.warn("[BudgetExceededHandler] WARN userId='{}' effective={} limit={} "
                    + "fraction={}% correlationId={}",
                    userId, effective, dailyLimit,
                    String.format("%.1f", usageFraction * 100), correlationId);
            return BudgetDecision.WARN;
        }

        // Case 3: Healthy
        log.debug("[BudgetExceededHandler] ALLOW userId='{}' effective={} limit={} "
                + "correlationId={}", userId, effective, dailyLimit, correlationId);
        return BudgetDecision.ALLOW;
    }

    /**
     * Checks whether a per-request reservation would exceed the per-request cap.
     * Called before {@link TokenBudgetManager#reserve} to catch oversized requests.
     *
     * @param estimatedTokens the estimated token count for this request
     * @param correlationId   for log tracing
     * @return true if the reservation is within the per-request cap; false otherwise
     */
    public boolean isWithinPerRequestCap(int estimatedTokens, String correlationId) {
        int cap = policy.perRequestCap();
        if (estimatedTokens > cap) {
            log.warn("[BudgetExceededHandler] per-request cap exceeded: estimated={} cap={} "
                    + "correlationId={}", estimatedTokens, cap, correlationId);
            return false;
        }
        return true;
    }
}