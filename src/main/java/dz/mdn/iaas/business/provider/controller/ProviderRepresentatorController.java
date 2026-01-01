/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProviderRepresentatorController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Controller
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Controller
 *
 **/

package dz.mdn.iaas.business.provider.controller;

import dz.mdn.iaas.business.provider.dto.ProviderRepresentatorDTO;
import dz.mdn.iaas.business.provider.service.ProviderRepresentatorService;
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
@RequestMapping("/business/provider/providerRepresentator")
@Slf4j
public class ProviderRepresentatorController extends GenericController<ProviderRepresentatorDTO, Long> {

    private final ProviderRepresentatorService providerRepresentatorService;

    public ProviderRepresentatorController(ProviderRepresentatorService providerRepresentatorService) {
        super(providerRepresentatorService, "ProviderRepresentator");
        this.providerRepresentatorService = providerRepresentatorService;
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_REPRESENTATOR:READ')")
    public ResponseEntity<ProviderRepresentatorDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_REPRESENTATOR:READ')")
    public ResponseEntity<Page<ProviderRepresentatorDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_REPRESENTATOR:READ')")
    public ResponseEntity<List<ProviderRepresentatorDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_REPRESENTATOR:ADMIN')")
    public ResponseEntity<ProviderRepresentatorDTO> create(@Valid @RequestBody ProviderRepresentatorDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_REPRESENTATOR:ADMIN')")
    public ResponseEntity<ProviderRepresentatorDTO> update(@PathVariable Long id, @Valid @RequestBody ProviderRepresentatorDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_REPRESENTATOR:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_REPRESENTATOR:READ')")
    public ResponseEntity<Page<ProviderRepresentatorDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_REPRESENTATOR:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROVIDER_REPRESENTATOR:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<ProviderRepresentatorDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return providerRepresentatorService.getAll(pageable);
        }
        return providerRepresentatorService.globalSearch(query, pageable);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('PROVIDER_REPRESENTATOR:READ')")
    public ResponseEntity<List<ProviderRepresentatorDTO>> getAllList() {
        log.debug("GET /provider-representator/list");
        return success(providerRepresentatorService.getAll());
    }

    @GetMapping("/provider/{providerId}")
    @PreAuthorize("hasAuthority('PROVIDER_REPRESENTATOR:READ')")
    public ResponseEntity<List<ProviderRepresentatorDTO>> getByProvider(@PathVariable Long providerId) {
        log.debug("GET /provider-representator/provider/{}", providerId);
        return success(providerRepresentatorService.getByProviderId(providerId));
    }
}
