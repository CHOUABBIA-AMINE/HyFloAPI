package dz.sh.trc.hyflo.intelligence.assistant.controller;

import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantRequestDTO;
import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantResponseDTO;
import dz.sh.trc.hyflo.intelligence.assistant.service.OperatorAssistantService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing the AI-powered operator assistant endpoints.
 *
 * <p>URI base: {@code /intelligence/assistant} — consistent with the HyFloAPI
 * module-based URI convention. No new endpoint format introduced.</p>
 *
 * <p>No WebSocket or streaming in this commit.
 * Zero business logic — full delegation to {@link OperatorAssistantService}.</p>
 *
 * <h3>Endpoints</h3>
 * <ul>
 *   <li>{@code POST   /intelligence/assistant/chat}              — send a message</li>
 *   <li>{@code GET    /intelligence/assistant/history/{sessionId}} — fetch history</li>
 *   <li>{@code DELETE /intelligence/assistant/session/{sessionId}} — clear memory</li>
 * </ul>
 */
@Validated
@RestController
@RequestMapping("/intelligence/assistant")
public class AssistantController {

    private final OperatorAssistantService operatorAssistantService;

    public AssistantController(OperatorAssistantService operatorAssistantService) {
        this.operatorAssistantService = operatorAssistantService;
    }

    /**
     * Sends an operator message to the AI assistant and returns the reply.
     *
     * <p>POST /intelligence/assistant/chat</p>
     */
    @PostMapping("/chat")
    @PreAuthorize("hasAuthority('INTELLIGENCE_ASSISTANT_USE')")
    public ResponseEntity<AssistantResponseDTO> chat(
            @Valid @RequestBody AssistantRequestDTO request) {
        return ResponseEntity.ok(operatorAssistantService.chat(request));
    }

    /**
     * Retrieves the ordered conversation history for a session.
     *
     * <p>GET /intelligence/assistant/history/{sessionId}</p>
     */
    @GetMapping("/history/{sessionId}")
    @PreAuthorize("hasAuthority('INTELLIGENCE_ASSISTANT_USE')")
    public ResponseEntity<List<AssistantResponseDTO>> getHistory(
            @PathVariable
            @NotBlank(message = "Session ID must not be blank")
            String sessionId) {
        return ResponseEntity.ok(operatorAssistantService.getHistory(sessionId));
    }

    /**
     * Clears all conversation memory for a session.
     *
     * <p>DELETE /intelligence/assistant/session/{sessionId}</p>
     */
    @DeleteMapping("/session/{sessionId}")
    @PreAuthorize("hasAuthority('INTELLIGENCE_ASSISTANT_USE')")
    public ResponseEntity<Void> clearSession(
            @PathVariable
            @NotBlank(message = "Session ID must not be blank")
            String sessionId) {
        operatorAssistantService.clearSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}