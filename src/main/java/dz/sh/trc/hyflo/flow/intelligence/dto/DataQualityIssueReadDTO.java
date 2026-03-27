/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DataQualityIssueReadDTO
 *  @CreatedOn  : 03-25-2026
 *  @MovedOn    : 03-28-2026 — refactor: flow.core.dto → flow.intelligence.dto
 *
 *  @Type       : Class
 *  @Layer      : DTO (Read)
 *  @Package    : Flow / Intelligence
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

    private Long id;
    private String issueType;
    private BigDecimal qualityScore;
    private String details;
    private Boolean acknowledged;
    private LocalDateTime raisedAt;
    private Long readingId;
    private Long derivedReadingId;
}
