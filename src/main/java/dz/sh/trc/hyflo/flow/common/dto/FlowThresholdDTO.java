/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdDTO
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: moved from flow.core.dto → flow.common.dto
 *                             Entity import updated to flow.common.model.FlowThreshold
 *
 *  @Type       : Class
 *  @Layer      : DTO (canonical — read + write)
 *  @Package    : Flow / Common
 *
 *  @Description: Combined read/write DTO for FlowThreshold.
 *                Canonical DTO — not split per HyFlo v2 DTO convention.
 *
 **/

package dz.sh.trc.hyflo.flow.common.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.flow.common.model.FlowThreshold;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO for pipeline operating thresholds (create / update / read)")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowThresholdDTO {

    @Schema(description = "Record identifier (null for create requests)")
    private Long id;

    @Schema(description = "ID of the pipeline this threshold applies to", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Pipeline ID is mandatory")
    private Long pipelineId;

    @Schema(description = "Pipeline code (read-only, populated by query)", example = "GZ1-HASSI-ARZEW")
    private String pipelineCode;

    @Schema(description = "Minimum safe pressure (bar)", example = "50.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "pressureMin is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "pressureMin must be positive")
    private BigDecimal pressureMin;

    @Schema(description = "Maximum safe pressure (bar)", example = "120.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "pressureMax is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "pressureMax must be positive")
    private BigDecimal pressureMax;

    @Schema(description = "Minimum safe temperature (\u00b0C)", example = "5.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "temperatureMin is mandatory")
    private BigDecimal temperatureMin;

    @Schema(description = "Maximum safe temperature (\u00b0C)", example = "85.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "temperatureMax is mandatory")
    private BigDecimal temperatureMax;

    @Schema(description = "Minimum acceptable flow rate (m\u00b3/h)", example = "500.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "flowRateMin is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "flowRateMin must be positive")
    private BigDecimal flowRateMin;

    @Schema(description = "Maximum acceptable flow rate (m\u00b3/h)", example = "2000.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "flowRateMax is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "flowRateMax must be positive")
    private BigDecimal flowRateMax;

    @Schema(description = "Minimum acceptable contained volume (m\u00b3)", example = "500.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "containedVolumeMin is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "containedVolumeMin must be positive")
    private BigDecimal containedVolumeMin;

    @Schema(description = "Maximum acceptable contained volume (m\u00b3)", example = "40000.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "containedVolumeMax is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "containedVolumeMax must be positive")
    private BigDecimal containedVolumeMax;

    @Schema(description = "Alert tolerance percentage (\u00b1%)", example = "5.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "alertTolerance is mandatory")
    @DecimalMin(value = "0.0", inclusive = true, message = "alertTolerance must be non-negative")
    private BigDecimal alertTolerance;

    @Schema(description = "Whether this threshold configuration is currently active", example = "true")
    private Boolean active;

    public static FlowThresholdDTO fromEntity(FlowThreshold entity) {
        if (entity == null) return null;
        return FlowThresholdDTO.builder()
                .id(entity.getId())
                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)
                .pipelineCode(entity.getPipeline() != null ? entity.getPipeline().getCode() : null)
                .pressureMin(entity.getPressureMin())
                .pressureMax(entity.getPressureMax())
                .temperatureMin(entity.getTemperatureMin())
                .temperatureMax(entity.getTemperatureMax())
                .flowRateMin(entity.getFlowRateMin())
                .flowRateMax(entity.getFlowRateMax())
                .containedVolumeMin(entity.getContainedVolumeMin())
                .containedVolumeMax(entity.getContainedVolumeMax())
                .alertTolerance(entity.getAlertTolerance())
                .active(entity.getActive())
                .build();
    }
}
