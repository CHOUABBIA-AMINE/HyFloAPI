/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ArchiveBoxController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Environment
 *
 **/

package dz.mdn.iaas.common.environment.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.mdn.iaas.common.environment.dto.ArchiveBoxDTO;
import dz.mdn.iaas.common.environment.service.ArchiveBoxService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * ArchiveBox REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus archive box-specific operations
 */
@RestController
@RequestMapping("/common/environment/archiveBox")
@Slf4j
public class ArchiveBoxController extends GenericController<ArchiveBoxDTO, Long> {

    private final ArchiveBoxService archiveBoxService;

    public ArchiveBoxController(ArchiveBoxService archiveBoxService) {
        super(archiveBoxService, "ArchiveBox");
        this.archiveBoxService = archiveBoxService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:READ')")
    public ResponseEntity<ArchiveBoxDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:READ')")
    public ResponseEntity<Page<ArchiveBoxDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:READ')")
    public ResponseEntity<List<ArchiveBoxDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:ADMIN')")
    public ResponseEntity<ArchiveBoxDTO> create(@Valid @RequestBody ArchiveBoxDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:ADMIN')")
    public ResponseEntity<ArchiveBoxDTO> update(@PathVariable Long id, @Valid @RequestBody ArchiveBoxDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:READ')")
    public ResponseEntity<Page<ArchiveBoxDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<ArchiveBoxDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return archiveBoxService.getAll(pageable);
        }
        return archiveBoxService.globalSearch(query, pageable);
    }

    /**
     * Get all archive boxes as list (non-paginated)
     * GET /archive-box/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:READ')")
    public ResponseEntity<List<ArchiveBoxDTO>> getAllList() {
        log.debug("GET /archive-box/list - Getting all archive boxes as list");
        List<ArchiveBoxDTO> archiveBoxes = archiveBoxService.getAll();
        return success(archiveBoxes);
    }

    /**
     * Get archive boxes by shelf floor ID
     * GET /archive-box/shelf-floor/{shelfFloorId}
     */
    @GetMapping("/shelfFloor/{shelfFloorId}")
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:READ')")
    public ResponseEntity<List<ArchiveBoxDTO>> getByShelfFloorId(@PathVariable Long shelfFloorId) {
        log.debug("GET /archive-box/shelf-floor/{} - Getting archive boxes by shelf floor ID", shelfFloorId);
        List<ArchiveBoxDTO> archiveBoxes = archiveBoxService.getByShelfFloorId(shelfFloorId);
        return success(archiveBoxes);
    }

    /**
     * Get shelf floors by shelf ID
     * GET /shelf-floor/shelf/{shelfId}
     */
    @GetMapping("/shelf/{shelfId}")
    @PreAuthorize("hasAuthority('ARCHIVE_BOX:READ')")
    public ResponseEntity<List<ArchiveBoxDTO>> getByShelfId(@PathVariable Long shelfId) {
        log.debug("GET /archive-box/shelf/{} - Getting archive boxs by shelf ID", shelfId);
        List<ArchiveBoxDTO> archiveBoxes = archiveBoxService.getByShelfId(shelfId);
        return success(archiveBoxes);
    }
}
