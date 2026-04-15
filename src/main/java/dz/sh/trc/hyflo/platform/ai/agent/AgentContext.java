package dz.sh.trc.hyflo.platform.ai.agent;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable context carrier passed through every AI agent call.
 *
 * <p>Holds all cross-cutting metadata needed by any adapter implementation:
 * session continuity, request traceability, domain hints, and system
 * instruction override. Adapters use this to construct the final prompt
 * and populate LLM response metadata.</p>
 *
 * <p>Constructed by {@link AgentRequest} — never instantiated directly
 * by business or intelligence code.</p>
 */
public final class AgentContext {

    /**
     * Conversation session identifier for multi-turn memory.
     * Null for stateless single-shot calls (flow analysis, crisis assessment).
     */
    private final String sessionId;

    /**
     * Correlation ID propagated from the HTTP layer for full call tracing.
     */
    private final String correlationId;

    /**
     * Domain context key-value pairs injected into the LLM prompt.
     * Example entries: {@code pipelineId=PIPE-001}, {@code alertId=ALT-42}.
     */
    private final Map<String, String> domainContext;

    /**
     * Optional system instruction override.
     * When set, replaces the adapter's default system prompt.
     */
    private final String systemInstructionOverride;

    private AgentContext(Builder builder) {
        this.sessionId                = builder.sessionId;
        this.correlationId            = builder.correlationId;
        this.domainContext            = builder.domainContext != null
                                        ? Collections.unmodifiableMap(builder.domainContext)
                                        : Collections.emptyMap();
        this.systemInstructionOverride = builder.systemInstructionOverride;
    }

    public String getSessionId()                  { return sessionId; }
    public String getCorrelationId()              { return correlationId; }
    public Map<String, String> getDomainContext() { return domainContext; }
    public String getSystemInstructionOverride()  { return systemInstructionOverride; }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String sessionId;
        private String correlationId;
        private Map<String, String> domainContext;
        private String systemInstructionOverride;

        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder domainContext(Map<String, String> domainContext) {
            this.domainContext = domainContext;
            return this;
        }

        public Builder systemInstructionOverride(String systemInstructionOverride) {
            this.systemInstructionOverride = systemInstructionOverride;
            return this;
        }

        public AgentContext build() {
            return new AgentContext(this);
        }
    }

    @Override
    public String toString() {
        return "AgentContext{sessionId='" + sessionId
                + "', correlationId='" + correlationId + "'}";
    }
}