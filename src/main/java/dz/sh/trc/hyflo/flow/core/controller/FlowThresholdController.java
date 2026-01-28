/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowThresholdController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowThresholdService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/core/threshold")
@Slf4j
public class FlowThresholdController extends GenericController<FlowThresholdDTO, Long> {

    private final FlowThresholdService flowThresholdService;
    
    public FlowThresholdController(FlowThresholdService flowThresholdService) {
        super(flowThresholdService, "FlowThreshold");
        this.flowThresholdService = flowThresholdService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    public ResponseEntity<FlowThresholdDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    public ResponseEntity<Page<FlowThresholdDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    public ResponseEntity<List<FlowThresholdDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:WRITE')")
    public ResponseEntity<FlowThresholdDTO> create(@Valid @RequestBody FlowThresholdDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:WRITE')")
    public ResponseEntity<FlowThresholdDTO> update(@PathVariable Long id, @Valid @RequestBody FlowThresholdDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    public ResponseEntity<Page<FlowThresholdDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    public ResponseEntity<List<FlowThresholdDTO>> getByPipeline(@PathVariable Long pipelineId) {
        log.info("GET /flow/core/threshold/pipeline/{} - Getting thresholds by pipeline", pipelineId);
        return ResponseEntity.ok(flowThresholdService.findByPipeline(pipelineId));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    public ResponseEntity<Page<FlowThresholdDTO>> getActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("GET /flow/core/threshold/active - Getting all active thresholds");
        return ResponseEntity.ok(flowThresholdService.findAllActive(
                buildPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/pipeline/{pipelineId}/active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    public ResponseEntity<Page<FlowThresholdDTO>> getActiveByPipeline(
            @PathVariable Long pipelineId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("GET /flow/core/threshold/pipeline/{}/active - Getting active thresholds for pipeline", pipelineId);
        return ResponseEntity.ok(flowThresholdService.findActiveByPipeline(
                pipelineId, buildPageable(page, size, sortBy, sortDir)));
    }
}
