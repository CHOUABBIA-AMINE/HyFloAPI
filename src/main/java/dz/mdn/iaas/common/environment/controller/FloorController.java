/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FloorController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Environment
 *
 **/

package dz.mdn.iaas.common.environment.controller;

import dz.mdn.iaas.common.environment.dto.FloorDTO;
import dz.mdn.iaas.common.environment.service.FloorService;
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
 * Floor REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus floor-specific operations
 */
@RestController
@RequestMapping("/common/environment/floor")
@Slf4j
public class FloorController extends GenericController<FloorDTO, Long> {

    private final FloorService floorService;

    public FloorController(FloorService floorService) {
        super(floorService, "Floor");
        this.floorService = floorService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FLOOR:READ')")
    public ResponseEntity<FloorDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOOR:READ')")
    public ResponseEntity<Page<FloorDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOOR:READ')")
    public ResponseEntity<List<FloorDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOOR:ADMIN')")
    public ResponseEntity<FloorDTO> create(@Valid @RequestBody FloorDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOOR:ADMIN')")
    public ResponseEntity<FloorDTO> update(@PathVariable Long id, @Valid @RequestBody FloorDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOOR:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOOR:READ')")
    public ResponseEntity<Page<FloorDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOOR:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOOR:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<FloorDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return floorService.getAll(pageable);
        }
        return floorService.globalSearch(query, pageable);
    }

    /**
     * Get all floors as list (non-paginated)
     * GET /floor/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('FLOOR:READ')")
    public ResponseEntity<List<FloorDTO>> getAllList() {
        log.debug("GET /floor/list - Getting all floors as list");
        List<FloorDTO> floors = floorService.getAll();
        return success(floors);
    }
}
