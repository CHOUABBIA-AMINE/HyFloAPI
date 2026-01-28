/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StructureTypeController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Type
 *
 **/

package dz.sh.trc.hyflo.general.type.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.general.type.dto.StructureTypeDTO;
import dz.sh.trc.hyflo.general.type.service.StructureTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/general/type/structure")
@Slf4j
public class StructureTypeController extends GenericController<StructureTypeDTO, Long> {

    private final StructureTypeService structureTypeService;

    public StructureTypeController(StructureTypeService structureTypeService) {
        super(structureTypeService, "StructureType");
        this.structureTypeService = structureTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    public ResponseEntity<StructureTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    public ResponseEntity<Page<StructureTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    public ResponseEntity<List<StructureTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:MANAGE')")
    public ResponseEntity<StructureTypeDTO> create(@Valid @RequestBody StructureTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:MANAGE')")
    public ResponseEntity<StructureTypeDTO> update(@PathVariable Long id, @Valid @RequestBody StructureTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    public ResponseEntity<Page<StructureTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<StructureTypeDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return structureTypeService.getAll(pageable);
        }
        return structureTypeService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all structure types as list (non-paginated)
     * GET /structure-type/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    public ResponseEntity<List<StructureTypeDTO>> getAllList() {
        log.debug("GET /structure-type/list - Getting all structure types as list");
        List<StructureTypeDTO> types = structureTypeService.getAll();
        return success(types);
    }
}
