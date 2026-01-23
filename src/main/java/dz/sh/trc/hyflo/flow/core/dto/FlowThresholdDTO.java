/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowThresholdDTO
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

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.common.dto.SeverityDTO;
import dz.sh.trc.hyflo.flow.common.model.Severity;
import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Flow threshold DTO for pipeline operational limits configuration")
public class FlowThresholdDTO extends GenericDTO<FlowThreshold> {

    @NotBlank(message = "Threshold name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Schema(description = "Threshold name", example = "Max Pressure Warning", required = true, maxLength = 100)
    private String name;

    @NotBlank(message = "Parameter type is required")
    @Size(max = 50, message = "Parameter type must not exceed 50 characters")
    @Schema(description = "Parameter type", example = "PRESSURE", required = true, maxLength = 50)
    private String parameterType;

    @DecimalMin(value = "0.0", message = "Minimum value cannot be negative")
    @Schema(description = "Minimum acceptable value", example = "50.00")
    private BigDecimal minValue;

    @DecimalMin(value = "0.0", message = "Maximum value cannot be negative")
    @Schema(description = "Maximum acceptable value", example = "100.00")
    private BigDecimal maxValue;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Schema(description = "Threshold description", maxLength = 500)
    private String description;

    // Foreign Key IDs
    @NotNull(message = "Pipeline is required")
    @Schema(description = "Pipeline ID", required = true)
    private Long pipelineId;

    @NotNull(message = "Severity is required")
    @Schema(description = "Severity ID", required = true)
    private Long severityId;

    // Nested DTOs
    @Schema(description = "Pipeline details")
    private PipelineDTO pipeline;

    @Schema(description = "Severity details")
    private SeverityDTO severity;

    @Override
    public FlowThreshold toEntity() {
        FlowThreshold entity = new FlowThreshold();
        entity.setId(getId());
        entity.setName(this.name);
        entity.setParameterType(this.parameterType);
        entity.setMinValue(this.minValue);
        entity.setMaxValue(this.maxValue);
        entity.setDescription(this.description);

        if (this.pipelineId != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(this.pipelineId);
            entity.setPipeline(pipeline);
        }

        if (this.severityId != null) {
            Severity severity = new Severity();
            severity.setId(this.severityId);
            entity.setSeverity(severity);
        }

        return entity;
    }

    @Override
    public void updateEntity(FlowThreshold entity) {
        if (this.name != null) entity.setName(this.name);
        if (this.parameterType != null) entity.setParameterType(this.parameterType);
        if (this.minValue != null) entity.setMinValue(this.minValue);
        if (this.maxValue != null) entity.setMaxValue(this.maxValue);
        if (this.description != null) entity.setDescription(this.description);

        if (this.pipelineId != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(this.pipelineId);
            entity.setPipeline(pipeline);
        }

        if (this.severityId != null) {
            Severity severity = new Severity();
            severity.setId(this.severityId);
            entity.setSeverity(severity);
        }
    }

    public static FlowThresholdDTO fromEntity(FlowThreshold entity) {
        if (entity == null) return null;

        return FlowThresholdDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .parameterType(entity.getParameterType())
                .minValue(entity.getMinValue())
                .maxValue(entity.getMaxValue())
                .description(entity.getDescription())

                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)
                .severityId(entity.getSeverity() != null ? entity.getSeverity().getId() : null)

                .pipeline(entity.getPipeline() != null ? PipelineDTO.fromEntity(entity.getPipeline()) : null)
                .severity(entity.getSeverity() != null ? SeverityDTO.fromEntity(entity.getSeverity()) : null)
                .build();
    }
}
