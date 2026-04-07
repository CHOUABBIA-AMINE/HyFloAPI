package dz.sh.trc.hyflo.intelligence.flow.dto;

import java.time.Instant;
import java.util.List;

/**
 * Short-term flow forecast produced by the AI.
 */
public record FlowForecastDTO(

        String pipelineId,
        String segmentRef,

        /**
         * Forecast horizon in hours from the last reading timestamp.
         */
        int forecastHorizonHours,

        List<ForecastPointDTO> forecastPoints,

        /**
         * AI confidence level for this forecast (0.0–1.0).
         */
        double confidenceScore,

        /**
         * AI-generated justification for the forecast.
         */
        String rationale,

        String aiProvider,
        Instant generatedAt,
        String correlationId
) {
    /**
     * A single predicted data point in the forecast series.
     */
    public record ForecastPointDTO(

            Instant predictedAt,
            double predictedFlowRateM3h,

            /**
             * Lower bound of the confidence interval.
             */
            double lowerBoundM3h,

            /**
             * Upper bound of the confidence interval.
             */
            double upperBoundM3h
    ) {}
}