/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StructureController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Organizational Structure Management", description = "APIs for managing hierarchical organizational structures and units")
@SecurityRequirement(name = "bearer-auth")
public class StructureController extends GenericController<StructureDTO, Long> {

    private final StructureService structureService;

    public StructureController(StructureService structureService) {
        super(structureService, "Structure");
        this.structureService = structureService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    @Operation(summary = "Get structure by ID", description = "Retrieves a single organizational structure by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structure found", content = @Content(schema = @Schema(implementation = StructureDTO.class))),
        @ApiResponse(responseCode = "404", description = "Structure not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StructureDTO> getById(
            @Parameter(description = "Structure ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    @Operation(summary = "Get all structures (paginated)", description = "Retrieves a paginated list of all organizational structures")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structures retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StructureDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    @Operation(summary = "Get all structures (unpaginated)", description = "Retrieves all organizational structures without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structures retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StructureDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:MANAGE')")
    @Operation(summary = "Create new structure", description = "Creates a new organizational structure with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Structure created successfully", content = @Content(schema = @Schema(implementation = StructureDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Structure name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StructureDTO> create(
            @Parameter(description = "Structure data", required = true) 
            @Valid @RequestBody StructureDTO dto) {
        StructureDTO created = structureService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:MANAGE')")
    @Operation(summary = "Update structure", description = "Updates an existing organizational structure")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structure updated successfully", content = @Content(schema = @Schema(implementation = StructureDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Structure not found"),
        @ApiResponse(responseCode = "409", description = "Structure name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StructureDTO> update(
            @Parameter(description = "Structure ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated structure data", required = true) @Valid @RequestBody StructureDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:MANAGE')")
    @Operation(summary = "Delete structure", description = "Deletes an organizational structure permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Structure deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Structure not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete structure - has child structures or dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Structure ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    @Operation(summary = "Search structures", description = "Searches structures by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StructureDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    @Operation(summary = "Check if structure exists", description = "Checks if a structure with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Structure ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE:READ')")
    @Operation(summary = "Count structures", description = "Returns the total number of organizational structures in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structure count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Get all structures as list", description = "Retrieves all organizational structures as a simple list without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structure list retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Get structures by type", description = "Retrieves all organizational structures of a specific structure type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structures retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Structure type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StructureDTO>> getByStructureTypeId(
            @Parameter(description = "Structure type ID", required = true, example = "1") 
            @PathVariable Long structureTypeId) {
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
    @Operation(summary = "Get child structures", description = "Retrieves all direct child structures of a parent structure")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Child structures retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Parent structure not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StructureDTO>> getByParentStructureId(
            @Parameter(description = "Parent structure ID", required = true, example = "1") 
            @PathVariable Long parentStructureId) {
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
    @Operation(summary = "Get root structures", description = "Retrieves all top-level structures that have no parent (root of hierarchy)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Root structures retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StructureDTO>> getRootStructures() {
        log.debug("GET /structure/root - Getting root structures");
        List<StructureDTO> structures = structureService.getRootStructures();
        return success(structures);
    }
}
