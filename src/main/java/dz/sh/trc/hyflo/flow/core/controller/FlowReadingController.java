/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-05-2026 - Merged FlowMonitoringController endpoints
 * 	@UpdatedOn	: 02-07-2026 - Added 6 new operational monitoring endpoints
 * 	@UpdatedOn	: 02-07-2026 - Updated to use proper DTOs instead of Map<String, Object>
 * 	@UpdatedOn	: 02-10-2026 - Removed 6 monitoring endpoints (moved to FlowMonitoringController)
 * 	@UpdatedOn	: 02-10-2026 - Removed workflow endpoints (moved to FlowWorkflowController)
 * 	@UpdatedOn	: 02-16-2026 - Added comprehensive OpenAPI documentation
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 * 	@Description: REST controller for flow reading CRUD operations.
 * 	              Now focused exclusively on data management (no workflow logic).
 *
 * 	@Refactoring: Workflow endpoints extracted to FlowWorkflowController
 * 	              
 * 	              Endpoints removed (moved to FlowWorkflowController):
 * 	              - POST /{id}/validate
 * 	              - POST /{id}/reject
 * 	              - POST /submit
 * 	              - POST /validate
 * 	              - POST /slot-coverage
 * 	              
 * 	              This controller now handles ONLY:
 * 	              - CRUD operations (create, read, update, delete)
 * 	              - Query operations (by pipeline, slot, date, status)
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for flow reading CRUD operations.
 * 
 * This controller handles:
 * - Create, Read, Update, Delete operations
 * - Query operations (by pipeline, slot, date range, status)
 * - Paginated queries
 * 
 * For workflow operations (validate, reject), see FlowWorkflowController.
 * For analytics/monitoring, see FlowMonitoringController (intelligence module).
 * 
 * Architecture:
 * - This controller: CRUD + queries
 * - FlowWorkflowController: Workflow transitions
 * - FlowMonitoringController: Analytics queries
 */
