/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RegionController
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Organization
 *
 **/

package dz.sh.trc.hyflo.system.organization.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.organization.dto.RegionDTO;
import dz.sh.trc.hyflo.system.organization.service.RegionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/common/region")
@Slf4j
public class RegionController extends GenericController<RegionDTO, Long> {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        super(regionService, "Region");
        this.regionService = regionService;
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<RegionDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<Page<RegionDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<List<RegionDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:ADMIN')")
    public ResponseEntity<RegionDTO> create(@Valid @RequestBody RegionDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:ADMIN')")
    public ResponseEntity<RegionDTO> update(@PathVariable Long id, @Valid @RequestBody RegionDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<Page<RegionDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/zone/{zoneId}")
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<List<RegionDTO>> getByZone(@PathVariable Long zoneId) {
        log.info("GET /network/region/zone/{} - Getting regions by zone", zoneId);
        return ResponseEntity.ok(regionService.findByZone(zoneId));
    }

    @GetMapping("/activity/{activityId}")
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<List<RegionDTO>> getByActivity(@PathVariable Long activityId) {
        log.info("GET /network/region/activity/{} - Getting regions by activity", activityId);
        return ResponseEntity.ok(regionService.findByActivity(activityId));
    }
}
