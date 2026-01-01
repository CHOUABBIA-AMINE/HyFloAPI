/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RoomController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Environment
 *
 **/

package dz.mdn.iaas.common.environment.controller;

import dz.mdn.iaas.common.environment.dto.RoomDTO;
import dz.mdn.iaas.common.environment.service.RoomService;
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
 * Room REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus room-specific operations
 */
@RestController
@RequestMapping("/common/environment/room")
@Slf4j
public class RoomController extends GenericController<RoomDTO, Long> {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        super(roomService, "Room");
        this.roomService = roomService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('ROOM:READ')")
    public ResponseEntity<RoomDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ROOM:READ')")
    public ResponseEntity<Page<RoomDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ROOM:READ')")
    public ResponseEntity<List<RoomDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ROOM:ADMIN')")
    public ResponseEntity<RoomDTO> create(@Valid @RequestBody RoomDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ROOM:ADMIN')")
    public ResponseEntity<RoomDTO> update(@PathVariable Long id, @Valid @RequestBody RoomDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ROOM:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ROOM:READ')")
    public ResponseEntity<Page<RoomDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ROOM:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ROOM:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<RoomDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return roomService.getAll(pageable);
        }
        return roomService.globalSearch(query, pageable);
    }

    /**
     * Get all rooms as list (non-paginated)
     * GET /room/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROOM:READ')")
    public ResponseEntity<List<RoomDTO>> getAllList() {
        log.debug("GET /room/list - Getting all rooms as list");
        List<RoomDTO> rooms = roomService.getAll();
        return success(rooms);
    }

    /**
     * Get rooms by floor ID
     * GET /room/floor/{floorId}
     */
    @GetMapping("/bloc/{blocId}")
    @PreAuthorize("hasAuthority('ROOM:READ')")
    public ResponseEntity<List<RoomDTO>> getByBlocId(@PathVariable Long blocId) {
        log.debug("GET /room/bloc/{} - Getting rooms by bloc ID", blocId);
        List<RoomDTO> rooms = roomService.getByBlocId(blocId);
        return success(rooms);
    }

    /**
     * Get rooms by floor ID
     * GET /room/floor/{floorId}
     */
    @GetMapping("/floor/{floorId}")
    @PreAuthorize("hasAuthority('ROOM:READ')")
    public ResponseEntity<List<RoomDTO>> getByFloorId(@PathVariable Long floorId) {
        log.debug("GET /room/floor/{} - Getting rooms by floor ID", floorId);
        List<RoomDTO> rooms = roomService.getByFloorId(floorId);
        return success(rooms);
    }
}
