package dz.sh.trc.hyflo.platform.ai.adapter;

import java.time.Instant;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantMessageDTO;
import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantResponseDTO;
import dz.sh.trc.hyflo.intelligence.assistant.port.AssistantPort;

/** No-op fallback for {@link AssistantPort}. Active when {@code hyflo.ai.enabled=false}. */
@Component
@ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpAssistantAgentAdapter implements AssistantPort {

    @Override
    public AssistantResponseDTO chat(AssistantMessageDTO message) {
        return new AssistantResponseDTO(
                message.sessionId(),
                "The HyFlo AI assistant is currently unavailable (hyflo.ai.enabled=false).",
                "NONE", 0, 0, true, Instant.now(), message.correlationId());
    }
}