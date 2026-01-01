/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ClearanceController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Controller
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Controller
 *
 **/

package dz.sh.trc.hyflo.business.provider.controller;

import dz.sh.trc.hyflo.business.provider.dto.ClearanceDTO;
import dz.sh.trc.hyflo.business.provider.service.ClearanceService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clearance REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus clearance-specific operations
 * 
 * Inherited Endpoints:
 * - POST   /clearance                 Create clearance
 * - GET    /clearance/{id}            Get by ID
 * - GET    /clearance                 Get all (paginated)
 * - GET    /clearance/all             Get all (non-paginated)
 * - PUT    /clearance/{id}            Update clearance
 * - DELETE /clearance/{id}            Delete clearance
 * - GET    /clearance/search?q=...    Global search
 * - GET    /clearance/{id}/exists     Check existence
 * - GET    /clearance/count           Total count
 */
@RestController
@RequestMapping("/business/provider/clearance")
@Slf4j
public class ClearanceController extends GenericController<ClearanceDTO, Long> {

    private final ClearanceService clearanceService;

    public ClearanceController(ClearanceService clearanceService) {
        super(clearanceService, "Clearance");
        this.clearanceService = clearanceService;
    }

    @Override
    @PreAuthorize("hasAuthority('CLEARANCE:READ')")
    public ResponseEntity<ClearanceDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CLEARANCE:READ')")
    public ResponseEntity<Page<ClearanceDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CLEARANCE:READ')")
    public ResponseEntity<List<ClearanceDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('CLEARANCE:ADMIN')")
    public ResponseEntity<ClearanceDTO> create(@Valid @RequestBody ClearanceDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CLEARANCE:ADMIN')")
    public ResponseEntity<ClearanceDTO> update(@PathVariable Long id, @Valid @RequestBody ClearanceDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CLEARANCE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CLEARANCE:READ')")
    public ResponseEntity<Page<ClearanceDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CLEARANCE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CLEARANCE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<ClearanceDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return clearanceService.getAll(pageable);
        }
        return clearanceService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all clearances without pagination (custom implementation)
     * GET /clearance/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('CLEARANCE:READ')")
    public ResponseEntity<List<ClearanceDTO>> getAllList() {
        log.debug("GET /clearance/list - Getting all clearances as list");
        List<ClearanceDTO> clearances = clearanceService.getAll();
        return success(clearances);
    }
}
