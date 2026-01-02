/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: HydrocarbonFieldController
 *	@CreatedOn	: 12-19-2025
 *	@UpdatedOn	: 12-19-2025
 *
 *	@Type		: Controller
 *	@Layer		: Network / Controller
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.core.dto.HydrocarbonFieldDTO;
import dz.sh.trc.hyflo.network.core.service.HydrocarbonFieldService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/core/hydrocarbonField")
@Slf4j
public class HydrocarbonFieldController extends GenericController<HydrocarbonFieldDTO, Long> {

    private final HydrocarbonFieldService hydrocarbonFieldService;

    public HydrocarbonFieldController(HydrocarbonFieldService hydrocarbonFieldService) {
        super(hydrocarbonFieldService, "HydrocarbonField");
        this.hydrocarbonFieldService = hydrocarbonFieldService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:READ')")
    public ResponseEntity<HydrocarbonFieldDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:READ')")
    public ResponseEntity<Page<HydrocarbonFieldDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:READ')")
    public ResponseEntity<List<HydrocarbonFieldDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:ADMIN')")
    public ResponseEntity<HydrocarbonFieldDTO> create(@Valid @RequestBody HydrocarbonFieldDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:ADMIN')")
    public ResponseEntity<HydrocarbonFieldDTO> update(@PathVariable Long id, @Valid @RequestBody HydrocarbonFieldDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD:READ')")
    public ResponseEntity<Page<HydrocarbonFieldDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<HydrocarbonFieldDTO> searchByQuery(String query, Pageable pageable) {
        return hydrocarbonFieldService.globalSearch(query, pageable);
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
