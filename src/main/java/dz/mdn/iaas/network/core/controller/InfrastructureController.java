/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: InfrastructureController
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Controller
 *	@Layer		: Network / Controller
 *	@Package	: Network / Core
 *
 **/

package dz.mdn.iaas.network.core.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.mdn.iaas.configuration.template.GenericController;
import dz.mdn.iaas.network.core.dto.InfrastructureDTO;
import dz.mdn.iaas.network.core.service.InfrastructureService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/network/core/infrastructure")
@Slf4j
public class InfrastructureController extends GenericController<InfrastructureDTO, Long> {

    private final InfrastructureService infrastructureService;

    public InfrastructureController(InfrastructureService infrastructureService) {
        super(infrastructureService, "Infrastructure");
        this.infrastructureService = infrastructureService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    public ResponseEntity<InfrastructureDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    public ResponseEntity<Page<InfrastructureDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    public ResponseEntity<List<InfrastructureDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:ADMIN')")
    public ResponseEntity<InfrastructureDTO> create(@Valid @RequestBody InfrastructureDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:ADMIN')")
    public ResponseEntity<InfrastructureDTO> update(@PathVariable Long id, @Valid @RequestBody InfrastructureDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    public ResponseEntity<Page<InfrastructureDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<InfrastructureDTO> searchByQuery(String query, Pageable pageable) {
        return infrastructureService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
    
    @GetMapping("/region/{regionId}")
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    public ResponseEntity<List<InfrastructureDTO>> getByRegionId(@PathVariable Long regionId) {
        log.info("GET /network/infrastructure/activity/{} - Getting infrastructures by region", regionId);
        return ResponseEntity.ok(infrastructureService.findByRegion(regionId));
    }	
}
