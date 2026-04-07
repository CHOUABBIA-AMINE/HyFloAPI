package dz.sh.trc.hyflo.intelligence.crisis.service;

import dz.sh.trc.hyflo.intelligence.capability.CrisisAssessmentCapability;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentRequestDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentResultDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisRecommendationDTO;
import dz.sh.trc.hyflo.intelligence.crisis.port.CrisisAgentPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Entry point for all AI-powered crisis assessment use cases.
 *
 * <p>Implements {@link CrisisAssessmentCapability} — the contract declared
 * in the capability layer. Delegates all LLM calls through
 * {@link CrisisAgentPort} — never calls Spring AI directly.</p>
 *
 * <h3>Dependency direction (strict):</h3>
 * <pre>
 *   CrisisAssessmentAgentController
 *               ↓
 *   CrisisAssessmentAgentService  (this — intelligence layer)
 *               ↓
 *   CrisisAgentPort               (outbound port — intelligence layer)
 *               ↓ (injected at runtime)
 *   CrisisAgentAdapter            (platform/ai — Spring AI adapter)
 * </pre>
 */
@Service
public class CrisisAssessmentAgentService implements CrisisAssessmentCapability {

    private static final Logger log =
            LoggerFactory.getLogger(CrisisAssessmentAgentService.class);

    private final CrisisAgentPort crisisAgentPort;

    public CrisisAssessmentAgentService(CrisisAgentPort crisisAgentPort) {
        this.crisisAgentPort =
                Objects.requireNonNull(crisisAgentPort, "CrisisAgentPort must not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CrisisAssessmentResultDTO assess(CrisisAssessmentRequestDTO request) {
        Objects.requireNonNull(request, "CrisisAssessmentRequestDTO must not be null");
        log.info("[CrisisAssessmentAgentService] assess — crisisEventId={} correlationId={}",
                request.crisisEventId(), request.correlationId());
        return crisisAgentPort.assess(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CrisisRecommendationDTO recommend(CrisisAssessmentRequestDTO request) {
        Objects.requireNonNull(request, "CrisisAssessmentRequestDTO must not be null");
        log.info("[CrisisAssessmentAgentService] recommend — crisisEventId={} correlationId={}",
                request.crisisEventId(), request.correlationId());
        return crisisAgentPort.recommend(request);
    }
}