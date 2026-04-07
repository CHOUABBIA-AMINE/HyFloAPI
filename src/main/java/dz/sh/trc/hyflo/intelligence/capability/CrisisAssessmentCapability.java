package dz.sh.trc.hyflo.intelligence.capability;

import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentRequestDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentResultDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisRecommendationDTO;

/**
 * AI capability contract for crisis event assessment and response guidance.
 *
 * <p>Defines what the crisis domain expects from an AI capability:
 * severity assessment, situation summarisation, and structured
 * operational recommendations for field teams.</p>
 *
 * <p>Implemented by {@code intelligence.crisis.service.CrisisAssessmentService}.
 * The service delegates LLM calls through
 * {@code intelligence.crisis.port.CrisisAssessmentPort}.</p>
 */
public interface CrisisAssessmentCapability extends AiCapability {

    /**
     * Assesses a reported crisis event and returns a structured AI evaluation
     * including severity score, context summary, and impact analysis.
     *
     * @param request crisis context including event type, location, and sensor data
     * @return structured assessment result with severity and narrative
     */
    CrisisAssessmentResultDTO assess(CrisisAssessmentRequestDTO request);

    /**
     * Generates operational recommendations for responding to an active crisis.
     *
     * @param request crisis context
     * @return structured recommendation with prioritised action steps
     */
    CrisisRecommendationDTO recommend(CrisisAssessmentRequestDTO request);
}