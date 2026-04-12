package dz.sh.trc.hyflo.platform.ai.adapter;

import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisRequestDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisResultDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnomalyReportDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowForecastDTO;
import dz.sh.trc.hyflo.intelligence.flow.port.FlowAnomalyAgentPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/**
 * Safe no-op fallback for {@link FlowAnomalyAgentPort}.
 *
 * <p>Active when {@code hyflo.ai.enabled=false}. Returns deterministic,
 * clearly labelled fallback DTOs. Never calls any LLM. Never throws.</p>
 */
@Component
@ConditionalOnProperty(name = "hyflo.ai.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpFlowAnomalyAgentAdapter implements FlowAnomalyAgentPort {

    private static final Logger log = LoggerFactory.getLogger(NoOpFlowAnomalyAgentAdapter.class);

    @Override
    public FlowAnalysisResultDTO analyse(FlowAnalysisRequestDTO request) {
        log.debug("[NoOpFlowAnomalyAgentAdapter] analyse — AI disabled, returning fallback");
        return new FlowAnalysisResultDTO(
                request.pipelineId(), request.segmentRef(),
                "AI analysis unavailable — hyflo.ai.enabled=false.",
                -1, false, "NONE", "NONE", 0, 0, Instant.now(), request.correlationId());
    }

    @Override
    public FlowAnomalyReportDTO detectAnomalies(FlowAnalysisRequestDTO request) {
        log.debug("[NoOpFlowAnomalyAgentAdapter] detectAnomalies — AI disabled");
        return new FlowAnomalyReportDTO(
                request.pipelineId(), request.segmentRef(),
                0, List.of(),
                "Anomaly detection unavailable — hyflo.ai.enabled=false.",
                "NONE", Instant.now(), request.correlationId());
    }

    @Override
    public FlowForecastDTO forecast(FlowAnalysisRequestDTO request) {
        log.debug("[NoOpFlowAnomalyAgentAdapter] forecast — AI disabled");
        return new FlowForecastDTO(
                request.pipelineId(), request.segmentRef(),
                0, "Forecast unavailable — hyflo.ai.enabled=false.",
                0.0, List.of(), "NONE", Instant.now(), request.correlationId());
    }
}