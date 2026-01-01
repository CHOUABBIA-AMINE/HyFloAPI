/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ConsultationStepController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Consultation
 *
 **/

package dz.sh.trc.hyflo.business.consultation.controller;

import dz.sh.trc.hyflo.business.consultation.dto.ConsultationStepDTO;
import dz.sh.trc.hyflo.business.consultation.service.ConsultationStepService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/consultation/consultationStep")
@Slf4j
public class ConsultationStepController extends GenericController<ConsultationStepDTO, Long> {

    private final ConsultationStepService consultationStepService;

    public ConsultationStepController(ConsultationStepService consultationStepService) {
        super(consultationStepService, "ConsultationStep");
        this.consultationStepService = consultationStepService;
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_STEP:READ')")
    public ResponseEntity<ConsultationStepDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_STEP:READ')")
    public ResponseEntity<Page<ConsultationStepDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_STEP:READ')")
    public ResponseEntity<List<ConsultationStepDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_STEP:ADMIN')")
    public ResponseEntity<ConsultationStepDTO> create(@Valid @RequestBody ConsultationStepDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_STEP:ADMIN')")
    public ResponseEntity<ConsultationStepDTO> update(@PathVariable Long id, @Valid @RequestBody ConsultationStepDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_STEP:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_STEP:READ')")
    public ResponseEntity<Page<ConsultationStepDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_STEP:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_STEP:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('CONSULTATION_STEP:READ')")
    public ResponseEntity<List<ConsultationStepDTO>> getAllList() {
        log.debug("GET /consultation-step/list");
        return success(consultationStepService.getAll());
    }

    @GetMapping("/phase/{phaseId}")
    @PreAuthorize("hasAuthority('CONSULTATION_STEP:READ')")
    public ResponseEntity<List<ConsultationStepDTO>> getByPhase(@PathVariable Long phaseId) {
        log.debug("GET /consultation-step/phase/{}", phaseId);
        return success(consultationStepService.getByPhaseId(phaseId));
    }
}
