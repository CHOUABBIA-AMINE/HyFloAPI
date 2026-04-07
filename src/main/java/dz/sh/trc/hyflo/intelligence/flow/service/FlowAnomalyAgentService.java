package dz.sh.trc.hyflo.intelligence.flow.service;

import dz.sh.trc.hyflo.intelligence.capability.FlowAnalysisCapability;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisRequestDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisResultDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnomalyReportDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowForecastDTO;
import dz.sh.trc.hyflo.intelligence.flow.port.FlowAnomalyAgentPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Entry point for all AI-powered flow analysis use cases.
 *
 * <p>Implements {@link FlowAnalysisCapability} — the contract declared in
 * the capability layer. Delegates all LLM calls through
 * {@link FlowAnomalyAgentPort} — never calls Spring AI directly.</p>
 *
 * <p>Controller layer calls this service using the existing HyFloAPI
 * response/request DTO style. No new endpoint format introduced.</p>
 *
 * <h3>Dependency direction (strict):</h3>
 * <pre>
 *   Controller
 *       ↓
 *   FlowAnomalyAgentService  (this class — intelligence layer)
 *       ↓
 *   FlowAnomalyAgentPort     (outbound port — intelligence layer)
 *       ↓
 *   FlowAnomalyAgentAdapter  (platform/ai — Spring AI adapter)
 * </pre>
 */
@Service
public class FlowAnomalyAgentService implements FlowAnalysisCapability {

    private static final Logger log =
            LoggerFactory.getLogger(FlowAnomalyAgentService.class);

    private final FlowAnomalyAgentPort flowAnomalyAgentPort;

    public FlowAnomalyAgentService(FlowAnomalyAgentPort flowAnomalyAgentPort) {
        this.flowAnomalyAgentPort =
                Objects.requireNonNull(flowAnomalyAgentPort, "FlowAnomalyAgentPort must not be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Validates the request is non-null, logs the invocation for traceability,
     * then delegates to the platform port.</p>
     */
    @Override
    public FlowAnalysisResultDTO analyse(FlowAnalysisRequestDTO request) {
        Objects.requireNonNull(request, "FlowAnalysisRequestDTO must not be null");
        log.info("[FlowAnomalyAgentService] analyse — pipeline={} correlationId={}",
                request.pipelineId(), request.correlationId());
        return flowAnomalyAgentPort.analyse(request);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delegates anomaly detection to the AI platform port and returns
     * the structured report. The controller receives exactly this DTO.</p>
     */
    @Override
    public FlowAnomalyReportDTO detectAnomalies(FlowAnalysisRequestDTO request) {
        Objects.requireNonNull(request, "FlowAnalysisRequestDTO must not be null");
        log.info("[FlowAnomalyAgentService] detectAnomalies — pipeline={} correlationId={}",
                request.pipelineId(), request.correlationId());
        return flowAnomalyAgentPort.detectAnomalies(request);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delegates forecast generation to the AI platform port.</p>
     */
    @Override
    public FlowForecastDTO forecast(FlowAnalysisRequestDTO request) {
        Objects.requireNonNull(request, "FlowAnalysisRequestDTO must not be null");
        log.info("[FlowAnomalyAgentService] forecast — pipeline={} correlationId={}",
                request.pipelineId(), request.correlationId());
        return flowAnomalyAgentPort.forecast(request);
    }
}