/**
 * 
 * 	@Author		: HyFlo v2 DTO
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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Read DTO for pipeline segment risk scores.
 */
@Schema(description = "Read DTO for pipeline segment risk scores")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PipelineRiskScoreReadDto {

    @Schema(description = "Technical identifier of the risk score")
    private Long id;

    @Schema(description = "Identifier of the pipeline segment this score applies to")
    private Long pipelineSegmentId;

    @Schema(description = "Timestamp when this risk score was calculated")
    private LocalDateTime calculatedAt;

    @Schema(description = "Timestamp until which this risk score is considered valid")
    private LocalDateTime validUntil;

    @Schema(description = "Risk score (0-100, higher means higher risk)")
    private BigDecimal riskScore;

    @Schema(description = "Name or version of the model used to compute this score")
    private String modelName;

    @Schema(description = "Optional details about the inputs or assumptions used")
    private String details;
}
