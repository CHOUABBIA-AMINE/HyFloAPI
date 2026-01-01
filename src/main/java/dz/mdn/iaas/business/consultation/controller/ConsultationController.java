/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ConsultationController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Consultation
 *
 **/

package dz.mdn.iaas.business.consultation.controller;

import dz.mdn.iaas.business.consultation.dto.ConsultationDTO;
import dz.mdn.iaas.business.consultation.service.ConsultationService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/consultation/consultation")
@Slf4j
public class ConsultationController extends GenericController<ConsultationDTO, Long> {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        super(consultationService, "Consultation");
        this.consultationService = consultationService;
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION:READ')")
    public ResponseEntity<ConsultationDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION:READ')")
    public ResponseEntity<Page<ConsultationDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION:READ')")
    public ResponseEntity<List<ConsultationDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION:ADMIN')")
    public ResponseEntity<ConsultationDTO> create(@Valid @RequestBody ConsultationDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION:ADMIN')")
    public ResponseEntity<ConsultationDTO> update(@PathVariable Long id, @Valid @RequestBody ConsultationDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION:READ')")
    public ResponseEntity<Page<ConsultationDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONSULTATION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<ConsultationDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return consultationService.getAll(pageable);
        }
        return consultationService.globalSearch(query, pageable);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('CONSULTATION:READ')")
    public ResponseEntity<List<ConsultationDTO>> getAllList() {
        log.debug("GET /consultation/list");
        return success(consultationService.getAll());
    }
}
