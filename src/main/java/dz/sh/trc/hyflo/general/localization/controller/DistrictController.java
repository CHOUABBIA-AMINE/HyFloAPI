/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DistrictController
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
import dz.sh.trc.hyflo.general.localization.dto.DistrictDTO;
import dz.sh.trc.hyflo.general.localization.service.DistrictService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/general/localization/district")
@Slf4j
public class DistrictController extends GenericController<DistrictDTO, Long> {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        super(districtService, "District");
        this.districtService = districtService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    public ResponseEntity<DistrictDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    public ResponseEntity<Page<DistrictDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    public ResponseEntity<List<DistrictDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:ADMIN')")
    public ResponseEntity<DistrictDTO> create(@Valid @RequestBody DistrictDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:ADMIN')")
    public ResponseEntity<DistrictDTO> update(@PathVariable Long id, @Valid @RequestBody DistrictDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    public ResponseEntity<Page<DistrictDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<DistrictDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return districtService.getAll(pageable);
        }
        return districtService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all districts as list (non-paginated)
     * GET /district/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    public ResponseEntity<List<DistrictDTO>> getAllList() {
        log.debug("GET /district/list - Getting all districts as list");
        List<DistrictDTO> districts = districtService.getAll();
        return success(districts);
    }

    /**
     * Get districts by state ID
     * GET /district/state/{stateId}
     */
    @GetMapping("/state/{stateId}")
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    public ResponseEntity<List<DistrictDTO>> getByStateId(@PathVariable Long stateId) {
        log.debug("GET /district/state/{} - Getting districts by state ID", stateId);
        List<DistrictDTO> districts = districtService.getByStateId(stateId);
        return success(districts);
    }
}
