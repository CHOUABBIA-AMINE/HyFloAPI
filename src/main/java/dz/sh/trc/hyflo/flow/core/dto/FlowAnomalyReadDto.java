/**
 * 
 * 	@Author		: HyFlo v2 DTO
 *
 * 	@Name		: FlowAnomalyReadDto
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Read DTO for persisted flow anomalies.
 */
@Schema(description = "Read DTO for flow anomalies")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FlowAnomalyReadDto {

    @Schema(description = "Technical identifier of the anomaly")
    private Long id;

    @Schema(description = "Identifier of the direct reading associated with this anomaly (if any)")
    private Long readingId;

    @Schema(description = "Identifier of the derived reading associated with this anomaly (if any)")
    private Long derivedReadingId;

    @Schema(description = "Identifier of the pipeline segment affected (if any)")
    private Long pipelineSegmentId;

    @Schema(description = "Timestamp when this anomaly was detected")
    private LocalDateTime detectedAt;

    @Schema(description = "Anomaly type classification")
    private String anomalyType;

    @Schema(description = "Anomaly score produced by the AI model")
    private BigDecimal score;

    @Schema(description = "AI model name or version that produced this anomaly")
    private String modelName;

    @Schema(description = "Human-readable explanation or feature attribution summary")
    private String explanation;

    @Schema(description = "Identifier of the validation status reference (if any)")
    private Long statusId;

    @Schema(description = "Identifier of the data source reference (if any)")
    private Long dataSourceId;
}
