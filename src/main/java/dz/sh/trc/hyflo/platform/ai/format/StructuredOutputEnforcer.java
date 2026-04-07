package dz.sh.trc.hyflo.platform.ai.format;

import com.fasterxml.jackson.databind.ObjectMapper;
import dz.sh.trc.hyflo.platform.ai.model.LLMClientPort;
import dz.sh.trc.hyflo.platform.ai.model.LLMRequest;
import dz.sh.trc.hyflo.platform.ai.model.LLMResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Orchestrates the parse → validate → correct → retry loop for LLM outputs.
 *
 * <h3>Loop contract:</h3>
 * <pre>
 * attempt = 1
 * LOOP:
 *   validate(rawOutput)
 *   if valid → deserialize to T → return ParsedOutput.success
 *   if invalid AND attempt ≤ maxCorrectionRetries:
 *     correctionPrompt = promptBuilder.build(errors, schema, attempt)
 *     rawOutput = llmClient.call(correctionPrompt)
 *     attempt++
 *     continue LOOP
 *   else → return ParsedOutput.failure (or throw OutputParseException)
 * </pre>
 *
 * <h3>Limits:</h3>
 * <ul>
 *   <li>Default: {@code maxCorrectionRetries = 2} → max 3 total LLM calls
 *       (1 original + 2 corrections)</li>
 *   <li>Configurable via {@code hyflo.ai.format.max-correction-retries}</li>
 * </ul>
 *
 * <h3>LLM call for correction:</h3>
 * <p>The correction uses the same {@link LLMClientPort} that the agent uses,
 * but sends a minimal correction-only request with no history — just the
 * correction prompt as a single user message. This avoids re-processing
 * the full conversation and keeps correction tokens minimal.</p>
 *
 * <h3>Failure behavior:</h3>
 * <ul>
 *   <li>If {@code throwOnFailure = true} (default) → throws
 *       {@link OutputParseException} after exhausting retries</li>
 *   <li>If {@code throwOnFailure = false} → returns
 *       {@link ParsedOutput#failure(List, String, int, String)} for
 *       callers that want graceful degradation</li>
 * </ul>
 *
 * <h3>Raw map boundary:</h3>
 * <p>The internal {@code Map<String,Object>} from Jackson's tree walk
 * is confined to this class. Business code only ever receives
 * {@code ParsedOutput<T>} with a typed {@code T}.</p>
 */
@Component
@ConfigurationProperties(prefix = "hyflo.ai.format")
public class StructuredOutputEnforcer {

    private static final Logger log = LoggerFactory.getLogger(StructuredOutputEnforcer.class);

    private int     maxCorrectionRetries = 2;
    private boolean throwOnFailure       = true;

    private final JsonSchemaValidator          validator;
    private final OutputCorrectionPromptBuilder promptBuilder;
    private final LLMClientPort                llmClient;
    private final ObjectMapper                 objectMapper;

    public StructuredOutputEnforcer(JsonSchemaValidator validator,
                                     OutputCorrectionPromptBuilder promptBuilder,
                                     LLMClientPort llmClient,
                                     ObjectMapper objectMapper) {
        this.validator     = Objects.requireNonNull(validator,     "JsonSchemaValidator");
        this.promptBuilder = Objects.requireNonNull(promptBuilder, "OutputCorrectionPromptBuilder");
        this.llmClient     = Objects.requireNonNull(llmClient,     "LLMClientPort");
        this.objectMapper  = Objects.requireNonNull(objectMapper,  "ObjectMapper");
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Enforces structured output for the given raw LLM string.
     *
     * <p>Validates the raw string, applies correction retries if needed,
     * and deserializes the final result into {@code targetType}.</p>
     *
     * @param rawOutput     the raw string returned by the LLM
     * @param targetType    the Java class to deserialize into (e.g. {@code FlowScoreDTO.class})
     * @param schema        the validation descriptor for this output type
     * @param correlationId the current agent turn ID (for logging and exception enrichment)
     * @param <T>           the target business type
     * @return a {@link ParsedOutput} containing the typed result or failure details
     * @throws OutputParseException if all retries are exhausted and
     *         {@link #throwOnFailure} is {@code true}
     */
    public <T> ParsedOutput<T> enforce(String rawOutput,
                                        Class<T> targetType,
                                        JsonSchemaValidator.SchemaDescriptor schema,
                                        String correlationId) {
        Objects.requireNonNull(targetType,    "targetType must not be null");
        Objects.requireNonNull(schema,        "schema must not be null");
        Objects.requireNonNull(correlationId, "correlationId must not be null");

        String  currentRaw = rawOutput;
        int     attempt    = 1;
        int     maxAttempts = 1 + maxCorrectionRetries;
        List<String> lastErrors = List.of();

        while (attempt <= maxAttempts) {
            log.debug("[StructuredOutputEnforcer] attempt {}/{} for type='{}' correlationId={}",
                    attempt, maxAttempts, targetType.getSimpleName(), correlationId);

            // Step 1: Validate
            JsonSchemaValidator.ValidationResult validation =
                    validator.validate(currentRaw, schema);

            if (validation.isValid()) {
                // Step 2: Deserialize — raw map stays inside this method
                try {
                    T value = objectMapper.readValue(
                            validation.cleanedJson(), targetType);

                    log.debug("[StructuredOutputEnforcer] success on attempt {} for type='{}' "
                            + "correlationId={}", attempt, targetType.getSimpleName(), correlationId);

                    return ParsedOutput.success(value, correlationId, attempt,
                            validation.cleanedJson());

                } catch (Exception e) {
                    // Valid JSON structure but Jackson can't map it → treat as validation error
                    List<String> deserErrors = List.of(
                            "Output passed schema validation but could not be deserialized "
                            + "into " + targetType.getSimpleName() + ": " + e.getMessage()
                            + ". Ensure field names and types match exactly.");
                    log.warn("[StructuredOutputEnforcer] deserialization failed attempt={} "
                            + "correlationId={}: {}", attempt, correlationId, e.getMessage());

                    if (attempt < maxAttempts) {
                        currentRaw = callCorrectionLLM(currentRaw, deserErrors, schema, attempt,
                                correlationId);
                        attempt++;
                        lastErrors = deserErrors;
                        continue;
                    }
                    lastErrors = deserErrors;
                    break;
                }
            }

            // Validation failed
            lastErrors = validation.errors();
            log.warn("[StructuredOutputEnforcer] validation failed attempt={} errors={} "
                    + "correlationId={}", attempt, lastErrors.size(), correlationId);

            if (attempt < maxAttempts) {
                currentRaw = callCorrectionLLM(currentRaw, lastErrors, schema, attempt,
                        correlationId);
                attempt++;
            } else {
                // Last attempt exhausted
                break;
            }
        }

        // All attempts exhausted
        String summary = promptBuilder.buildExhaustionSummary(attempt - 1, lastErrors);
        log.warn("[StructuredOutputEnforcer] all correction retries exhausted for type='{}' "
                + "correlationId={}\n{}", targetType.getSimpleName(), correlationId, summary);

        if (throwOnFailure) {
            throw new OutputParseException(correlationId, lastErrors,
                    attempt - 1, targetType.getSimpleName());
        }

        return ParsedOutput.failure(lastErrors, correlationId, attempt - 1, currentRaw);
    }

    // -------------------------------------------------------------------------
    // Internal: correction LLM call
    // -------------------------------------------------------------------------

    private String callCorrectionLLM(String badOutput,
                                      List<String> errors,
                                      JsonSchemaValidator.SchemaDescriptor schema,
                                      int attemptNumber,
                                      String correlationId) {
        String correctionPrompt = promptBuilder.build(
                badOutput, errors, schema.schemaHint(), attemptNumber);

        log.debug("[StructuredOutputEnforcer] sending correction prompt attempt={} "
                + "correlationId={}", attemptNumber, correlationId);

        try {
            // Correction call: stateless, no conversation history — single user message
            LLMRequest correctionRequest = LLMRequest.builder()
                    .userMessage(correctionPrompt)
                    .correlationId(correlationId)
                    .build();

            LLMResponse response = llmClient.complete(correctionRequest);
            String corrected = response.content();

            log.debug("[StructuredOutputEnforcer] correction response received attempt={} "
                    + "correlationId={}", attemptNumber, correlationId);

            return corrected != null ? corrected : "";

        } catch (Exception e) {
            log.warn("[StructuredOutputEnforcer] correction LLM call failed attempt={} "
                    + "correlationId={}: {}", attemptNumber, correlationId, e.getMessage());
            // Return the bad output unchanged; validation on next iteration will re-collect errors
            return badOutput;
        }
    }

    // -------------------------------------------------------------------------
    // Properties setters (used by @ConfigurationProperties)
    // -------------------------------------------------------------------------

    public int     getMaxCorrectionRetries()   { return maxCorrectionRetries; }
    public void    setMaxCorrectionRetries(int v) { this.maxCorrectionRetries = v; }
    public boolean isThrowOnFailure()          { return throwOnFailure; }
    public void    setThrowOnFailure(boolean v)   { this.throwOnFailure = v; }
}