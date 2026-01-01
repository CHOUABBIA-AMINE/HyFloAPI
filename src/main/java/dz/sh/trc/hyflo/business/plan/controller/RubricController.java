/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RubricController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Plan
 *
 **/

package dz.sh.trc.hyflo.business.plan.controller;

import dz.sh.trc.hyflo.business.plan.dto.RubricDTO;
import dz.sh.trc.hyflo.business.plan.service.RubricService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/plan/rubric")
@Slf4j
public class RubricController extends GenericController<RubricDTO, Long> {

    private final RubricService rubricService;

    public RubricController(RubricService rubricService) {
        super(rubricService, "Rubric");
        this.rubricService = rubricService;
    }

    @Override
    @PreAuthorize("hasAuthority('RUBRIC:READ')")
    public ResponseEntity<RubricDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('RUBRIC:READ')")
    public ResponseEntity<Page<RubricDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('RUBRIC:READ')")
    public ResponseEntity<List<RubricDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('RUBRIC:ADMIN')")
    public ResponseEntity<RubricDTO> create(@Valid @RequestBody RubricDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('RUBRIC:ADMIN')")
    public ResponseEntity<RubricDTO> update(@PathVariable Long id, @Valid @RequestBody RubricDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('RUBRIC:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('RUBRIC:READ')")
    public ResponseEntity<Page<RubricDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('RUBRIC:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('RUBRIC:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('RUBRIC:READ')")
    public ResponseEntity<List<RubricDTO>> getAllList() {
        log.debug("GET /rubric/list");
        return success(rubricService.getAll());
    }
}
