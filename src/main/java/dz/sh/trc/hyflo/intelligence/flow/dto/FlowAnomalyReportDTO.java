package dz.sh.trc.hyflo.intelligence.flow.dto;

import java.time.Instant;
import java.util.List;

/**
 * Structured anomaly report returned by the AI anomaly detection use case.
 */
public record FlowAnomalyReportDTO(

        String pipelineId,
        String segmentRef,

        /**
         * Total number of anomalies detected in the analysis window.
         */
        int anomalyCount,

        List<DetectedAnomalyDTO> anomalies,

        /**
         * AI-generated summary of the anomaly pattern observed.
         */
        String summary,

        String aiProvider,
        Instant generatedAt,
        String correlationId
) {
    /**
     * A single detected anomaly with classification and context.
     */
    public record DetectedAnomalyDTO(

            String sensorId,
            Instant detectedAt,

            /**
             * Anomaly type: PRESSURE_DROP | FLOW_SURGE | TEMPERATURE_SPIKE
             * | FLATLINE | OSCILLATION | UNKNOWN
             */
            String anomalyType,

            /**
             * Severity: LOW | MEDIUM | HIGH | CRITICAL
             */
            String severity,

            /**
             * AI-generated plain-language description of this anomaly.
             */
            String description,

            /**
             * Recommended immediate action from the AI.
             */
            String recommendedAction
    ) {}
}