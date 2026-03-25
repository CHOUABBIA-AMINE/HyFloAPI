/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DerivedFlowReadingCommandDto
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command DTO for system-driven creation of a DerivedFlowReading.
 *
 * Derived readings are calculated from a source raw FlowReading and anchored
 * to a specific PipelineSegment. They are not entered by operators directly.
 */
@Schema(description = "Command DTO for system-driven creation of a derived flow reading")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DerivedFlowReadingCommandDto {

    @Schema(description = "ID of the source raw FlowReading this derived reading is calculated from",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Source reading ID is required")
    private Long sourceReadingId;

    @Schema(description = "ID of the pipeline segment this derived reading is anchored to",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Pipeline segment ID is required")
    private Long pipelineSegmentId;

    @Schema(description = "Reference date of this derived reading", example = "2026-03-20",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Reading date is required")
    @PastOrPresent(message = "Reading date cannot be in the future")
    private LocalDate readingDate;

    @Schema(description = "ID of the reading slot associated with this derived reading",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long readingSlotId;

    @Schema(description = "ID of the data source used for derivation",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long dataSourceId;

    @Schema(description = "ID of the validation status for this derived reading",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long validationStatusId;

    @Schema(description = "Timestamp when this derived reading was calculated",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @PastOrPresent(message = "Calculation timestamp cannot be in the future")
    private LocalDateTime calculatedAt;

    // --- Derived measurement values ---

    @Schema(description = "Derived pressure in bar", example = "66.5000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "0.0", maximum = "500.0")
    @DecimalMin(value = "0.0", message = "Pressure cannot be negative")
    @DecimalMax(value = "500.0", message = "Pressure exceeds maximum (500 bar)")
    private BigDecimal pressure;

    @Schema(description = "Derived temperature in degrees Celsius", example = "21.8000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "-50.0", maximum = "200.0")
    @DecimalMin(value = "-50.0", message = "Temperature below minimum range")
    @DecimalMax(value = "200.0", message = "Temperature exceeds maximum range")
    private BigDecimal temperature;

    @Schema(description = "Derived flow rate in m\u00b3/h or bpd", example = "1100.0000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @PositiveOrZero(message = "Flow rate must be zero or positive")
    private BigDecimal flowRate;

    @Schema(description = "Derived contained volume in cubic meters", example = "4500.0000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @PositiveOrZero(message = "Contained volume must be zero or positive")
    private BigDecimal containedVolume;

    @Schema(description = "Calculation method or model identifier used for derivation",
            example = "LINEAR_INTERPOLATION_V1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String calculationMethod;
}
