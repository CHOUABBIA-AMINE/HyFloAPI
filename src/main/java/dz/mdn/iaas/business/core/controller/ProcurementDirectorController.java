/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProcurementDirectorController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Core
 *
 **/

package dz.mdn.iaas.business.core.controller;

import dz.mdn.iaas.business.core.dto.ProcurementDirectorDTO;
import dz.mdn.iaas.business.core.service.ProcurementDirectorService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProcurementDirector REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for procurement directors
 * 
 * Inherited Endpoints:
 * - POST   /procurementDirector                 Create
 * - GET    /procurementDirector/{id}            Get by ID
 * - GET    /procurementDirector                 Get all (paginated)
 * - GET    /procurementDirector/all             Get all (non-paginated)
 * - PUT    /procurementDirector/{id}            Update
 * - DELETE /procurementDirector/{id}            Delete
 * - GET    /procurementDirector/search?q=...    Global search
 * - GET    /procurementDirector/{id}/exists     Check existence
 * - GET    /procurementDirector/count           Total count
 */
@RestController
@RequestMapping("/business/core/procurementDirector")
@Slf4j
public class ProcurementDirectorController extends GenericController<ProcurementDirectorDTO, Long> {

    private final ProcurementDirectorService procurementDirectorService;

    public ProcurementDirectorController(ProcurementDirectorService procurementDirectorService) {
        super(procurementDirectorService, "ProcurementDirector");
        this.procurementDirectorService = procurementDirectorService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_DIRECTOR:READ')")
    public ResponseEntity<ProcurementDirectorDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_DIRECTOR:READ')")
    public ResponseEntity<Page<ProcurementDirectorDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_DIRECTOR:READ')")
    public ResponseEntity<List<ProcurementDirectorDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_DIRECTOR:ADMIN')")
    public ResponseEntity<ProcurementDirectorDTO> create(@Valid @RequestBody ProcurementDirectorDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_DIRECTOR:ADMIN')")
    public ResponseEntity<ProcurementDirectorDTO> update(@PathVariable Long id, @Valid @RequestBody ProcurementDirectorDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_DIRECTOR:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_DIRECTOR:READ')")
    public ResponseEntity<Page<ProcurementDirectorDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_DIRECTOR:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_DIRECTOR:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<ProcurementDirectorDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return procurementDirectorService.getAll(pageable);
        }
        return procurementDirectorService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all procurement directors as list
     * GET /procurementDirector/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('PROCUREMENT_DIRECTOR:READ')")
    public ResponseEntity<List<ProcurementDirectorDTO>> getAllList() {
        log.debug("GET /procurementDirector/list - Getting all procurement directors as list");
        List<ProcurementDirectorDTO> directors = procurementDirectorService.getAll();
        return success(directors);
    }

    /**
     * Check if designation exists
     * GET /procurementDirector/exists/designation/{designation}
     */
    @GetMapping("/exists/designation/{designation}")
    @PreAuthorize("hasAuthority('PROCUREMENT_DIRECTOR:READ')")
    public ResponseEntity<Boolean> existsByDesignation(@PathVariable String designation) {
        log.debug("GET /procurementDirector/exists/designation/{}", designation);
        boolean exists = procurementDirectorService.existsByDesignation(designation);
        return success(exists);
    }
}
