package dz.sh.trc.hyflo.platform.ai.agent;

import java.util.Objects;

/**
 * Immutable platform-level request value object for any AI agent call.
 *
 * <p>This is the internal representation used exclusively within
 * {@code platform/ai}. Intelligence-layer ports (e.g.
 * {@code FlowAnomalyAgentPort}) pass their domain DTOs to adapters which
 * translate them into {@code AgentRequest} before calling the LLM.</p>
 *
 * <p>Built via the inner {@link Builder} to guarantee all required
 * fields are present.</p>
 */
public final class AgentRequest {

    /**
     * The fully assembled user prompt sent to the LLM.
     * Adapters are responsible for injecting domain context into this prompt
     * before constructing the request.
     */
    private final String prompt;

    /**
     * Cross-cutting context: session, correlation, domain hints.
     */
    private final AgentContext context;

    /**
     * Maximum tokens the LLM may produce in the response.
     * When null, the adapter falls back to the value from {@link AiProperties}.
     */
    private final Integer maxTokens;

    /**
     * Sampling temperature override (0.0 = deterministic, 1.0 = creative).
     * When null, the adapter falls back to the value from {@link AiProperties}.
     */
    private final Double temperature;

    private AgentRequest(Builder builder) {
        this.prompt      = Objects.requireNonNull(builder.prompt, "prompt must not be null");
        this.context     = Objects.requireNonNull(builder.context, "context must not be null");
        this.maxTokens   = builder.maxTokens;
        this.temperature = builder.temperature;
    }

    public String getPrompt()        { return prompt; }
    public AgentContext getContext()  { return context; }
    public Integer getMaxTokens()    { return maxTokens; }
    public Double getTemperature()   { return temperature; }

    public static Builder builder(String prompt, AgentContext context) {
        return new Builder(prompt, context);
    }

    public static final class Builder {
        private final String prompt;
        private final AgentContext context;
        private Integer maxTokens;
        private Double temperature;

        private Builder(String prompt, AgentContext context) {
            this.prompt  = prompt;
            this.context = context;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public AgentRequest build() {
            return new AgentRequest(this);
        }
    }

    @Override
    public String toString() {
        return "AgentRequest{correlationId='" + context.getCorrelationId()
                + "', sessionId='" + context.getSessionId() + "'}";
    }
}