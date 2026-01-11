/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: HydrocarbonPlantTypeController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.type.dto.HydrocarbonPlantTypeDTO;
import dz.sh.trc.hyflo.network.type.service.HydrocarbonPlantTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/type/hydrocarbon/plant")
@Slf4j
public class HydrocarbonPlantTypeController extends GenericController<HydrocarbonPlantTypeDTO, Long> {

    private final HydrocarbonPlantTypeService hydrocarbonPlantTypeService;

    public HydrocarbonPlantTypeController(HydrocarbonPlantTypeService hydrocarbonPlantTypeService) {
        super(hydrocarbonPlantTypeService, "HydrocarbonPlantType");
        this.hydrocarbonPlantTypeService = hydrocarbonPlantTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:READ')")
    public ResponseEntity<HydrocarbonPlantTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:READ')")
    public ResponseEntity<Page<HydrocarbonPlantTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:READ')")
    public ResponseEntity<List<HydrocarbonPlantTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:ADMIN')")
    public ResponseEntity<HydrocarbonPlantTypeDTO> create(@Valid @RequestBody HydrocarbonPlantTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:ADMIN')")
    public ResponseEntity<HydrocarbonPlantTypeDTO> update(@PathVariable Long id, @Valid @RequestBody HydrocarbonPlantTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:READ')")
    public ResponseEntity<Page<HydrocarbonPlantTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<HydrocarbonPlantTypeDTO> searchByQuery(String query, Pageable pageable) {
        return hydrocarbonPlantTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
