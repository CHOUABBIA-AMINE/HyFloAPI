package dz.sh.trc.hyflo.platform.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Typed configuration properties for the HyFlo AI platform module.
 * Binds to the {@code hyflo.ai.*} prefix in {@code application.properties}.
 */
@ConfigurationProperties(prefix = "hyflo.ai")
public class AiProperties {

    private boolean enabled = false;
    private String provider = "ollama";
    private StructuredOutput structuredOutput = new StructuredOutput();

    /** Model routing configuration — maps agent types to model IDs. */
    private Routing routing = new Routing();

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public boolean isEnabled()                          { return enabled; }
    public void setEnabled(boolean enabled)             { this.enabled = enabled; }

    public String getProvider()                         { return provider; }
    public void setProvider(String provider)            { this.provider = provider; }

    public StructuredOutput getStructuredOutput()       { return structuredOutput; }
    public void setStructuredOutput(StructuredOutput s) { this.structuredOutput = s; }

    public Routing getRouting()                         { return routing; }
    public void setRouting(Routing routing)             { this.routing = routing; }

    // -------------------------------------------------------------------------
    // Nested config: StructuredOutput (unchanged from Commit 2.1)
    // -------------------------------------------------------------------------

    public static class StructuredOutput {
        private boolean enabled = true;
        private boolean strict = false;
        private int maxRepairAttempts = 2;

        public boolean isEnabled()                         { return enabled; }
        public void setEnabled(boolean enabled)            { this.enabled = enabled; }
        public boolean isStrict()                          { return strict; }
        public void setStrict(boolean strict)              { this.strict = strict; }
        public int getMaxRepairAttempts()                  { return maxRepairAttempts; }
        public void setMaxRepairAttempts(int v)            { this.maxRepairAttempts = v; }
    }

    // -------------------------------------------------------------------------
    // Nested config: Routing (new in Commit 2.4)
    // -------------------------------------------------------------------------

    /**
     * Model routing configuration.
     *
     * <p>Model IDs must match identifiers understood by the active provider.
     * For OpenAI: {@code gpt-4o}, {@code gpt-4o-mini}, etc.
     * For Ollama: {@code mistral}, {@code llama3}, etc.</p>
     */
    public static class Routing {

        /**
         * Default model used when no routing rule matches.
         * Property: {@code hyflo.ai.routing.default-model-id}
         */
        private String defaultModelId = "gpt-4o-mini";

        /**
         * Strong/expensive model for safety-critical calls (crisis, CRITICAL severity).
         * Property: {@code hyflo.ai.routing.strong-model-id}
         */
        private String strongModelId = "gpt-4o";

        /**
         * Fast/cheap model for high-volume stateless calls (flow analysis, network).
         * Property: {@code hyflo.ai.routing.fast-model-id}
         */
        private String fastModelId = "gpt-4o-mini";

        /**
         * Local model for offline or cost-zero routing (Ollama-backed).
         * Property: {@code hyflo.ai.routing.local-model-id}
         */
        private String localModelId = "mistral";

        public String getDefaultModelId()               { return defaultModelId; }
        public void setDefaultModelId(String v)         { this.defaultModelId = v; }
        public String getStrongModelId()                { return strongModelId; }
        public void setStrongModelId(String v)          { this.strongModelId = v; }
        public String getFastModelId()                  { return fastModelId; }
        public void setFastModelId(String v)            { this.fastModelId = v; }
        public String getLocalModelId()                 { return localModelId; }
        public void setLocalModelId(String v)           { this.localModelId = v; }
    }
}