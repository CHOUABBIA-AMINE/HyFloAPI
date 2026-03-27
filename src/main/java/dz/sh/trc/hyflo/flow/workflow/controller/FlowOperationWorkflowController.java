/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationWorkflowController
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Workflow
 *
 *  @Description: Workflow lifecycle endpoints for FlowOperation.
 *                Exposes approve and reject under /flow/workflow/operation/...
 *                These endpoints are extracted from FlowOperationController
 *                per HyFlo v2 architecture.
 *
 *  Endpoints:
 *    POST /flow/workflow/operation/{id}/approve
 *    POST /flow/workflow/operation/{id}/reject
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.controller;

import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDTO;
import dz.sh.trc.hyflo.flow.workflow.service.FlowOperationWorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flow/workflow/operation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Workflow — Operations",
     description = "Lifecycle transitions for FlowOperation: approve and reject")
@SecurityRequirement(name = "bearer-auth")
public class FlowOperationWorkflowController {

    private final FlowOperationWorkflowService workflowService;

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(
            summary = "Approve a pending flow operation",
            description = "Transitions the flow operation from PENDING to VALIDATED status. "
                    + "Updates linked WorkflowInstance if present.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operation approved successfully"),
            @ApiResponse(responseCode = "409", description = "Operation is not in PENDING state"),
            @ApiResponse(responseCode = "404", description = "Operation or validator employee not found")
    })
    public ResponseEntity<FlowOperationReadDTO> approve(
            @Parameter(description = "Flow operation ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Employee ID performing the approval", required = true)
            @RequestParam Long validatorId) {
        log.info("POST /flow/workflow/operation/{}/approve validatorId={}", id, validatorId);
        return ResponseEntity.ok(workflowService.approve(id, validatorId));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(
            summary = "Reject a pending flow operation",
            description = "Transitions the flow operation from PENDING to REJECTED status. "
                    + "Rejection reason is appended to the operation audit notes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operation rejected successfully"),
            @ApiResponse(responseCode = "409", description = "Operation is not in a rejectable state"),
            @ApiResponse(responseCode = "404", description = "Operation or validator employee not found")
    })
    public ResponseEntity<FlowOperationReadDTO> reject(
            @Parameter(description = "Flow operation ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Employee ID performing the rejection", required = true)
            @RequestParam Long validatorId,
            @Parameter(description = "Mandatory rejection reason (appended to audit notes)", required = true)
            @RequestParam String reason) {
        log.info("POST /flow/workflow/operation/{}/reject validatorId={}", id, validatorId);
        return ResponseEntity.ok(workflowService.reject(id, validatorId, reason));
    }
}
