/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadingWorkflowV2Controller
 *  @CreatedOn  : Phase 4 — Commit 29
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Workflow
 *
 *  @Description: v2 REST controller for reading governance workflow transitions.
 *                Exposes explicit approve / reject actions on submitted readings.
 *                Uses ReadingWorkflowService.approve() and .reject() directly
 *                with exact method signatures confirmed from Phase 3 implementation.
 *                Returns FlowReadingReadDTO — workflow state visible through reading state.
 *
 *  Phase 4 — Commit 29
 *
 *  Service method signatures (confirmed Phase 3):
 *    approve(Long id, Long approvedById)  → FlowReadingReadDTO
 *    reject(Long id, Long rejectedById, String rejectionReason)  → FlowReadingReadDTO
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.controller;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.workflow.dto.ReadingValidationRequestDTO;
import dz.sh.trc.hyflo.flow.workflow.service.ReadingWorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/flow/workflow")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reading Workflow v2",
     description = "Governance workflow for operational flow readings — approve and reject lifecycle transitions")
@SecurityRequirement(name = "bearer-auth")
public class ReadingWorkflowController {

    private final ReadingWorkflowService workflowService;

    /**
     * Approve a submitted flow reading.
     *
     * Delegates to ReadingWorkflowService.approve(readingId, employeeId).
     * WorkflowInstance is updated as source of truth.
     * ValidationStatus compatibility projection is maintained.
     * DerivedFlowReading generation is triggered post-approval.
     */
    @PostMapping("/readings/{readingId}/approve")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(summary = "Approve a submitted flow reading",
               description = "Approves a submitted flow reading. Transitions workflow instance to APPROVED state. "
                           + "Triggers derived reading generation for the approved reading. "
                           + "Only employees with FLOW:VALIDATE authority may perform this action.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading approved — workflow transitioned to APPROVED"),
        @ApiResponse(responseCode = "409", description = "Reading is already APPROVED or in an invalid state for this transition"),
        @ApiResponse(responseCode = "404", description = "Reading or employee not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<FlowReadingReadDTO> approveReading(
            @Parameter(description = "Flow reading ID to approve") @PathVariable Long readingId,
            @Valid @RequestBody ReadingValidationRequestDTO request) {
        log.info("POST /api/v2/flow/workflow/readings/{}/approve by employeeId={}",
                readingId, request.getEmployeeId());
        return ResponseEntity.ok(workflowService.approve(readingId, request.getEmployeeId()));
    }

    /**
     * Reject a submitted flow reading.
     *
     * Delegates to ReadingWorkflowService.reject(readingId, employeeId, comments).
     * Rejection reason is appended to audit notes.
     * Reading may be corrected and resubmitted after rejection.
     */
    @PostMapping("/readings/{readingId}/reject")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(summary = "Reject a submitted flow reading",
               description = "Rejects a submitted flow reading with a mandatory rejection reason. "
                           + "Transitions workflow instance to REJECTED. "
                           + "Rejection reason is appended to the reading audit trail. "
                           + "Rejected readings may be corrected and resubmitted by the original operator.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading rejected — workflow transitioned to REJECTED"),
        @ApiResponse(responseCode = "409", description = "Reading is in an invalid state for rejection (e.g., already APPROVED)"),
        @ApiResponse(responseCode = "404", description = "Reading or employee not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<FlowReadingReadDTO> rejectReading(
            @Parameter(description = "Flow reading ID to reject") @PathVariable Long readingId,
            @Valid @RequestBody ReadingValidationRequestDTO request) {
        log.info("POST /api/v2/flow/workflow/readings/{}/reject by employeeId={}",
                readingId, request.getEmployeeId());
        return ResponseEntity.ok(workflowService.reject(
                readingId, request.getEmployeeId(), request.getComments()));
    }
}
