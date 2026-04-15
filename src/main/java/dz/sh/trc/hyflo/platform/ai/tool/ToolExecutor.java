package dz.sh.trc.hyflo.platform.ai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Dispatches a named tool call, enforces execution timeout,
 * and integrates with {@link ToolExecutionGuard} before each call.
 *
 * <h3>Execution flow for each call:</h3>
 * <pre>
 *   ToolExecutor.execute(correlationId, toolName, input)
 *     → ToolExecutionGuard.checkAndRecord(correlationId, toolName)  [1. guard check]
 *     → ToolRegistry.getByName(toolName)                            [2. resolve function]
 *     → ToolFunction.apply(input)       with timeout                [3. execute with timeout]
 *     → ToolResult (success or failure)                             [4. return]
 * </pre>
 *
 * <h3>Timeout handling:</h3>
 * <p>Each tool call is submitted to a virtual-thread executor and subject
 * to a configurable per-call timeout (default 10s). On timeout, a
 * {@link ToolExecutionException} is thrown with {@code isTransient=true}
 * — the agent can decide to skip this tool and continue reasoning.</p>
 *
 * <h3>Error policy:</h3>
 * <ul>
 *   <li>Timeout → {@code ToolExecutionException(transient=true)}</li>
 *   <li>Tool not found → {@code ToolExecutionException(transient=false)}</li>
 *   <li>Guard limit exceeded → {@code AgentExecutionException(transient=false)}</li>
 *   <li>Tool throws → wrapped in {@code ToolExecutionException(transient=false)}</li>
 * </ul>
 */
@Component
public class ToolExecutor {

    private static final Logger log = LoggerFactory.getLogger(ToolExecutor.class);

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    private final ToolRegistry        registry;
    private final ToolExecutionGuard  guard;
    private final ExecutorService     executorService;
    private final int                 timeoutSeconds;

    public ToolExecutor(ToolRegistry registry, ToolExecutionGuard guard) {
        this.registry        = Objects.requireNonNull(registry, "ToolRegistry must not be null");
        this.guard           = Objects.requireNonNull(guard,    "ToolExecutionGuard must not be null");
        // Virtual threads (Java 21) — lightweight, no thread-pool sizing needed
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
        this.timeoutSeconds  = DEFAULT_TIMEOUT_SECONDS;

        log.info("[ToolExecutor] Initialised — timeoutSeconds={} registry.size={}",
                timeoutSeconds, registry.size());
    }

    /**
     * Executes a tool by name with the given raw input object.
     *
     * @param correlationId the current agent turn correlation ID
     * @param toolName      the tool to invoke (must match {@link AiTool#name()})
     * @param input         the deserialized input object (type must match the tool's input type)
     * @return the tool result — never null
     * @throws ToolExecutionException       if the tool fails, is not found, or times out
     * @throws dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException
     *                                       if a guard limit is exceeded
     */
    @SuppressWarnings("unchecked")
    public ToolResult execute(String correlationId, String toolName, Object input) {
        Objects.requireNonNull(correlationId, "correlationId must not be null");
        Objects.requireNonNull(toolName,      "toolName must not be null");
        Objects.requireNonNull(input,         "input must not be null");

        // Step 1: Guard check (throws on limit exceeded)
        guard.checkAndRecord(correlationId, toolName);

        // Step 2: Resolve tool
        ToolRegistry.RegisteredTool registeredTool = registry.getByName(toolName)
                .orElseThrow(() -> new ToolExecutionException(
                        toolName,
                        "tool not found in registry — was it registered with @AiTool?",
                        correlationId));

        log.debug("[ToolExecutor] executing tool='{}' correlationId={}", toolName, correlationId);

        // Step 3: Execute with timeout
        Callable<ToolResult> callable = () -> {
            Instant start = Instant.now();
            try {
                ToolFunction<Object, ToolResult> fn =
                        (ToolFunction<Object, ToolResult>) registeredTool.function();
                ToolResult result = fn.apply(input);

                log.debug("[ToolExecutor] tool='{}' completed success={} durationMs={} correlationId={}",
                        toolName, result.isSuccess(),
                        java.time.Duration.between(start, Instant.now()).toMillis(),
                        correlationId);
                return result;

            } catch (Exception e) {
                log.warn("[ToolExecutor] tool='{}' threw exception: {} correlationId={}",
                        toolName, e.getMessage(), correlationId);
                throw new ToolExecutionException(toolName, e.getMessage(), e,
                        correlationId, false);
            }
        };

        Future<ToolResult> future = executorService.submit(callable);
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);

        } catch (TimeoutException e) {
            future.cancel(true);
            log.warn("[ToolExecutor] tool='{}' timed out after {}s correlationId={}",
                    toolName, timeoutSeconds, correlationId);
            throw new ToolExecutionException(toolName,
                    "tool execution timed out after " + timeoutSeconds + "s",
                    e, correlationId, true);

        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ToolExecutionException tee) throw tee;
            throw new ToolExecutionException(toolName,
                    cause != null ? cause.getMessage() : "unknown error",
                    cause, correlationId, false);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ToolExecutionException(toolName,
                    "tool execution interrupted", e, correlationId, true);
        }
    }
}