/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MailController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Communication
 *
 **/

package dz.sh.trc.hyflo.common.communication.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.common.communication.dto.MailDTO;
import dz.sh.trc.hyflo.common.communication.service.MailService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Mail REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for Mail management
 */
@RestController
@RequestMapping("/common/communication/mail")
@Slf4j
public class MailController extends GenericController<MailDTO, Long> {

    private final MailService mailService;

    public MailController(MailService mailService) {
        super(mailService, "Mail");
        this.mailService = mailService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('MAIL:READ')")
    public ResponseEntity<MailDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL:READ')")
    public ResponseEntity<Page<MailDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL:READ')")
    public ResponseEntity<List<MailDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL:ADMIN')")
    public ResponseEntity<MailDTO> create(@Valid @RequestBody MailDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL:ADMIN')")
    public ResponseEntity<MailDTO> update(@PathVariable Long id, @Valid @RequestBody MailDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL:READ')")
    public ResponseEntity<Page<MailDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<MailDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return mailService.getAll(pageable);
        }
        return mailService.globalSearch(query, pageable);
    }

    /**
     * Get all mails as list (non-paginated)
     * GET /mail/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('MAIL:READ')")
    public ResponseEntity<List<MailDTO>> getAllList() {
        log.debug("GET /mail/list - Getting all mails as list");
        List<MailDTO> mails = mailService.getAll();
        return success(mails);
    }
    
    
    // ==================== REFERENCED MAILS ENDPOINTS ====================

    /**
     * Get all mails referenced by a specific mail
     * 
     * Frontend call: GET /common/communication/mail/{id}/referenced-mails
     * Returns: List<MailDTO>
     */
    @GetMapping("/{id}/referencedMails")
    public ResponseEntity<List<MailDTO>> getReferencedMails(@PathVariable Long id) {
        try {
            List<MailDTO> referencedMails = mailService.getReferencedMails(id);
            return ResponseEntity.ok(referencedMails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update all referenced mails (replaces existing references)
     * 
     * Frontend call: PUT /common/communication/mail/{id}/referenced-mails
     * Body: [1, 2, 3, 4]
     */
    @PutMapping("/{id}/referencedMails")
    public ResponseEntity<?> updateReferencedMails(
            @PathVariable Long id,
            @RequestBody List<Long> referencedMailIds) {
        try {
            mailService.updateReferencedMails(id, referencedMailIds);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update references");
        }
    }

    
}
