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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.entity.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingService;
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
@Tag(name = "Flow Reading", description = "Flow reading CRUD operations and queries")
@Slf4j
public class FlowReadingController extends GenericController<FlowReadingDTO, Long> {

    private final FlowReadingService flowReadingService;
    
    public FlowReadingController(FlowReadingService flowReadingService) {
        super(flowReadingService, "FlowReading");
        this.flowReadingService = flowReadingService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<FlowReadingDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_READING:MANAGE')")
    public ResponseEntity<FlowReadingDTO> create(@Valid @RequestBody FlowReadingDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_READING:MANAGE')")
    public ResponseEntity<FlowReadingDTO> update(@PathVariable Long id, @Valid @RequestBody FlowReadingDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_READING:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM QUERY ENDPOINTS ==========

    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getByPipeline(@PathVariable Long pipelineId) {
        log.info("GET /flow/core/reading/pipeline/{} - Getting readings by pipeline", pipelineId);
        return ResponseEntity.ok(flowReadingService.findByPipeline(pipelineId));
    }

    @GetMapping("/slot/{readingSlotId}")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getByReadingSlot(@PathVariable Long readingSlotId) {
        log.info("GET /flow/core/reading/readingSlot/{} - Getting readings by reading slot", readingSlotId);
        return ResponseEntity.ok(flowReadingService.findByReadingSlot(readingSlotId));
    }

    @GetMapping("/pipeline/{pipelineId}/latest")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getLatestByPipeline(
            @PathVariable Long pipelineId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /flow/core/reading/pipeline/{}/latest - Getting latest readings for pipeline", pipelineId);
        return ResponseEntity.ok(flowReadingService.findLatestByPipeline(
                pipelineId, buildPageable(page, size, "recordedAt", "desc")));
    }

    @GetMapping("/time-range")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        log.info("GET /flow/core/reading/time-range - Getting readings from {} to {}", startTime, endTime);
        return ResponseEntity.ok(flowReadingService.findByTimeRange(startTime, endTime));
    }

    @GetMapping("/pipeline/{pipelineId}/time-range")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getByPipelineAndTimeRange(
            @PathVariable Long pipelineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "recordedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/reading/pipeline/{}/time-range - Getting readings from {} to {}", 
                 pipelineId, startTime, endTime);
        return ResponseEntity.ok(flowReadingService.findByPipelineAndTimeRangePaginated(
                pipelineId, startTime, endTime, buildPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/pipeline/{pipelineId}/date-range")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getByPipelineAndReadingDateRange(
            @PathVariable Long pipelineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "recordedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/reading/pipeline/{}/date-range - Getting readings from {} to {}", 
                 pipelineId, startDate, endDate);
        return ResponseEntity.ok(flowReadingService.findByPipelineAndReadingDateRangePaginated(
                pipelineId, startDate, endDate, buildPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/pipeline/{pipelineId}/slot/{readingSlotId}/date-range")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getByPipelineAndReadingSlotAndReadingDateRange(
            @PathVariable Long pipelineId,
            @PathVariable Long readingSlotId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "recordedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/reading/pipeline/{}/slot/{}/date-range - Getting readings from {} to {}", 
                 pipelineId, readingSlotId, startDate, endDate);
        return ResponseEntity.ok(flowReadingService.findByPipelineAndReadingSlotAndReadingDateRangePaginated(
                pipelineId, readingSlotId, startDate, endDate, buildPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/status/{statusId}")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getByValidationStatus(@PathVariable Long statusId) {
        log.info("GET /flow/core/reading/status/{} - Getting readings by validation status", statusId);
        return ResponseEntity.ok(flowReadingService.findByValidationStatus(statusId));
    }

    @GetMapping("/validationStatus/{statusId}")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getByValidationStatusPaginated(
			@PathVariable Long statusId,
			@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "recordedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
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
