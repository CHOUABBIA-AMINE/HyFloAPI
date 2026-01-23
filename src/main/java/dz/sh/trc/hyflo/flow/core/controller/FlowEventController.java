/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowEventController
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
import dz.sh.trc.hyflo.flow.core.dto.FlowEventDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowEventService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/core/event")
@Slf4j
public class FlowEventController extends GenericController<FlowEventDTO, Long> {

    private final FlowEventService flowEventService;
    
    public FlowEventController(FlowEventService flowEventService) {
        super(flowEventService, "FlowEvent");
        this.flowEventService = flowEventService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<FlowEventDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Page<FlowEventDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<List<FlowEventDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_EVENT:WRITE')")
    public ResponseEntity<FlowEventDTO> create(@Valid @RequestBody FlowEventDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_EVENT:WRITE')")
    public ResponseEntity<FlowEventDTO> update(@PathVariable Long id, @Valid @RequestBody FlowEventDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_EVENT:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Page<FlowEventDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/infrastructure/{infrastructureId}")
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<List<FlowEventDTO>> getByInfrastructure(@PathVariable Long infrastructureId) {
        log.info("GET /flow/core/event/infrastructure/{} - Getting events by infrastructure", infrastructureId);
        return ResponseEntity.ok(flowEventService.findByInfrastructure(infrastructureId));
    }

    @GetMapping("/severity/{severityId}")
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<List<FlowEventDTO>> getBySeverity(@PathVariable Long severityId) {
        log.info("GET /flow/core/event/severity/{} - Getting events by severity", severityId);
        return ResponseEntity.ok(flowEventService.findBySeverity(severityId));
    }

    @GetMapping("/status/{statusId}")
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<List<FlowEventDTO>> getByStatus(@PathVariable Long statusId) {
        log.info("GET /flow/core/event/status/{} - Getting events by status", statusId);
        return ResponseEntity.ok(flowEventService.findByStatus(statusId));
    }

    @GetMapping("/impact-on-flow")
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<List<FlowEventDTO>> getByImpactOnFlow(
            @RequestParam(defaultValue = "true") Boolean impactOnFlow) {
        log.info("GET /flow/core/event/impact-on-flow - Getting events with impact on flow: {}", impactOnFlow);
        return ResponseEntity.ok(flowEventService.findByImpactOnFlow(impactOnFlow));
    }

    @GetMapping("/infrastructure/{infrastructureId}/time-range")
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Page<FlowEventDTO>> getByInfrastructureAndTimeRange(
            @PathVariable Long infrastructureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "eventTimestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/event/infrastructure/{}/time-range - Getting events from {} to {}", 
                 infrastructureId, startTime, endTime);
        return ResponseEntity.ok(flowEventService.findByInfrastructureAndTimeRange(
                infrastructureId, startTime, endTime, createPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/severity/{severityId}/time-range")
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Page<FlowEventDTO>> getBySeverityAndTimeRange(
            @PathVariable Long severityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "eventTimestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/event/severity/{}/time-range - Getting events from {} to {}", 
                 severityId, startTime, endTime);
        return ResponseEntity.ok(flowEventService.findBySeverityAndTimeRange(
                severityId, startTime, endTime, createPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/impact-on-flow/time-range")
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Page<FlowEventDTO>> getWithImpactOnFlowByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "eventTimestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/event/impact-on-flow/time-range - Getting events with impact from {} to {}", 
                 startTime, endTime);
        return ResponseEntity.ok(flowEventService.findWithImpactOnFlowByTimeRange(
                startTime, endTime, createPageable(page, size, sortBy, sortDir)));
    }
}
