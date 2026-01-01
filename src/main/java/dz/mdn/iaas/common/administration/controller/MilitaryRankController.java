/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryRankController
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.iaas.common.administration.controller;

import dz.mdn.iaas.common.administration.dto.MilitaryRankDTO;
import dz.mdn.iaas.common.administration.service.MilitaryRankService;
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
 * MilitaryRank REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus military rank-specific operations
 */
@RestController
@RequestMapping("/common/administration/militaryRank")
@Slf4j
public class MilitaryRankController extends GenericController<MilitaryRankDTO, Long> {

    private final MilitaryRankService militaryRankService;

    public MilitaryRankController(MilitaryRankService militaryRankService) {
        super(militaryRankService, "MilitaryRank");
        this.militaryRankService = militaryRankService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('MILITARY_RANK:READ')")
    public ResponseEntity<MilitaryRankDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_RANK:READ')")
    public ResponseEntity<Page<MilitaryRankDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_RANK:READ')")
    public ResponseEntity<List<MilitaryRankDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_RANK:ADMIN')")
    public ResponseEntity<MilitaryRankDTO> create(@Valid @RequestBody MilitaryRankDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_RANK:ADMIN')")
    public ResponseEntity<MilitaryRankDTO> update(@PathVariable Long id, @Valid @RequestBody MilitaryRankDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_RANK:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_RANK:READ')")
    public ResponseEntity<Page<MilitaryRankDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_RANK:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MILITARY_RANK:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<MilitaryRankDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return militaryRankService.getAll(pageable);
        }
        return militaryRankService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all military ranks as list (non-paginated)
     * GET /military-rank/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('MILITARY_RANK:READ')")
    public ResponseEntity<List<MilitaryRankDTO>> getAllList() {
        log.debug("GET /military-rank/list - Getting all military ranks as list");
        List<MilitaryRankDTO> ranks = militaryRankService.getAll();
        return success(ranks);
    }

    /**
     * Get military ranks by category ID
     * GET /military-rank/category/{categoryId}
     */
    @GetMapping("/category/{militaryCategoryId}")
    @PreAuthorize("hasAuthority('MILITARY_RANK:READ')")
    public ResponseEntity<List<MilitaryRankDTO>> getByMilitaryCategoryId(@PathVariable Long militaryCategoryId) {
        log.debug("GET /military-rank/category/{} - Getting military ranks by military category ID", militaryCategoryId);
        List<MilitaryRankDTO> militaryRanks = militaryRankService.getByMilitaryCategoryId(militaryCategoryId);
        return success(militaryRanks);
    }
}
