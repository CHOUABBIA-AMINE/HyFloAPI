/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationWorkflowController
 *  @CreatedOn  : 03-28-2026 — extracted from FlowOperationController (workflow refactor)
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Workflow
 *
 *  @Description: Exposes approve and reject endpoints for FlowOperation.
 *                Moved from FlowOperationController to enforce:
 *                  core controller → CRUD only
 *                  workflow controller → lifecycle transitions only
 *
 *                Endpoints:
 *                  POST /flow/workflow/operation/{id}/approve
 *                  POST /flow/workflow/operation/{id}/reject
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flow/workflow/operation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Workflow — Operations",
     description = "Lifecycle transitions for flow operations (approve / reject)")
@SecurityRequirement(name = "bearer-auth")
public class FlowOperationWorkflowController {

    private final FlowOperationWorkflowService workflowService;

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(
        summary = "Approve a pending flow operation",
        description = "Transitions operation from PENDING to VALIDATED. Updates linked WorkflowInstance."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operation approved"),
        @ApiResponse(responseCode = "409", description = "Operation is not in PENDING state"),
        @ApiResponse(responseCode = "404", description = "Operation or validator not found")
    })
    public ResponseEntity<FlowOperationReadDTO> approve(
            @Parameter(description = "FlowOperation ID") @PathVariable Long id,
            @Parameter(description = "Employee ID performing the approval")
            @RequestParam Long validatorId) {
        log.info("POST /flow/workflow/operation/{}/approve validatorId={}", id, validatorId);
        return ResponseEntity.ok(workflowService.approve(id, validatorId));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(
        summary = "Reject a pending flow operation",
        description = "Transitions operation from PENDING to REJECTED. Rejection reason appended to audit notes."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operation rejected"),
        @ApiResponse(responseCode = "409", description = "Operation is not in a rejectable state"),
        @ApiResponse(responseCode = "404", description = "Operation or validator not found")
    })
    public ResponseEntity<FlowOperationReadDTO> reject(
            @Parameter(description = "FlowOperation ID") @PathVariable Long id,
            @Parameter(description = "Employee ID performing the rejection")
            @RequestParam Long validatorId,
            @Parameter(description = "Mandatory rejection reason")
            @RequestParam String reason) {
        log.info("POST /flow/workflow/operation/{}/reject validatorId={}", id, validatorId);
        return ResponseEntity.ok(workflowService.reject(id, validatorId, reason));
    }
}
