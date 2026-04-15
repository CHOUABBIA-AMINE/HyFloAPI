package dz.sh.trc.hyflo.platform.ai.prompt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.misc.STMessage;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Renders prompt templates by substituting variables into StringTemplate 4 templates.
 *
 * <p>All prompt rendering must go through this class. Callers should never
 * instantiate {@link ST} directly or call {@link String#format} on prompt text.</p>
 *
 * <h3>Rendering contract:</h3>
 * <ol>
 *   <li>Resolves the template via {@link PromptTemplateRegistry}.</li>
 *   <li>Validates that all declared variables are provided in {@code variables}.</li>
 *   <li>Renders the template via ST4, with a strict error listener that
 *       converts any ST4 warning/error into a {@link PromptTemplateException}.</li>
 *   <li>Returns a {@link RenderedPrompt} carrying the rendered text and the
 *       {@link PromptVersion} for injection into
 *       {@link dz.sh.trc.hyflo.platform.ai.agent.AgentResponse}.</li>
 * </ol>
 *
 * <h3>Variable substitution:</h3>
 * <p>StringTemplate 4 uses {@code <variableName>} syntax in template files.
 * Each entry in the {@code variables} map is added to the ST instance via
 * {@code st.add(key, value.toString())}. All values are coerced to strings —
 * complex objects should be pre-formatted by the caller before passing.</p>
 *
 * <h3>Missing variables:</h3>
 * <p>If the {@code variables} map is missing a key declared in the template's
 * {@code @vars} annotation, a {@link PromptTemplateException} is thrown before
 * rendering begins. This makes prompt drift a compile-time-equivalent failure
 * rather than a silent blank substitution in the LLM context.</p>
 *
 * <h3>ST4 delimiter choice:</h3>
 * <p>Templates use angle-bracket delimiters ({@code < >}), which is ST4's
 * default. Dollar-sign delimiters are NOT used because dollar signs appear in
 * hydrocarbon domain content (unit prices, currency references).</p>
 */
@Component
public class PromptRenderer {

    private static final Logger log = LoggerFactory.getLogger(PromptRenderer.class);

    private final PromptTemplateRegistry registry;

    public PromptRenderer(PromptTemplateRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "PromptTemplateRegistry");
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Renders the latest version of the named template with the given variables.
     *
     * @param templateId    the template ID (e.g. {@code "flow-anomaly"})
     * @param variables     variable name → value map; all declared vars must be present
     * @param correlationId for log and error tracing
     * @return a {@link RenderedPrompt} with the rendered text and version metadata
     * @throws PromptTemplateException if the template is not found, a variable
     *         is missing, or ST4 encounters a rendering error
     */
    public RenderedPrompt render(String templateId,
                                  Map<String, Object> variables,
                                  String correlationId) {
        Objects.requireNonNull(templateId, "templateId");
        Objects.requireNonNull(variables,  "variables");

        PromptTemplate template = registry.getLatest(templateId);
        return renderTemplate(template, variables, correlationId);
    }

    /**
     * Renders a specific version of the named template.
     *
     * @param templateId    the template ID
     * @param version       the version number to render
     * @param variables     variable name → value map
     * @param correlationId for tracing
     * @return a {@link RenderedPrompt}
     * @throws PromptTemplateException if the specified version does not exist
     */
    public RenderedPrompt renderVersion(String templateId,
                                         int version,
                                         Map<String, Object> variables,
                                         String correlationId) {
        Objects.requireNonNull(templateId, "templateId");
        Objects.requireNonNull(variables,  "variables");

        PromptTemplate template = registry.getVersion(templateId, version)
                .orElseThrow(() -> new PromptTemplateException(
                        "Prompt template version not found: '" + templateId + ":v" + version + "'"));

        return renderTemplate(template, variables, correlationId);
    }

    // -------------------------------------------------------------------------
    // Internal rendering
    // -------------------------------------------------------------------------

    private RenderedPrompt renderTemplate(PromptTemplate template,
                                           Map<String, Object> variables,
                                           String correlationId) {
        // Step 1: Validate declared variables
        Set<String> declared = template.declaredVariables();
        if (!declared.isEmpty()) {
            for (String declared_var : declared) {
                if (!variables.containsKey(declared_var)) {
                    throw new PromptTemplateException(
                            "Prompt template '" + template.qualifiedName()
                            + "' requires variable '" + declared_var
                            + "' but it was not provided. correlationId=" + correlationId);
                }
            }
        }

        // Step 2: Build ST instance with strict error listener
        StrictErrorListener errorListener = new StrictErrorListener(
                template.qualifiedName(), correlationId);

        ST st = new ST(template.rawText(), '<', '>');
        st.groupThatCreatedThisInstance.setListener(errorListener);

        // Step 3: Add all variables
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            Object value = entry.getValue();
            st.add(entry.getKey(), value != null ? value.toString() : "");
        }

        // Step 4: Render
        String rendered = st.render();

        if (errorListener.hasErrors()) {
            throw new PromptTemplateException(
                    "ST4 rendering errors in template '" + template.qualifiedName()
                    + "': " + errorListener.firstError()
                    + " correlationId=" + correlationId);
        }

        log.debug("[PromptRenderer] rendered template='{}' hash={} vars={} correlationId={}",
                template.qualifiedName(),
                template.promptVersion().contentHash().substring(0, 8),
                variables.keySet(),
                correlationId);

        return new RenderedPrompt(rendered, template.promptVersion());
    }

    // -------------------------------------------------------------------------
    // Strict ST4 error listener
    // -------------------------------------------------------------------------

    private static final class StrictErrorListener implements STErrorListener {
        private final String templateName;
        private final String correlationId;
        private String firstError = null;

        StrictErrorListener(String templateName, String correlationId) {
            this.templateName  = templateName;
            this.correlationId = correlationId;
        }

        @Override public void compileTimeError(STMessage msg) { record(msg); }
        @Override public void runTimeError(STMessage msg)     { record(msg); }
        @Override public void IOError(STMessage msg)          { record(msg); }
        @Override public void internalError(STMessage msg)    { record(msg); }

        private void record(STMessage msg) {
            if (firstError == null) {
                firstError = msg.toString();
                log.warn("[PromptRenderer] ST4 error in '{}' correlationId={}: {}",
                        templateName, correlationId, msg);
            }
        }

        boolean hasErrors()  { return firstError != null; }
        String  firstError() { return firstError; }
    }

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------

    /**
     * The output of a render call: the rendered prompt text + version metadata.
     *
     * <p>The {@link PromptVersion} is forwarded to
     * {@link dz.sh.trc.hyflo.platform.ai.agent.AgentResponse} so every API
     * response carries the exact prompt identity that produced it.</p>
     *
     * @param renderedText  the fully substituted prompt string
     * @param promptVersion the version identity and hash of the template used
     */
    public record RenderedPrompt(
            String        renderedText,
            PromptVersion promptVersion
    ) {}
}