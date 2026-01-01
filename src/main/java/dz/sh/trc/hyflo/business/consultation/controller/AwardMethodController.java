/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AwardMethodController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Consultation
 *
 **/

package dz.sh.trc.hyflo.business.consultation.controller;

import dz.sh.trc.hyflo.business.consultation.dto.AwardMethodDTO;
import dz.sh.trc.hyflo.business.consultation.service.AwardMethodService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/consultation/awardMethod")
@Slf4j
public class AwardMethodController extends GenericController<AwardMethodDTO, Long> {

    private final AwardMethodService awardMethodService;

    public AwardMethodController(AwardMethodService awardMethodService) {
        super(awardMethodService, "AwardMethod");
        this.awardMethodService = awardMethodService;
    }

    @Override
    @PreAuthorize("hasAuthority('AWARD_METHOD:READ')")
    public ResponseEntity<AwardMethodDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AWARD_METHOD:READ')")
    public ResponseEntity<Page<AwardMethodDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AWARD_METHOD:READ')")
    public ResponseEntity<List<AwardMethodDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('AWARD_METHOD:ADMIN')")
    public ResponseEntity<AwardMethodDTO> create(@Valid @RequestBody AwardMethodDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AWARD_METHOD:ADMIN')")
    public ResponseEntity<AwardMethodDTO> update(@PathVariable Long id, @Valid @RequestBody AwardMethodDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AWARD_METHOD:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AWARD_METHOD:READ')")
    public ResponseEntity<Page<AwardMethodDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AWARD_METHOD:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AWARD_METHOD:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('AWARD_METHOD:READ')")
    public ResponseEntity<List<AwardMethodDTO>> getAllList() {
        log.debug("GET /award-method/list");
        return success(awardMethodService.getAll());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('AWARD_METHOD:READ')")
    public ResponseEntity<List<AwardMethodDTO>> getActiveMethods() {
        log.debug("GET /award-method/active");
        return success(awardMethodService.getActiveMethods());
    }
}
