package dz.sh.trc.hyflo.intelligence.network.dto;

import java.time.Instant;
import java.util.List;

/**
 * AI-generated operational insight for a pipeline network segment.
 */
public record NetworkInsightResultDTO(

        String networkSegmentId,
        String topologyZone,

        /**
         * AI-generated narrative describing the current network health.
         */
        String insightNarrative,

        /**
         * Overall network health score for the segment (0–100).
         */
        int networkHealthScore,

        /**
         * Key operational indicators surfaced by the AI.
         */
        List<KeyIndicatorDTO> keyIndicators,

        /**
         * Whether the AI detects a risk of cascading failure.
         */
        boolean cascadingRiskDetected,

        String aiProvider,
        int promptTokensUsed,
        int completionTokensUsed,

        Instant analysedAt,
        String correlationId
) {
    /**
     * A single key indicator flagged by the AI during analysis.
     */
    public record KeyIndicatorDTO(

            String nodeId,
            String indicatorType,

            /**
             * Significance level: INFO | WARNING | CRITICAL
             */
            String significance,

            String description
    ) {}
}