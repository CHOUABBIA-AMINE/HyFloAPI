package dz.sh.trc.hyflo.platform.ai.tool;

/**
 * Contract for all AI tool implementations.
 *
 * <p>Implementing classes must be annotated with {@link AiTool} and
 * registered as Spring beans.</p>
 *
 * @param <I> the strongly-typed input record or class
 * @param <O> the return type — should always be {@link ToolResult}
 */
@FunctionalInterface
public interface ToolFunction<I, O> {

    /**
     * Executes the tool with the provided input.
     *
     * @param input the parsed tool input (never null)
     * @return the tool result (never null)
     * @throws ToolExecutionException if the tool execution fails
     */
    O apply(I input);
}