/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ExclusionTypeController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Controller
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Controller
 *
 **/

package dz.sh.trc.hyflo.business.provider.controller;

import dz.sh.trc.hyflo.business.provider.dto.ExclusionTypeDTO;
import dz.sh.trc.hyflo.business.provider.service.ExclusionTypeService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/provider/exclusionType")
@Slf4j
public class ExclusionTypeController extends GenericController<ExclusionTypeDTO, Long> {

    private final ExclusionTypeService exclusionTypeService;

    public ExclusionTypeController(ExclusionTypeService exclusionTypeService) {
        super(exclusionTypeService, "ExclusionType");
        this.exclusionTypeService = exclusionTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('EXCLUSION_TYPE:READ')")
    public ResponseEntity<ExclusionTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EXCLUSION_TYPE:READ')")
    public ResponseEntity<Page<ExclusionTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EXCLUSION_TYPE:READ')")
    public ResponseEntity<List<ExclusionTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('EXCLUSION_TYPE:ADMIN')")
    public ResponseEntity<ExclusionTypeDTO> create(@Valid @RequestBody ExclusionTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('EXCLUSION_TYPE:ADMIN')")
    public ResponseEntity<ExclusionTypeDTO> update(@PathVariable Long id, @Valid @RequestBody ExclusionTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('EXCLUSION_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EXCLUSION_TYPE:READ')")
    public ResponseEntity<Page<ExclusionTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EXCLUSION_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EXCLUSION_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<ExclusionTypeDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return exclusionTypeService.getAll(pageable);
        }
        return exclusionTypeService.globalSearch(query, pageable);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('EXCLUSION_TYPE:READ')")
    public ResponseEntity<List<ExclusionTypeDTO>> getAllList() {
        log.debug("GET /exclusion-type/list");
        return success(exclusionTypeService.getAll());
    }
}
