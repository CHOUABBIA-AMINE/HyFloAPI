/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: StationController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.core.dto.StationDTO;
import dz.sh.trc.hyflo.network.core.service.StationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/core/station")
@Slf4j
public class StationController extends GenericController<StationDTO, Long> {

    private final StationService stationService;

    public StationController(StationService stationService) {
        super(stationService, "Station");
        this.stationService = stationService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    public ResponseEntity<StationDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    public ResponseEntity<Page<StationDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    public ResponseEntity<List<StationDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:ADMIN')")
    public ResponseEntity<StationDTO> create(@Valid @RequestBody StationDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:ADMIN')")
    public ResponseEntity<StationDTO> update(@PathVariable Long id, @Valid @RequestBody StationDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    public ResponseEntity<Page<StationDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<StationDTO> searchByQuery(String query, Pageable pageable) {
        return stationService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
