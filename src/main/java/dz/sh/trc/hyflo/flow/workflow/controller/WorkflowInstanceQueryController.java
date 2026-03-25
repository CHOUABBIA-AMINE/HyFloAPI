/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : WorkflowInstanceQueryController
 *  @CreatedOn  : Phase 4 — Commit 30
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Workflow
 *
 *  @Description: Read-only REST controller for workflow instance auditability.
 *                Exposes governance audit trail and workflow state queries.
 *                Returns WorkflowInstanceReadDto exclusively.
 *                No write operations — workflow state changes are
 *                performed through ReadingWorkflowV2Controller.
 *
 *  OWNERSHIP NOTE:
 *  WorkflowInstance does NOT own a FK back to FlowReading.
 *  FlowReading owns the FK to its WorkflowInstance.
 *  Therefore getByReadingId is NOT exposed here —
 *  use GET /api/v2/flow/readings/{id} which includes workflowInstanceId
 *  in FlowReadingReadDto, then query by ID here.
 *
 *  Phase 4 — Commit 30
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.controller;

import dz.sh.trc.hyflo.flow.workflow.dto.query.WorkflowInstanceReadDto;
import dz.sh.trc.hyflo.flow.workflow.service.WorkflowInstanceQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v2/flow/workflow/instances")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Workflow Instances",
     description = "Audit and traceability queries for operational reading governance workflow instances")
@SecurityRequirement(name = "bearer-auth")
public class WorkflowInstanceQueryController {

    private final WorkflowInstanceQueryService queryService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:AUDIT')")
    @Operation(summary = "Get workflow instance by ID",
               description = "Returns a single workflow instance by its unique identifier. Used for audit trail inspection.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Workflow instance found"),
        @ApiResponse(responseCode = "404", description = "Workflow instance not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<WorkflowInstanceReadDto> getById(
            @Parameter(description = "Workflow instance ID") @PathVariable Long id) {
        log.debug("GET /api/v2/flow/workflow/instances/{}", id);
        return ResponseEntity.ok(queryService.getById(id));
    }

    @GetMapping("/state/{stateCode}")
    @PreAuthorize("hasAuthority('FLOW:AUDIT')")
    @Operation(summary = "List workflow instances by state",
               description = "Returns all workflow instances in a given state. "
                           + "Valid states: SUBMITTED, APPROVED, REJECTED. "
                           + "Used for supervisor dashboards and pending action queues.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Workflow instances returned")
    })
    public ResponseEntity<List<WorkflowInstanceReadDto>> getByState(
            @Parameter(description = "Workflow state code (e.g., SUBMITTED, APPROVED, REJECTED)")
            @PathVariable String stateCode) {
        log.debug("GET /api/v2/flow/workflow/instances/state/{}", stateCode);
        return ResponseEntity.ok(queryService.getByState(stateCode));
    }

    @GetMapping("/actor/initiator/{employeeId}")
    @PreAuthorize("hasAuthority('FLOW:AUDIT')")
    @Operation(summary = "List workflow instances initiated by employee",
               description = "Returns all workflow instances that were initiated by a specific employee. "
                           + "Useful for operator submission history.")
    public ResponseEntity<List<WorkflowInstanceReadDto>> getByInitiatingActor(
            @Parameter(description = "Initiating employee ID") @PathVariable Long employeeId) {
        log.debug("GET /api/v2/flow/workflow/instances/actor/initiator/{}", employeeId);
        return ResponseEntity.ok(queryService.getByInitiatingActor(employeeId));
    }

    @GetMapping("/actor/reviewer/{employeeId}")
    @PreAuthorize("hasAuthority('FLOW:AUDIT')")
    @Operation(summary = "List workflow instances by last reviewing actor",
               description = "Returns all workflow instances where a specific employee performed "
                           + "the most recent state transition (approved or rejected). "
                           + "Used for supervisor action history.")
    public ResponseEntity<List<WorkflowInstanceReadDto>> getByLastActor(
            @Parameter(description = "Reviewing employee ID") @PathVariable Long employeeId) {
        log.debug("GET /api/v2/flow/workflow/instances/actor/reviewer/{}", employeeId);
        return ResponseEntity.ok(queryService.getByLastActor(employeeId));
    }

    @GetMapping("/range")
    @PreAuthorize("hasAuthority('FLOW:AUDIT')")
    @Operation(summary = "List workflow instances by date range",
               description = "Returns workflow instances opened within a date range. "
                           + "Based on startedAt timestamp. Used for governance period reporting.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Workflow instances returned")
    })
    public ResponseEntity<List<WorkflowInstanceReadDto>> getByDateRange(
            @Parameter(description = "Start date (inclusive), format: yyyy-MM-dd")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date (inclusive), format: yyyy-MM-dd")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        log.debug("GET /api/v2/flow/workflow/instances/range from={} to={}", from, to);
        return ResponseEntity.ok(queryService.getByDateRange(from, to));
    }

    @GetMapping("/target/{targetTypeCode}")
    @PreAuthorize("hasAuthority('FLOW:AUDIT')")
    @Operation(summary = "List workflow instances by target type",
               description = "Returns all workflow instances for a specific governance process type. "
                           + "Example: FLOW_READING_VALIDATION.")
    public ResponseEntity<List<WorkflowInstanceReadDto>> getByTargetType(
            @Parameter(description = "Target type code (e.g., FLOW_READING_VALIDATION)")
            @PathVariable String targetTypeCode) {
        log.debug("GET /api/v2/flow/workflow/instances/target/{}", targetTypeCode);
        return ResponseEntity.ok(queryService.getByTargetType(targetTypeCode));
    }
}