@RestController
@RequestMapping("/flow/core/reading")
@Tag(name = "Flow Reading Management", description = "APIs for managing flow readings with CRUD operations and query capabilities (workflow in FlowWorkflowController)")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class FlowReadingController extends GenericController<FlowReadingDTO, Long> {

    private final FlowReadingService flowReadingService;
    
    public FlowReadingController(FlowReadingService flowReadingService) {
        super(flowReadingService, "FlowReading");
        this.flowReadingService = flowReadingService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get flow reading by ID", description = "Retrieves a single flow reading by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow reading found", content = @Content(schema = @Schema(implementation = FlowReadingDTO.class))),
        @ApiResponse(responseCode = "404", description = "Flow reading not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<FlowReadingDTO> getById(
            @Parameter(description = "Flow reading ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all flow readings (paginated)", description = "Retrieves a paginated list of all flow readings")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow readings retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all flow readings (unpaginated)", description = "Retrieves all flow readings without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow readings retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create flow reading", description = "Creates a new flow reading")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Flow reading created successfully", content = @Content(schema = @Schema(implementation = FlowReadingDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:MANAGE')")
    public ResponseEntity<FlowReadingDTO> create(
            @Parameter(description = "Flow reading data", required = true) 
            @Valid @RequestBody FlowReadingDTO dto) {
        FlowReadingDTO created = flowReadingService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update flow reading", description = "Updates an existing flow reading")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow reading updated successfully", content = @Content(schema = @Schema(implementation = FlowReadingDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Flow reading not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:MANAGE')")
    public ResponseEntity<FlowReadingDTO> update(
            @Parameter(description = "Flow reading ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated flow reading data", required = true) @Valid @RequestBody FlowReadingDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete flow reading", description = "Deletes a flow reading permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Flow reading deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Flow reading not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Flow reading ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search flow readings", description = "Searches flow readings by multiple criteria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if flow reading exists", description = "Checks if a flow reading with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Flow reading ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count flow readings", description = "Returns the total number of flow readings")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow reading count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM QUERY ENDPOINTS ==========

    @GetMapping("/pipeline/{pipelineId}")
    @Operation(summary = "Get readings by pipeline", description = "Retrieves all flow readings for a specific pipeline")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Readings retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getByPipeline(
            @Parameter(description = "Pipeline ID", required = true, example = "1") 
            @PathVariable Long pipelineId) {
        log.info("GET /flow/core/reading/pipeline/{} - Getting readings by pipeline", pipelineId);
        return ResponseEntity.ok(flowReadingService.findByPipeline(pipelineId));
    }

    @GetMapping("/slot/{readingSlotId}")
    @Operation(summary = "Get readings by slot", description = "Retrieves all flow readings for a specific reading slot (time period)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Readings retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Reading slot not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getByReadingSlot(
            @Parameter(description = "Reading slot ID", required = true, example = "1") 
            @PathVariable Long readingSlotId) {
        log.info("GET /flow/core/reading/readingSlot/{} - Getting readings by reading slot", readingSlotId);
        return ResponseEntity.ok(flowReadingService.findByReadingSlot(readingSlotId));
    }

    @GetMapping("/pipeline/{pipelineId}/latest")
    @Operation(summary = "Get latest readings by pipeline", description = "Retrieves the most recent flow readings for a specific pipeline")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Latest readings retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getLatestByPipeline(
            @Parameter(description = "Pipeline ID", required = true, example = "1") @PathVariable Long pipelineId,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size) {
        log.info("GET /flow/core/reading/pipeline/{}/latest - Getting latest readings for pipeline", pipelineId);
        return ResponseEntity.ok(flowReadingService.findLatestByPipeline(
                pipelineId, buildPageable(page, size, "recordedAt", "desc")));
    }

    @GetMapping("/time-range")
    @Operation(summary = "Get readings by time range", description = "Retrieves flow readings within a specified timestamp range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Readings retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid time range parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getByTimeRange(
            @Parameter(description = "Start time (ISO 8601)", required = true, example = "2026-01-01T00:00:00") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "End time (ISO 8601)", required = true, example = "2026-12-31T23:59:59") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        log.info("GET /flow/core/reading/time-range - Getting readings from {} to {}", startTime, endTime);
        return ResponseEntity.ok(flowReadingService.findByTimeRange(startTime, endTime));
    }

    @GetMapping("/pipeline/{pipelineId}/time-range")
    @Operation(summary = "Get readings by pipeline and time range", description = "Retrieves paginated flow readings for a pipeline within a timestamp range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Readings retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid time range or pagination parameters"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getByPipelineAndTimeRange(
            @Parameter(description = "Pipeline ID", required = true, example = "1") @PathVariable Long pipelineId,
            @Parameter(description = "Start time (ISO 8601)", required = true, example = "2026-01-01T00:00:00") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "End time (ISO 8601)", required = true, example = "2026-12-31T23:59:59") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "recordedAt") @RequestParam(defaultValue = "recordedAt") String sortBy,
            @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/reading/pipeline/{}/time-range - Getting readings from {} to {}", 
                 pipelineId, startTime, endTime);
        return ResponseEntity.ok(flowReadingService.findByPipelineAndTimeRangePaginated(
                pipelineId, startTime, endTime, buildPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/pipeline/{pipelineId}/date-range")
    @Operation(summary = "Get readings by pipeline and date range", description = "Retrieves paginated flow readings for a pipeline within a date range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Readings retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date range or pagination parameters"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getByPipelineAndReadingDateRange(
            @Parameter(description = "Pipeline ID", required = true, example = "1") @PathVariable Long pipelineId,
            @Parameter(description = "Start date (ISO 8601)", required = true, example = "2026-01-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (ISO 8601)", required = true, example = "2026-12-31") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "recordedAt") @RequestParam(defaultValue = "recordedAt") String sortBy,
            @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/reading/pipeline/{}/date-range - Getting readings from {} to {}", 
                 pipelineId, startDate, endDate);
        return ResponseEntity.ok(flowReadingService.findByPipelineAndReadingDateRangePaginated(
                pipelineId, startDate, endDate, buildPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/pipeline/{pipelineId}/slot/{readingSlotId}/date-range")
    @Operation(summary = "Get readings by pipeline, slot, and date range", 
               description = "Retrieves paginated flow readings for a specific pipeline and reading slot within a date range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Readings retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date range or pagination parameters"),
        @ApiResponse(responseCode = "404", description = "Pipeline or reading slot not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getByPipelineAndReadingSlotAndReadingDateRange(
            @Parameter(description = "Pipeline ID", required = true, example = "1") @PathVariable Long pipelineId,
            @Parameter(description = "Reading slot ID", required = true, example = "1") @PathVariable Long readingSlotId,
            @Parameter(description = "Start date (ISO 8601)", required = true, example = "2026-01-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (ISO 8601)", required = true, example = "2026-12-31") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "recordedAt") @RequestParam(defaultValue = "recordedAt") String sortBy,
            @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/reading/pipeline/{}/slot/{}/date-range - Getting readings from {} to {}", 
                 pipelineId, readingSlotId, startDate, endDate);
        return ResponseEntity.ok(flowReadingService.findByPipelineAndReadingSlotAndReadingDateRangePaginated(
                pipelineId, readingSlotId, startDate, endDate, buildPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/status/{statusId}")
    @Operation(summary = "Get readings by validation status", description = "Retrieves all flow readings with a specific validation status (PENDING, VALIDATED, REJECTED)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Readings retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Validation status not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getByValidationStatus(
            @Parameter(description = "Validation status ID", required = true, example = "1") 
            @PathVariable Long statusId) {
        log.info("GET /flow/core/reading/status/{} - Getting readings by validation status", statusId);
        return ResponseEntity.ok(flowReadingService.findByValidationStatus(statusId));
    }

    @GetMapping("/validationStatus/{statusId}")
    @Operation(summary = "Get readings by validation status (paginated)", 
               description = "Retrieves paginated flow readings with a specific validation status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Readings retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "404", description = "Validation status not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_READING:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getByValidationStatusPaginated(
			@Parameter(description = "Validation status ID", required = true, example = "1") @PathVariable Long statusId,
			@Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "recordedAt") @RequestParam(defaultValue = "recordedAt") String sortBy,
            @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/reading/validationStatus/{} (paginated) - Getting readings by validation status", statusId);
        return ResponseEntity.ok(flowReadingService.findByValidationStatus(
                statusId, buildPageable(page, size, sortBy, sortDir)));
    }

    // ========== MIGRATION NOTES ==========

    /**
     * ⚠️ WORKFLOW ENDPOINTS MOVED
     * 
     * The following endpoints were moved to FlowWorkflowController:
     * 
     * 1. POST /{id}/validate
     *    → MOVED to: POST /flow/core/workflow/readings/{id}/validate
     *    → Use FlowWorkflowController for validation workflow
     * 
     * 2. POST /{id}/reject
     *    → MOVED to: POST /flow/core/workflow/readings/{id}/reject
     *    → Use FlowWorkflowController for rejection workflow
     * 
     * 3. POST /submit
     *    → REMOVED: Use standard POST /flow/core/reading (create) instead
     *    → Workflow-based submission was redundant with CRUD create
     * 
     * 4. POST /validate (with ReadingValidationRequestDTO)
     *    → MOVED to: POST /flow/core/workflow/readings/{id}/validate
     *    → Simplified to use path variable + query params
     * 
     * 5. POST /slot-coverage
     *    → MOVED to: Intelligence module (FlowMonitoringController)
     *    → Analytics feature, not CRUD operation
     * 
     * Controller responsibilities:
     * - FlowReadingController: CRUD + queries (this controller)
     * - FlowWorkflowController: Workflow transitions (validate, reject)
     * - FlowMonitoringController: Analytics (coverage, statistics, pending readings)
     */
}
