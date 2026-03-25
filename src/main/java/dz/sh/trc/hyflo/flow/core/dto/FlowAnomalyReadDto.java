/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowAnomalyReadDto
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowAnomaly;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Read DTO for a flow anomaly detected by the intelligence engine")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowAnomalyReadDto extends GenericDTO<FlowAnomaly> {

    @Schema(description = "Type of anomaly", example = "PRESSURE_DROP")
    private String anomalyType;

    @Schema(description = "Severity score (0.0 – 1.0)", example = "0.8700")
    private BigDecimal severityScore;

    @Schema(description = "Confidence score (0.0 – 1.0)", example = "0.9200")
    private BigDecimal confidenceScore;

    @Schema(description = "Detection model name", example = "ISOLATION_FOREST_V2")
    private String modelName;

    @Schema(description = "Human-readable explanation")
    private String explanation;

    @Schema(description = "Detection timestamp")
    private LocalDateTime detectedAt;

    @Schema(description = "ID of the source raw reading")
    private Long readingId;

    @Schema(description = "ID of the source derived reading")
    private Long derivedReadingId;

    @Schema(description = "ID of the affected pipeline segment")
    private Long pipelineSegmentId;

    @Schema(description = "Code of the affected pipeline segment", example = "GZ1-SEG-01")
    private String pipelineSegmentCode;

    @Override
    public FlowAnomaly toEntity() {
        throw new UnsupportedOperationException("Use intelligence engine for anomaly creation");
    }

    @Override
    public void updateEntity(FlowAnomaly entity) {
        throw new UnsupportedOperationException("Use intelligence engine for anomaly updates");
    }

    public static FlowAnomalyReadDto fromEntity(FlowAnomaly entity) {
        if (entity == null) return null;
        return FlowAnomalyReadDto.builder()
                .id(entity.getId())
                .anomalyType(entity.getAnomalyType())
                .severityScore(entity.getSeverityScore())
                .confidenceScore(entity.getConfidenceScore())
                .modelName(entity.getModelName())
                .explanation(entity.getExplanation())
                .detectedAt(entity.getDetectedAt())
                .readingId(entity.getReading() != null ? entity.getReading().getId() : null)
                .derivedReadingId(entity.getDerivedReading() != null ? entity.getDerivedReading().getId() : null)
                .pipelineSegmentId(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getId() : null)
                .pipelineSegmentCode(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getCode() : null)
                .build();
    }
}
