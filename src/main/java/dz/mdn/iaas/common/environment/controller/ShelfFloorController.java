/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ShelfFloorController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Environment
 *
 **/

package dz.mdn.iaas.common.environment.controller;

import dz.mdn.iaas.common.environment.dto.ShelfFloorDTO;
import dz.mdn.iaas.common.environment.service.ShelfFloorService;
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
 * ShelfFloor REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus shelf floor-specific operations
 */
@RestController
@RequestMapping("/common/environment/shelfFloor")
@Slf4j
public class ShelfFloorController extends GenericController<ShelfFloorDTO, Long> {

    private final ShelfFloorService shelfFloorService;

    public ShelfFloorController(ShelfFloorService shelfFloorService) {
        super(shelfFloorService, "ShelfFloor");
        this.shelfFloorService = shelfFloorService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('SHELF_FLOOR:READ')")
    public ResponseEntity<ShelfFloorDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF_FLOOR:READ')")
    public ResponseEntity<Page<ShelfFloorDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF_FLOOR:READ')")
    public ResponseEntity<List<ShelfFloorDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF_FLOOR:ADMIN')")
    public ResponseEntity<ShelfFloorDTO> create(@Valid @RequestBody ShelfFloorDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF_FLOOR:ADMIN')")
    public ResponseEntity<ShelfFloorDTO> update(@PathVariable Long id, @Valid @RequestBody ShelfFloorDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF_FLOOR:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF_FLOOR:READ')")
    public ResponseEntity<Page<ShelfFloorDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF_FLOOR:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF_FLOOR:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<ShelfFloorDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return shelfFloorService.getAll(pageable);
        }
        return shelfFloorService.globalSearch(query, pageable);
    }

    /**
     * Get all shelf floors as list (non-paginated)
     * GET /shelf-floor/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('SHELF_FLOOR:READ')")
    public ResponseEntity<List<ShelfFloorDTO>> getAllList() {
        log.debug("GET /shelf-floor/list - Getting all shelf floors as list");
        List<ShelfFloorDTO> shelfFloors = shelfFloorService.getAll();
        return success(shelfFloors);
    }
}
