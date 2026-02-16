/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: EquipmentController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.core.dto.EquipmentDTO;
import dz.sh.trc.hyflo.network.core.service.EquipmentService;
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
@RequestMapping("/network/core/equipment")
@Slf4j
@Tag(name = "Equipment Management", description = "APIs for managing network equipment and devices")
@SecurityRequirement(name = "bearer-auth")
public class EquipmentController extends GenericController<EquipmentDTO, Long> {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        super(equipmentService, "Equipment");
        this.equipmentService = equipmentService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    @Operation(summary = "Get equipment by ID", description = "Retrieves a single equipment item by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment found", content = @Content(schema = @Schema(implementation = EquipmentDTO.class))),
        @ApiResponse(responseCode = "404", description = "Equipment not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EquipmentDTO> getById(
            @Parameter(description = "Equipment ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    @Operation(summary = "Get all equipment (paginated)", description = "Retrieves a paginated list of all equipment items")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<EquipmentDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    @Operation(summary = "Get all equipment (unpaginated)", description = "Retrieves all equipment items without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EquipmentDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:MANAGE')")
    @Operation(summary = "Create new equipment", description = "Creates a new equipment item with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Equipment created successfully", content = @Content(schema = @Schema(implementation = EquipmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Equipment name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EquipmentDTO> create(
            @Parameter(description = "Equipment data", required = true) 
            @Valid @RequestBody EquipmentDTO dto) {
        EquipmentDTO created = equipmentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:MANAGE')")
    @Operation(summary = "Update equipment", description = "Updates an existing equipment item")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment updated successfully", content = @Content(schema = @Schema(implementation = EquipmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Equipment not found"),
        @ApiResponse(responseCode = "409", description = "Equipment name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EquipmentDTO> update(
            @Parameter(description = "Equipment ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated equipment data", required = true) @Valid @RequestBody EquipmentDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:MANAGE')")
    @Operation(summary = "Delete equipment", description = "Deletes an equipment item permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Equipment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Equipment not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete equipment - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Equipment ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    @Operation(summary = "Search equipment", description = "Searches equipment by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<EquipmentDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    @Operation(summary = "Check if equipment exists", description = "Checks if an equipment item with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Equipment ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    @Operation(summary = "Count equipment", description = "Returns the total number of equipment items in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/facility/{facilityId}")
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    @Operation(summary = "Get equipment by facility", description = "Retrieves all equipment for a specific facility")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EquipmentDTO>> getByFacility(
            @Parameter(description = "Facility ID", required = true, example = "1") 
            @PathVariable Long facilityId) {
        log.info("REST request to get Equipment by facility id: {}", facilityId);
        return ResponseEntity.ok(equipmentService.findByFacility(facilityId));
    }

    @GetMapping("/type/{typeId}")
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    @Operation(summary = "Get equipment by type", description = "Retrieves all equipment of a specific type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipment retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EQUIPMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EquipmentDTO>> getByEquipmentType(
            @Parameter(description = "Equipment type ID", required = true, example = "1") 
            @PathVariable Long typeId) {
        log.info("REST request to get Equipment by equipment type id: {}", typeId);
        return ResponseEntity.ok(equipmentService.findByEquipmentType(typeId));
    }
}
