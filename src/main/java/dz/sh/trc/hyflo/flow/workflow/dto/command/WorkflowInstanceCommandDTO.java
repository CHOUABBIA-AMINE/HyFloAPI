/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : WorkflowInstanceCommandDTO
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Command
 *  @Package    : Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.dto.command;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command DTO for opening or transitioning a WorkflowInstance.
 *
 * Used by service orchestration layer only. Operators do not
 * submit this DTO directly — it is constructed internally
 * by workflow transition services.
 */
@Schema(description = "Command DTO for opening or transitioning a workflow governance instance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkflowInstanceCommandDTO {

    @Schema(description = "ID of the workflow target type (e.g. FLOW_READING_VALIDATION)",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Workflow target type ID is required")
    private Long targetTypeId;

    @Schema(description = "ID of the initial or target workflow state",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Workflow state ID is required")
    private Long currentStateId;

    @Schema(description = "ID of the employee initiating this workflow instance",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long initiatedById;

    @Schema(description = "ID of the employee performing the latest transition",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long lastActorId;

    @Schema(description = "Free-text comment by the actor at transition time (e.g. rejection reason)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 2000)
    @Size(max = 2000, message = "Comment must not exceed 2000 characters")
    private String comment;
}
