package dz.sh.trc.hyflo.platform.ai.agent;

/**
 * Unchecked exception thrown by {@link AgentExecutor} when an AI call fails.
 *
 * <p>Carries the {@code correlationId} for traceability in logs and
 * error responses. The {@link dz.sh.trc.hyflo.system.exception.GlobalExceptionHandler}
 * maps this to {@code SYSTEM_AI_EXECUTION_ERROR} in Phase 3.</p>
 */
public class AgentExecutionException extends RuntimeException {

    private final String correlationId;

    public AgentExecutionException(String message, String correlationId) {
        super(message);
        this.correlationId = correlationId;
    }

    public AgentExecutionException(String message, Throwable cause, String correlationId) {
        super(message, cause);
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }
}