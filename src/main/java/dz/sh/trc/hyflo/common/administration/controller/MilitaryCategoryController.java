/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryCategoryController
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Administration
 *
 **/

package dz.sh.trc.hyflo.common.administration.controller;

import dz.sh.trc.hyflo.common.administration.dto.MilitaryCategoryDTO;
import dz.sh.trc.hyflo.common.administration.service.MilitaryCategoryService;
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
 * MilitaryCategory REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for Military Category management
 */
@RestController
@RequestMapping("/common/administration/militaryCategory")
@Slf4j
public class MilitaryCategoryController extends GenericController<MilitaryCategoryDTO, Long> {

    private final MilitaryCategoryService militaryCategoryService;

    public MilitaryCategoryController(MilitaryCategoryService militaryCategoryService) {
        super(militaryCategoryService, "MilitaryCategory");
        this.militaryCategoryService = militaryCategoryService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('MILITARY_CATEGORY:READ')")
    public ResponseEntity<MilitaryCategoryDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_CATEGORY:READ')")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_CATEGORY:READ')")
    public ResponseEntity<List<MilitaryCategoryDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_CATEGORY:ADMIN')")
    public ResponseEntity<MilitaryCategoryDTO> create(@Valid @RequestBody MilitaryCategoryDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_CATEGORY:ADMIN')")
    public ResponseEntity<MilitaryCategoryDTO> update(@PathVariable Long id, @Valid @RequestBody MilitaryCategoryDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_CATEGORY:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_CATEGORY:READ')")
    public ResponseEntity<Page<MilitaryCategoryDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_CATEGORY:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_CATEGORY:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<MilitaryCategoryDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return militaryCategoryService.getAll(pageable);
        }
        return militaryCategoryService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all military categories as list (non-paginated)
     * GET /military-category/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('MILITARY_CATEGORY:READ')")
    public ResponseEntity<List<MilitaryCategoryDTO>> getAllList() {
        log.debug("GET /military-category/list - Getting all military categories as list");
        List<MilitaryCategoryDTO> categories = militaryCategoryService.getAll();
        return success(categories);
    }
}
