/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProcessingPlantTypeController
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
import dz.sh.trc.hyflo.network.type.dto.ProcessingPlantTypeDTO;
import dz.sh.trc.hyflo.network.type.service.ProcessingPlantTypeService;
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
public class ProcessingPlantTypeController extends GenericController<ProcessingPlantTypeDTO, Long> {

    private final ProcessingPlantTypeService processingPlantTypeService;

    public ProcessingPlantTypeController(ProcessingPlantTypeService processingPlantTypeService) {
        super(processingPlantTypeService, "ProcessingPlantType");
        this.processingPlantTypeService = processingPlantTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT_TYPE:READ')")
    public ResponseEntity<ProcessingPlantTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT_TYPE:READ')")
    public ResponseEntity<Page<ProcessingPlantTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT_TYPE:READ')")
    public ResponseEntity<List<ProcessingPlantTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT_TYPE:ADMIN')")
    public ResponseEntity<ProcessingPlantTypeDTO> create(@Valid @RequestBody ProcessingPlantTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT_TYPE:ADMIN')")
    public ResponseEntity<ProcessingPlantTypeDTO> update(@PathVariable Long id, @Valid @RequestBody ProcessingPlantTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT_TYPE:READ')")
    public ResponseEntity<Page<ProcessingPlantTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<ProcessingPlantTypeDTO> searchByQuery(String query, Pageable pageable) {
        return processingPlantTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('HYDROCARBON_PLANT_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
