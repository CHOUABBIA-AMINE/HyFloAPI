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
import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
@Schema(description = "Flow threshold DTO for pipeline operating thresholds configuration")
public class FlowThresholdDTO extends GenericDTO<FlowThreshold> {

    @NotNull(message = "Minimum pressure is required")
    @PositiveOrZero(message = "Minimum pressure must be zero or positive")
    @Schema(description = "Minimum safe pressure (bar)", example = "50.0", requiredMode = RequiredMode.REQUIRED)
    private BigDecimal pressureMin;

    @NotNull(message = "Maximum pressure is required")
    @DecimalMax(value = "500.0", message = "Maximum pressure exceeds absolute limit")
    @Schema(description = "Maximum safe pressure (bar)", example = "120.0", requiredMode = RequiredMode.REQUIRED, maximum = "500.0")
    private BigDecimal pressureMax;

    @NotNull(message = "Minimum temperature is required")
    @DecimalMin(value = "-50.0", message = "Minimum temperature below absolute limit")
    @Schema(description = "Minimum safe temperature (°C)", example = "5.0", requiredMode = RequiredMode.REQUIRED, minimum = "-50.0")
    private BigDecimal temperatureMin;

    @NotNull(message = "Maximum temperature is required")
    @DecimalMax(value = "200.0", message = "Maximum temperature exceeds absolute limit")
    @Schema(description = "Maximum safe temperature (°C)", example = "85.0", requiredMode = RequiredMode.REQUIRED, maximum = "200.0")
    private BigDecimal temperatureMax;

    @NotNull(message = "Minimum flow rate is required")
    @PositiveOrZero(message = "Minimum flow rate must be zero or positive")
    @Schema(description = "Minimum acceptable flow rate (m³/h)", example = "500.0", requiredMode = RequiredMode.REQUIRED)
    private BigDecimal flowRateMin;

    @NotNull(message = "Maximum flow rate is required")
    @PositiveOrZero(message = "Maximum flow rate must be positive")
    @Schema(description = "Maximum acceptable flow rate (m³/h)", example = "2000.0", requiredMode = RequiredMode.REQUIRED)
    private BigDecimal flowRateMax;

    @NotNull(message = "Minimum contained volume is required")
    @PositiveOrZero(message = "Minimum contained volume must be zero or positive")
    @Schema(description = "Minimum acceptable contained volume (m³)", example = "500.0", requiredMode = RequiredMode.REQUIRED)
    private BigDecimal containedVolumeMin;

    @NotNull(message = "Maximum contained volume is required")
    @PositiveOrZero(message = "Maximum contained volume must be positive")
    @Schema(description = "Maximum acceptable contained volume (m³)", example = "40000.0", requiredMode = RequiredMode.REQUIRED)
    private BigDecimal containedVolumeMax;

    @NotNull(message = "Alert tolerance is required")
    @DecimalMin(value = "0.0", message = "Alert tolerance cannot be negative")
    @DecimalMax(value = "50.0", message = "Alert tolerance cannot exceed 50%")
    @Schema(description = "Alert tolerance percentage (±5%)", example = "5.0", requiredMode = RequiredMode.REQUIRED, minimum = "0.0", maximum = "50.0")
    private BigDecimal alertTolerance;

    @NotNull(message = "Active status is required")
    @Schema(description = "Is this threshold active", example = "true", requiredMode = RequiredMode.REQUIRED)
    private Boolean active;

    // Foreign Key IDs
    @NotNull(message = "Pipeline is required")
    @Schema(description = "Pipeline ID", requiredMode = RequiredMode.REQUIRED)
    private Long pipelineId;

    // Nested DTOs
    @Schema(description = "Pipeline details")
    private PipelineDTO pipeline;

    @Override
    public FlowThreshold toEntity() {
        FlowThreshold entity = new FlowThreshold();
        entity.setId(getId());
        entity.setPressureMin(this.pressureMin);
        entity.setPressureMax(this.pressureMax);
        entity.setTemperatureMin(this.temperatureMin);
        entity.setTemperatureMax(this.temperatureMax);
        entity.setFlowRateMin(this.flowRateMin);
        entity.setFlowRateMax(this.flowRateMax);
        entity.setContainedVolumeMin(this.containedVolumeMin);
        entity.setContainedVolumeMax(this.containedVolumeMax);
        entity.setAlertTolerance(this.alertTolerance);
        entity.setActive(this.active);

        if (this.pipelineId != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(this.pipelineId);
            entity.setPipeline(pipeline);
        }

        return entity;
    }

    @Override
    public void updateEntity(FlowThreshold entity) {
        if (this.pressureMin != null) entity.setPressureMin(this.pressureMin);
        if (this.pressureMax != null) entity.setPressureMax(this.pressureMax);
        if (this.temperatureMin != null) entity.setTemperatureMin(this.temperatureMin);
        if (this.temperatureMax != null) entity.setTemperatureMax(this.temperatureMax);
        if (this.flowRateMin != null) entity.setFlowRateMin(this.flowRateMin);
        if (this.flowRateMax != null) entity.setFlowRateMax(this.flowRateMax);
        if (this.containedVolumeMin != null) entity.setContainedVolumeMin(this.containedVolumeMin);
        if (this.containedVolumeMax != null) entity.setContainedVolumeMax(this.containedVolumeMax);
        if (this.alertTolerance != null) entity.setAlertTolerance(this.alertTolerance);
        if (this.active != null) entity.setActive(this.active);

        if (this.pipelineId != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(this.pipelineId);
            entity.setPipeline(pipeline);
        }
    }

    public static FlowThresholdDTO fromEntity(FlowThreshold entity) {
        if (entity == null) return null;

        return FlowThresholdDTO.builder()
                .id(entity.getId())
                .pressureMin(entity.getPressureMin())
                .pressureMax(entity.getPressureMax())
                .temperatureMin(entity.getTemperatureMin())
                .temperatureMax(entity.getTemperatureMax())
                .flowRateMin(entity.getFlowRateMin())
                .flowRateMax(entity.getFlowRateMax())
                .containedVolumeMin(entity.getContainedVolumeMin())
                .containedVolumeMax(entity.getContainedVolumeMin())
                .alertTolerance(entity.getAlertTolerance())
                .active(entity.getActive())

                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)

                .pipeline(entity.getPipeline() != null ? PipelineDTO.fromEntity(entity.getPipeline()) : null)
                .build();
    }
}
