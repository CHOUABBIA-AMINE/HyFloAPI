/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: HydrocarbonPlantController
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
import dz.sh.trc.hyflo.network.core.dto.HydrocarbonPlantDTO;
import dz.sh.trc.hyflo.network.core.service.HydrocarbonPlantService;
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
public class HydrocarbonPlantController extends GenericController<HydrocarbonPlantDTO, Long> {

    private final HydrocarbonPlantService hydrocarbonPlantService;

    public HydrocarbonPlantController(HydrocarbonPlantService hydrocarbonPlantService) {
        super(hydrocarbonPlantService, "HydrocarbonPlant");
        this.hydrocarbonPlantService = hydrocarbonPlantService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:READ')")
    public ResponseEntity<HydrocarbonPlantDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:READ')")
    public ResponseEntity<Page<HydrocarbonPlantDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:READ')")
    public ResponseEntity<List<HydrocarbonPlantDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:ADMIN')")
    public ResponseEntity<HydrocarbonPlantDTO> create(@Valid @RequestBody HydrocarbonPlantDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:ADMIN')")
    public ResponseEntity<HydrocarbonPlantDTO> update(@PathVariable Long id, @Valid @RequestBody HydrocarbonPlantDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:READ')")
    public ResponseEntity<Page<HydrocarbonPlantDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<HydrocarbonPlantDTO> searchByQuery(String query, Pageable pageable) {
        return hydrocarbonPlantService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
