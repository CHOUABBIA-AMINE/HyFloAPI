/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingCommandDto
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Command
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command DTO for creating or updating a FlowReading.
 *
 * Carries only write-side fields.
 * Validation annotations belong exclusively on this class.
 * Mapping logic lives in FlowReadingMapper — not here.
 */
@Schema(description = "Command DTO for recording or updating an operational flow reading on a pipeline")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowReadingCommandDto {

    @Schema(description = "Date when the reading was taken", example = "2026-03-20",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Reading date is required")
    @PastOrPresent(message = "Reading date cannot be in the future")
    private LocalDate readingDate;

    @Schema(description = "Measured flow volume in m\u00b3", example = "12500.5000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "0.0")
    @PositiveOrZero(message = "Volume (m\u00b3) must be zero or positive")
    private BigDecimal volumeM3;

    @Schema(description = "Measured flow volume in MSCF", example = "441.4500",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "0.0")
    @PositiveOrZero(message = "Volume (MSCF) must be zero or positive")
    private BigDecimal volumeMscf;

    @Schema(description = "Inlet pressure in bar", example = "68.5000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "0.0", maximum = "500.0")
    @DecimalMin(value = "0.0", message = "Inlet pressure cannot be negative")
    @DecimalMax(value = "500.0", message = "Inlet pressure exceeds maximum (500 bar)")
    private BigDecimal inletPressureBar;

    @Schema(description = "Outlet pressure in bar", example = "65.2000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "0.0", maximum = "500.0")
    @DecimalMin(value = "0.0", message = "Outlet pressure cannot be negative")
    @DecimalMax(value = "500.0", message = "Outlet pressure exceeds maximum (500 bar)")
    private BigDecimal outletPressureBar;

    @Schema(description = "Temperature in Celsius", example = "22.3000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "-50.0", maximum = "200.0")
    @DecimalMin(value = "-50.0", message = "Temperature below minimum range")
    @DecimalMax(value = "200.0", message = "Temperature exceeds maximum range")
    private BigDecimal temperatureCelsius;

    @Schema(description = "Operator notes", example = "Normal operational conditions",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 500)
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    @Schema(description = "Submission timestamp", example = "2026-03-20T06:00:00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @PastOrPresent(message = "Submission timestamp cannot be in the future")
    private LocalDateTime submittedAt;

    // --- Foreign Key references (write-side only) ---

    @Schema(description = "ID of the pipeline where this reading was taken", example = "101",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Pipeline ID is required")
    private Long pipelineId;

    @Schema(description = "ID of the reading slot", example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Reading slot ID is required")
    private Long readingSlotId;

    @Schema(description = "ID of the validation status", example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Validation status ID is required")
    private Long validationStatusId;

    @Schema(description = "ID of the employee who recorded this reading", example = "234",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Recording employee ID is required")
    private Long recordedById;

    @Schema(description = "ID of the data source for this reading", example = "3",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long dataSourceId;

    @Schema(description = "ID of the workflow instance governing this reading lifecycle",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long workflowInstanceId;
}
