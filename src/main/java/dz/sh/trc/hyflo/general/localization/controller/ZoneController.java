/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ZoneController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.general.localization.dto.ZoneDTO;
import dz.sh.trc.hyflo.general.localization.service.ZoneService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/general/localization/zone")
@Slf4j
public class ZoneController extends GenericController<ZoneDTO, Long> {

    @SuppressWarnings("unused")
	private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        super(zoneService, "Zone");
        this.zoneService = zoneService;
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    public ResponseEntity<ZoneDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    public ResponseEntity<Page<ZoneDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    public ResponseEntity<List<ZoneDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:ADMIN')")
    public ResponseEntity<ZoneDTO> create(@Valid @RequestBody ZoneDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:ADMIN')")
    public ResponseEntity<ZoneDTO> update(@PathVariable Long id, @Valid @RequestBody ZoneDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    public ResponseEntity<Page<ZoneDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
