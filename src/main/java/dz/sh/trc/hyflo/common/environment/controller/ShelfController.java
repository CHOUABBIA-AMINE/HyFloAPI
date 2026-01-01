/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ShelfController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Environment
 *
 **/

package dz.sh.trc.hyflo.common.environment.controller;

import dz.sh.trc.hyflo.common.environment.dto.ShelfDTO;
import dz.sh.trc.hyflo.common.environment.service.ShelfService;
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
 * Shelf REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus shelf-specific operations
 */
@RestController
@RequestMapping("/common/environment/shelf")
@Slf4j
public class ShelfController extends GenericController<ShelfDTO, Long> {

    private final ShelfService shelfService;

    public ShelfController(ShelfService shelfService) {
        super(shelfService, "Shelf");
        this.shelfService = shelfService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('SHELF:READ')")
    public ResponseEntity<ShelfDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF:READ')")
    public ResponseEntity<Page<ShelfDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF:READ')")
    public ResponseEntity<List<ShelfDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF:ADMIN')")
    public ResponseEntity<ShelfDTO> create(@Valid @RequestBody ShelfDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF:ADMIN')")
    public ResponseEntity<ShelfDTO> update(@PathVariable Long id, @Valid @RequestBody ShelfDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF:READ')")
    public ResponseEntity<Page<ShelfDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('SHELF:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<ShelfDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return shelfService.getAll(pageable);
        }
        return shelfService.globalSearch(query, pageable);
    }

    /**
     * Get all shelves as list (non-paginated)
     * GET /shelf/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('SHELF:READ')")
    public ResponseEntity<List<ShelfDTO>> getAllList() {
        log.debug("GET /shelf/list - Getting all shelves as list");
        List<ShelfDTO> shelves = shelfService.getAll();
        return success(shelves);
    }

    /**
     * Get shelves by room ID
     * GET /shelf/room/{roomId}
     */
    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasAuthority('SHELF:READ')")
    public ResponseEntity<List<ShelfDTO>> getByRoomId(@PathVariable Long roomId) {
        log.debug("GET /shelf/room/{} - Getting shelves by room ID", roomId);
        List<ShelfDTO> shelves = shelfService.getByRoomId(roomId);
        return success(shelves);
    }
}
