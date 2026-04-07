package dz.sh.trc.hyflo.platform.ai.agent;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable platform-level response value object from any AI agent call.
 *
 * <p>Returned by every {@link AgentPort} implementation. Adapters translate
 * this into domain-specific response DTOs (e.g. {@code FlowAnalysisResultDTO})
 * before returning to the intelligence layer.</p>
 */
public final class AgentResponse {

    /**
     * Raw generated text content from the LLM.
     */
    private final String content;

    /**
     * Provider that handled this call (OPENAI | OLLAMA | NONE).
     */
    private final String provider;

    /**
     * Model identifier used (e.g. {@code gpt-4o}, {@code mistral}).
     */
    private final String modelId;

    private final int promptTokens;
    private final int completionTokens;

    /**
     * Whether this response was produced by the no-op fallback
     * (i.e. AI is disabled and no LLM was called).
     */
    private final boolean fallback;

    private final Instant respondedAt;
    private final String correlationId;

    private AgentResponse(Builder builder) {
        this.content        = Objects.requireNonNull(builder.content, "content must not be null");
        this.provider       = Objects.requireNonNull(builder.provider, "provider must not be null");
        this.modelId        = builder.modelId;
        this.promptTokens   = builder.promptTokens;
        this.completionTokens = builder.completionTokens;
        this.fallback       = builder.fallback;
        this.respondedAt    = builder.respondedAt != null ? builder.respondedAt : Instant.now();
        this.correlationId  = builder.correlationId;
    }

    public String getContent()          { return content; }
    public String getProvider()         { return provider; }
    public String getModelId()          { return modelId; }
    public int getPromptTokens()        { return promptTokens; }
    public int getCompletionTokens()    { return completionTokens; }
    public int getTotalTokens()         { return promptTokens + completionTokens; }
    public boolean isFallback()         { return fallback; }
    public Instant getRespondedAt()     { return respondedAt; }
    public String getCorrelationId()    { return correlationId; }

    public static Builder builder(String content, String provider) {
        return new Builder(content, provider);
    }

    public static final class Builder {
        private final String content;
        private final String provider;
        private String modelId;
        private int promptTokens;
        private int completionTokens;
        private boolean fallback;
        private Instant respondedAt;
        private String correlationId;

        private Builder(String content, String provider) {
            this.content  = content;
            this.provider = provider;
        }

        public Builder modelId(String modelId) {
            this.modelId = modelId;
            return this;
        }

        public Builder promptTokens(int promptTokens) {
            this.promptTokens = promptTokens;
            return this;
        }

        public Builder completionTokens(int completionTokens) {
            this.completionTokens = completionTokens;
            return this;
        }

        public Builder fallback(boolean fallback) {
            this.fallback = fallback;
            return this;
        }

        public Builder respondedAt(Instant respondedAt) {
            this.respondedAt = respondedAt;
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public AgentResponse build() {
            return new AgentResponse(this);
        }
    }

    @Override
    public String toString() {
        return "AgentResponse{provider='" + provider + "', modelId='" + modelId
                + "', tokens=" + getTotalTokens() + ", fallback=" + fallback
                + ", correlationId='" + correlationId + "'}";
    }
}