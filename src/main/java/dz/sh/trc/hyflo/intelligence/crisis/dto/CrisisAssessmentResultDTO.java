package dz.sh.trc.hyflo.intelligence.crisis.dto;

import java.time.Instant;

/**
 * AI-generated crisis assessment result.
 *
 * <p>Contains the severity score, AI narrative, impact analysis,
 * and metadata for traceability.</p>
 */
public record CrisisAssessmentResultDTO(

        String crisisEventId,
        String pipelineId,
        String locationRef,

        /**
         * AI-computed severity score (0–100).
         * 0 = no threat, 100 = catastrophic.
         */
        int severityScore,

        /**
         * Severity classification derived from score.
         * Values: NONE | LOW | MEDIUM | HIGH | CRITICAL
         */
        String severityLevel,

        /**
         * AI-generated narrative summarising the crisis situation.
         */
        String situationSummary,

        /**
         * AI-generated impact analysis (people, infrastructure, environment).
         */
        String impactAnalysis,

        /**
         * Whether immediate evacuation is recommended by the AI.
         */
        boolean evacuationRecommended,

        String aiProvider,
        int promptTokensUsed,
        int completionTokensUsed,

        Instant assessedAt,
        String correlationId
) {}