/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CoordinateController
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
import dz.sh.trc.hyflo.general.localization.dto.CoordinateDTO;
import dz.sh.trc.hyflo.general.localization.service.CoordinateService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/general/localization/coordinate")
@Slf4j
public class CoordinateController extends GenericController<CoordinateDTO, Long> {

    private final CoordinateService coordinateService;

    public CoordinateController(CoordinateService coordinateService) {
        super(coordinateService, "Coordinate");
        this.coordinateService = coordinateService;
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    public ResponseEntity<CoordinateDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    public ResponseEntity<Page<CoordinateDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    public ResponseEntity<List<CoordinateDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:ADMIN')")
    public ResponseEntity<CoordinateDTO> create(@Valid @RequestBody CoordinateDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:ADMIN')")
    public ResponseEntity<CoordinateDTO> update(@PathVariable Long id, @Valid @RequestBody CoordinateDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    public ResponseEntity<Page<CoordinateDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/locality/{localityId}")
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    public ResponseEntity<List<CoordinateDTO>> getByLocality(@PathVariable Long localityId) {
        log.info("GET /network/coordinate/locality/{} - Getting coordinates by locality", localityId);
        return ResponseEntity.ok(coordinateService.findByInfrastructure(localityId));
    }
}
