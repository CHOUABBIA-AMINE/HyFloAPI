package dz.sh.trc.hyflo.platform.ai.routing;

import dz.sh.trc.hyflo.platform.ai.agent.strategy.AgentStrategy.AgentExecutionMode;

import java.util.Objects;

/**
 * Immutable context object used by {@link ModelRouter} to select a model.
 *
 * <p>Constructed by platform adapters (e.g. {@code SpringAiLLMAdapter},
 * {@code OllamaLLMAdapter}) from the
 * {@link dz.sh.trc.hyflo.platform.ai.agent.AgentRequest}
 * before asking the router which model to use.</p>
 *
 * <h3>Fields used by routing rules:</h3>
 * <ul>
 *   <li>{@code agentType} — the domain use case category
 *       (FLOW | CRISIS | NETWORK | ASSISTANT)</li>
 *   <li>{@code executionMode} — stateless / tool-calling / conversational</li>
 *   <li>{@code routingHint} — explicit caller hint, e.g. {@code "prefer-fast"}
 *       or {@code "require-strong"}; may be null</li>
 *   <li>{@code criticalityLevel} — LOW | MEDIUM | HIGH | CRITICAL; affects
 *       whether stronger models are selected for safety-sensitive calls</li>
 * </ul>
 */
public final class RoutingContext {

    /**
     * Domain use-case category.
     * Determines the default model tier for the call.
     */
    public enum AgentType {
        /** Flow anomaly analysis — high volume, cheap model acceptable */
        FLOW,
        /** Crisis assessment — safety-critical, requires strong model */
        CRISIS,
        /** Network topology insight — moderate complexity */
        NETWORK,
        /** Operator assistant — conversational, benefits from fast model */
        ASSISTANT
    }

    /**
     * Criticality signal injected by the intelligence layer.
     * HIGH and CRITICAL route to stronger (larger) models.
     */
    public enum CriticalityLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    private final AgentType agentType;
    private final AgentExecutionMode executionMode;
    private final CriticalityLevel criticalityLevel;

    /**
     * Optional explicit routing hint from the caller.
     * Examples: {@code "prefer-fast"}, {@code "require-strong"}, {@code "local-only"}.
     * Null means no hint — routing rules decide entirely.
     */
    private final String routingHint;

    private RoutingContext(Builder builder) {
        this.agentType        = Objects.requireNonNull(builder.agentType, "agentType must not be null");
        this.executionMode    = Objects.requireNonNull(builder.executionMode, "executionMode must not be null");
        this.criticalityLevel = builder.criticalityLevel != null
                                ? builder.criticalityLevel
                                : CriticalityLevel.MEDIUM;
        this.routingHint      = builder.routingHint;
    }

    public AgentType getAgentType()               { return agentType; }
    public AgentExecutionMode getExecutionMode()  { return executionMode; }
    public CriticalityLevel getCriticalityLevel() { return criticalityLevel; }
    public String getRoutingHint()                { return routingHint; }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private AgentType agentType;
        private AgentExecutionMode executionMode;
        private CriticalityLevel criticalityLevel;
        private String routingHint;

        public Builder agentType(AgentType agentType) {
            this.agentType = agentType;
            return this;
        }

        public Builder executionMode(AgentExecutionMode executionMode) {
            this.executionMode = executionMode;
            return this;
        }

        public Builder criticalityLevel(CriticalityLevel criticalityLevel) {
            this.criticalityLevel = criticalityLevel;
            return this;
        }

        public Builder routingHint(String routingHint) {
            this.routingHint = routingHint;
            return this;
        }

        public RoutingContext build() {
            return new RoutingContext(this);
        }
    }
}