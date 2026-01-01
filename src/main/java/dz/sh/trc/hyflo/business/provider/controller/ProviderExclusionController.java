/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProviderExclusionController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Controller
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Controller
 *
 **/

package dz.sh.trc.hyflo.business.provider.controller;

import dz.sh.trc.hyflo.business.provider.dto.ProviderExclusionDTO;
import dz.sh.trc.hyflo.business.provider.service.ProviderExclusionService;
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
@RequestMapping("/business/provider/providerExclusion")
@Slf4j
public class ProviderExclusionController extends GenericController<ProviderExclusionDTO, Long> {

    private final ProviderExclusionService providerExclusionService;

    public ProviderExclusionController(ProviderExclusionService providerExclusionService) {
        super(providerExclusionService, "ProviderExclusion");
        this.providerExclusionService = providerExclusionService;
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_EXCLUSION:READ')")
    public ResponseEntity<ProviderExclusionDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_EXCLUSION:READ')")
    public ResponseEntity<Page<ProviderExclusionDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_EXCLUSION:READ')")
    public ResponseEntity<List<ProviderExclusionDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_EXCLUSION:ADMIN')")
    public ResponseEntity<ProviderExclusionDTO> create(@Valid @RequestBody ProviderExclusionDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_EXCLUSION:ADMIN')")
    public ResponseEntity<ProviderExclusionDTO> update(@PathVariable Long id, @Valid @RequestBody ProviderExclusionDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_EXCLUSION:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_EXCLUSION:READ')")
    public ResponseEntity<Page<ProviderExclusionDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_EXCLUSION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_EXCLUSION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<ProviderExclusionDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return providerExclusionService.getAll(pageable);
        }
        return providerExclusionService.globalSearch(query, pageable);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('PROVIDER_EXCLUSION:READ')")
    public ResponseEntity<List<ProviderExclusionDTO>> getAllList() {
        log.debug("GET /provider-exclusion/list");
        return success(providerExclusionService.getAll());
    }

    @GetMapping("/provider/{providerId}")
    @PreAuthorize("hasAuthority('PROVIDER_EXCLUSION:READ')")
    public ResponseEntity<List<ProviderExclusionDTO>> getByProvider(@PathVariable Long providerId) {
        log.debug("GET /provider-exclusion/provider/{}", providerId);
        return success(providerExclusionService.getByProviderId(providerId));
    }
}
