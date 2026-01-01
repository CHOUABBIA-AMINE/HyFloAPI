/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FlowVolumeController
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Controller
 *	@Layer		: Network / Controller
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.flow.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.flow.dto.FlowVolumeDTO;
import dz.sh.trc.hyflo.network.flow.service.FlowVolumeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/network/flow/volume")
@Slf4j
public class FlowVolumeController extends GenericController<FlowVolumeDTO, Long> {

    //private final FlowVolumeService flowService;

    public FlowVolumeController(FlowVolumeService flowVolumeService) {
        super(flowVolumeService, "FlowVolume");
        //this.flowService = flowService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<FlowVolumeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Page<FlowVolumeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<List<FlowVolumeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<FlowVolumeDTO> create(@Valid @RequestBody FlowVolumeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<FlowVolumeDTO> update(@PathVariable Long id, @Valid @RequestBody FlowVolumeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Page<FlowVolumeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    /*@Override
    protected Page<FlowVolumeDTO> searchByQuery(String query, Pageable pageable) {
        return flowService.globalSearch(query, pageable);
    }*/

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
