package dz.sh.trc.hyflo.platform.ai.cost;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;

/**
 * Thrown by {@link BudgetExceededHandler} when a user's daily token budget
 * is exhausted and {@link CostPolicy#isBlockOnExceeded()} is {@code true}.
 *
 * <p>Extends {@link AgentExecutionException} with {@code isTransient=false}:
 * a budget exhaustion is a deterministic gate, not an infrastructure failure.
 * The API layer should translate this to HTTP 429 (Too Many Requests).</p>
 */
public class BudgetExhaustedException extends AgentExecutionException {

    private final String userId;
    private final int    effectiveUsage;
    private final int    dailyLimit;

    public BudgetExhaustedException(String userId,
                                     int effectiveUsage,
                                     int dailyLimit,
                                     String correlationId) {
        super("Daily token budget exhausted for user '" + userId
              + "': effective=" + effectiveUsage + " limit=" + dailyLimit + ".",
              correlationId);
        this.userId         = userId;
        this.effectiveUsage = effectiveUsage;
        this.dailyLimit     = dailyLimit;
    }

    public String userId()         { return userId; }
    public int    effectiveUsage() { return effectiveUsage; }
    public int    dailyLimit()     { return dailyLimit; }
}