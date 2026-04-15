package dz.sh.trc.hyflo.platform.ai.agent;

/**
 * Unchecked exception thrown anywhere in the platform/ai call chain.
 *
 * <p>Carries:</p>
 * <ul>
 *   <li>{@code correlationId} — for cross-log traceability</li>
 *   <li>{@code isTransient} — if {@code true}, the caller may retry or
 *       invoke {@link dz.sh.trc.hyflo.platform.ai.resilience.FallbackModelResolver}</li>
 * </ul>
 */
public class AgentExecutionException extends RuntimeException {

    private final String correlationId;

    /**
     * {@code true} if the failure is transient and a fallback or retry is appropriate.
     * {@code false} if the failure is permanent (auth error, invalid request) and
     * should be propagated immediately without retry.
     */
    private final boolean transient_;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** Convenience — non-transient, no correlationId. */
    public AgentExecutionException(String message) {
        super(message);
        this.correlationId = null;
        this.transient_    = false;
    }

    /** Convenience — non-transient with correlationId. */
    public AgentExecutionException(String message, String correlationId) {
        super(message);
        this.correlationId = correlationId;
        this.transient_    = false;
    }

    /** Non-transient with cause and correlationId. */
    public AgentExecutionException(String message, Throwable cause, String correlationId) {
        super(message, cause);
        this.correlationId = correlationId;
        this.transient_    = false;
    }

    /** Full constructor — explicit transient flag. */
    public AgentExecutionException(String message,
                                   Throwable cause,
                                   String correlationId,
                                   boolean isTransient) {
        super(message, cause);
        this.correlationId = correlationId;
        this.transient_    = isTransient;
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public String getCorrelationId() { return correlationId; }

    /** @return {@code true} if retry or fallback resolution is appropriate */
    public boolean isTransient()     { return transient_; }
}