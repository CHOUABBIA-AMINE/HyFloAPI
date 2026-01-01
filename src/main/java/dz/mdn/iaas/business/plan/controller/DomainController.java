/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DomainController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Plan
 *
 **/

package dz.mdn.iaas.business.plan.controller;

import dz.mdn.iaas.business.plan.dto.DomainDTO;
import dz.mdn.iaas.business.plan.service.DomainService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/plan/domain")
@Slf4j
public class DomainController extends GenericController<DomainDTO, Long> {

    private final DomainService domainService;

    public DomainController(DomainService domainService) {
        super(domainService, "Domain");
        this.domainService = domainService;
    }

    @Override
    @PreAuthorize("hasAuthority('DOMAIN:READ')")
    public ResponseEntity<DomainDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DOMAIN:READ')")
    public ResponseEntity<Page<DomainDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('DOMAIN:READ')")
    public ResponseEntity<List<DomainDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('DOMAIN:ADMIN')")
    public ResponseEntity<DomainDTO> create(@Valid @RequestBody DomainDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('DOMAIN:ADMIN')")
    public ResponseEntity<DomainDTO> update(@PathVariable Long id, @Valid @RequestBody DomainDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('DOMAIN:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DOMAIN:READ')")
    public ResponseEntity<Page<DomainDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('DOMAIN:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DOMAIN:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('DOMAIN:READ')")
    public ResponseEntity<List<DomainDTO>> getAllList() {
        log.debug("GET /domain/list");
        return success(domainService.getAll());
    }
}
