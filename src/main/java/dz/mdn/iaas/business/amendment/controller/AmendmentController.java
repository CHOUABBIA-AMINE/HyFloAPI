/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AmendmentController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Amendment
 *
 **/

package dz.mdn.iaas.business.amendment.controller;

import dz.mdn.iaas.business.amendment.dto.AmendmentDTO;
import dz.mdn.iaas.business.amendment.service.AmendmentService;
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
 * Amendment REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus amendment-specific operations
 */
@RestController
@RequestMapping("/business/amendment/amendment")
@Slf4j
public class AmendmentController extends GenericController<AmendmentDTO, Long> {

    private final AmendmentService amendmentService;

    public AmendmentController(AmendmentService amendmentService) {
        super(amendmentService, "Amendment");
        this.amendmentService = amendmentService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT:READ')")
    public ResponseEntity<AmendmentDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT:READ')")
    public ResponseEntity<Page<AmendmentDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT:READ')")
    public ResponseEntity<List<AmendmentDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT:ADMIN')")
    public ResponseEntity<AmendmentDTO> create(@Valid @RequestBody AmendmentDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT:ADMIN')")
    public ResponseEntity<AmendmentDTO> update(@PathVariable Long id, @Valid @RequestBody AmendmentDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT:READ')")
    public ResponseEntity<Page<AmendmentDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<AmendmentDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return amendmentService.getAll(pageable);
        }
        return amendmentService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('AMENDMENT:READ')")
    public ResponseEntity<List<AmendmentDTO>> getAllList() {
        log.debug("GET /amendment/list - Getting all amendments as list");
        List<AmendmentDTO> amendments = amendmentService.getAll();
        return success(amendments);
    }
}
