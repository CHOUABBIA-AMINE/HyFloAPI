/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StructureTypeController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
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

@RestController
@RequestMapping("/general/type/structure")
@Slf4j
@Tag(name = "Structure Type Management", description = "APIs for managing organizational structure types and categories")
@SecurityRequirement(name = "bearer-auth")
public class StructureTypeController extends GenericController<StructureTypeDTO, Long> {

    private final StructureTypeService structureTypeService;

    public StructureTypeController(StructureTypeService structureTypeService) {
        super(structureTypeService, "StructureType");
        this.structureTypeService = structureTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    @Operation(summary = "Get structure type by ID", description = "Retrieves a single organizational structure type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structure type found", content = @Content(schema = @Schema(implementation = StructureTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Structure type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StructureTypeDTO> getById(
            @Parameter(description = "Structure type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    @Operation(summary = "Get all structure types (paginated)", description = "Retrieves a paginated list of all structure types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structure types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StructureTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    @Operation(summary = "Get all structure types (unpaginated)", description = "Retrieves all structure types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structure types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StructureTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:MANAGE')")
    @Operation(summary = "Create new structure type", description = "Creates a new structure type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Structure type created successfully", content = @Content(schema = @Schema(implementation = StructureTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Structure type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StructureTypeDTO> create(
            @Parameter(description = "Structure type data", required = true) 
            @Valid @RequestBody StructureTypeDTO dto) {
        StructureTypeDTO created = structureTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:MANAGE')")
    @Operation(summary = "Update structure type", description = "Updates an existing structure type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structure type updated successfully", content = @Content(schema = @Schema(implementation = StructureTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Structure type not found"),
        @ApiResponse(responseCode = "409", description = "Structure type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StructureTypeDTO> update(
            @Parameter(description = "Structure type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated structure type data", required = true) @Valid @RequestBody StructureTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:MANAGE')")
    @Operation(summary = "Delete structure type", description = "Deletes a structure type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Structure type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Structure type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete structure type - has dependencies (structures using this type)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Structure type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    @Operation(summary = "Search structure types", description = "Searches structure types by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StructureTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    @Operation(summary = "Check if structure type exists", description = "Checks if a structure type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Structure type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STRUCTURE_TYPE:READ')")
    @Operation(summary = "Count structure types", description = "Returns the total number of structure types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structure type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Get all structure types as list", description = "Retrieves all structure types as a simple list without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structure type list retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STRUCTURE_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StructureTypeDTO>> getAllList() {
        log.debug("GET /structure-type/list - Getting all structure types as list");
        List<StructureTypeDTO> types = structureTypeService.getAll();
        return success(types);
    }
}
