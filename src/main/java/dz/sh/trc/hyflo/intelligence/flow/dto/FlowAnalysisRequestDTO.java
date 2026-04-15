package dz.sh.trc.hyflo.intelligence.flow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.List;

/**
 * Request DTO carrying flow context data for AI analysis.
 *
 * <p>Passed from the controller into {@link FlowAnomalyAgentService}
 * and forwarded to the platform layer via {@link
 * dz.sh.trc.hyflo.intelligence.flow.port.FlowAnomalyAgentPort}.</p>
 */
public record FlowAnalysisRequestDTO(

        @NotBlank(message = "Pipeline ID must not be blank")
        String pipelineId,

        @NotBlank(message = "Segment reference must not be blank")
        String segmentRef,

        @NotNull(message = "Analysis window start is required")
        Instant windowStart,

        @NotNull(message = "Analysis window end is required")
        Instant windowEnd,

        @NotNull(message = "Readings list must not be null")
        List<FlowReadingDTO> readings,

        /**
         * Optional free-text note from the operator.
         * Injected into the LLM prompt as additional context.
         */
        String operatorNote,

        /**
         * Correlation ID propagated from the HTTP request header
         * (X-Correlation-ID). Used for tracing across AI calls.
         */
        String correlationId
) {
    /**
     * Embedded value object — a single sensor reading within the window.
     */
    public record FlowReadingDTO(

            @NotBlank(message = "Sensor ID must not be blank")
            String sensorId,

            @NotNull(message = "Measurement timestamp is required")
            Instant measuredAt,

            @Positive(message = "Flow rate must be positive")
            double flowRateM3h,

            double pressureBar,

            double temperatureCelsius
    ) {}
}