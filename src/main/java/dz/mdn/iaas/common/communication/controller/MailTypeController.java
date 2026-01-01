/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MailTypeController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Communication
 *
 **/

package dz.mdn.iaas.common.communication.controller;

import dz.mdn.iaas.common.communication.dto.MailTypeDTO;
import dz.mdn.iaas.common.communication.service.MailTypeService;
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
 * MailType REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for Mail Type management
 */
@RestController
@RequestMapping("/common/communication/mailType")
@Slf4j
public class MailTypeController extends GenericController<MailTypeDTO, Long> {

    private final MailTypeService mailTypeService;

    public MailTypeController(MailTypeService mailTypeService) {
        super(mailTypeService, "MailType");
        this.mailTypeService = mailTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('MAIL_TYPE:READ')")
    public ResponseEntity<MailTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_TYPE:READ')")
    public ResponseEntity<Page<MailTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_TYPE:READ')")
    public ResponseEntity<List<MailTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_TYPE:ADMIN')")
    public ResponseEntity<MailTypeDTO> create(@Valid @RequestBody MailTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_TYPE:ADMIN')")
    public ResponseEntity<MailTypeDTO> update(@PathVariable Long id, @Valid @RequestBody MailTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_TYPE:READ')")
    public ResponseEntity<Page<MailTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<MailTypeDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return mailTypeService.getAll(pageable);
        }
        return mailTypeService.globalSearch(query, pageable);
    }

    /**
     * Get all mail types as list (non-paginated)
     * GET /mail-type/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('MAIL_TYPE:READ')")
    public ResponseEntity<List<MailTypeDTO>> getAllList() {
        log.debug("GET /mail-type/list - Getting all mail types as list");
        List<MailTypeDTO> mailTypes = mailTypeService.getAll();
        return success(mailTypes);
    }
}
