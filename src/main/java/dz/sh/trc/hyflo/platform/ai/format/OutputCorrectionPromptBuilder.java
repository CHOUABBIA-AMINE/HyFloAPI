package dz.sh.trc.hyflo.platform.ai.format;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Builds a correction prompt to send back to the LLM when structured output
 * validation fails.
 *
 * <h3>Correction prompt principles:</h3>
 * <ul>
 *   <li>Be precise — paste the exact errors, not vague instructions</li>
 *   <li>Include the original (bad) output so the model has context</li>
 *   <li>Include the expected schema hint so the model knows what to produce</li>
 *   <li>Forbid markdown — the model must return raw JSON only</li>
 *   <li>Keep the prompt under ~400 tokens to minimize re-generation cost</li>
 * </ul>
 *
 * <h3>Usage:</h3>
 * <pre>{@code
 * String correctionPrompt = promptBuilder.build(
 *     badOutput,
 *     validationResult.errors(),
 *     schema.schemaHint(),
 *     attemptNumber
 * );
 * // Send correctionPrompt as the next user message in the conversation
 * }</pre>
 *
 * <h3>Why not inject the LLM call here?</h3>
 * <p>This class is a pure prompt builder — it has no LLM dependency.
 * Calling the LLM is the responsibility of {@link StructuredOutputEnforcer},
 * which has access to {@code LLMClientPort}. This keeps the builder
 * testable without mocking AI infrastructure.</p>
 */
@Component
public class OutputCorrectionPromptBuilder {

    private static final int MAX_RAW_EXCERPT_CHARS = 800;

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Builds a correction prompt combining the bad output, validation errors,
     * and schema instructions.
     *
     * @param badOutput      the raw string the LLM produced (will be truncated if long)
     * @param errors         the validation errors from {@link JsonSchemaValidator}
     * @param schemaHint     the LLM-readable schema description
     * @param attemptNumber  which correction attempt this is (1-based)
     * @return a complete prompt string to send as the next message
     */
    public String build(String badOutput, List<String> errors,
                        String schemaHint, int attemptNumber) {
        Objects.requireNonNull(errors, "errors must not be null");

        String excerpt = truncate(badOutput, MAX_RAW_EXCERPT_CHARS);

        StringBuilder sb = new StringBuilder(512);

        sb.append("Your previous response (attempt ").append(attemptNumber)
          .append(") did not conform to the required format.\n\n");

        sb.append("=== ERRORS FOUND ===\n");
        for (int i = 0; i < errors.size(); i++) {
            sb.append(i + 1).append(". ").append(errors.get(i)).append('\n');
        }

        sb.append("\n=== YOUR PREVIOUS OUTPUT (excerpt) ===\n");
        sb.append(excerpt.isBlank() ? "(empty or null)" : excerpt).append('\n');

        if (schemaHint != null && !schemaHint.isBlank()) {
            sb.append("\n=== REQUIRED OUTPUT SCHEMA ===\n");
            sb.append(schemaHint).append('\n');
        }

        sb.append("\n=== INSTRUCTIONS ===\n");
        sb.append("Produce ONLY a valid JSON object that fixes all the errors listed above.\n");
        sb.append("Rules:\n");
        sb.append("- No markdown, no code fences (do NOT use ```json)\n");
        sb.append("- No prose, no explanation — JSON object only\n");
        sb.append("- Every required field must be present and non-null\n");
        sb.append("- Enum fields must use exactly one of the allowed values\n");
        sb.append("- Numeric fields must be within their declared range\n");

        return sb.toString();
    }

    /**
     * Builds a final-failure message to log when all correction retries
     * are exhausted. Not sent to the LLM — used for audit logging only.
     *
     * @param maxAttempts the total number of attempts that were made
     * @param lastErrors  the errors from the final attempt
     * @return a human-readable failure summary
     */
    public String buildExhaustionSummary(int maxAttempts, List<String> lastErrors) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("Structured output correction exhausted after ")
          .append(maxAttempts).append(" attempt(s). Remaining errors:\n");
        for (String e : lastErrors) {
            sb.append("  - ").append(e).append('\n');
        }
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static String truncate(String s, int maxChars) {
        if (s == null) return "";
        if (s.length() <= maxChars) return s;
        return s.substring(0, maxChars) + "\n... [truncated]";
    }
}