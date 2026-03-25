/**
 * 
 * 	@Author		: HyFlo v2 DTO
 *
 * 	@Name		: DataQualityIssueReadDto
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
 * Read DTO for data quality evaluations.
 */
@Schema(description = "Read DTO for data quality evaluations")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataQualityIssueReadDto {

    @Schema(description = "Technical identifier of the quality issue")
    private Long id;

    @Schema(description = "Identifier of the direct reading evaluated (if any)")
    private Long readingId;

    @Schema(description = "Identifier of the derived reading evaluated (if any)")
    private Long derivedReadingId;

    @Schema(description = "Identifier of the quality flag reference (if any)")
    private Long qualityFlagId;

    @Schema(description = "Numerical quality score")
    private BigDecimal score;

    @Schema(description = "Classification of the data quality issue")
    private String issueType;

    @Schema(description = "Human-readable details about detected data quality issues")
    private String details;

    @Schema(description = "Timestamp when data quality evaluation was performed")
    private LocalDateTime evaluatedAt;

    @Schema(description = "Identifier of the data source reference (if any)")
    private Long dataSourceId;
}
