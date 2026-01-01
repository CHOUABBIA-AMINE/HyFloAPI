/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DocumentTypeController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Document
 *
 **/

package dz.sh.trc.hyflo.common.document.controller;

import dz.sh.trc.hyflo.common.document.dto.DocumentTypeDTO;
import dz.sh.trc.hyflo.common.document.service.DocumentTypeService;
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
 * DocumentType REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for Document Type management
 */
@RestController
@RequestMapping("/common/document/documentType")
@Slf4j
public class DocumentTypeController extends GenericController<DocumentTypeDTO, Long> {

    private final DocumentTypeService documentTypeService;

    public DocumentTypeController(DocumentTypeService documentTypeService) {
        super(documentTypeService, "DocumentType");
        this.documentTypeService = documentTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE:READ')")
    public ResponseEntity<DocumentTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE:READ')")
    public ResponseEntity<Page<DocumentTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE:READ')")
    public ResponseEntity<List<DocumentTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE:ADMIN')")
    public ResponseEntity<DocumentTypeDTO> create(@Valid @RequestBody DocumentTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE:ADMIN')")
    public ResponseEntity<DocumentTypeDTO> update(@PathVariable Long id, @Valid @RequestBody DocumentTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE:READ')")
    public ResponseEntity<Page<DocumentTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<DocumentTypeDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return documentTypeService.getAll(pageable);
        }
        return documentTypeService.globalSearch(query, pageable);
    }

    /**
     * Get all document types as list (non-paginated)
     * GET /document-type/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('DOCUMENT_TYPE:READ')")
    public ResponseEntity<List<DocumentTypeDTO>> getAllList() {
        log.debug("GET /document-type/list - Getting all document types as list");
        List<DocumentTypeDTO> documentTypes = documentTypeService.getAll();
        return success(documentTypes);
    }
}
