/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: BlocController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Environment
 *
 **/

package dz.sh.trc.hyflo.common.environment.controller;

import dz.sh.trc.hyflo.common.environment.dto.BlocDTO;
import dz.sh.trc.hyflo.common.environment.service.BlocService;
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
 * Bloc REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for Bloc management
 */
@RestController
@RequestMapping("/common/environment/bloc")
@Slf4j
public class BlocController extends GenericController<BlocDTO, Long> {

    private final BlocService blocService;

    public BlocController(BlocService blocService) {
        super(blocService, "Bloc");
        this.blocService = blocService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('BLOC:READ')")
    public ResponseEntity<BlocDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('BLOC:READ')")
    public ResponseEntity<Page<BlocDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('BLOC:READ')")
    public ResponseEntity<List<BlocDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('BLOC:ADMIN')")
    public ResponseEntity<BlocDTO> create(@Valid @RequestBody BlocDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('BLOC:ADMIN')")
    public ResponseEntity<BlocDTO> update(@PathVariable Long id, @Valid @RequestBody BlocDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('BLOC:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('BLOC:READ')")
    public ResponseEntity<Page<BlocDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('BLOC:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('BLOC:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<BlocDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return blocService.getAll(pageable);
        }
        return blocService.globalSearch(query, pageable);
    }

    /**
     * Get all blocs as list (non-paginated)
     * GET /bloc/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('BLOC:READ')")
    public ResponseEntity<List<BlocDTO>> getAllList() {
        log.debug("GET /bloc/list - Getting all blocs as list");
        List<BlocDTO> blocs = blocService.getAll();
        return success(blocs);
    }
}
