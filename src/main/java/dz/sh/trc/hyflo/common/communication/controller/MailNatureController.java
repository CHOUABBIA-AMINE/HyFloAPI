/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MailNatureController
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Communication
 *
 **/

package dz.sh.trc.hyflo.common.communication.controller;

import dz.sh.trc.hyflo.common.communication.dto.MailNatureDTO;
import dz.sh.trc.hyflo.common.communication.service.MailNatureService;
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
 * MailNature REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for Mail Nature management
 */
@RestController
@RequestMapping("/common/communication/mailNature")
@Slf4j
public class MailNatureController extends GenericController<MailNatureDTO, Long> {

    private final MailNatureService mailNatureService;

    public MailNatureController(MailNatureService mailNatureService) {
        super(mailNatureService, "MailNature");
        this.mailNatureService = mailNatureService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('MAIL_NATURE:READ')")
    public ResponseEntity<MailNatureDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_NATURE:READ')")
    public ResponseEntity<Page<MailNatureDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_NATURE:READ')")
    public ResponseEntity<List<MailNatureDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_NATURE:ADMIN')")
    public ResponseEntity<MailNatureDTO> create(@Valid @RequestBody MailNatureDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_NATURE:ADMIN')")
    public ResponseEntity<MailNatureDTO> update(@PathVariable Long id, @Valid @RequestBody MailNatureDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_NATURE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_NATURE:READ')")
    public ResponseEntity<Page<MailNatureDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_NATURE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('MAIL_NATURE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<MailNatureDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return mailNatureService.getAll(pageable);
        }
        return mailNatureService.globalSearch(query, pageable);
    }

    /**
     * Get all mail natures as list (non-paginated)
     * GET /mail-nature/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('MAIL_NATURE:READ')")
    public ResponseEntity<List<MailNatureDTO>> getAllList() {
        log.debug("GET /mail-nature/list - Getting all mail natures as list");
        List<MailNatureDTO> mailNatures = mailNatureService.getAll();
        return success(mailNatures);
    }
}
