package dz.sh.trc.hyflo.platform.ai.format;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;

import java.util.Collections;
import java.util.List;

/**
 * Thrown by {@link StructuredOutputEnforcer} when all correction retries
 * are exhausted and the LLM output still fails validation.
 *
 * <p>Extends {@link AgentExecutionException} with {@code isTransient=false}
 * — a structured output failure is not a transient infrastructure error,
 * it is an LLM-level behavioral failure that requires escalation or
 * fallback to a default response.</p>
 *
 * <p>Contains the full list of validation errors and the total number of
 * attempts made, for structured error handling and audit logging.</p>
 */
public class OutputParseException extends AgentExecutionException {

    private final List<String> validationErrors;
    private final int          totalAttempts;
    private final String       targetType;

    public OutputParseException(String correlationId,
                                 List<String> validationErrors,
                                 int totalAttempts,
                                 String targetType) {
        super("LLM structured output validation failed for type '" + targetType
                + "' after " + totalAttempts + " attempt(s): "
                + String.join("; ", validationErrors),
                correlationId,
                false);
        this.validationErrors = Collections.unmodifiableList(validationErrors);
        this.totalAttempts    = totalAttempts;
        this.targetType       = targetType;
    }

    public List<String> getValidationErrors() { return validationErrors; }
    public int          getTotalAttempts()     { return totalAttempts; }
    public String       getTargetType()        { return targetType; }
}