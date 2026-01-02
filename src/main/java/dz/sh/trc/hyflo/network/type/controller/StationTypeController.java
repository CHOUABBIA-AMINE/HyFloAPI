/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: StationTypeController
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.type.dto.StationTypeDTO;
import dz.sh.trc.hyflo.network.type.service.StationTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/type/station")
@Slf4j
public class StationTypeController extends GenericController<StationTypeDTO, Long> {

    private final StationTypeService stationTypeService;

    public StationTypeController(StationTypeService stationTypeService) {
        super(stationTypeService, "StationType");
        this.stationTypeService = stationTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    public ResponseEntity<StationTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    public ResponseEntity<Page<StationTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    public ResponseEntity<List<StationTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:ADMIN')")
    public ResponseEntity<StationTypeDTO> create(@Valid @RequestBody StationTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:ADMIN')")
    public ResponseEntity<StationTypeDTO> update(@PathVariable Long id, @Valid @RequestBody StationTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    public ResponseEntity<Page<StationTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<StationTypeDTO> searchByQuery(String query, Pageable pageable) {
        return stationTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
