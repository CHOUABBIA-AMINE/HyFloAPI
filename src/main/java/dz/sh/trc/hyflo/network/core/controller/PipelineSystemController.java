/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PipelineSystemController
 *	@CreatedOn	: 12-11-2025
 *	@UpdatedOn	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Common 
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

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
import dz.sh.trc.hyflo.network.core.dto.PipelineSystemDTO;
import dz.sh.trc.hyflo.network.core.service.PipelineSystemService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/network/core/pipelineSystem")
@Slf4j
public class PipelineSystemController extends GenericController<PipelineSystemDTO, Long> {

    @SuppressWarnings("unused")
	private final PipelineSystemService pipelineSystemService;

    public PipelineSystemController(PipelineSystemService pipelineSystemService) {
        super(pipelineSystemService, "PipelineSystem");
        this.pipelineSystemService = pipelineSystemService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    public ResponseEntity<PipelineSystemDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    public ResponseEntity<Page<PipelineSystemDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    public ResponseEntity<List<PipelineSystemDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:ADMIN')")
    public ResponseEntity<PipelineSystemDTO> create(@Valid @RequestBody PipelineSystemDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:ADMIN')")
    public ResponseEntity<PipelineSystemDTO> update(@PathVariable Long id, @Valid @RequestBody PipelineSystemDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    public ResponseEntity<Page<PipelineSystemDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    public ResponseEntity<List<PipelineSystemDTO>> getByProduct(@PathVariable Long productId) {
        log.info("REST request to get Pipeline System by product id: {}", productId);
        return ResponseEntity.ok(pipelineSystemService.findByProduct(productId));
    }

    @GetMapping("/status/{operationalStatusId}")
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    public ResponseEntity<List<PipelineSystemDTO>> getByOperationalStatus(@PathVariable Long operationalStatusId) {
        log.info("REST request to get Pipeline System by operational status id: {}", operationalStatusId);
        return ResponseEntity.ok(pipelineSystemService.findByOperationalStatus(operationalStatusId));
    }

    @GetMapping("/region/{regionId}")
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    public ResponseEntity<List<PipelineSystemDTO>> getByRegion(@PathVariable Long regionId) {
        log.info("REST request to get Pipeline System by region id: {}", regionId);
        return ResponseEntity.ok(pipelineSystemService.findByRegion(regionId));
    }
}
