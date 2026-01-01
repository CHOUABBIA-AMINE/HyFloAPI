/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AmendmentStepController
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

import dz.mdn.iaas.business.amendment.dto.AmendmentStepDTO;
import dz.mdn.iaas.business.amendment.service.AmendmentStepService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/business/amendment/amendmentStep")
@Slf4j
public class AmendmentStepController extends GenericController<AmendmentStepDTO, Long> {

    private final AmendmentStepService amendmentStepService;

    public AmendmentStepController(AmendmentStepService amendmentStepService) {
        super(amendmentStepService, "AmendmentStep");
        this.amendmentStepService = amendmentStepService;
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_STEP:READ')")
    public ResponseEntity<AmendmentStepDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_STEP:READ')")
    public ResponseEntity<Page<AmendmentStepDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_STEP:READ')")
    public ResponseEntity<List<AmendmentStepDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_STEP:ADMIN')")
    public ResponseEntity<AmendmentStepDTO> create(@Valid @RequestBody AmendmentStepDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_STEP:ADMIN')")
    public ResponseEntity<AmendmentStepDTO> update(@PathVariable Long id, @Valid @RequestBody AmendmentStepDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_STEP:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_STEP:READ')")
    public ResponseEntity<Page<AmendmentStepDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_STEP:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_STEP:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('AMENDMENT_STEP:READ')")
    public ResponseEntity<List<AmendmentStepDTO>> getAllList() {
        log.debug("GET /amendment-step/list");
        return success(amendmentStepService.getAll());
    }

    @GetMapping("/phase/{phaseId}")
    @PreAuthorize("hasAuthority('AMENDMENT_STEP:READ')")
    public ResponseEntity<List<AmendmentStepDTO>> getByPhase(@PathVariable Long phaseId) {
        log.debug("GET /amendment-step/phase/{}", phaseId);
        return success(amendmentStepService.getByPhaseId(phaseId));
    }
}
