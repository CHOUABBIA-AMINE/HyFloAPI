/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DocumentController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Document
 *
 **/

package dz.sh.trc.hyflo.common.document.controller;

import dz.sh.trc.hyflo.common.document.dto.DocumentDTO;
import dz.sh.trc.hyflo.common.document.service.DocumentService;
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
 * Document REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for Document management
 */
@RestController
@RequestMapping("/common/document/document")
@Slf4j
public class DocumentController extends GenericController<DocumentDTO, Long> {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        super(documentService, "Document");
        this.documentService = documentService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT:READ')")
    public ResponseEntity<DocumentDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT:READ')")
    public ResponseEntity<Page<DocumentDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT:READ')")
    public ResponseEntity<List<DocumentDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT:ADMIN')")
    public ResponseEntity<DocumentDTO> create(@Valid @RequestBody DocumentDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT:ADMIN')")
    public ResponseEntity<DocumentDTO> update(@PathVariable Long id, @Valid @RequestBody DocumentDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT:READ')")
    public ResponseEntity<Page<DocumentDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<DocumentDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return documentService.getAll(pageable);
        }
        return documentService.globalSearch(query, pageable);
    }

    /**
     * Get all documents as list (non-paginated)
     * GET /document/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('DOCUMENT:READ')")
    public ResponseEntity<List<DocumentDTO>> getAllList() {
        log.debug("GET /document/list - Getting all documents as list");
        List<DocumentDTO> documents = documentService.getAll();
        return success(documents);
    }
}
