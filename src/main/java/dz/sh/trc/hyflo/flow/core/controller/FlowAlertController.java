/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowAlertController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.FlowAlertDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowAlertService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/core/alert")
@Slf4j
public class FlowAlertController extends GenericController<FlowAlertDTO, Long> {

    private final FlowAlertService flowAlertService;
    
    public FlowAlertController(FlowAlertService flowAlertService) {
        super(flowAlertService, "FlowAlert");
        this.flowAlertService = flowAlertService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<FlowAlertDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<List<FlowAlertDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_ALERT:WRITE')")
    public ResponseEntity<FlowAlertDTO> create(@Valid @RequestBody FlowAlertDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_ALERT:WRITE')")
    public ResponseEntity<FlowAlertDTO> update(@PathVariable Long id, @Valid @RequestBody FlowAlertDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_ALERT:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/threshold/{thresholdId}")
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<List<FlowAlertDTO>> getByThreshold(@PathVariable Long thresholdId) {
        log.info("GET /flow/core/alert/threshold/{} - Getting alerts by threshold", thresholdId);
        return ResponseEntity.ok(flowAlertService.findByThreshold(thresholdId));
    }

    @GetMapping("/status/{statusId}")
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<List<FlowAlertDTO>> getByStatus(@PathVariable Long statusId) {
        log.info("GET /flow/core/alert/status/{} - Getting alerts by status", statusId);
        return ResponseEntity.ok(flowAlertService.findByStatus(statusId));
    }

    @GetMapping("/unacknowledged")
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getUnacknowledged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/core/alert/unacknowledged - Getting unacknowledged alerts");
        return ResponseEntity.ok(flowAlertService.findUnacknowledged(
                createPageable(page, size, "alertTimestamp", "desc")));
    }

    @GetMapping("/unresolved")
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getUnresolved(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/core/alert/unresolved - Getting unresolved alerts");
        return ResponseEntity.ok(flowAlertService.findUnresolved(
                createPageable(page, size, "alertTimestamp", "desc")));
    }

    @GetMapping("/pipeline/{pipelineId}/unresolved")
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getUnresolvedByPipeline(
            @PathVariable Long pipelineId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/core/alert/pipeline/{}/unresolved - Getting unresolved alerts for pipeline", pipelineId);
        return ResponseEntity.ok(flowAlertService.findUnresolvedByPipeline(
                pipelineId, createPageable(page, size, "alertTimestamp", "desc")));
    }

    @GetMapping("/pipeline/{pipelineId}/time-range")
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getByPipelineAndTimeRange(
            @PathVariable Long pipelineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "alertTimestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/alert/pipeline/{}/time-range - Getting alerts from {} to {}", 
                 pipelineId, startTime, endTime);
        return ResponseEntity.ok(flowAlertService.findByPipelineAndTimeRange(
                pipelineId, startTime, endTime, createPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/status/{statusId}/time-range")
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getByStatusAndTimeRange(
            @PathVariable Long statusId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "alertTimestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/alert/status/{}/time-range - Getting alerts from {} to {}", 
                 statusId, startTime, endTime);
        return ResponseEntity.ok(flowAlertService.findByStatusAndTimeRange(
                statusId, startTime, endTime, createPageable(page, size, sortBy, sortDir)));
    }
}
