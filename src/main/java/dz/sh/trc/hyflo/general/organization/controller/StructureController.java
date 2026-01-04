/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StructureController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import dz.sh.trc.hyflo.general.organization.service.StructureService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Structure REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus structure-specific operations
 */
@RestController
@RequestMapping("/general/organization/structure")
@Slf4j
public class StructureController extends GenericController<StructureDTO, Long> {

    private final StructureService structureService;

    public StructureController(StructureService structureService) {
        super(structureService, "Structure");
        this.structureService = structureService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    public ResponseEntity<StructureDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    public ResponseEntity<Page<StructureDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    public ResponseEntity<List<StructureDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:ADMIN')")
    public ResponseEntity<StructureDTO> create(@Valid @RequestBody StructureDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:ADMIN')")
    public ResponseEntity<StructureDTO> update(@PathVariable Long id, @Valid @RequestBody StructureDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    public ResponseEntity<Page<StructureDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<StructureDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return structureService.getAll(pageable);
        }
        log.debug("GET /search?q={} - Searching", query);
        return structureService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all structures as list (non-paginated)
     * GET /structure/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    public ResponseEntity<List<StructureDTO>> getAllList() {
        log.debug("GET /structure/list - Getting all structures as list");
        List<StructureDTO> structures = structureService.getAll();
        return success(structures);
    }

    /**
     * Get structures by structure type ID
     * GET /structure/type/{structureTypeId}
     */
    @GetMapping("/type/{structureTypeId}")
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    public ResponseEntity<List<StructureDTO>> getByStructureTypeId(@PathVariable Long structureTypeId) {
        log.debug("GET /structure/type/{} - Getting structures by structure type ID", structureTypeId);
        List<StructureDTO> structures = structureService.getByStructureTypeId(structureTypeId);
        return success(structures);
    }

    /**
     * Get structures by parent structure ID
     * GET /structure/parent/{parentStructureId}
     */
    @GetMapping("/parent/{parentStructureId}")
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    public ResponseEntity<List<StructureDTO>> getByParentStructureId(@PathVariable Long parentStructureId) {
        log.debug("GET /structure/parent/{} - Getting structures by parent structure ID", parentStructureId);
        List<StructureDTO> structures = structureService.getByParentStructureId(parentStructureId);
        return success(structures);
    }

    /**
     * Get root structures (no parent)
     * GET /structure/root
     */
    @GetMapping("/root")
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    public ResponseEntity<List<StructureDTO>> getRootStructures() {
        log.debug("GET /structure/root - Getting root structures");
        List<StructureDTO> structures = structureService.getRootStructures();
        return success(structures);
    }
}
