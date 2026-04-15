package dz.sh.trc.hyflo.platform.ai.agent.strategy;

import dz.sh.trc.hyflo.platform.ai.agent.AgentExecutionException;
import dz.sh.trc.hyflo.platform.ai.agent.AgentPort;
import dz.sh.trc.hyflo.platform.ai.agent.AgentRequest;
import dz.sh.trc.hyflo.platform.ai.agent.AgentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Strategy for tool-calling (function-calling) AI execution flows.
 *
 * <p>Reserved for future domain use cases where the LLM must invoke
 * registered tools (e.g. fetching live sensor readings, querying the
 * pipeline topology, looking up maintenance history) before producing
 * its final response.</p>
 *
 * <h3>Current state — Commit 2.2 (skeleton):</h3>
 * <p>The tool registry and multi-step loop are not yet implemented.
 * This strategy delegates directly to {@link AgentPort#call(AgentRequest)}
 * as a single-shot call, identical to {@link StatelessAnalysisStrategy}.</p>
 *
 * <h3>Planned extension (Phase 3):</h3>
 * <p>This class will be extended to:</p>
 * <ol>
 *   <li>Inject a {@code ToolRegistry} containing registered tool definitions</li>
 *   <li>Run a tool-call loop: call LLM → inspect tool calls → invoke tools
 *       → re-prompt with results → repeat until final answer</li>
 *   <li>Return the final {@link AgentResponse} after all tool rounds</li>
 * </ol>
 *
 * <p>The interface contract ({@link AgentStrategy#execute}) remains unchanged
 * — callers are unaffected by the internal loop extension.</p>
 */
@Component
public class ToolCallingStrategy implements AgentStrategy {

    private static final Logger log = LoggerFactory.getLogger(ToolCallingStrategy.class);

    private final AgentPort agentPort;

    public ToolCallingStrategy(AgentPort agentPort) {
        this.agentPort = Objects.requireNonNull(agentPort, "AgentPort must not be null");
    }

    @Override
    public AgentExecutionMode mode() {
        return AgentExecutionMode.TOOL_CALLING;
    }

    /**
     * {@inheritDoc}
     *
     * <p><strong>Current behaviour (skeleton):</strong> delegates to
     * {@link AgentPort#call} as a single-shot call.</p>
     *
     * <p><strong>Planned behaviour (Phase 3):</strong> multi-step tool loop
     * with {@code ToolRegistry} injection.</p>
     */
    @Override
    public AgentResponse execute(AgentRequest request) {
        Objects.requireNonNull(request, "AgentRequest must not be null");
        String correlationId = request.getContext().getCorrelationId();

        log.debug("[ToolCallingStrategy] execute (skeleton — no tool loop yet) — correlationId={}",
                correlationId);

        try {
            /*
             * Phase 3: replace this single call with the tool-calling loop.
             * The contract stays the same — only the internal implementation changes.
             */
            return agentPort.call(request);
        } catch (AgentExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw new AgentExecutionException(
                    "ToolCallingStrategy failed during AgentPort call", e, correlationId);
        }
    }
}