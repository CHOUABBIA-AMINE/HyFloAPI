/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PipelineController
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Common 
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.service.PipelineService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/core/pipeline")
@Slf4j
public class PipelineController extends GenericController<PipelineDTO, Long> {

    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService) {
        super(pipelineService, "Pipeline");
        this.pipelineService = pipelineService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    public ResponseEntity<PipelineDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    public ResponseEntity<Page<PipelineDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    public ResponseEntity<List<PipelineDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:ADMIN')")
    public ResponseEntity<PipelineDTO> create(@Valid @RequestBody PipelineDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:ADMIN')")
    public ResponseEntity<PipelineDTO> update(@PathVariable Long id, @Valid @RequestBody PipelineDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    public ResponseEntity<Page<PipelineDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/system/{systemId}")
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    public ResponseEntity<List<PipelineDTO>> getByPipelineSystem(@PathVariable Long systemId) {
        log.info("REST request to get Pipeline by pipeline system id: {}", systemId);
        return ResponseEntity.ok(pipelineService.findByPipelineSystem(systemId));
    }
}
