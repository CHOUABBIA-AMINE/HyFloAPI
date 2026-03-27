/**
 *
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : FlowAlertReadDTO
 *  @CreatedOn  : 01-23-2026
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

@Schema(description = "Read DTO for a flow alert")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowAlertReadDTO {

    private Long id;
    private Long thresholdId;
    private String thresholdPipelineCode;
    private Long flowReadingId;
    private String message;
    private BigDecimal actualValue;
    private BigDecimal thresholdValue;
    private LocalDateTime alertTimestamp;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime resolvedAt;
    private String resolutionNotes;
    private Boolean notificationSent;
    private LocalDateTime notificationSentAt;
    private Long statusId;
    private String statusName;
    private Long resolvedById;
    private Long acknowledgedById;
}
