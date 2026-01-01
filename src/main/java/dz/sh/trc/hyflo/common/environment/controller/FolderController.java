/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FolderController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Environment
 *
 **/

package dz.sh.trc.hyflo.common.environment.controller;

import dz.sh.trc.hyflo.common.environment.dto.FolderDTO;
import dz.sh.trc.hyflo.common.environment.service.FolderService;
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
 * Folder REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus folder-specific operations
 */
@RestController
@RequestMapping("/common/environment/folder")
@Slf4j
public class FolderController extends GenericController<FolderDTO, Long> {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        super(folderService, "Folder");
        this.folderService = folderService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FOLDER:READ')")
    public ResponseEntity<FolderDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FOLDER:READ')")
    public ResponseEntity<Page<FolderDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FOLDER:READ')")
    public ResponseEntity<List<FolderDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FOLDER:ADMIN')")
    public ResponseEntity<FolderDTO> create(@Valid @RequestBody FolderDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FOLDER:ADMIN')")
    public ResponseEntity<FolderDTO> update(@PathVariable Long id, @Valid @RequestBody FolderDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FOLDER:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FOLDER:READ')")
    public ResponseEntity<Page<FolderDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FOLDER:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FOLDER:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<FolderDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return folderService.getAll(pageable);
        }
        return folderService.globalSearch(query, pageable);
    }

    /**
     * Get all folders as list (non-paginated)
     * GET /folder/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('FOLDER:READ')")
    public ResponseEntity<List<FolderDTO>> getAllList() {
        log.debug("GET /folder/list - Getting all folders as list");
        List<FolderDTO> folders = folderService.getAll();
        return success(folders);
    }

    /**
     * Get folders by archive box ID
     * GET /folder/archive-box/{archiveBoxId}
     */
    @GetMapping("/archiveBox/{archiveBoxId}")
    @PreAuthorize("hasAuthority('FOLDER:READ')")
    public ResponseEntity<List<FolderDTO>> getByArchiveBoxId(@PathVariable Long archiveBoxId) {
        log.debug("GET /folder/archive-box/{} - Getting folders by archive box ID", archiveBoxId);
        List<FolderDTO> folders = folderService.getByArchiveBoxId(archiveBoxId);
        return success(folders);
    }
}
