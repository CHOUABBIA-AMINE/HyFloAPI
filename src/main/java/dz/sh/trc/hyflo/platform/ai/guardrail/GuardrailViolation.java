package dz.sh.trc.hyflo.platform.ai.guardrail;

import java.time.Instant;
import java.util.Objects;

/**
 * A single typed violation detected by any guardrail in the chain.
 *
 * <p>Violations are immutable value objects. They carry enough information
 * to be logged, audited, or surfaced to an operator dashboard without
 * requiring the caller to parse a raw string message.</p>
 *
 * <h3>Severity:</h3>
 * <ul>
 *   <li>{@link Severity#BLOCK} — hard stop; the chain must not continue.</li>
 *   <li>{@link Severity#WARN} — soft issue; chain continues but violation is recorded.</li>
 *   <li>{@link Severity#INFO} — mutation applied (e.g. PII redacted); informational only.</li>
 * </ul>
 */
public final class GuardrailViolation {

    /**
     * Violation severity, driving the chain's continuation decision.
     */
    public enum Severity { BLOCK, WARN, INFO }

    /**
     * Structural category of the violation, used for filtering and routing.
     */
    public enum Category {
        INPUT_TOO_LONG,
        INPUT_INJECTION_DETECTED,
        INPUT_ENCODING_ANOMALY,
        PII_REDACTED,
        CONTEXT_ENTITY_NOT_FOUND,
        CONTEXT_DOMAIN_KEY_MISSING,
        CONTEXT_DOMAIN_VALUE_INVALID,
        TOOL_OUTPUT_NULL,
        TOOL_OUTPUT_SCHEMA_INVALID,
        TOOL_OUTPUT_STALE_REFERENCE,
        OUTPUT_HALLUCINATION_SIGNAL,
        OUTPUT_SENSITIVE_CONTENT,
        OUTPUT_EMPTY
    }

    private final Severity  severity;
    private final Category  category;
    private final String    guardrailName;
    private final String    detail;
    private final String    correlationId;
    private final Instant   detectedAt;

    private GuardrailViolation(Builder b) {
        this.severity      = Objects.requireNonNull(b.severity,      "severity");
        this.category      = Objects.requireNonNull(b.category,      "category");
        this.guardrailName = Objects.requireNonNull(b.guardrailName, "guardrailName");
        this.detail        = b.detail != null ? b.detail : "";
        this.correlationId = b.correlationId != null ? b.correlationId : "unknown";
        this.detectedAt    = Instant.now();
    }

    public Severity severity()      { return severity; }
    public Category category()      { return category; }
    public String   guardrailName() { return guardrailName; }
    public String   detail()        { return detail; }
    public String   correlationId() { return correlationId; }
    public Instant  detectedAt()    { return detectedAt; }

    public boolean isBlocking() { return severity == Severity.BLOCK; }

    @Override
    public String toString() {
        return "GuardrailViolation{" + severity + "/" + category
                + " [" + guardrailName + "] " + detail + "}";
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Severity severity;
        private Category category;
        private String   guardrailName;
        private String   detail;
        private String   correlationId;

        public Builder severity(Severity v)      { this.severity      = v; return this; }
        public Builder category(Category v)      { this.category      = v; return this; }
        public Builder guardrailName(String v)   { this.guardrailName = v; return this; }
        public Builder detail(String v)          { this.detail        = v; return this; }
        public Builder correlationId(String v)   { this.correlationId = v; return this; }
        public GuardrailViolation build()        { return new GuardrailViolation(this); }
    }
}