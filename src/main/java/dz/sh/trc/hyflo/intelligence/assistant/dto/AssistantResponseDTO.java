package dz.sh.trc.hyflo.intelligence.assistant.dto;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO returned by the HyFlo assistant for a single conversation turn.
 *
 * <p>Contains the AI-generated reply, session metadata, detected intent,
 * token usage, and timing — sufficient for the frontend to render the
 * response and manage session state.</p>
 */
public record AssistantResponseDTO(

        /**
         * AI-generated reply text to the operator's message.
         */
        String reply,

        /**
         * Session identifier to reuse on the next turn.
         * Newly assigned if the request had no sessionId.
         */
        String sessionId,

        /**
         * Detected intent of the operator's message.
         * Examples: FLOW_QUERY | CRISIS_QUERY | NETWORK_QUERY
         *           | FORECAST_REQUEST | GENERAL_QUESTION | UNKNOWN
         */
        String detectedIntent,

        /**
         * Confidence score for the detected intent (0.0–1.0).
         */
        double intentConfidence,

        /**
         * Suggested follow-up questions the operator may want to ask.
         * Empty list if the AI has no suggestions.
         */
        List<String> suggestedFollowUps,

        /**
         * Provider that generated this response (OPENAI | OLLAMA | NONE).
         */
        String aiProvider,

        int promptTokensUsed,
        int completionTokensUsed,

        /**
         * Wall-clock latency of the LLM call in milliseconds.
         */
        long latencyMs,

        Instant respondedAt,
        String correlationId
) {}