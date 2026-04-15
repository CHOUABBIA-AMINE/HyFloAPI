package dz.sh.trc.hyflo.intelligence.assistant.port;

import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantRequestDTO;
import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantResponseDTO;

import java.util.List;

/**
 * Outbound port connecting the assistant intelligence service to the AI platform layer.
 *
 * <p>This interface is the boundary between {@code intelligence/assistant} and
 * the actual LLM adapter in {@code platform/ai}. The service depends on this
 * port exclusively — never on Spring AI classes directly.</p>
 *
 * <p>The live implementation is
 * {@code dz.sh.trc.hyflo.platform.ai.adapter.AssistantAgentAdapter},
 * injected when {@code hyflo.ai.enabled=true}.<br>
 * The no-op fallback {@code NoOpAssistantAgentAdapter} is injected otherwise,
 * returning a safe static message without calling any LLM.</p>
 *
 * <h3>Contract guarantees required from implementations:</h3>
 * <ul>
 *   <li>Must never throw an unchecked exception directly — wrap in
 *       capability-layer exception if needed</li>
 *   <li>Must propagate the {@code correlationId} from request to response</li>
 *   <li>Must populate {@code sessionId} in the response (assign a new UUID
 *       if the request had none)</li>
 * </ul>
 */
public interface AssistantAgentPort {

    /**
     * Sends the operator's message to the LLM and returns the assistant reply.
     *
     * @param request the operator message with session context and domain hints
     * @return the assistant response with reply text and session metadata
     */
    AssistantResponseDTO chat(AssistantRequestDTO request);

    /**
     * Retrieves the ordered conversation history for a given session.
     *
     * <p>Returns an ordered list of previous turns for the session identified
     * by {@code sessionId}. Returns an empty list if the session does not
     * exist or has expired.</p>
     *
     * @param sessionId the session identifier
     * @return ordered list of previous assistant responses in the session
     */
    List<AssistantResponseDTO> getHistory(String sessionId);

    /**
     * Clears all conversation memory for a given session.
     *
     * <p>Called when the operator ends a session or requests a context reset.
     * After clearing, the next {@link #chat} call with the same sessionId
     * will start a fresh conversation.</p>
     *
     * @param sessionId the session identifier to clear
     */
    void clearSession(String sessionId);
}