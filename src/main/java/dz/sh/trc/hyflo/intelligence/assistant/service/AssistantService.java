package dz.sh.trc.hyflo.intelligence.assistant.service;

import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantRequestDTO;
import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantResponseDTO;
import dz.sh.trc.hyflo.intelligence.assistant.port.AssistantAgentPort;
import dz.sh.trc.hyflo.intelligence.capability.AssistantCapability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Entry point for all AI-powered operator assistant use cases.
 *
 * <p>Implements {@link AssistantCapability} — the contract declared in the
 * capability layer. Delegates all LLM calls through {@link AssistantAgentPort}
 * — never calls Spring AI directly.</p>
 *
 * <h3>Dependency direction (strict):</h3>
 * <pre>
 *   AssistantController
 *           ↓
 *   OperatorAssistantService  (this — intelligence layer)
 *           ↓
 *   AssistantAgentPort        (outbound port — intelligence layer)
 *           ↓  (injected at runtime)
 *   AssistantAgentAdapter     (platform/ai — Spring AI adapter)
 * </pre>
 *
 * <p>No WebSocket or streaming in this commit.
 * Streaming support will be added in a dedicated platform/ai commit
 * without changing this service's public interface.</p>
 */
@Service
public class AssistantService implements AssistantCapability {

    private static final Logger log =
            LoggerFactory.getLogger(AssistantService.class);

    private final AssistantAgentPort assistantAgentPort;

    public AssistantService(AssistantAgentPort assistantAgentPort) {
        this.assistantAgentPort =
                Objects.requireNonNull(assistantAgentPort, "AssistantAgentPort must not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssistantResponseDTO chat(AssistantRequestDTO request) {
        Objects.requireNonNull(request, "AssistantRequestDTO must not be null");
        log.info("[OperatorAssistantService] chat — sessionId={} correlationId={}",
                request.sessionId(), request.correlationId());
        return assistantAgentPort.chat(request);
    }

    /**
     * Retrieves the ordered conversation history for a given session.
     *
     * @param sessionId the session identifier
     * @return ordered list of previous assistant responses; empty if unknown session
     */
    public List<AssistantResponseDTO> getHistory(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        log.info("[OperatorAssistantService] getHistory — sessionId={}", sessionId);
        return assistantAgentPort.getHistory(sessionId);
    }

    /**
     * Clears conversation memory for a session.
     *
     * @param sessionId the session identifier to clear
     */
    public void clearSession(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        log.info("[OperatorAssistantService] clearSession — sessionId={}", sessionId);
        assistantAgentPort.clearSession(sessionId);
    }
}