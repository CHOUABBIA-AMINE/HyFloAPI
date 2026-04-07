package dz.sh.trc.hyflo.intelligence.capability;

import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisRequestDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisResultDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnomalyReportDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowForecastDTO;

/**
 * AI capability contract for hydrocarbon flow analysis intelligence.
 *
 * <p>Defines what the flow domain expects from an AI capability:
 * anomaly detection, forecasting, and natural-language analysis
 * of flow measurements from the pipeline network.</p>
 *
 * <p>Implemented by {@code intelligence.flow.service.FlowAnalysisService}.
 * The service uses a port ({@code intelligence.flow.port.FlowAnalysisPort})
 * to delegate actual LLM calls to the platform layer.</p>
 */
public interface FlowAnalysisCapability extends AiCapability {

    /**
     * Analyses a flow reading context and returns an AI-generated
     * natural-language analysis with structured anomaly indicators.
     *
     * @param request flow context data including sensor readings and pipeline ID
     * @return structured analysis result with narrative and anomaly flags
     */
    FlowAnalysisResultDTO analyse(FlowAnalysisRequestDTO request);

    /**
     * Detects anomalies in a set of flow readings and returns a structured report.
     *
     * @param request flow context data
     * @return anomaly report with severity classification and recommended actions
     */
    FlowAnomalyReportDTO detectAnomalies(FlowAnalysisRequestDTO request);

    /**
     * Generates a short-term flow forecast based on historical readings.
     *
     * @param request flow context including historical window
     * @return forecast DTO with predicted values and confidence intervals
     */
    FlowForecastDTO forecast(FlowAnalysisRequestDTO request);
}