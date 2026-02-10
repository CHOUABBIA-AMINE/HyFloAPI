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
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.command.ReadingSubmitRequestDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.ReadingValidationRequestDTO;
import dz.sh.trc.hyflo.flow.core.dto.entity.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingService;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingWorkflowService;
import dz.sh.trc.hyflo.flow.intelligence.dto.monitoring.SlotCoverageRequestDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.monitoring.SlotCoverageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/core/reading")
@Tag(name = "Flow Reading", description = "Flow reading management with CRUD operations, queries, and workflow")
@Slf4j
public class FlowReadingController extends GenericController<FlowReadingDTO, Long> {

    private final FlowReadingService flowReadingService;
    private final FlowReadingWorkflowService workflowService;
    
    public FlowReadingController(
            FlowReadingService flowReadingService,
            FlowReadingWorkflowService workflowService) {
        super(flowReadingService, "FlowReading");
        this.flowReadingService = flowReadingService;
        this.workflowService = workflowService;
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

    @GetMapping("/pipeline/{pipelineId}/slot/{readingSlotId}date-range")
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
        log.info("GET /flow/core/reading/validationStatus/{} - Getting readings by validation status", statusId);
        return ResponseEntity.ok(flowReadingService.findByValidationStatus(statusId));
    }

    @GetMapping("/validationStatus/{statusId}")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getByValidationStatus(
			@PathVariable Long statusId,
			@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "recordedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/reading/validationStatus/{} - Getting readings by validation status", statusId);
        return ResponseEntity.ok(flowReadingService.findByValidationStatus(statusId, buildPageable(page, size, sortBy, sortDir)));
    }

    // ========== VALIDATION WORKFLOW ENDPOINTS (ID-BASED) ==========

    /**
     * Validate a flow reading (ID-based approach)
     * Updates validation status to VALIDATED and records validator information
     * 
     * @param id Reading ID
     * @param validatedById Employee ID of the validator
     * @return Updated reading with VALIDATED status
     */
    @PostMapping("/{id}/validate")
    @PreAuthorize("hasAuthority('FLOW_READING:MANAGE')")
    @Operation(summary = "Validate a reading by ID", description = "Updates validation status to VALIDATED")
    public ResponseEntity<FlowReadingDTO> validate(
            @PathVariable Long id,
            @RequestParam Long validatedById) {
        log.info("POST /flow/core/reading/{}/validate - Validating reading by employee ID: {}", id, validatedById);
        FlowReadingDTO validated = flowReadingService.validate(id, validatedById);
        return ResponseEntity.ok(validated);
    }

    /**
     * Reject a flow reading (ID-based approach)
     * Updates validation status to REJECTED and records rejection information
     * 
     * @param id Reading ID
     * @param rejectedById Employee ID of the rejector
     * @param rejectionReason Reason for rejection
     * @return Updated reading with REJECTED status
     */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('FLOW_READING:MANAGE')")
    @Operation(summary = "Reject a reading by ID", description = "Updates validation status to REJECTED")
    public ResponseEntity<FlowReadingDTO> reject(
            @PathVariable Long id,
            @RequestParam Long rejectedById,
            @RequestParam String rejectionReason) {
        log.info("POST /flow/core/reading/{}/reject - Rejecting reading by employee ID: {} with reason: {}", 
                 id, rejectedById, rejectionReason);
        FlowReadingDTO rejected = flowReadingService.reject(id, rejectedById, rejectionReason);
        return ResponseEntity.ok(rejected);
    }

    // ========== WORKFLOW MONITORING ENDPOINTS (MERGED FROM FlowMonitoringController) ==========

    /**
     * Get slot coverage for date + slot + structure
     * Provides monitoring view of reading coverage across pipelines within a structure
     * Shows which pipelines have submitted/validated readings for a specific date and slot
     * 
     * Migrated from: POST /flow/core/monitoring/slot-coverage
     * 
     * @param request Slot coverage request with date, slot, and structure filters
     * @return Coverage summary with pipeline-level reading status
     */
    @PostMapping("/slot-coverage")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    @Operation(
        summary = "Get slot coverage for date + slot + structure",
        description = "Returns coverage summary showing which pipelines have readings for the specified slot and date"
    )
    public ResponseEntity<SlotCoverageResponseDTO> getSlotCoverage(
        @Valid @RequestBody SlotCoverageRequestDTO request
    ) {
        SlotCoverageResponseDTO response = workflowService.getSlotCoverage(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Submit or update a reading (workflow approach)
     * Alternative to standard create/update for workflow-based submission
     * Handles business logic for reading submission including validation checks
     * 
     * Migrated from: POST /flow/core/monitoring/readings/submit
     * 
     * @param request Reading submission request with all required data
     * @return 201 Created on success
     */
    @PostMapping("/submit")
    @PreAuthorize("hasAuthority('FLOW_READING:MANAGE')")
    @Operation(
        summary = "Submit or update a reading via workflow",
        description = "Workflow-based reading submission with business validation"
    )
    public ResponseEntity<Void> submitReading(
        @Valid @RequestBody ReadingSubmitRequestDTO request
    ) {
        workflowService.submitReading(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    /**
     * Validate a reading (workflow approach with approve/reject)
     * Consolidates validation logic in workflow service
     * Supports both approval and rejection with appropriate status updates
     * 
     * Migrated from: POST /flow/core/monitoring/readings/validate
     * 
     * @param request Validation request with reading ID, action (approve/reject), and validator info
     * @return 200 OK on success
     */
    @PostMapping("/validate")
    @PreAuthorize("hasAuthority('FLOW_READING:MANAGE')")
    @Operation(
        summary = "Validate a reading (approve or reject)",
        description = "Workflow-based validation supporting both approval and rejection actions"
    )
    public ResponseEntity<Void> validateReading(
        @Valid @RequestBody ReadingValidationRequestDTO request
    ) {
        workflowService.validateReading(request);
        return ResponseEntity.ok().build();
    }
}
