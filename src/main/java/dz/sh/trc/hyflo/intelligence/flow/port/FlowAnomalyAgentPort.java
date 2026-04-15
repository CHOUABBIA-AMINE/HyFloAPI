package dz.sh.trc.hyflo.intelligence.flow.port;

import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisRequestDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnalysisResultDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowAnomalyReportDTO;
import dz.sh.trc.hyflo.intelligence.flow.dto.FlowForecastDTO;

/**
 * Outbound port connecting the flow intelligence service to the AI platform layer.
 *
 * <p>This interface is the boundary between the {@code intelligence/flow} domain
 * and the actual LLM adapter in {@code platform/ai}. The service depends on this
 * port — never on Spring AI classes directly.</p>
 *
 * <p>The implementation lives in
 * {@code dz.sh.trc.hyflo.platform.ai.adapter.FlowAnomalyAgentAdapter}
 * and is injected at runtime by Spring via the {@code @ConditionalOnProperty}
 * guard ({@code hyflo.ai.enabled=true}).</p>
 *
 * <p>When AI is disabled, a {@code NoOpFlowAnomalyAgentAdapter} is injected
 * instead, returning safe fallback responses without calling any LLM.</p>
 */
public interface FlowAnomalyAgentPort {

    /**
     * Sends flow readings to the LLM and returns a structured analysis.
     *
     * @param request the flow context including readings and pipeline reference
     * @return AI-generated flow analysis result
     */
    FlowAnalysisResultDTO analyse(FlowAnalysisRequestDTO request);

    /**
     * Detects anomalies in the provided flow readings.
     *
     * @param request the flow context
     * @return structured anomaly report with per-sensor findings
     */
    FlowAnomalyReportDTO detectAnomalies(FlowAnalysisRequestDTO request);

    /**
     * Generates a short-term flow forecast based on historical readings.
     *
     * @param request the flow context including the historical window
     * @return forecast DTO with predicted values and confidence intervals
     */
    FlowForecastDTO forecast(FlowAnalysisRequestDTO request);
}