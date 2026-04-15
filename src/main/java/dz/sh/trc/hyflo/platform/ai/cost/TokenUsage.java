package dz.sh.trc.hyflo.platform.ai.cost;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable snapshot of actual token consumption for a single LLM call.
 *
 * <p>This is a pure value object — it carries no identity and is never
 * mutated after construction. Instances are produced by the agent executor
 * after each LLM response and passed to {@link TokenBudgetManager#commit}.</p>
 *
 * <h3>Token taxonomy:</h3>
 * <ul>
 *   <li>{@link #promptTokens} — tokens consumed by the input context
 *       (system prompt + conversation history + tool schemas + user message).</li>
 *   <li>{@link #completionTokens} — tokens produced by the model response
 *       (including tool call arguments if the model emitted a tool invocation).</li>
 *   <li>{@link #totalTokens} — derived: {@code promptTokens + completionTokens}.</li>
 * </ul>
 *
 * <h3>Cost estimation:</h3>
 * <p>Actual USD cost is NOT computed here — that requires a price-per-token
 * mapping that changes with model versions. {@link CostPolicy} holds
 * token budgets only (not dollar budgets) to avoid coupling to pricing APIs.</p>
 */
public final class TokenUsage {

    private final int     promptTokens;
    private final int     completionTokens;
    private final String  modelId;
    private final String  correlationId;
    private final Instant recordedAt;

    private TokenUsage(Builder b) {
        this.promptTokens     = b.promptTokens;
        this.completionTokens = b.completionTokens;
        this.modelId          = b.modelId != null ? b.modelId : "unknown";
        this.correlationId    = b.correlationId;
        this.recordedAt       = Instant.now();
    }

    public int     promptTokens()     { return promptTokens; }
    public int     completionTokens() { return completionTokens; }
    public int     totalTokens()      { return promptTokens + completionTokens; }
    public String  modelId()          { return modelId; }
    public String  correlationId()    { return correlationId; }
    public Instant recordedAt()       { return recordedAt; }

    /** Returns a zero-usage record for initialization and test stubs. */
    public static TokenUsage zero(String modelId) {
        return builder().promptTokens(0).completionTokens(0).modelId(modelId).build();
    }

    @Override
    public String toString() {
        return "TokenUsage{prompt=" + promptTokens + ", completion=" + completionTokens
                + ", total=" + totalTokens() + ", model=" + modelId + "}";
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private int    promptTokens;
        private int    completionTokens;
        private String modelId;
        private String correlationId;

        public Builder promptTokens(int v)      { this.promptTokens     = v; return this; }
        public Builder completionTokens(int v)  { this.completionTokens = v; return this; }
        public Builder modelId(String v)        { this.modelId          = v; return this; }
        public Builder correlationId(String v)  { this.correlationId    = v; return this; }
        public TokenUsage build()               { return new TokenUsage(this); }
    }
}