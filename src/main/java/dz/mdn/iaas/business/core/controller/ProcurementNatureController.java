/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProcurementNatureController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Core
 *
 **/

package dz.mdn.iaas.business.core.controller;

import dz.mdn.iaas.business.core.dto.ProcurementNatureDTO;
import dz.mdn.iaas.business.core.service.ProcurementNatureService;
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
 * ProcurementNature REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for procurement natures
 * 
 * Inherited Endpoints:
 * - POST   /procurementNature                 Create
 * - GET    /procurementNature/{id}            Get by ID
 * - GET    /procurementNature                 Get all (paginated)
 * - GET    /procurementNature/all             Get all (non-paginated)
 * - PUT    /procurementNature/{id}            Update
 * - DELETE /procurementNature/{id}            Delete
 * - GET    /procurementNature/search?q=...    Global search
 * - GET    /procurementNature/{id}/exists     Check existence
 * - GET    /procurementNature/count           Total count
 */
@RestController
@RequestMapping("/business/core/procurementNature")
@Slf4j
public class ProcurementNatureController extends GenericController<ProcurementNatureDTO, Long> {

    private final ProcurementNatureService procurementNatureService;

    public ProcurementNatureController(ProcurementNatureService procurementNatureService) {
        super(procurementNatureService, "ProcurementNature");
        this.procurementNatureService = procurementNatureService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_NATURE:READ')")
    public ResponseEntity<ProcurementNatureDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_NATURE:READ')")
    public ResponseEntity<Page<ProcurementNatureDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_NATURE:READ')")
    public ResponseEntity<List<ProcurementNatureDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_NATURE:ADMIN')")
    public ResponseEntity<ProcurementNatureDTO> create(@Valid @RequestBody ProcurementNatureDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_NATURE:ADMIN')")
    public ResponseEntity<ProcurementNatureDTO> update(@PathVariable Long id, @Valid @RequestBody ProcurementNatureDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_NATURE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_NATURE:READ')")
    public ResponseEntity<Page<ProcurementNatureDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_NATURE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_NATURE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<ProcurementNatureDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return procurementNatureService.getAll(pageable);
        }
        return procurementNatureService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all procurement natures as list
     * GET /procurementNature/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('PROCUREMENT_NATURE:READ')")
    public ResponseEntity<List<ProcurementNatureDTO>> getAllList() {
        log.debug("GET /procurementNature/list - Getting all procurement natures as list");
        List<ProcurementNatureDTO> natures = procurementNatureService.getAll();
        return success(natures);
    }

    /**
     * Check if designation exists
     * GET /procurementNature/exists/designation/{designation}
     */
    @GetMapping("/exists/designation/{designation}")
    @PreAuthorize("hasAuthority('PROCUREMENT_NATURE:READ')")
    public ResponseEntity<Boolean> existsByDesignation(@PathVariable String designation) {
        log.debug("GET /procurementNature/exists/designation/{}", designation);
        boolean exists = procurementNatureService.existsByDesignationFr(designation);
        return success(exists);
    }
}
