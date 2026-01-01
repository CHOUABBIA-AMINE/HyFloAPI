/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ConsultationPhaseController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Consultation
 *
 **/

package dz.sh.trc.hyflo.business.consultation.controller;

import dz.sh.trc.hyflo.business.consultation.dto.ConsultationPhaseDTO;
import dz.sh.trc.hyflo.business.consultation.service.ConsultationPhaseService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/consultation/consultationPhase")
@Slf4j
public class ConsultationPhaseController extends GenericController<ConsultationPhaseDTO, Long> {

    private final ConsultationPhaseService consultationPhaseService;

    public ConsultationPhaseController(ConsultationPhaseService consultationPhaseService) {
        super(consultationPhaseService, "ConsultationPhase");
        this.consultationPhaseService = consultationPhaseService;
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_PHASE:READ')")
    public ResponseEntity<ConsultationPhaseDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_PHASE:READ')")
    public ResponseEntity<Page<ConsultationPhaseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_PHASE:READ')")
    public ResponseEntity<List<ConsultationPhaseDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_PHASE:ADMIN')")
    public ResponseEntity<ConsultationPhaseDTO> create(@Valid @RequestBody ConsultationPhaseDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_PHASE:ADMIN')")
    public ResponseEntity<ConsultationPhaseDTO> update(@PathVariable Long id, @Valid @RequestBody ConsultationPhaseDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_PHASE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_PHASE:READ')")
    public ResponseEntity<Page<ConsultationPhaseDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_PHASE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION_PHASE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('CONSULTATION_PHASE:READ')")
    public ResponseEntity<List<ConsultationPhaseDTO>> getAllList() {
        log.debug("GET /consultation-phase/list");
        return success(consultationPhaseService.getAll());
    }
}
