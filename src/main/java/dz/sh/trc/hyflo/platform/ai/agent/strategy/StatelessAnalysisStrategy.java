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
 * Strategy for single-shot, stateless AI analysis calls.
 *
 * <p>Used by all domain analysis ports that do not require session memory:
 * flow anomaly detection, crisis assessment, and network insight analysis.</p>
 *
 * <p>Execution contract:</p>
 * <ul>
 *   <li>Sends the assembled prompt to the active {@link AgentPort} once</li>
 *   <li>Does not read or write any session state</li>
 *   <li>Returns the raw {@link AgentResponse} — no enrichment here</li>
 * </ul>
 */
@Component
public class StatelessAnalysisStrategy implements AgentStrategy {

    private static final Logger log = LoggerFactory.getLogger(StatelessAnalysisStrategy.class);

    private final AgentPort agentPort;

    public StatelessAnalysisStrategy(AgentPort agentPort) {
        this.agentPort = Objects.requireNonNull(agentPort, "AgentPort must not be null");
    }

    @Override
    public AgentExecutionMode mode() {
        return AgentExecutionMode.STATELESS;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delegates directly to {@link AgentPort#call(AgentRequest)}.
     * Any exception from the port is wrapped in {@link AgentExecutionException}.</p>
     */
    @Override
    public AgentResponse execute(AgentRequest request) {
        Objects.requireNonNull(request, "AgentRequest must not be null");
        String correlationId = request.getContext().getCorrelationId();

        log.debug("[StatelessAnalysisStrategy] execute — correlationId={}", correlationId);

        try {
            return agentPort.call(request);
        } catch (AgentExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw new AgentExecutionException(
                    "StatelessAnalysisStrategy failed during AgentPort call", e, correlationId);
        }
    }
}