/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : WorkflowInstanceReadDto
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.dto.query;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Read DTO for a workflow governance instance lifecycle")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkflowInstanceReadDto {

    @Schema(description = "Technical identifier of the workflow instance")
    private Long id;

    @Schema(description = "ID of the workflow target type")
    private Long targetTypeId;

    @Schema(description = "Code of the workflow target type", example = "FLOW_READING_VALIDATION")
    private String targetTypeCode;

    @Schema(description = "ID of the current workflow state")
    private Long currentStateId;

    @Schema(description = "Code of the current workflow state", example = "SUBMITTED")
    private String currentStateCode;

    @Schema(description = "Label of the current workflow state", example = "Submitted for validation")
    private String currentStateLabel;

    @Schema(description = "Timestamp when this workflow instance was opened")
    private LocalDateTime startedAt;

    @Schema(description = "Timestamp when this workflow instance reached terminal state (null if active)")
    private LocalDateTime completedAt;

    @Schema(description = "ID of the employee who initiated the workflow")
    private Long initiatedById;

    @Schema(description = "Full name of the employee who initiated the workflow")
    private String initiatedByFullName;

    @Schema(description = "ID of the employee who performed the most recent transition")
    private Long lastActorId;

    @Schema(description = "Full name of the last actor")
    private String lastActorFullName;

    @Schema(description = "Comment provided by last actor at transition time")
    private String comment;

    @Schema(description = "JSON audit trail of all workflow transitions (append-only)")
    private String history;

    // ---- NO fromEntity / mapping logic ----
    // All mapping is owned by WorkflowInstanceMapper.
}
