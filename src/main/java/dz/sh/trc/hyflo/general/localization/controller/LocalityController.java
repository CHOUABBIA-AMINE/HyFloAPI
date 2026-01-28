/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: LocalityController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.general.localization.dto.LocalityDTO;
import dz.sh.trc.hyflo.general.localization.service.LocalityService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/general/localization/locality")
@Slf4j
public class LocalityController extends GenericController<LocalityDTO, Long> {

    private final LocalityService localityService;

    public LocalityController(LocalityService localityService) {
        super(localityService, "Locality");
        this.localityService = localityService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    public ResponseEntity<LocalityDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    public ResponseEntity<Page<LocalityDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    public ResponseEntity<List<LocalityDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:MANAGE')")
    public ResponseEntity<LocalityDTO> create(@Valid @RequestBody LocalityDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:MANAGE')")
    public ResponseEntity<LocalityDTO> update(@PathVariable Long id, @Valid @RequestBody LocalityDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    public ResponseEntity<Page<LocalityDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<LocalityDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return localityService.getAll(pageable);
        }
        return localityService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all localities as list (non-paginated)
     * GET /locality/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    public ResponseEntity<List<LocalityDTO>> getAllList() {
        log.debug("GET /locality/list - Getting all localities as list");
        List<LocalityDTO> localities = localityService.getAll();
        return success(localities);
    }

    /**
     * Get localities by district ID
     * GET /locality/district/{districtId}
     */
    @GetMapping("/district/{districtId}")
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    public ResponseEntity<List<LocalityDTO>> getByDistrictId(@PathVariable Long districtId) {
        log.debug("GET /locality/district/{} - Getting localities by district ID", districtId);
        List<LocalityDTO> localities = localityService.getByDistrictId(districtId);
        return success(localities);
    }
}
