/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProcurementStatusController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Core
 *
 **/

package dz.mdn.iaas.business.core.controller;

import dz.mdn.iaas.business.core.dto.ProcurementStatusDTO;
import dz.mdn.iaas.business.core.service.ProcurementStatusService;
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
 * ProcurementStatus REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for procurement statuses
 * 
 * Inherited Endpoints:
 * - POST   /procurementStatus                 Create
 * - GET    /procurementStatus/{id}            Get by ID
 * - GET    /procurementStatus                 Get all (paginated)
 * - GET    /procurementStatus/all             Get all (non-paginated)
 * - PUT    /procurementStatus/{id}            Update
 * - DELETE /procurementStatus/{id}            Delete
 * - GET    /procurementStatus/search?q=...    Global search
 * - GET    /procurementStatus/{id}/exists     Check existence
 * - GET    /procurementStatus/count           Total count
 */
@RestController
@RequestMapping("/business/core/procurementStatus")
@Slf4j
public class ProcurementStatusController extends GenericController<ProcurementStatusDTO, Long> {

    private final ProcurementStatusService procurementStatusService;

    public ProcurementStatusController(ProcurementStatusService procurementStatusService) {
        super(procurementStatusService, "ProcurementStatus");
        this.procurementStatusService = procurementStatusService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_STATUS:READ')")
    public ResponseEntity<ProcurementStatusDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_STATUS:READ')")
    public ResponseEntity<Page<ProcurementStatusDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_STATUS:READ')")
    public ResponseEntity<List<ProcurementStatusDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_STATUS:ADMIN')")
    public ResponseEntity<ProcurementStatusDTO> create(@Valid @RequestBody ProcurementStatusDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_STATUS:ADMIN')")
    public ResponseEntity<ProcurementStatusDTO> update(@PathVariable Long id, @Valid @RequestBody ProcurementStatusDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_STATUS:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_STATUS:READ')")
    public ResponseEntity<Page<ProcurementStatusDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_STATUS:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCUREMENT_STATUS:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<ProcurementStatusDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return procurementStatusService.getAll(pageable);
        }
        return procurementStatusService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all procurement statuses as list
     * GET /procurementStatus/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('PROCUREMENT_STATUS:READ')")
    public ResponseEntity<List<ProcurementStatusDTO>> getAllList() {
        log.debug("GET /procurementStatus/list - Getting all procurement statuses as list");
        List<ProcurementStatusDTO> statuses = procurementStatusService.getAll();
        return success(statuses);
    }

    /**
     * Check if designation exists
     * GET /procurementStatus/exists/designation/{designation}
     */
    @GetMapping("/exists/designation/{designation}")
    @PreAuthorize("hasAuthority('PROCUREMENT_STATUS:READ')")
    public ResponseEntity<Boolean> existsByDesignation(@PathVariable String designation) {
        log.debug("GET /procurementStatus/exists/designation/{}", designation);
        boolean exists = procurementStatusService.existsByDesignationFr(designation);
        return success(exists);
    }
}
