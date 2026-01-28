/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-27-2026 - Added validate and reject endpoints with @RequestParam
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/core/reading")
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
    @PreAuthorize("hasAuthority('FLOW_READING:ADMIN')")
    public ResponseEntity<FlowReadingDTO> create(@Valid @RequestBody FlowReadingDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_READING:ADMIN')")
    public ResponseEntity<FlowReadingDTO> update(@PathVariable Long id, @Valid @RequestBody FlowReadingDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_READING:ADMIN')")
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

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getByPipeline(@PathVariable Long pipelineId) {
        log.info("GET /flow/core/reading/pipeline/{} - Getting readings by pipeline", pipelineId);
        return ResponseEntity.ok(flowReadingService.findByPipeline(pipelineId));
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

    @GetMapping("/validationStatus/{statusId}")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<FlowReadingDTO>> getByValidationStatus(@PathVariable Long statusId) {
        log.info("GET /flow/core/reading/validationStatus/{} - Getting readings by validation status", statusId);
        return ResponseEntity.ok(flowReadingService.findByValidationStatus(statusId));
    }

    // ========== VALIDATION WORKFLOW ENDPOINTS ==========

    /**
     * Validate a flow reading
     * Updates validation status to VALIDATED and records validator information
     * 
     * @param id Reading ID
     * @param validatedById Employee ID of the validator
     * @return Updated reading with VALIDATED status
     */
    @PostMapping("/{id}/validate")
    @PreAuthorize("hasAuthority('FLOW_READING:ADMIN')")
    public ResponseEntity<FlowReadingDTO> validate(
            @PathVariable Long id,
            @RequestParam Long validatedById) {
        log.info("POST /flow/core/reading/{}/validate - Validating reading by employee ID: {}", id, validatedById);
        FlowReadingDTO validated = flowReadingService.validate(id, validatedById);
        return ResponseEntity.ok(validated);
    }

    /**
     * Reject a flow reading
     * Updates validation status to REJECTED and records rejection information
     * 
     * @param id Reading ID
     * @param rejectedById Employee ID of the rejector
     * @param rejectionReason Reason for rejection
     * @return Updated reading with REJECTED status
     */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('FLOW_READING:ADMIN')")
    public ResponseEntity<FlowReadingDTO> reject(
            @PathVariable Long id,
            @RequestParam Long rejectedById,
            @RequestParam String rejectionReason) {
        log.info("POST /flow/core/reading/{}/reject - Rejecting reading by employee ID: {} with reason: {}", 
                 id, rejectedById, rejectionReason);
        FlowReadingDTO rejected = flowReadingService.reject(id, rejectedById, rejectionReason);
        return ResponseEntity.ok(rejected);
    }
}
