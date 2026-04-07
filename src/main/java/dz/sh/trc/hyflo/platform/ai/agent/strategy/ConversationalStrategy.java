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
 * Strategy for multi-turn conversational AI flows with session memory.
 *
 * <p>Used by the operator assistant use case. Differs from
 * {@link StatelessAnalysisStrategy} in that the session context in the
 * {@link dz.sh.trc.hyflo.platform.ai.agent.AgentContext} is meaningful:
 * the adapter uses {@code sessionId} to retrieve and append conversation
 * history before constructing the final prompt.</p>
 *
 * <h3>Current state — Commit 2.2 (skeleton):</h3>
 * <p>Session memory management is not yet implemented at this layer.
 * The strategy delegates to {@link AgentPort#call} with the full request,
 * and the concrete adapter is responsible for handling session memory
 * using the {@code sessionId} from {@link AgentRequest#getContext()}.</p>
 *
 * <h3>Key difference from {@link StatelessAnalysisStrategy}:</h3>
 * <ul>
 *   <li>{@code STATELESS}: {@code sessionId} is null or ignored</li>
 *   <li>{@code CONVERSATIONAL}: {@code sessionId} is mandatory and the adapter
 *       must persist turn history keyed by it</li>
 * </ul>
 *
 * <h3>Planned extension (Phase 3):</h3>
 * <p>This class will be extended to:</p>
 * <ol>
 *   <li>Inject a {@code ConversationMemoryStore} for session history management</li>
 *   <li>Retrieve conversation history before the call</li>
 *   <li>Append the new turn to history after a successful response</li>
 *   <li>Implement session expiry and clearance delegated from
 *       {@code AssistantAgentPort#clearSession}</li>
 * </ol>
 */
@Component
public class ConversationalStrategy implements AgentStrategy {

    private static final Logger log = LoggerFactory.getLogger(ConversationalStrategy.class);

    private final AgentPort agentPort;

    public ConversationalStrategy(AgentPort agentPort) {
        this.agentPort = Objects.requireNonNull(agentPort, "AgentPort must not be null");
    }

    @Override
    public AgentExecutionMode mode() {
        return AgentExecutionMode.CONVERSATIONAL;
    }

    /**
     * {@inheritDoc}
     *
     * <p><strong>Current behaviour (skeleton):</strong> passes the request
     * directly to {@link AgentPort#call}. The {@code sessionId} in the
     * context is carried through and available to the adapter.</p>
     *
     * <p><strong>Planned behaviour (Phase 3):</strong> wraps the call with
     * memory load-before / persist-after using {@code ConversationMemoryStore}.</p>
     */
    @Override
    public AgentResponse execute(AgentRequest request) {
        Objects.requireNonNull(request, "AgentRequest must not be null");
        String correlationId = request.getContext().getCorrelationId();
        String sessionId     = request.getContext().getSessionId();

        log.debug("[ConversationalStrategy] execute — sessionId={} correlationId={}",
                sessionId, correlationId);

        if (sessionId == null || sessionId.isBlank()) {
            log.warn("[ConversationalStrategy] sessionId is null/blank — " +
                     "conversational memory will not be maintained. correlationId={}",
                     correlationId);
        }

        try {
            /*
             * Phase 3: wrap this call with memory load/persist around it.
             * The contract stays the same — only memory management is added.
             */
            return agentPort.call(request);
        } catch (AgentExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw new AgentExecutionException(
                    "ConversationalStrategy failed during AgentPort call", e, correlationId);
        }
    }
}