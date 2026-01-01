/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AmendmentTypeController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Amendment
 *
 **/

package dz.sh.trc.hyflo.business.amendment.controller;

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

import dz.sh.trc.hyflo.business.amendment.dto.AmendmentTypeDTO;
import dz.sh.trc.hyflo.business.amendment.service.AmendmentTypeService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/business/amendment/amendmentType")
@Slf4j
public class AmendmentTypeController extends GenericController<AmendmentTypeDTO, Long> {

    private final AmendmentTypeService amendmentTypeService;

    public AmendmentTypeController(AmendmentTypeService amendmentTypeService) {
        super(amendmentTypeService, "AmendmentType");
        this.amendmentTypeService = amendmentTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_TYPE:READ')")
    public ResponseEntity<AmendmentTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_TYPE:READ')")
    public ResponseEntity<Page<AmendmentTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_TYPE:READ')")
    public ResponseEntity<List<AmendmentTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_TYPE:ADMIN')")
    public ResponseEntity<AmendmentTypeDTO> create(@Valid @RequestBody AmendmentTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_TYPE:ADMIN')")
    public ResponseEntity<AmendmentTypeDTO> update(@PathVariable Long id, @Valid @RequestBody AmendmentTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_TYPE:READ')")
    public ResponseEntity<Page<AmendmentTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AMENDMENT_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('AMENDMENT_TYPE:READ')")
    public ResponseEntity<List<AmendmentTypeDTO>> getAllList() {
        log.debug("GET /amendment-type/list");
        return success(amendmentTypeService.getAll());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('AMENDMENT_TYPE:READ')")
    public ResponseEntity<List<AmendmentTypeDTO>> getActiveTypes() {
        log.debug("GET /amendment-type/active");
        return success(amendmentTypeService.getActiveTypes());
    }
}
