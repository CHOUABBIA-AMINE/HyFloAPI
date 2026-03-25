/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: DataQualityIssueReadDto
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
import dz.sh.trc.hyflo.flow.core.model.DataQualityIssue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Read DTO for a data quality issue flagged on a flow reading")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataQualityIssueReadDto extends GenericDTO<DataQualityIssue> {

    @Schema(description = "Type of quality issue", example = "OUT_OF_RANGE")
    private String issueType;

    @Schema(description = "Quality score (0.0 – 1.0)", example = "0.4300")
    private BigDecimal qualityScore;

    @Schema(description = "Detailed explanation of the issue")
    private String details;

    @Schema(description = "Whether the operator acknowledged the issue")
    private Boolean acknowledged;

    @Schema(description = "Timestamp when the issue was raised")
    private LocalDateTime raisedAt;

    @Schema(description = "ID of the source reading")
    private Long readingId;

    @Schema(description = "ID of the source derived reading")
    private Long derivedReadingId;

    @Override
    public DataQualityIssue toEntity() {
        throw new UnsupportedOperationException("Use intelligence engine for quality issue creation");
    }

    @Override
    public void updateEntity(DataQualityIssue entity) {
        throw new UnsupportedOperationException("Use intelligence engine for quality issue updates");
    }

    public static DataQualityIssueReadDto fromEntity(DataQualityIssue entity) {
        if (entity == null) return null;
        return DataQualityIssueReadDto.builder()
                .id(entity.getId())
                .issueType(entity.getIssueType())
                .qualityScore(entity.getQualityScore())
                .details(entity.getDetails())
                .acknowledged(entity.getAcknowledged())
                .raisedAt(entity.getRaisedAt())
                .readingId(entity.getReading() != null ? entity.getReading().getId() : null)
                .derivedReadingId(entity.getDerivedReading() != null ? entity.getDerivedReading().getId() : null)
                .build();
    }
}
