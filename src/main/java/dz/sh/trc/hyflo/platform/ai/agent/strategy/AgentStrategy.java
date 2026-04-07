package dz.sh.trc.hyflo.platform.ai.agent.strategy;

import dz.sh.trc.hyflo.platform.ai.agent.AgentRequest;
import dz.sh.trc.hyflo.platform.ai.agent.AgentResponse;

/**
 * Strategy contract for all AI execution modes inside {@code platform/ai}.
 *
 * <p>Each strategy encapsulates one execution pattern:
 * <ul>
 *   <li>{@link StatelessAnalysisStrategy} — single-shot prompt/response,
 *       no session memory (flow anomaly, crisis assessment, network insight)</li>
 *   <li>{@link ToolCallingStrategy} — prompt with tool definitions injected,
 *       multi-step tool loop (reserved for future pipeline tool calls)</li>
 *   <li>{@link ConversationalStrategy} — multi-turn session with memory
 *       (operator assistant)</li>
 * </ul>
 *
 * <p>{@link AgentExecutor} selects the correct strategy via
 * {@link AgentStrategyRegistry} and never contains execution logic itself.</p>
 *
 * <h3>Invariants all implementations must honour:</h3>
 * <ul>
 *   <li>Constructor injection only — no field injection, no {@code @Autowired}</li>
 *   <li>No dependency on any controller, servlet, or HTTP layer</li>
 *   <li>Must propagate {@code correlationId} from request to response</li>
 *   <li>Must never throw unchecked exceptions — wrap in
 *       {@link dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException}</li>
 * </ul>
 */
public interface AgentStrategy {

    /**
     * Executes the AI call and returns the response.
     *
     * @param request the fully assembled platform-level request
     * @return the platform-level response — never null
     */
    AgentResponse execute(AgentRequest request);

    /**
     * Returns the execution mode identifier this strategy handles.
     *
     * <p>Used by {@link AgentStrategyRegistry} to select the correct
     * strategy at runtime without conditional chains in the executor.</p>
     *
     * @return execution mode constant — must match a value in
     *         {@link AgentExecutionMode}
     */
    AgentExecutionMode mode();

    /**
     * Enumerates the supported AI execution modes.
     *
     * <ul>
     *   <li>{@code STATELESS} — one-shot prompt with no memory
     *       (flow, crisis, network analysis)</li>
     *   <li>{@code TOOL_CALLING} — prompt augmented with tool/function definitions</li>
     *   <li>{@code CONVERSATIONAL} — multi-turn session with memory management</li>
     * </ul>
     */
    enum AgentExecutionMode {
        STATELESS,
        TOOL_CALLING,
        CONVERSATIONAL
    }
}