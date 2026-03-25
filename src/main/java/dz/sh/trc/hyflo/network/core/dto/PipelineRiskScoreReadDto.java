/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: PipelineRiskScoreReadDto
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.core.model.PipelineRiskScore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Read DTO for a pipeline segment risk score")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineRiskScoreReadDto extends GenericDTO<PipelineRiskScore> {

    @Schema(description = "Composite risk score (0.0 – 1.0)", example = "0.7400")
    private BigDecimal riskScore;

    @Schema(description = "Pressure risk component (0.0 – 1.0)", example = "0.6800")
    private BigDecimal pressureRisk;

    @Schema(description = "Flow risk component (0.0 – 1.0)", example = "0.5500")
    private BigDecimal flowRisk;

    @Schema(description = "Age/corrosion risk component (0.0 – 1.0)", example = "0.8000")
    private BigDecimal ageCorrosionRisk;

    @Schema(description = "Name of the risk model used", example = "COMPOSITE_RISK_V1")
    private String modelName;

    @Schema(description = "Contributing factors or notes")
    private String details;

    @Schema(description = "Calculation timestamp")
    private LocalDateTime calculatedAt;

    @Schema(description = "ID of the scored pipeline segment")
    private Long pipelineSegmentId;

    @Schema(description = "Code of the scored pipeline segment", example = "GZ1-SEG-01")
    private String pipelineSegmentCode;

    @Override
    public PipelineRiskScore toEntity() {
        throw new UnsupportedOperationException("Use risk engine for risk score creation");
    }

    @Override
    public void updateEntity(PipelineRiskScore entity) {
        throw new UnsupportedOperationException("Use risk engine for risk score updates");
    }

    public static PipelineRiskScoreReadDto fromEntity(PipelineRiskScore entity) {
        if (entity == null) return null;
        return PipelineRiskScoreReadDto.builder()
                .id(entity.getId())
                .riskScore(entity.getRiskScore())
                .pressureRisk(entity.getPressureRisk())
                .flowRisk(entity.getFlowRisk())
                .ageCorrosionRisk(entity.getAgeCorrosionRisk())
                .modelName(entity.getModelName())
                .details(entity.getDetails())
                .calculatedAt(entity.getCalculatedAt())
                .pipelineSegmentId(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getId() : null)
                .pipelineSegmentCode(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getCode() : null)
                .build();
    }
}
