/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: DataQualityIssueReadDTO
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

@Schema(description = "Read DTO for a data quality issue flagged on a flow reading")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataQualityIssueReadDTO {

    @Schema(description = "Record identifier")
    private Long id;

    @Schema(description = "Type of quality issue", example = "OUT_OF_RANGE")
    private String issueType;

    @Schema(description = "Quality score for the reading (0.0 - 1.0)", example = "0.43")
    private BigDecimal qualityScore;

    @Schema(description = "Detailed explanation of the issue detected")
    private String details;

    @Schema(description = "Whether the operator acknowledged the issue")
    private Boolean acknowledged;

    @Schema(description = "Timestamp when the issue was raised")
    private LocalDateTime raisedAt;

    @Schema(description = "Source flow reading identifier")
    private Long readingId;

    @Schema(description = "Source derived flow reading identifier (if applicable)")
    private Long derivedReadingId;
}
