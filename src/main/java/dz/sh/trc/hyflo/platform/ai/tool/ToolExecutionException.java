package dz.sh.trc.hyflo.platform.ai.tool;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;

/**
 * Thrown by {@link ToolExecutor} when a tool call fails unrecoverably.
 * Extends {@link AgentExecutionException} so the resilience pipeline
 * can classify it as transient or permanent.
 */
public class ToolExecutionException extends AgentExecutionException {

    private final String toolName;

    public ToolExecutionException(String toolName, String message,
                                   Throwable cause, String correlationId,
                                   boolean isTransient) {
        super("Tool [" + toolName + "] failed: " + message, cause,
                correlationId, isTransient);
        this.toolName = toolName;
    }

    public ToolExecutionException(String toolName, String message,
                                   String correlationId) {
        super("Tool [" + toolName + "] failed: " + message, correlationId);
        this.toolName = toolName;
    }

    public String getToolName() { return toolName; }
}