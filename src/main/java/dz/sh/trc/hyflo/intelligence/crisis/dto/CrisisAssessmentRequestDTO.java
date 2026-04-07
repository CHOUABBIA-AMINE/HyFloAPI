package dz.sh.trc.hyflo.intelligence.crisis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

/**
 * Request DTO carrying crisis event context for AI assessment.
 *
 * <p>Passed from the controller into {@link
 * dz.sh.trc.hyflo.intelligence.crisis.service.CrisisAssessmentAgentService}
 * and forwarded to the platform layer via {@link
 * dz.sh.trc.hyflo.intelligence.crisis.port.CrisisAgentPort}.</p>
 */
public record CrisisAssessmentRequestDTO(

        @NotBlank(message = "Crisis event ID must not be blank")
        String crisisEventId,

        @NotBlank(message = "Pipeline ID must not be blank")
        String pipelineId,

        @NotBlank(message = "Location reference must not be blank")
        String locationRef,

        /**
         * Crisis type: LEAK | RUPTURE | PRESSURE_SURGE | CONTAMINATION
         * | EQUIPMENT_FAILURE | UNKNOWN
         */
        @NotBlank(message = "Crisis type must not be blank")
        String crisisType,

        @NotNull(message = "Event detected-at timestamp is required")
        Instant detectedAt,

        /**
         * Sensor readings captured at the time of the event.
         * Injected into the LLM prompt as structured context.
         */
        @NotNull(message = "Sensor readings must not be null")
        List<CrisisSensorReadingDTO> sensorReadings,

        /**
         * Optional operator observation note injected as LLM context.
         */
        String operatorNote,

        String correlationId
) {
    /**
     * A single sensor measurement captured during the crisis event.
     */
    public record CrisisSensorReadingDTO(

            @NotBlank(message = "Sensor ID must not be blank")
            String sensorId,

            String sensorType,
            double value,
            String unit,
            Instant capturedAt
    ) {}
}