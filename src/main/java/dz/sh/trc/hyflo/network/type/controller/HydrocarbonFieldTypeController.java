/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: HydrocarbonFieldTypeController
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
import dz.sh.trc.hyflo.network.type.dto.HydrocarbonFieldTypeDTO;
import dz.sh.trc.hyflo.network.type.service.HydrocarbonFieldTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/type/hydrocarbonField")
@Slf4j
public class HydrocarbonFieldTypeController extends GenericController<HydrocarbonFieldTypeDTO, Long> {

    private final HydrocarbonFieldTypeService hydrocarbonFieldTypeService;

    public HydrocarbonFieldTypeController(HydrocarbonFieldTypeService hydrocarbonFieldTypeService) {
        super(hydrocarbonFieldTypeService, "HydrocarbonFieldType");
        this.hydrocarbonFieldTypeService = hydrocarbonFieldTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:READ')")
    public ResponseEntity<HydrocarbonFieldTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:READ')")
    public ResponseEntity<Page<HydrocarbonFieldTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:READ')")
    public ResponseEntity<List<HydrocarbonFieldTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:ADMIN')")
    public ResponseEntity<HydrocarbonFieldTypeDTO> create(@Valid @RequestBody HydrocarbonFieldTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:ADMIN')")
    public ResponseEntity<HydrocarbonFieldTypeDTO> update(@PathVariable Long id, @Valid @RequestBody HydrocarbonFieldTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_FIELD_TYPE:READ')")
    public ResponseEntity<Page<HydrocarbonFieldTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<HydrocarbonFieldTypeDTO> searchByQuery(String query, Pageable pageable) {
        return hydrocarbonFieldTypeService.globalSearch(query, pageable);
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
