/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: LocationController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.controller;

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
import dz.sh.trc.hyflo.general.localization.dto.LocationDTO;
import dz.sh.trc.hyflo.general.localization.service.LocationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/general/localization/location")
@Slf4j
public class LocationController extends GenericController<LocationDTO, Long> {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        super(locationService, "Location");
        this.locationService = locationService;
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    public ResponseEntity<LocationDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    public ResponseEntity<Page<LocationDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    public ResponseEntity<List<LocationDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:ADMIN')")
    public ResponseEntity<LocationDTO> create(@Valid @RequestBody LocationDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:ADMIN')")
    public ResponseEntity<LocationDTO> update(@PathVariable Long id, @Valid @RequestBody LocationDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    public ResponseEntity<Page<LocationDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/locality/{localityId}")
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    public ResponseEntity<List<LocationDTO>> getByLocality(@PathVariable Long localityId) {
        log.info("GET /network/location/locality/{} - Getting locations by locality", localityId);
        return ResponseEntity.ok(locationService.findByLocality(localityId));
    }

    @GetMapping("/facility/{facilityId}")
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    public ResponseEntity<List<LocationDTO>> getByFacility(@PathVariable Long facilityId) {
        log.info("GET /network/location/infrastructure/{} - Getting locations by facility", facilityId);
        return ResponseEntity.ok(locationService.findByFacility(facilityId));
    }
}
