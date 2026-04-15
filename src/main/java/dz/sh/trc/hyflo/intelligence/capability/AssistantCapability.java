package dz.sh.trc.hyflo.intelligence.capability;

import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantRequestDTO;
import dz.sh.trc.hyflo.intelligence.assistant.dto.AssistantResponseDTO;

/**
 * AI capability contract for the conversational HyFlo assistant.
 *
 * <p>Defines what the assistant domain expects from an AI capability:
 * free-form question answering, contextual follow-up, and
 * domain-aware conversation grounded in HyFlo operational data.</p>
 *
 * <p>Implemented by {@code intelligence.assistant.service.AssistantService}.
 * The service delegates LLM calls through
 * {@code intelligence.assistant.port.AssistantPort}.</p>
 */
public interface AssistantCapability extends AiCapability {

    /**
     * Processes a user message in the context of the HyFlo assistant session
     * and returns an AI-generated response.
     *
     * @param message the user's message including session context
     * @return the assistant's response with generated text and metadata
     */
    AssistantResponseDTO chat(AssistantRequestDTO message);
}