/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AmendmentPhaseController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Amendment
 *
 **/

package dz.mdn.iaas.business.amendment.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.mdn.iaas.business.amendment.dto.AmendmentPhaseDTO;
import dz.mdn.iaas.business.amendment.service.AmendmentPhaseService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/business/amendment/amendmentPhase")
@Slf4j
public class AmendmentPhaseController extends GenericController<AmendmentPhaseDTO, Long> {

    private final AmendmentPhaseService amendmentPhaseService;

    public AmendmentPhaseController(AmendmentPhaseService amendmentPhaseService) {
        super(amendmentPhaseService, "AmendmentPhase");
        this.amendmentPhaseService = amendmentPhaseService;
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_PHASE:READ')")
    public ResponseEntity<AmendmentPhaseDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_PHASE:READ')")
    public ResponseEntity<Page<AmendmentPhaseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_PHASE:READ')")
    public ResponseEntity<List<AmendmentPhaseDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_PHASE:ADMIN')")
    public ResponseEntity<AmendmentPhaseDTO> create(@Valid @RequestBody AmendmentPhaseDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_PHASE:ADMIN')")
    public ResponseEntity<AmendmentPhaseDTO> update(@PathVariable Long id, @Valid @RequestBody AmendmentPhaseDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_PHASE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_PHASE:READ')")
    public ResponseEntity<Page<AmendmentPhaseDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_PHASE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_PHASE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('AMENDMENT_PHASE:READ')")
    public ResponseEntity<List<AmendmentPhaseDTO>> getAllList() {
        log.debug("GET /amendment-phase/list");
        return success(amendmentPhaseService.getAll());
    }
}
