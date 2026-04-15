package dz.sh.trc.hyflo.intelligence.flow.dto;

import java.time.Instant;

/**
 * Response DTO returned by the AI flow analysis use case.
 *
 * <p>Contains both the AI-generated narrative and structured
 * indicators consumed by the frontend dashboard.</p>
 */
public record FlowAnalysisResultDTO(

        String pipelineId,
        String segmentRef,

        /**
         * AI-generated natural-language analysis of the flow readings.
         */
        String narrative,

        /**
         * Overall health score computed by the AI (0–100).
         */
        int healthScore,

        /**
         * True if the AI detected at least one anomaly requiring attention.
         */
        boolean anomalyDetected,

        /**
         * Severity level of the most critical finding.
         * Values: NONE | LOW | MEDIUM | HIGH | CRITICAL
         */
        String severityLevel,

        /**
         * Provider that generated this analysis (OPENAI | OLLAMA | NONE).
         */
        String aiProvider,

        int promptTokensUsed,
        int completionTokensUsed,

        Instant analysedAt,
        String correlationId
) {}