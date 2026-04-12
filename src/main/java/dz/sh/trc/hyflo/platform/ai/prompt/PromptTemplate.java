package dz.sh.trc.hyflo.platform.ai.prompt;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * A loaded and validated prompt template, ready for rendering.
 *
 * <p>A {@code PromptTemplate} binds a {@link PromptVersion} (identity + hash)
 * to the raw template text and the set of variable names it declares.
 * Instances are created exclusively by {@link PromptTemplateRegistry} at
 * startup and are immutable thereafter.</p>
 *
 * <h3>Variable declaration:</h3>
 * <p>Each template file documents its variables in a header comment block.
 * The registry parses the {@code @vars:} line to build the declared variable
 * set, which is used for validation at render time. Example:</p>
 * <pre>
 * // @vars: pipelineId, stationId, anomalyType, measuredValue, threshold
 * </pre>
 *
 * <h3>Render:</h3>
 * <p>Do not call StringTemplate directly. Always render via
 * {@link PromptRenderer#render(String, java.util.Map, String)} which enforces
 * variable completeness and records the prompt version in the response metadata.</p>
 */
public final class PromptTemplate {

    private final PromptVersion promptVersion;
    private final String        rawText;
    private final Set<String>   declaredVariables;

    PromptTemplate(PromptVersion promptVersion,
                   String rawText,
                   Set<String> declaredVariables) {
        this.promptVersion     = Objects.requireNonNull(promptVersion,     "promptVersion");
        this.rawText           = Objects.requireNonNull(rawText,           "rawText");
        this.declaredVariables = Collections.unmodifiableSet(
                Objects.requireNonNull(declaredVariables, "declaredVariables"));
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public PromptVersion promptVersion()      { return promptVersion; }
    public String        rawText()            { return rawText; }
    public Set<String>   declaredVariables()  { return declaredVariables; }

    /** Convenience: returns the template ID (e.g. {@code "flow-anomaly"}). */
    public String templateId()  { return promptVersion.templateId(); }

    /** Convenience: returns the version number (e.g. {@code 1}). */
    public int    version()     { return promptVersion.version(); }

    /** Convenience: returns the qualified name (e.g. {@code "flow-anomaly:v1"}). */
    public String qualifiedName() { return promptVersion.qualifiedName(); }

    @Override
    public String toString() {
        return "PromptTemplate{" + qualifiedName()
                + ", vars=" + declaredVariables + "}";
    }
}