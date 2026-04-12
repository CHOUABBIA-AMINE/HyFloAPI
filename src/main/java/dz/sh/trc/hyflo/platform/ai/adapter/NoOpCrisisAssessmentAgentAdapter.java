package dz.sh.trc.hyflo.platform.ai.adapter;

import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentRequestDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisAssessmentResultDTO;
import dz.sh.trc.hyflo.intelligence.crisis.dto.CrisisRecommendationDTO;
import dz.sh.trc.hyflo.intelligence.crisis.port.CrisisAssessmentPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/** No-op fallback for {@link CrisisAssessmentPort}. Active when {@code hyflo.ai.enabled=false}. */
@Component
@ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpCrisisAssessmentAgentAdapter implements CrisisAssessmentPort {

    @Override
    public CrisisAssessmentResultDTO assess(CrisisAssessmentRequestDTO request) {
        return new CrisisAssessmentResultDTO(
                request.eventType(), request.location(),
                -1, "NONE",
                "Crisis assessment unavailable — hyflo.ai.enabled=false.",
                "", false, "NONE", 0, 0, Instant.now(), request.correlationId());
    }

    @Override
    public CrisisRecommendationDTO recommend(CrisisAssessmentRequestDTO request) {
        return new CrisisRecommendationDTO(
                request.eventType(), request.location(),
                "STANDARD",
                List.of("AI recommendations unavailable — hyflo.ai.enabled=false."),
                0, "Unavailable.", "NONE", Instant.now(), request.correlationId());
    }
}