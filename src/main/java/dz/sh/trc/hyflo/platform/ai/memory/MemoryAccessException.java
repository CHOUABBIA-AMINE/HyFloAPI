package dz.sh.trc.hyflo.platform.ai.memory;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;

/**
 * Thrown by {@link PgVectorConversationMemory} when a database operation fails.
 *
 * <p>Extends {@link AgentExecutionException} with {@code isTransient=true}
 * because a database connectivity failure is a recoverable infrastructure error.
 * The resilience layer may choose to degrade gracefully (e.g., fall back to
 * stateless mode for this turn).</p>
 */
public class MemoryAccessException extends AgentExecutionException {

    private final String sessionId;

    public MemoryAccessException(String message, Throwable cause, String sessionId) {
        super(message, cause, sessionId, true);  // isTransient = true
        this.sessionId = sessionId;
    }

    public String getSessionId() { return sessionId; }
}