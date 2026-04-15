package dz.sh.trc.hyflo.platform.ai.tool;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a Spring bean as a platform AI tool that can be discovered
 * by {@link ToolRegistry} and dispatched by {@link ToolExecutor}.
 *
 * <h3>Contract for annotated classes:</h3>
 * <ul>
 *   <li>Must implement {@link ToolFunction} (single {@code apply} method)</li>
 *   <li>Must be a Spring-managed bean ({@code @Component} or equivalent)</li>
 *   <li>Must not access any repository directly; delegate to existing services</li>
 *   <li>{@code name} must be unique across all registered tools</li>
 *   <li>{@code description} must describe what the tool does in clear,
 *       LLM-readable language; it becomes part of the tool schema sent to
 *       the LLM provider</li>
 * </ul>
 *
 * <h3>Why a custom annotation instead of Spring AI's @Tool?</h3>
 * <p>Spring AI's {@code @Tool} (from {@code spring-ai-core}) is method-level
 * and tightly coupled to the {@code ChatClient} builder. This platform-level
 * annotation decouples tool discovery from any specific Spring AI version,
 * allowing {@link ToolRegistry} to manage the lifecycle independently of
 * which LLM provider is active.</p>
 *
 * <h3>Example usage:</h3>
 * <pre>{@code
 * @AiTool(
 *   name = "flow_query",
 *   description = "Query active flow operations and sensor readings for a pipeline segment.",
 *   domain = AiTool.Domain.FLOW
 * )
 * @Component
 * public class FlowQueryTool implements ToolFunction<FlowQueryTool.Input, ToolResult> {
 *   ...
 * }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AiTool {

    /**
     * Unique tool name. Used by {@link ToolExecutor} for dispatch and
     * by the LLM provider as the function name in the tool schema.
     * Must match {@code ^[a-z][a-z0-9_]{1,63}$}.
     */
    String name();

    /**
     * Human-readable description of what the tool does. Sent verbatim
     * to the LLM provider as the function description — write it for
     * the model, not just for developers.
     */
    String description();

    /**
     * Domain this tool belongs to. Used by {@link ToolRegistry} to
     * return the right tool subset per agent type.
     */
    Domain domain() default Domain.GENERAL;

    /**
     * Whether this tool is read-only (safe to call multiple times with
     * the same input). Non-idempotent tools should set this to false.
     * The guard may apply stricter limits to non-idempotent tools.
     */
    boolean readOnly() default true;

    /**
     * Domain categories — aligns with HyFlo's business modules.
     */
    enum Domain {
        FLOW,
        CRISIS,
        NETWORK,
        AUDIT,
        GENERAL
    }
}