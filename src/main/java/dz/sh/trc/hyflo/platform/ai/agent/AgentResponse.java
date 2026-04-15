package dz.sh.trc.hyflo.platform.ai.agent;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable platform-level response value object from any AI agent call.
 *
 * <p>Returned by every {@link AgentPort} implementation. Adapters translate
 * this into domain-specific response DTOs (e.g. {@code FlowAnalysisResultDTO})
 * before returning to the intelligence layer.</p>
 *
 * <p><strong>Step 4.3 additions:</strong> {@link #promptId} and
 * {@link #promptVersion} carry the identity of the
 * {@link dz.sh.trc.hyflo.platform.ai.prompt.PromptVersion} used to produce
 * this response. Both fields are optional (nullable) for backwards compatibility
 * with callers that do not yet use the prompt registry.</p>
 */
public final class AgentResponse {

    private final String  content;
    private final String  provider;
    private final String  modelId;
    private final int     promptTokens;
    private final int     completionTokens;
    private final boolean fallback;
    private final Instant respondedAt;
    private final String  correlationId;

    // ---- Step 4.3: prompt traceability ----
    /** The template ID that produced this response (e.g. {@code "flow-anomaly"}). */
    private final String promptId;
    /** The rendered template version (e.g. {@code "flow-anomaly:v1"}). */
    private final String promptVersion;

    private AgentResponse(Builder builder) {
        this.content          = Objects.requireNonNull(builder.content,   "content");
        this.provider         = Objects.requireNonNull(builder.provider,  "provider");
        this.modelId          = builder.modelId;
        this.promptTokens     = builder.promptTokens;
        this.completionTokens = builder.completionTokens;
        this.fallback         = builder.fallback;
        this.respondedAt      = builder.respondedAt != null ? builder.respondedAt : Instant.now();
        this.correlationId    = builder.correlationId;
        this.promptId         = builder.promptId;
        this.promptVersion    = builder.promptVersion;
    }

    public String  getContent()          { return content; }
    public String  getProvider()         { return provider; }
    public String  getModelId()          { return modelId; }
    public int     getPromptTokens()     { return promptTokens; }
    public int     getCompletionTokens() { return completionTokens; }
    public int     getTotalTokens()      { return promptTokens + completionTokens; }
    public boolean isFallback()          { return fallback; }
    public Instant getRespondedAt()      { return respondedAt; }
    public String  getCorrelationId()    { return correlationId; }

    /** Returns the template ID used (e.g. {@code "flow-anomaly"}), or null. */
    public String  getPromptId()         { return promptId; }

    /** Returns the qualified prompt version (e.g. {@code "flow-anomaly:v1"}), or null. */
    public String  getPromptVersion()    { return promptVersion; }

    public static Builder builder(String content, String provider) {
        return new Builder(content, provider);
    }

    public static final class Builder {
        private final String content;
        private final String provider;
        private String  modelId;
        private int     promptTokens;
        private int     completionTokens;
        private boolean fallback;
        private Instant respondedAt;
        private String  correlationId;
        private String  promptId;
        private String  promptVersion;

        private Builder(String content, String provider) {
            this.content  = content;
            this.provider = provider;
        }

        public Builder modelId(String v)           { this.modelId          = v; return this; }
        public Builder promptTokens(int v)         { this.promptTokens     = v; return this; }
        public Builder completionTokens(int v)     { this.completionTokens = v; return this; }
        public Builder fallback(boolean v)         { this.fallback         = v; return this; }
        public Builder respondedAt(Instant v)      { this.respondedAt      = v; return this; }
        public Builder correlationId(String v)     { this.correlationId    = v; return this; }

        /**
         * Sets the prompt traceability fields from a rendered prompt's version.
         *
         * @param pv the {@link dz.sh.trc.hyflo.platform.ai.prompt.PromptVersion}
         *           returned by {@link dz.sh.trc.hyflo.platform.ai.prompt.PromptRenderer}
         * @return this builder
         */
        public Builder promptVersion(
                dz.sh.trc.hyflo.platform.ai.prompt.PromptVersion pv) {
            if (pv != null) {
                this.promptId      = pv.templateId();
                this.promptVersion = pv.qualifiedName();
            }
            return this;
        }

        /** Direct setter for cases where a PromptVersion object is not available. */
        public Builder promptId(String v)          { this.promptId      = v; return this; }
        /** Direct setter for cases where a PromptVersion object is not available. */
        public Builder promptVersionString(String v){ this.promptVersion = v; return this; }

        public AgentResponse build()               { return new AgentResponse(this); }
    }

    @Override
    public String toString() {
        return "AgentResponse{provider='" + provider + "', modelId='" + modelId
                + "', tokens=" + getTotalTokens()
                + ", promptVersion='" + promptVersion + "'"
                + ", fallback=" + fallback
                + ", correlationId='" + correlationId + "'}";
    }
}