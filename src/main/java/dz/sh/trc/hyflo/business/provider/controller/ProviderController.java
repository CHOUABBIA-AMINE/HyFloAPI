/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProviderController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Controller
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Controller
 *
 **/

package dz.sh.trc.hyflo.business.provider.controller;

import dz.sh.trc.hyflo.business.provider.dto.ProviderDTO;
import dz.sh.trc.hyflo.business.provider.service.ProviderService;
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
@RequestMapping("/business/provider/provider")
@Slf4j
public class ProviderController extends GenericController<ProviderDTO, Long> {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        super(providerService, "Provider");
        this.providerService = providerService;
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER:READ')")
    public ResponseEntity<ProviderDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER:READ')")
    public ResponseEntity<Page<ProviderDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER:READ')")
    public ResponseEntity<List<ProviderDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER:ADMIN')")
    public ResponseEntity<ProviderDTO> create(@Valid @RequestBody ProviderDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER:ADMIN')")
    public ResponseEntity<ProviderDTO> update(@PathVariable Long id, @Valid @RequestBody ProviderDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER:READ')")
    public ResponseEntity<Page<ProviderDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<ProviderDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return providerService.getAll(pageable);
        }
        return providerService.globalSearch(query, pageable);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('PROVIDER:READ')")
    public ResponseEntity<List<ProviderDTO>> getAllList() {
        log.debug("GET /provider/list");
        return success(providerService.getAll());
    }
}
