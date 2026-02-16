/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026 - CRITICAL: Fixed redundant validation and wrong @Schema descriptions
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.flow.common.dto.ValidationStatusDTO;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for FlowReading entity.
 * Used for API requests and responses related to flow measurements.
 */
@Schema(description = "Flow measurement reading DTO capturing real-time pipeline operational parameters")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowReadingDTO extends GenericDTO<FlowReading> {

    @Schema(
        description = "Date when the reading was taken",
        example = "2026-01-27",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Reading date is required")
    @PastOrPresent(message = "Reading date cannot be in the future")
    private LocalDate readingDate;

    @Schema(
        description = "Pressure measurement in bar",
        example = "85.50",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        minimum = "0.0",
        maximum = "500.0"
    )
    @DecimalMin(value = "0.0", message = "Pressure cannot be negative")
    @DecimalMax(value = "500.0", message = "Pressure exceeds maximum (500 bar)")
    private BigDecimal pressure;

    @Schema(
        description = "Temperature measurement in degrees Celsius",
        example = "65.5",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        minimum = "-50.0",
        maximum = "200.0"
    )
    @DecimalMin(value = "-50.0", message = "Temperature below minimum range")
    @DecimalMax(value = "200.0", message = "Temperature exceeds maximum range")
    private BigDecimal temperature;

    @Schema(
        description = "Flow rate in cubic meters per hour (m³/h) or barrels per day (bpd)",
        example = "1250.75",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PositiveOrZero(message = "Flow rate must be zero or positive")
    private BigDecimal flowRate;

    @Schema(
        description = "Total volume contained in pipeline segment in cubic meters",
        example = "5000.00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PositiveOrZero(message = "Contained volume must be zero or positive")
    private BigDecimal containedVolume;

    @Schema(
        description = "Timestamp when this reading was recorded by the operator or system",
        example = "2026-01-22T00:15:00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Recording timestamp is required")
    @PastOrPresent(message = "Recording time cannot be in the future")
    private LocalDateTime recordedAt;

    @Schema(
        description = "Timestamp when this reading was validated by supervisor",
        example = "2026-01-22T08:30:00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Validation time cannot be in the future")
    private LocalDateTime validatedAt;

    @Schema(
        description = "Additional notes about this reading (anomalies, calibration, maintenance, etc.)",
        example = "Sensor calibrated this morning, reading within normal parameters",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 500
    )
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    // Foreign Key IDs
    @Schema(
        description = "ID of the employee who recorded this reading",
        example = "234",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Recording employee is required")
    private Long recordedById;

    @Schema(
        description = "ID of the employee who validated this reading",
        example = "345",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long validatedById;

    @Schema(
        description = "ID of the current validation status (pending, approved, rejected)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Validation status is required")
    private Long validationStatusId;

    @Schema(
        description = "ID of the pipeline where this reading was taken",
        example = "101",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Pipeline is required")
    private Long pipelineId;

    @Schema(
        description = "ID of the reading slot (time window for scheduled readings)",
        example = "2",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Reading slot is required")
    private Long readingSlotId;

    // Nested DTOs
    @Schema(
        description = "Details of the employee who recorded this reading",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private EmployeeDTO recordedBy;

    @Schema(
        description = "Details of the employee who validated this reading",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private EmployeeDTO validatedBy;

    @Schema(
        description = "Details of the validation status",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private ValidationStatusDTO validationStatus;

    @Schema(
        description = "Details of the pipeline",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private PipelineDTO pipeline;

    @Schema(
        description = "Details of the reading slot",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private ReadingSlotDTO readingSlot;

    @Override
    public FlowReading toEntity() {
        FlowReading entity = new FlowReading();
        entity.setId(getId());
        entity.setReadingDate(this.readingDate);
        entity.setPressure(this.pressure);
        entity.setTemperature(this.temperature);
        entity.setFlowRate(this.flowRate);
        entity.setContainedVolume(this.containedVolume);
        entity.setRecordedAt(this.recordedAt);
        entity.setValidatedAt(this.validatedAt);
        entity.setNotes(this.notes);

        if (this.recordedById != null) {
            Employee employee = new Employee();
            employee.setId(this.recordedById);
            entity.setRecordedBy(employee);
        }

        if (this.validatedById != null) {
            Employee employee = new Employee();
            employee.setId(this.validatedById);
            entity.setValidatedBy(employee);
        }

        if (this.validationStatusId != null) {
            ValidationStatus status = new ValidationStatus();
            status.setId(this.validationStatusId);
            entity.setValidationStatus(status);
        }

        if (this.pipelineId != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(this.pipelineId);
            entity.setPipeline(pipeline);
        }

        if (this.readingSlotId != null) {
            ReadingSlot readingSlot = new ReadingSlot();
            readingSlot.setId(this.readingSlotId);
            entity.setReadingSlot(readingSlot);
        }

        return entity;
    }

    @Override
    public void updateEntity(FlowReading entity) {
        if (this.readingDate != null) entity.setReadingDate(this.readingDate);
        if (this.pressure != null) entity.setPressure(this.pressure);
        if (this.temperature != null) entity.setTemperature(this.temperature);
        if (this.flowRate != null) entity.setFlowRate(this.flowRate);
        if (this.containedVolume != null) entity.setContainedVolume(this.containedVolume);
        if (this.recordedAt != null) entity.setRecordedAt(this.recordedAt);
        if (this.validatedAt != null) entity.setValidatedAt(this.validatedAt);
        if (this.notes != null) entity.setNotes(this.notes);

        if (this.recordedById != null) {
            Employee employee = new Employee();
            employee.setId(this.recordedById);
            entity.setRecordedBy(employee);
        }

        if (this.validatedById != null) {
            Employee employee = new Employee();
            employee.setId(this.validatedById);
            entity.setValidatedBy(employee);
        }

        if (this.validationStatusId != null) {
            ValidationStatus status = new ValidationStatus();
            status.setId(this.validationStatusId);
            entity.setValidationStatus(status);
        }

        if (this.pipelineId != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(this.pipelineId);
            entity.setPipeline(pipeline);
        }

        if (this.readingSlotId != null) {
            ReadingSlot readingSlot = new ReadingSlot();
            readingSlot.setId(this.readingSlotId);
            entity.setReadingSlot(readingSlot);
        }
    }

    /**
     * Converts a FlowReading entity to its DTO representation.
     *
     * @param entity the FlowReading entity to convert
     * @return FlowReadingDTO or null if entity is null
     */
    public static FlowReadingDTO fromEntity(FlowReading entity) {
        if (entity == null) return null;

        return FlowReadingDTO.builder()
                .id(entity.getId())
                .readingDate(entity.getReadingDate())
                .pressure(entity.getPressure())
                .temperature(entity.getTemperature())
                .flowRate(entity.getFlowRate())
                .containedVolume(entity.getContainedVolume())
                .recordedAt(entity.getRecordedAt())
                .validatedAt(entity.getValidatedAt())
                .notes(entity.getNotes())

                .recordedById(entity.getRecordedBy() != null ? entity.getRecordedBy().getId() : null)
                .validatedById(entity.getValidatedBy() != null ? entity.getValidatedBy().getId() : null)
                .validationStatusId(entity.getValidationStatus() != null ? entity.getValidationStatus().getId() : null)
                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)
                .readingSlotId(entity.getReadingSlot() != null ? entity.getReadingSlot().getId() : null)

                .recordedBy(entity.getRecordedBy() != null ? EmployeeDTO.fromEntity(entity.getRecordedBy()) : null)
                .validatedBy(entity.getValidatedBy() != null ? EmployeeDTO.fromEntity(entity.getValidatedBy()) : null)
                .validationStatus(entity.getValidationStatus() != null ? ValidationStatusDTO.fromEntity(entity.getValidationStatus()) : null)
                .pipeline(entity.getPipeline() != null ? PipelineDTO.fromEntity(entity.getPipeline()) : null)
                .readingSlot(entity.getReadingSlot() != null ? ReadingSlotDTO.fromEntity(entity.getReadingSlot()) : null)
                .build();
    }
}
