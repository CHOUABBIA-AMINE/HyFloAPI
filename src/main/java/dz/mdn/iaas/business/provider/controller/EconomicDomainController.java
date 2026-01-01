/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: EconomicDomainController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Controller
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Controller
 *
 **/

package dz.mdn.iaas.business.provider.controller;

import dz.mdn.iaas.business.provider.dto.EconomicDomainDTO;
import dz.mdn.iaas.business.provider.service.EconomicDomainService;
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
@RequestMapping("/business/provider/economicDomain")
@Slf4j
public class EconomicDomainController extends GenericController<EconomicDomainDTO, Long> {

    private final EconomicDomainService economicDomainService;

    public EconomicDomainController(EconomicDomainService economicDomainService) {
        super(economicDomainService, "EconomicDomain");
        this.economicDomainService = economicDomainService;
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_DOMAIN:READ')")
    public ResponseEntity<EconomicDomainDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_DOMAIN:READ')")
    public ResponseEntity<Page<EconomicDomainDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_DOMAIN:READ')")
    public ResponseEntity<List<EconomicDomainDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_DOMAIN:ADMIN')")
    public ResponseEntity<EconomicDomainDTO> create(@Valid @RequestBody EconomicDomainDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_DOMAIN:ADMIN')")
    public ResponseEntity<EconomicDomainDTO> update(@PathVariable Long id, @Valid @RequestBody EconomicDomainDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_DOMAIN:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_DOMAIN:READ')")
    public ResponseEntity<Page<EconomicDomainDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_DOMAIN:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_DOMAIN:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<EconomicDomainDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return economicDomainService.getAll(pageable);
        }
        return economicDomainService.globalSearch(query, pageable);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ECONOMIC_DOMAIN:READ')")
    public ResponseEntity<List<EconomicDomainDTO>> getAllList() {
        log.debug("GET /economic-domain/list");
        return success(economicDomainService.getAll());
    }
}
