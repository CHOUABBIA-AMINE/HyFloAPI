/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSegmentController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.core.dto.PipelineSegmentDTO;
import dz.sh.trc.hyflo.network.core.service.PipelineSegmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/core/pipelineSegment")
@Slf4j
public class PipelineSegmentController extends GenericController<PipelineSegmentDTO, Long> {

    private final PipelineSegmentService pipelineSegmentService;

    public PipelineSegmentController(PipelineSegmentService pipelineSegmentService) {
        super(pipelineSegmentService, "PipelineSegment");
        this.pipelineSegmentService = pipelineSegmentService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    public ResponseEntity<PipelineSegmentDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    public ResponseEntity<Page<PipelineSegmentDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    public ResponseEntity<List<PipelineSegmentDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:ADMIN')")
    public ResponseEntity<PipelineSegmentDTO> create(@Valid @RequestBody PipelineSegmentDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:ADMIN')")
    public ResponseEntity<PipelineSegmentDTO> update(@PathVariable Long id, @Valid @RequestBody PipelineSegmentDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    public ResponseEntity<Page<PipelineSegmentDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    public ResponseEntity<List<PipelineSegmentDTO>> getByPipeline(@PathVariable Long pipelineId) {
        log.info("REST request to get PipelineSegment by pipeline id: {}", pipelineId);
        return ResponseEntity.ok(pipelineSegmentService.findByPipeline(pipelineId));
    }
}
