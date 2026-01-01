/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: SubmissionController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Consultation
 *
 **/

package dz.mdn.iaas.business.consultation.controller;

import dz.mdn.iaas.business.consultation.dto.SubmissionDTO;
import dz.mdn.iaas.business.consultation.service.SubmissionService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/consultation/submission")
@Slf4j
public class SubmissionController extends GenericController<SubmissionDTO, Long> {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        super(submissionService, "Submission");
        this.submissionService = submissionService;
    }

    @Override
    @PreAuthorize("hasAuthority('SUBMISSION:READ')")
    public ResponseEntity<SubmissionDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('SUBMISSION:READ')")
    public ResponseEntity<Page<SubmissionDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('SUBMISSION:READ')")
    public ResponseEntity<List<SubmissionDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('SUBMISSION:ADMIN')")
    public ResponseEntity<SubmissionDTO> create(@Valid @RequestBody SubmissionDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('SUBMISSION:ADMIN')")
    public ResponseEntity<SubmissionDTO> update(@PathVariable Long id, @Valid @RequestBody SubmissionDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('SUBMISSION:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('SUBMISSION:READ')")
    public ResponseEntity<Page<SubmissionDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('SUBMISSION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('SUBMISSION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('SUBMISSION:READ')")
    public ResponseEntity<List<SubmissionDTO>> getAllList() {
        log.debug("GET /submission/list");
        return success(submissionService.getAll());
    }

    @GetMapping("/consultation/{consultationId}")
    @PreAuthorize("hasAuthority('SUBMISSION:READ')")
    public ResponseEntity<List<SubmissionDTO>> getByConsultation(@PathVariable Long consultationId) {
        log.debug("GET /submission/consultation/{}", consultationId);
        return success(submissionService.getByConsultationId(consultationId));
    }
}
