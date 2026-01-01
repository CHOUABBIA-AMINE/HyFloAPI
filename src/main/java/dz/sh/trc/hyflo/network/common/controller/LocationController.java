/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: LocationController
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Common 
 *
 **/

package dz.sh.trc.hyflo.network.common.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.common.dto.LocationDTO;
import dz.sh.trc.hyflo.network.common.service.LocationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/common/location")
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
}
