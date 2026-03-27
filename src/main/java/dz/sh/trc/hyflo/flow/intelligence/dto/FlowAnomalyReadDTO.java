/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowAnomalyReadDTO
 * 	@CreatedOn	: 03-28-2026 — extracted from flow.core.dto, relocated to flow.intelligence.dto
 *
 * 	@Type		: Class
 * 	@Layer		: DTO (Read)
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Read DTO for a flow anomaly detected by the intelligence engine")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowAnomalyReadDTO {

    @Schema(description = "Record identifier")
    private Long id;

    @Schema(description = "Type of anomaly detected", example = "PRESSURE_DROP")
    private String anomalyType;

    @Schema(description = "Anomaly severity score (0.0 - 1.0)", example = "0.87")
    private BigDecimal severityScore;

    @Schema(description = "Confidence score of the detection model (0.0 - 1.0)", example = "0.92")
    private BigDecimal confidenceScore;

    @Schema(description = "Name of the detection model used", example = "ISOLATION_FOREST_V2")
    private String modelName;

    @Schema(description = "Human-readable explanation of the anomaly")
    private String explanation;

    @Schema(description = "Timestamp when the anomaly was detected")
    private LocalDateTime detectedAt;

    @Schema(description = "Source flow reading identifier")
    private Long readingId;

    @Schema(description = "Source derived flow reading identifier (if applicable)")
    private Long derivedReadingId;

    @Schema(description = "Affected pipeline segment identifier")
    private Long pipelineSegmentId;

    @Schema(description = "Affected pipeline segment code")
    private String pipelineSegmentCode;
}
