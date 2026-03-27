/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowAlertReadDTO
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

@Schema(description = "Read DTO for a flow alert triggered by a threshold breach")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowAlertReadDTO {

    @Schema(description = "Record identifier")
    private Long id;

    @Schema(description = "Threshold identifier that was breached")
    private Long thresholdId;

    @Schema(description = "Pipeline name associated with the threshold")
    private String pipelineName;

    @Schema(description = "Flow reading identifier that triggered the alert")
    private Long flowReadingId;

    @Schema(description = "Alert message describing the issue")
    private String message;

    @Schema(description = "Actual measured value that triggered the alert")
    private BigDecimal actualValue;

    @Schema(description = "Threshold value that was breached")
    private BigDecimal thresholdValue;

    @Schema(description = "Timestamp when alert was triggered")
    private LocalDateTime alertTimestamp;

    @Schema(description = "Current alert status")
    private String status;

    @Schema(description = "Whether notification was sent to operators")
    private Boolean notificationSent;

    @Schema(description = "Timestamp when notification was sent")
    private LocalDateTime notificationSentAt;

    @Schema(description = "Timestamp when alert was acknowledged")
    private LocalDateTime acknowledgedAt;

    @Schema(description = "Timestamp when alert was resolved")
    private LocalDateTime resolvedAt;

    @Schema(description = "Resolution notes")
    private String resolutionNotes;
}
