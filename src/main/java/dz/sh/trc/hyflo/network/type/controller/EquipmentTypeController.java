/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: EquipmentTypeController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.type.dto.EquipmentTypeDTO;
import dz.sh.trc.hyflo.network.type.service.EquipmentTypeService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/type/equipment")
@Slf4j
@Tag(name = "Equipment Type Management", description = "APIs for managing equipment type classifications")
@SecurityRequirement(name = "bearer-auth")
public class EquipmentTypeController extends GenericController<EquipmentTypeDTO, Long> {

    @SuppressWarnings("unused")
	private final EquipmentTypeService equipmentTypeService;

    public EquipmentTypeController(EquipmentTypeService equipmentTypeService) {
        super(equipmentTypeService, "EquipmentType");
        this.equipmentTypeService = equipmentTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE:READ')")
    @Operation(summary = "Get equipment type by ID", description = "Retrieves a single equipment type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment type found", content = @Content(schema = @Schema(implementation = EquipmentTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Equipment type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EquipmentTypeDTO> getById(
            @Parameter(description = "Equipment type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE:READ')")
    @Operation(summary = "Get all equipment types (paginated)", description = "Retrieves a paginated list of all equipment types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<EquipmentTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE:READ')")
    @Operation(summary = "Get all equipment types (unpaginated)", description = "Retrieves all equipment types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EquipmentTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE:MANAGE')")
    @Operation(summary = "Create new equipment type", description = "Creates a new equipment type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Equipment type created successfully", content = @Content(schema = @Schema(implementation = EquipmentTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Equipment type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EquipmentTypeDTO> create(
            @Parameter(description = "Equipment type data", required = true) 
            @Valid @RequestBody EquipmentTypeDTO dto) {
        EquipmentTypeDTO created = equipmentTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE:MANAGE')")
    @Operation(summary = "Update equipment type", description = "Updates an existing equipment type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment type updated successfully", content = @Content(schema = @Schema(implementation = EquipmentTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Equipment type not found"),
        @ApiResponse(responseCode = "409", description = "Equipment type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EquipmentTypeDTO> update(
            @Parameter(description = "Equipment type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated equipment type data", required = true) @Valid @RequestBody EquipmentTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE:MANAGE')")
    @Operation(summary = "Delete equipment type", description = "Deletes an equipment type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Equipment type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Equipment type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete equipment type - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Equipment type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE:READ')")
    @Operation(summary = "Search equipment types", description = "Searches equipment types by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<EquipmentTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE:READ')")
    @Operation(summary = "Check if equipment type exists", description = "Checks if an equipment type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Equipment type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT_TYPE:READ')")
    @Operation(summary = "Count equipment types", description = "Returns the total number of equipment types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
