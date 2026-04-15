package dz.sh.trc.hyflo.platform.ai.tool;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Guards against runaway tool-calling loops during a single agent turn.
 *
 * <h3>Enforced limits:</h3>
 * <ul>
 *   <li><strong>maxToolCallsPerTurn</strong> — total tool invocations allowed
 *       across all tools in one agent reasoning loop (default: 10)</li>
 *   <li><strong>maxCallsPerTool</strong> — maximum times a specific tool may
 *       be called within the same turn (default: 3, prevents single-tool loops)</li>
 *   <li><strong>maxIterations</strong> — maximum number of LLM reasoning
 *       iterations (think → tool → think cycles) before forcing a final
 *       answer (default: 5)</li>
 * </ul>
 *
 * <h3>Scope:</h3>
 * <p>A "turn" is identified by a {@code correlationId} (the same ID used
 * throughout the agent and resilience layers). State is held in memory
 * and is automatically released after each turn via {@link #releaseTurn}.</p>
 *
 * <h3>Thread safety:</h3>
 * <p>Uses {@link ConcurrentHashMap} and {@link AtomicInteger} — safe for
 * concurrent agent execution.</p>
 */
@Component
@ConfigurationProperties(prefix = "hyflo.ai.tooling")
public class ToolExecutionGuard {

    private static final Logger log = LoggerFactory.getLogger(ToolExecutionGuard.class);

    // --- Configurable limits (hyflo.ai.tooling.*) ---
    private int maxToolCallsPerTurn = 10;
    private int maxCallsPerTool     = 3;
    private int maxIterations       = 5;

    // --- Per-turn state ---
    // key: correlationId → total tool call counter
    private final ConcurrentHashMap<String, AtomicInteger> totalCallCounters  = new ConcurrentHashMap<>();
    // key: correlationId:toolName → per-tool counter
    private final ConcurrentHashMap<String, AtomicInteger> perToolCounters    = new ConcurrentHashMap<>();
    // key: correlationId → iteration counter
    private final ConcurrentHashMap<String, AtomicInteger> iterationCounters  = new ConcurrentHashMap<>();

    // -------------------------------------------------------------------------
    // Guard API
    // -------------------------------------------------------------------------

    /**
     * Records a tool call attempt and throws if any limit would be exceeded.
     *
     * <p>Must be called by {@link ToolExecutor} before invoking the tool.</p>
     *
     * @param correlationId the current agent turn ID
     * @param toolName      the tool being called
     * @throws AgentExecutionException with {@code isTransient=false} if any
     *         limit is exceeded — this stops the agent loop immediately
     */
    public void checkAndRecord(String correlationId, String toolName) {
        Objects.requireNonNull(correlationId, "correlationId must not be null");
        Objects.requireNonNull(toolName,      "toolName must not be null");

        // 1. Check + increment total per-turn counter
        int total = totalCallCounters
                .computeIfAbsent(correlationId, k -> new AtomicInteger(0))
                .incrementAndGet();

        if (total > maxToolCallsPerTurn) {
            log.warn("[ToolExecutionGuard] maxToolCallsPerTurn={} exceeded for "
                    + "correlationId={} (total={})", maxToolCallsPerTurn, correlationId, total);
            throw new AgentExecutionException(
                    "Tool call limit exceeded: maximum " + maxToolCallsPerTurn
                    + " tool calls per turn. Produce a final answer with available data.",
                    correlationId,
                    false
            );
        }

        // 2. Check + increment per-tool counter
        String perToolKey = correlationId + ":" + toolName;
        int perTool = perToolCounters
                .computeIfAbsent(perToolKey, k -> new AtomicInteger(0))
                .incrementAndGet();

        if (perTool > maxCallsPerTool) {
            log.warn("[ToolExecutionGuard] maxCallsPerTool={} exceeded for tool='{}' "
                    + "correlationId={}", maxCallsPerTool, toolName, correlationId);
            throw new AgentExecutionException(
                    "Tool [" + toolName + "] called " + perTool + " times — "
                    + "maximum per-tool limit is " + maxCallsPerTool + ". "
                    + "Use the data already retrieved from this tool.",
                    correlationId,
                    false
            );
        }

        log.debug("[ToolExecutionGuard] tool='{}' correlationId={} total={}/{} perTool={}/{}",
                toolName, correlationId, total, maxToolCallsPerTurn,
                perTool, maxCallsPerTool);
    }

    /**
     * Records one reasoning iteration and throws if {@link #maxIterations} is exceeded.
     *
     * <p>Must be called by the agent executor at the start of each think → tool cycle.</p>
     *
     * @param correlationId the current agent turn ID
     * @throws AgentExecutionException if the iteration limit is reached
     */
    public void checkAndRecordIteration(String correlationId) {
        Objects.requireNonNull(correlationId, "correlationId must not be null");

        int iter = iterationCounters
                .computeIfAbsent(correlationId, k -> new AtomicInteger(0))
                .incrementAndGet();

        if (iter > maxIterations) {
            log.warn("[ToolExecutionGuard] maxIterations={} exceeded for correlationId={}",
                    maxIterations, correlationId);
            throw new AgentExecutionException(
                    "Reasoning iteration limit reached (" + maxIterations + "). "
                    + "You must produce a final answer now using the information already gathered.",
                    correlationId,
                    false
            );
        }

        log.debug("[ToolExecutionGuard] iteration {}/{} for correlationId={}",
                iter, maxIterations, correlationId);
    }

    /**
     * Releases all per-turn state after the agent turn completes.
     *
     * <p>Must be called in a {@code finally} block by the agent executor.</p>
     *
     * @param correlationId the turn to release
     */
    public void releaseTurn(String correlationId) {
        if (correlationId == null) return;

        totalCallCounters.remove(correlationId);
        iterationCounters.remove(correlationId);

        // Remove all per-tool keys for this turn
        perToolCounters.keySet().removeIf(k -> k.startsWith(correlationId + ":"));

        log.debug("[ToolExecutionGuard] released state for correlationId={}", correlationId);
    }

    // -------------------------------------------------------------------------
    // Status (for health/metrics)
    // -------------------------------------------------------------------------

    /** Returns current total call count for a turn (0 if not started). */
    public int getTotalCallCount(String correlationId) {
        AtomicInteger c = totalCallCounters.get(correlationId);
        return c == null ? 0 : c.get();
    }

    /** Returns current iteration count for a turn (0 if not started). */
    public int getIterationCount(String correlationId) {
        AtomicInteger c = iterationCounters.get(correlationId);
        return c == null ? 0 : c.get();
    }

    // -------------------------------------------------------------------------
    // Properties setters (used by @ConfigurationProperties)
    // -------------------------------------------------------------------------

    public int getMaxToolCallsPerTurn() { return maxToolCallsPerTurn; }
    public void setMaxToolCallsPerTurn(int v) { this.maxToolCallsPerTurn = v; }
    public int getMaxCallsPerTool()     { return maxCallsPerTool; }
    public void setMaxCallsPerTool(int v)     { this.maxCallsPerTool = v; }
    public int getMaxIterations()       { return maxIterations; }
    public void setMaxIterations(int v)       { this.maxIterations = v; }

    // Missing import needed inline:
    //private static void Objects_requireNonNull_inline(Object o, String msg) {
    //    if (o == null) throw new NullPointerException(msg);
    //}
}