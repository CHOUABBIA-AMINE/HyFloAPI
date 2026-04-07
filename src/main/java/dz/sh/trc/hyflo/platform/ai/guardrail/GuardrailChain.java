package dz.sh.trc.hyflo.platform.ai.guardrail;

import dz.sh.trc.hyflo.platform.ai.agent.AgentContext;
import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Ordered guardrail pipeline — the single entry point for all input
 * and output safety checks in the AI layer.
 *
 * <h3>Architecture rule:</h3>
 * <p>All call sites ({@code AgentExecutor}, use-case orchestrators)
 * interact only with {@code GuardrailChain}. Individual guardrails are
 * never invoked directly from outside the {@code guardrail} package.
 * This makes it easy to add, reorder, or disable guardrails without
 * touching call sites.</p>
 *
 * <h3>Two pipeline modes:</h3>
 * <ul>
 *   <li>{@link #applyInput(AgentContext, String)} — runs the INPUT pipeline:
 *       sanitization → PII redaction → context integrity validation.
 *       Returns the (possibly mutated) sanitized text or throws on BLOCK.</li>
 *   <li>{@link #applyOutput(AgentContext, String)} — runs the OUTPUT pipeline:
 *       content filter only. Returns a result object for the caller to
 *       inspect warnings; throws on BLOCK.</li>
 *   <li>{@link #applyToolOutput(AgentContext, String, String)} — validates a
 *       single tool call result before re-injection into the model context.</li>
 * </ul>
 *
 * <h3>Failure mode:</h3>
 * <p>On BLOCK, {@link #applyInput} and {@link #applyOutput} throw
 * {@link GuardrailException} (extends {@link AgentExecutionException}
 * with {@code transient=false}). The caller is responsible for
 * surfacing this to the end user with an appropriate message.</p>
 *
 * <h3>Warning mode:</h3>
 * <p>WARN violations are collected in the returned {@link GuardrailResult}
 * and logged. The agent continues. Callers may inspect
 * {@link GuardrailResult#hasWarnings()} to decide on additional logging
 * or audit actions.</p>
 */
@Component
public class GuardrailChain {

    private static final Logger log = LoggerFactory.getLogger(GuardrailChain.class);

    private final InputSanitizer             inputSanitizer;
    private final PiiRedactor                piiRedactor;
    private final ContextIntegrityValidator  contextIntegrityValidator;
    private final ToolOutputValidator        toolOutputValidator;
    private final OutputContentFilter        outputContentFilter;

    public GuardrailChain(InputSanitizer inputSanitizer,
                          PiiRedactor piiRedactor,
                          ContextIntegrityValidator contextIntegrityValidator,
                          ToolOutputValidator toolOutputValidator,
                          OutputContentFilter outputContentFilter) {
        this.inputSanitizer            = Objects.requireNonNull(inputSanitizer);
        this.piiRedactor               = Objects.requireNonNull(piiRedactor);
        this.contextIntegrityValidator = Objects.requireNonNull(contextIntegrityValidator);
        this.toolOutputValidator       = Objects.requireNonNull(toolOutputValidator);
        this.outputContentFilter       = Objects.requireNonNull(outputContentFilter);
    }

    // -------------------------------------------------------------------------
    // Input pipeline
    // -------------------------------------------------------------------------

    /**
     * Runs the full INPUT guardrail pipeline.
     *
     * <p>Pipeline order:</p>
     * <ol>
     *   <li>{@link InputSanitizer} — length + injection check</li>
     *   <li>{@link PiiRedactor} — PII masking (mutates text)</li>
     *   <li>{@link ContextIntegrityValidator} — entity existence checks</li>
     * </ol>
     *
     * @param context   the agent context (used for correlationId and domain validation)
     * @param userInput the raw user input text
     * @return a {@link GuardrailResult} with the sanitized text and any violations
     * @throws GuardrailException if any BLOCK violation is detected
     */
    public GuardrailResult applyInput(AgentContext context, String userInput) {
        Objects.requireNonNull(context, "AgentContext must not be null");

        String correlationId = resolveCorrelationId(context);
        List<GuardrailViolation> allViolations = new ArrayList<>();
        String processedText = userInput;

        // Step 1: Sanitize
        List<GuardrailViolation> sanitizerViolations =
                inputSanitizer.sanitize(processedText, correlationId);
        allViolations.addAll(sanitizerViolations);
        throwIfBlocked(allViolations, correlationId, "INPUT_SANITIZER");

        // Step 2: PII redaction (mutates text)
        PiiRedactor.RedactionResult redaction =
                piiRedactor.redact(processedText, correlationId);
        allViolations.addAll(redaction.violations());
        processedText = redaction.redactedText();

        // Step 3: Context integrity
        List<GuardrailViolation> contextViolations =
                contextIntegrityValidator.validate(context, correlationId);
        allViolations.addAll(contextViolations);
        throwIfBlocked(allViolations, correlationId, "CONTEXT_INTEGRITY");

        GuardrailResult result = GuardrailResult.of(allViolations, processedText);
        logResult("INPUT", correlationId, result);
        return result;
    }

    // -------------------------------------------------------------------------
    // Output pipeline
    // -------------------------------------------------------------------------

    /**
     * Runs the OUTPUT guardrail pipeline on a model response string.
     *
     * @param context     the agent context for correlationId
     * @param modelOutput the raw model response
     * @return a {@link GuardrailResult} (processedText = modelOutput unchanged)
     * @throws GuardrailException if any BLOCK violation is detected
     */
    public GuardrailResult applyOutput(AgentContext context, String modelOutput) {
        Objects.requireNonNull(context, "AgentContext must not be null");

        String correlationId = resolveCorrelationId(context);
        List<GuardrailViolation> violations =
                outputContentFilter.filter(modelOutput, correlationId);

        throwIfBlocked(violations, correlationId, "OUTPUT_FILTER");

        GuardrailResult result = GuardrailResult.of(violations, modelOutput);
        logResult("OUTPUT", correlationId, result);
        return result;
    }

    // -------------------------------------------------------------------------
    // Tool output pipeline
    // -------------------------------------------------------------------------

    /**
     * Validates a tool call result before re-injection into the model context.
     *
     * @param context    the agent context for correlationId
     * @param toolName   the name of the tool that produced the result
     * @param rawResult  the raw result string
     * @return a {@link GuardrailResult}
     * @throws GuardrailException if any BLOCK violation is detected
     */
    public GuardrailResult applyToolOutput(AgentContext context,
                                            String toolName,
                                            String rawResult) {
        Objects.requireNonNull(context,  "AgentContext must not be null");
        Objects.requireNonNull(toolName, "toolName must not be null");

        String correlationId = resolveCorrelationId(context);
        List<GuardrailViolation> violations =
                toolOutputValidator.validate(toolName, rawResult, correlationId);

        throwIfBlocked(violations, correlationId, "TOOL_OUTPUT_VALIDATOR[" + toolName + "]");

        GuardrailResult result = GuardrailResult.of(violations, rawResult);
        logResult("TOOL[" + toolName + "]", correlationId, result);
        return result;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void throwIfBlocked(List<GuardrailViolation> violations,
                                  String correlationId,
                                  String stage) {
        List<GuardrailViolation> blocking = violations.stream()
                .filter(GuardrailViolation::isBlocking)
                .toList();

        if (!blocking.isEmpty()) {
            log.warn("[GuardrailChain] BLOCKED at stage='{}' correlationId={} violations={}",
                    stage, correlationId, blocking.size());
            throw new GuardrailException(correlationId, stage, blocking);
        }
    }

    private String resolveCorrelationId(AgentContext context) {
        return context.getCorrelationId() != null
                ? context.getCorrelationId() : "unknown";
    }

    private void logResult(String pipeline, String correlationId, GuardrailResult result) {
        if (result.isBlocked()) return; // already logged in throwIfBlocked
        if (result.hasWarnings()) {
            log.warn("[GuardrailChain] pipeline='{}' correlationId={} outcome={} warnings={}",
                    pipeline, correlationId, result.outcome(),
                    result.violations().stream()
                          .filter(v -> v.severity() == GuardrailViolation.Severity.WARN)
                          .count());
        } else {
            log.debug("[GuardrailChain] pipeline='{}' correlationId={} outcome=PASS",
                    pipeline, correlationId);
        }
    }
}