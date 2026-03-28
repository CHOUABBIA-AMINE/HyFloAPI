/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanController
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Core
 *
 *  @Description: REST controller for FlowPlan domain.
 *                Uses FlowPlanCommandService for writes.
 *                Uses FlowPlanQueryService for reads.
 *                No entity exposure. Mirrors FlowReadingController pattern.
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.flow.core.dto.FlowPlanReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.FlowPlanCommandDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowPlanCommandService;
import dz.sh.trc.hyflo.flow.core.service.FlowPlanQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/plan")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Plans", description = "Management-entered operational planning targets for pipelines")
@SecurityRequirement(name = "bearer-auth")
public class FlowPlanController {

    private final FlowPlanCommandService commandService;
    private final FlowPlanQueryService   queryService;

    // =========================================================
    // READ ENDPOINTS
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get flow plan by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Plan found"),
        @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    public ResponseEntity<FlowPlanReadDTO> getById(
            @Parameter(description = "FlowPlan ID") @PathVariable Long id) {
        log.debug("GET /flow/plan/{}", id);
        return ResponseEntity.ok(queryService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "List all flow plans (paginated)")
    public ResponseEntity<Page<FlowPlanReadDTO>> getAll(
            @PageableDefault(size = 20, sort = "planDate") Pageable pageable) {
        return ResponseEntity.ok(queryService.getAll(pageable));
    }

    @GetMapping("/by-pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all plans for a pipeline (paginated)")
    public ResponseEntity<Page<FlowPlanReadDTO>> getByPipeline(
            @PathVariable Long pipelineId,
            @PageableDefault(size = 20, sort = "planDate") Pageable pageable) {
        return ResponseEntity.ok(queryService.getByPipelinePaged(pipelineId, pageable));
    }

    @GetMapping("/by-pipeline/{pipelineId}/by-date")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get plans for a pipeline on a specific date")
    public ResponseEntity<List<FlowPlanReadDTO>> getByPipelineAndDate(
            @PathVariable Long pipelineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(queryService.getByPipelineAndDate(pipelineId, date));
    }

    @GetMapping("/by-pipeline/{pipelineId}/by-range")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get plans for a pipeline over a date range")
    public ResponseEntity<List<FlowPlanReadDTO>> getByPipelineAndRange(
            @PathVariable Long pipelineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(queryService.getByPipelineAndDateRange(pipelineId, from, to));
    }

    @GetMapping("/by-pipeline/{pipelineId}/approved")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get APPROVED plans for a pipeline (paginated)")
    public ResponseEntity<Page<FlowPlanReadDTO>> getApprovedByPipeline(
            @PathVariable Long pipelineId,
            @PageableDefault(size = 20, sort = "planDate") Pageable pageable) {
        return ResponseEntity.ok(queryService.getApprovedByPipeline(pipelineId, pageable));
    }

    @GetMapping("/by-pipeline/{pipelineId}/latest")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get latest APPROVED plan for a pipeline on a given date")
    public ResponseEntity<FlowPlanReadDTO> getLatestApproved(
            @PathVariable Long pipelineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return queryService.getLatestApproved(pipelineId, date)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-scenario")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get plans by scenario code (paginated)")
    public ResponseEntity<Page<FlowPlanReadDTO>> getByScenario(
            @Parameter(description = "Scenario code, e.g. BASE, OPTIMISTIC")
            @RequestParam String scenarioCode,
            @PageableDefault(size = 20, sort = "planDate") Pageable pageable) {
        return ResponseEntity.ok(queryService.getByScenario(scenarioCode, pageable));
    }

    // =========================================================
    // WRITE ENDPOINTS
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAuthority('FLOW:WRITE')")
    @Operation(summary = "Create a new flow plan")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Plan created"),
        @ApiResponse(responseCode = "400", description = "Invalid plan data"),
        @ApiResponse(responseCode = "404", description = "Pipeline or PlanStatus not found")
    })
    public ResponseEntity<FlowPlanReadDTO> create(
            @Valid @RequestBody FlowPlanCommandDTO command) {
        log.debug("POST /flow/plan - create");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(commandService.create(command));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:WRITE')")
    @Operation(summary = "Update a DRAFT flow plan")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Plan updated"),
        @ApiResponse(responseCode = "409", description = "Plan is not in DRAFT status")
    })
    public ResponseEntity<FlowPlanReadDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FlowPlanCommandDTO command) {
        log.debug("PUT /flow/plan/{}", id);
        return ResponseEntity.ok(commandService.update(id, command));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('FLOW:APPROVE')")
    @Operation(summary = "Approve a DRAFT plan — transitions DRAFT → APPROVED")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Plan approved"),
        @ApiResponse(responseCode = "409", description = "Plan is not in DRAFT status")
    })
    public ResponseEntity<FlowPlanReadDTO> approve(
            @PathVariable Long id,
            @Parameter(description = "Employee ID performing the approval")
            @RequestParam Long approverEmployeeId) {
        log.debug("POST /flow/plan/{}/approve approverEmployeeId={}", id, approverEmployeeId);
        return ResponseEntity.ok(commandService.approve(id, approverEmployeeId));
    }

    @PostMapping("/{id}/supersede")
    @PreAuthorize("hasAuthority('FLOW:APPROVE')")
    @Operation(summary = "Supersede an APPROVED plan — transitions APPROVED → SUPERSEDED")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Plan superseded"),
        @ApiResponse(responseCode = "409", description = "Plan is not in APPROVED status")
    })
    public ResponseEntity<FlowPlanReadDTO> supersede(@PathVariable Long id) {
        log.debug("POST /flow/plan/{}/supersede", id);
        return ResponseEntity.ok(commandService.supersede(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:DELETE')")
    @Operation(summary = "Delete a DRAFT plan")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Plan deleted"),
        @ApiResponse(responseCode = "409", description = "Plan cannot be deleted in current status")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("DELETE /flow/plan/{}", id);
        commandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
