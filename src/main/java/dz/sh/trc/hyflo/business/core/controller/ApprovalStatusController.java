/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ApprovalStatusController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Core
 *
 **/

package dz.sh.trc.hyflo.business.core.controller;

import dz.sh.trc.hyflo.business.core.dto.ApprovalStatusDTO;
import dz.sh.trc.hyflo.business.core.service.ApprovalStatusService;
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
 * ApprovalStatus REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for approval statuses
 * 
 * Inherited Endpoints:
 * - POST   /approvalStatus                 Create
 * - GET    /approvalStatus/{id}            Get by ID
 * - GET    /approvalStatus                 Get all (paginated)
 * - GET    /approvalStatus/all             Get all (non-paginated)
 * - PUT    /approvalStatus/{id}            Update
 * - DELETE /approvalStatus/{id}            Delete
 * - GET    /approvalStatus/search?q=...    Global search
 * - GET    /approvalStatus/{id}/exists     Check existence
 * - GET    /approvalStatus/count           Total count
 */
@RestController
@RequestMapping("/business/core/approvalStatus")
@Slf4j
public class ApprovalStatusController extends GenericController<ApprovalStatusDTO, Long> {

    private final ApprovalStatusService approvalStatusService;

    public ApprovalStatusController(ApprovalStatusService approvalStatusService) {
        super(approvalStatusService, "ApprovalStatus");
        this.approvalStatusService = approvalStatusService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('APPROVAL_STATUS:READ')")
    public ResponseEntity<ApprovalStatusDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('APPROVAL_STATUS:READ')")
    public ResponseEntity<Page<ApprovalStatusDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('APPROVAL_STATUS:READ')")
    public ResponseEntity<List<ApprovalStatusDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('APPROVAL_STATUS:ADMIN')")
    public ResponseEntity<ApprovalStatusDTO> create(@Valid @RequestBody ApprovalStatusDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('APPROVAL_STATUS:ADMIN')")
    public ResponseEntity<ApprovalStatusDTO> update(@PathVariable Long id, @Valid @RequestBody ApprovalStatusDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('APPROVAL_STATUS:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('APPROVAL_STATUS:READ')")
    public ResponseEntity<Page<ApprovalStatusDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('APPROVAL_STATUS:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('APPROVAL_STATUS:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<ApprovalStatusDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return approvalStatusService.getAll(pageable);
        }
        return approvalStatusService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all approval statuses as list
     * GET /approvalStatus/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('APPROVAL_STATUS:READ')")
    public ResponseEntity<List<ApprovalStatusDTO>> getAllList() {
        log.debug("GET /approvalStatus/list - Getting all approval statuses as list");
        List<ApprovalStatusDTO> statuses = approvalStatusService.getAll();
        return success(statuses);
    }

    /**
     * Check if designation exists
     * GET /approvalStatus/exists/designation/{designation}
     */
    @GetMapping("/exists/designation/{designation}")
    @PreAuthorize("hasAuthority('APPROVAL_STATUS:READ')")
    public ResponseEntity<Boolean> existsByDesignation(@PathVariable String designation) {
        log.debug("GET /approvalStatus/exists/designation/{}", designation);
        boolean exists = approvalStatusService.existsByDesignationFr(designation);
        return success(exists);
    }
}
