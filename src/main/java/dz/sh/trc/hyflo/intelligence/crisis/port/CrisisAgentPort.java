package dz.sh.trc.hyflo.intelligence.crisis.port;

import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentRequestDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentResultDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisRecommendationDTO;

/**
 * Outbound port connecting the crisis intelligence service to the AI platform layer.
 *
 * <p>This interface is the boundary between {@code intelligence/crisis} and
 * the actual LLM adapter in {@code platform/ai}. The service depends on this
 * port exclusively — never on Spring AI classes directly.</p>
 *
 * <p>The live implementation is
 * {@code dz.sh.trc.hyflo.platform.ai.adapter.CrisisAgentAdapter},
 * injected when {@code hyflo.ai.enabled=true}.
 * The no-op fallback {@code NoOpCrisisAgentAdapter} is injected otherwise.</p>
 */
public interface CrisisAgentPort {

    /**
     * Assesses the crisis event and returns a structured AI evaluation
     * with severity score, narrative, and impact analysis.
     *
     * @param request crisis context including event type, location, and sensor data
     * @return structured assessment result
     */
    CrisisAssessmentResultDTO assess(CrisisAssessmentRequestDTO request);

    /**
     * Generates prioritised operational recommendations for the active crisis.
     *
     * @param request crisis context
     * @return structured recommendation with ordered action items
     */
    CrisisRecommendationDTO recommend(CrisisAssessmentRequestDTO request);
}