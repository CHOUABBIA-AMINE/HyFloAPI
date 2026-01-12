/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProcessingPlantController
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
import dz.sh.trc.hyflo.network.core.dto.ProcessingPlantDTO;
import dz.sh.trc.hyflo.network.core.service.ProcessingPlantService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/core/hydrocarbon/plant")
@Slf4j
public class ProcessingPlantController extends GenericController<ProcessingPlantDTO, Long> {

    private final ProcessingPlantService processingPlantService;

    public ProcessingPlantController(ProcessingPlantService processingPlantService) {
        super(processingPlantService, "ProcessingPlant");
        this.processingPlantService = processingPlantService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT:READ')")
    public ResponseEntity<ProcessingPlantDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT:READ')")
    public ResponseEntity<Page<ProcessingPlantDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT:READ')")
    public ResponseEntity<List<ProcessingPlantDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT:ADMIN')")
    public ResponseEntity<ProcessingPlantDTO> create(@Valid @RequestBody ProcessingPlantDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT:ADMIN')")
    public ResponseEntity<ProcessingPlantDTO> update(@PathVariable Long id, @Valid @RequestBody ProcessingPlantDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT:READ')")
    public ResponseEntity<Page<ProcessingPlantDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<ProcessingPlantDTO> searchByQuery(String query, Pageable pageable) {
        return processingPlantService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
