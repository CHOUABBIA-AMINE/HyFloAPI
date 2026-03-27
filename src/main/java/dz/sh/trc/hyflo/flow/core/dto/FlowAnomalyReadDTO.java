/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAnomalyReadDTO
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 - Phase 2: removed fromEntity() — mapper owns mapping
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : Flow / Core
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
public class FlowAnomalyReadDTO extends GenericDTO<FlowAnomaly> {

    @Schema(description = "Type of anomaly", example = "PRESSURE_DROP")
    private String anomalyType;

    @Schema(description = "Severity score (0.0 \u2013 1.0)", example = "0.8700")
    private BigDecimal severityScore;

    @Schema(description = "Confidence score (0.0 \u2013 1.0)", example = "0.9200")
    private BigDecimal confidenceScore;

    @Schema(description = "Detection model name", example = "ISOLATION_FOREST_V2")
    private String modelName;

    @Schema(description = "Human-readable explanation of the anomaly")
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

    // ---- NO fromEntity / mapping logic ----
    // All mapping is owned by FlowAnomalyMapper.

    @Override
    public FlowAnomaly toEntity() {
        throw new UnsupportedOperationException("Use intelligence engine for anomaly creation");
    }

    @Override
    public void updateEntity(FlowAnomaly entity) {
        throw new UnsupportedOperationException("Use intelligence engine for anomaly updates");
    }
}
