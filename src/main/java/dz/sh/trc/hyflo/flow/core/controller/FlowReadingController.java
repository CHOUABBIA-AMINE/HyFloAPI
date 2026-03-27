/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingController
 *  @CreatedOn  : Phase 4 — Commit 27
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Core
 *
 *  @Description: v2 REST controller for direct operational flow readings.
 *                Uses FlowReadingCommandService for writes.
 *                Uses FlowReadingQueryService for reads.
 *                No entity exposure. No legacy FlowReadingDTO in active v2 paths.
 *
 *  Phase 4 — Commit 27
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.FlowReadingCommandDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingCommandService;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/flow/reading")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Readings v2", description = "Operational flow reading management — direct pipeline readings lifecycle (v2 explicit contract)")
@SecurityRequirement(name = "bearer-auth")
public class FlowReadingController {

    private final FlowReadingCommandService commandService;
    private final FlowReadingQueryService queryService;

    // =========================================================
    // READ ENDPOINTS — all return FlowReadingReadDTO
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get flow reading by ID",
               description = "Returns a single operational flow reading by its unique identifier.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading found"),
        @ApiResponse(responseCode = "404", description = "Reading not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<FlowReadingReadDTO> getById(
            @Parameter(description = "Flow reading ID") @PathVariable Long id) {
        log.debug("GET /api/v2/flow/readings/{} - fetch by id", id);
        return ResponseEntity.ok(queryService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "List all flow readings (paginated)",
               description = "Returns a paginated list of all operational flow readings.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of flow readings returned")
    })
    public ResponseEntity<Page<FlowReadingReadDTO>> getAll(
            @PageableDefault(size = 20, sort = "readingDate") Pageable pageable) {
        return ResponseEntity.ok(queryService.getAll(pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Search flow readings",
               description = "Full-text search across flow reading fields (pipeline name, notes, etc.).")
    public ResponseEntity<Page<FlowReadingReadDTO>> search(
            @Parameter(description = "Search query string") @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(queryService.search(q, pageable));
    }

    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get readings by pipeline",
               description = "Returns all flow readings recorded for a specific pipeline.")
    public ResponseEntity<List<FlowReadingReadDTO>> getByPipeline(
            @Parameter(description = "Pipeline ID") @PathVariable Long pipelineId) {
        return ResponseEntity.ok(queryService.getByPipeline(pipelineId));
    }

    @GetMapping("/pipeline/{pipelineId}/range")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get readings by pipeline and date range",
               description = "Returns readings for a given pipeline within a date interval. Used for trend analysis and reporting.")
    public ResponseEntity<List<FlowReadingReadDTO>> getByPipelineAndDateRange(
            @PathVariable Long pipelineId,
            @Parameter(description = "Start date (inclusive), format: yyyy-MM-dd")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date (inclusive), format: yyyy-MM-dd")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(queryService.getByPipelineAndDateRange(pipelineId, from, to));
    }

    @GetMapping("/pipeline/{pipelineId}/slot/{slotId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get readings by pipeline and time slot",
               description = "Returns readings for a pipeline filtered by operational time slot.")
    public ResponseEntity<List<FlowReadingReadDTO>> getByPipelineAndSlot(
            @PathVariable Long pipelineId,
            @PathVariable Long slotId) {
        return ResponseEntity.ok(queryService.getByPipelineAndSlot(pipelineId, slotId));
    }

    @GetMapping("/pipeline/{pipelineId}/latest")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get latest readings for a pipeline",
               description = "Returns the N most recent flow readings for a pipeline. Used for operational dashboards.")
    public ResponseEntity<List<FlowReadingReadDTO>> getLatestByPipeline(
            @PathVariable Long pipelineId,
            @Parameter(description = "Maximum number of results to return")
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(queryService.getLatestByPipeline(pipelineId, limit));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get readings by date range (all pipelines)",
               description = "Returns all readings within a date range across the entire pipeline network.")
    public ResponseEntity<List<FlowReadingReadDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(queryService.getByDateRange(from, to));
    }

    @GetMapping("/status/{validationStatusId}")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(summary = "Get readings by validation status",
               description = "Returns readings filtered by their current workflow validation status.")
    public ResponseEntity<List<FlowReadingReadDTO>> getByValidationStatus(
            @PathVariable Long validationStatusId) {
        return ResponseEntity.ok(queryService.getByValidationStatus(validationStatusId));
    }

    // =========================================================
    // WRITE ENDPOINTS — accept FlowReadingCommandDTO
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAuthority('FLOW:WRITE')")
    @Operation(summary = "Record a new flow reading",
               description = "Creates a new direct operational reading for a pipeline. Initiates the record\u2192submit\u2192validate lifecycle.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reading created"),
        @ApiResponse(responseCode = "400", description = "Invalid reading data"),
        @ApiResponse(responseCode = "404", description = "Referenced pipeline or data source not found")
    })
    public ResponseEntity<FlowReadingReadDTO> createReading(
            @Valid @RequestBody FlowReadingCommandDTO command) {
        log.debug("POST /api/v2/flow/readings - create reading");
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(commandService.createReading(command));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:WRITE')")
    @Operation(summary = "Update a flow reading",
               description = "Updates an existing reading that has not yet been approved. Draft and rejected readings may be corrected.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading updated"),
        @ApiResponse(responseCode = "400", description = "Invalid update data or reading is in a non-editable state"),
        @ApiResponse(responseCode = "404", description = "Reading not found")
    })
    public ResponseEntity<FlowReadingReadDTO> updateReading(
            @PathVariable Long id,
            @Valid @RequestBody FlowReadingCommandDTO command) {
        log.debug("PUT /api/v2/flow/readings/{} - update reading", id);
        return ResponseEntity.ok(commandService.updateReading(id, command));
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('FLOW:SUBMIT')")
    @Operation(summary = "Submit reading for workflow validation",
               description = "Submits a recorded reading into the governance workflow. Transitions status to SUBMITTED.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading submitted to workflow"),
        @ApiResponse(responseCode = "409", description = "Reading is already submitted or approved")
    })
    public ResponseEntity<FlowReadingReadDTO> submitForWorkflow(
            @PathVariable Long id,
            @Parameter(description = "ID of the employee submitting the reading")
            @RequestParam Long submittedById) {
        log.debug("POST /api/v2/flow/readings/{}/submit - submittedById={}", id, submittedById);
        return ResponseEntity.ok(commandService.submitForWorkflow(id, submittedById));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:DELETE')")
    @Operation(summary = "Delete a flow reading",
               description = "Removes a reading that has not been approved. Throws error if reading is in an approved terminal state.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reading deleted"),
        @ApiResponse(responseCode = "409", description = "Reading cannot be deleted in current state")
    })
    public ResponseEntity<Void> deleteReading(@PathVariable Long id) {
        log.debug("DELETE /api/v2/flow/readings/{} - delete reading", id);
        commandService.deleteReading(id);
        return ResponseEntity.noContent().build();
    }
}
