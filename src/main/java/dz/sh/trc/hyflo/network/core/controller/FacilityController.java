/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FacilityController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

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

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.core.dto.FacilityDTO;
import dz.sh.trc.hyflo.network.core.service.FacilityService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/network/core/facility")
@Slf4j
public class FacilityController extends GenericController<FacilityDTO, Long> {

	private final FacilityService facilityService;
	
    public FacilityController(FacilityService facilityService) {
        super(facilityService, "Facility");
        this.facilityService = facilityService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    public ResponseEntity<FacilityDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    public ResponseEntity<Page<FacilityDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    public ResponseEntity<List<FacilityDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:MANAGE')")
    public ResponseEntity<FacilityDTO> create(@Valid @RequestBody FacilityDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:MANAGE')")
    public ResponseEntity<FacilityDTO> update(@PathVariable Long id, @Valid @RequestBody FacilityDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    public ResponseEntity<Page<FacilityDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/vendor/{vendorId}")
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    public ResponseEntity<List<FacilityDTO>> getByVendor(@PathVariable Long vendorId) {
        log.info("GET /network/facility/vendor/{} - Getting facilities by vendor", vendorId);
        return ResponseEntity.ok(facilityService.findByVendor(vendorId));
    }

    @GetMapping("/operationalStatus/{operationalStatusId}")
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    public ResponseEntity<List<FacilityDTO>> getByOperationalStatus(@PathVariable Long operationalStatusId) {
        log.info("GET /network/facility/operationalStatus/{} - Getting facilities by operational status", operationalStatusId);
        return ResponseEntity.ok(facilityService.findByOperationalStatus(operationalStatusId));
    }
}
