package dz.sh.trc.hyflo.platform.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Typed configuration properties for the HyFlo AI platform module.
 *
 * <p>Binds to the {@code hyflo.ai.*} prefix in {@code application.properties}.
 * All AI platform beans depend on this class — never on raw
 * {@code @Value} injections.</p>
 *
 * <pre>
 * # Minimum required properties
 * hyflo.ai.enabled=false
 * hyflo.ai.provider=ollama
 * hyflo.ai.structured-output.enabled=true
 * hyflo.ai.structured-output.strict=false
 * hyflo.ai.structured-output.max-repair-attempts=2
 * </pre>
 */
@ConfigurationProperties(prefix = "hyflo.ai")
public class AiProperties {

    /**
     * Master feature flag. When {@code false}, all AI beans are replaced
     * by no-op fallbacks and no LLM calls are made.
     */
    private boolean enabled = false;

    /**
     * Active provider adapter. Values: {@code openai} | {@code ollama}.
     * Ignored when {@code enabled=false}.
     */
    private String provider = "ollama";

    /**
     * Structured output validation settings.
     */
    private StructuredOutput structuredOutput = new StructuredOutput();

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public boolean isEnabled()                       { return enabled; }
    public void setEnabled(boolean enabled)          { this.enabled = enabled; }

    public String getProvider()                      { return provider; }
    public void setProvider(String provider)         { this.provider = provider; }

    public StructuredOutput getStructuredOutput()    { return structuredOutput; }
    public void setStructuredOutput(StructuredOutput s) { this.structuredOutput = s; }

    // -------------------------------------------------------------------------
    // Nested config classes
    // -------------------------------------------------------------------------

    public static class StructuredOutput {

        /**
         * Enable JSON schema validation on LLM structured responses.
         */
        private boolean enabled = true;

        /**
         * When {@code true}, a schema mismatch throws an exception.
         * When {@code false}, logs a warning and returns the raw content.
         */
        private boolean strict = false;

        /**
         * Maximum number of times the adapter will re-prompt the LLM
         * to repair malformed structured output before failing.
         */
        private int maxRepairAttempts = 2;

        public boolean isEnabled()                         { return enabled; }
        public void setEnabled(boolean enabled)            { this.enabled = enabled; }

        public boolean isStrict()                          { return strict; }
        public void setStrict(boolean strict)              { this.strict = strict; }

        public int getMaxRepairAttempts()                  { return maxRepairAttempts; }
        public void setMaxRepairAttempts(int v)            { this.maxRepairAttempts = v; }
    }
}