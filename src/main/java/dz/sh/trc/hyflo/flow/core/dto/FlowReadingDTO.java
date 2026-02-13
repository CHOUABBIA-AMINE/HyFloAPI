/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
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

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Flow measurement reading DTO capturing pipeline operational parameters")
public class FlowReadingDTO extends GenericDTO<FlowReading> {

	@NotNull(message = "Reading date is required")
    @PastOrPresent(message = "Reading date cannot be in the future")
	@Schema(example = "2026-01-27")
    private LocalDate readingDate;
	
	@PositiveOrZero(message = "Pressure must be zero or positive")
    @DecimalMin(value = "0.0", message = "Pressure cannot be negative")
    @DecimalMax(value = "500.0", message = "Pressure exceeds maximum (500 bar)")
    @Schema(description = "Pressure in bar", example = "85.50", minimum = "0.0", maximum = "500.0")
    private BigDecimal pressure;

    @DecimalMin(value = "-50.0", message = "Temperature below minimum range")
    @DecimalMax(value = "200.0", message = "Temperature exceeds maximum range")
    @Schema(description = "Temperature in Celsius", example = "65.5", minimum = "-50.0", maximum = "200.0")
    private BigDecimal temperature;

    @PositiveOrZero(message = "Flow rate must be zero or positive")
    @Schema(description = "Flow rate in m³/h", example = "1250.75")
    private BigDecimal flowRate;

    @PositiveOrZero(message = "Contained volume must be zero or positive")
    @Schema(description = "Contained volume in m³", example = "5000.00")
    private BigDecimal containedVolume;

    @NotNull(message = "Recording timestamp is required")
    @PastOrPresent(message = "Recording time cannot be in the future")
    @Schema(description = "Timestamp when recorded", example = "2026-01-22T00:15:00", required = true)
    private LocalDateTime recordedAt;

    @PastOrPresent(message = "Validation time cannot be in the future")
    @Schema(description = "Timestamp when validated", example = "2026-01-22T08:30:00")
    private LocalDateTime validatedAt;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Schema(description = "Additional notes", example = "Sensor calibrated", maxLength = 500)
    private String notes;

    // Foreign Key IDs
    @NotNull(message = "Recording employee is required")
    @Schema(description = "Recorded by employee ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long recordedById;

    @Schema(description = "Validated by employee ID")
    private Long validatedById;

    @NotNull(message = "Validation status is required")
    @Schema(description = "Validation status ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long validationStatusId;

    @NotNull(message = "Pipeline is required")
    @Schema(description = "Pipeline ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pipelineId;

    @NotNull(message = "Reading slot is required")
    @Schema(description = "Pipeline ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long readingSlotId;

    // Nested DTOs
    @Schema(description = "Recording employee details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private EmployeeDTO recordedBy;

    @Schema(description = "Validating employee details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private EmployeeDTO validatedBy;

    @Schema(description = "Validation status details")
    private ValidationStatusDTO validationStatus;

    @Schema(description = "Pipeline details")
    private PipelineDTO pipeline;

    @Schema(description = "ReadingSlot details")
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
