/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FacilityTypeController
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
import dz.sh.trc.hyflo.network.type.dto.FacilityTypeDTO;
import dz.sh.trc.hyflo.network.type.service.FacilityTypeService;
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
@RequestMapping("/network/type/facility")
@Slf4j
@Tag(name = "Facility Type Management", description = "APIs for managing facility type classifications")
@SecurityRequirement(name = "bearer-auth")
public class FacilityTypeController extends GenericController<FacilityTypeDTO, Long> {

    @SuppressWarnings("unused")
	private final FacilityTypeService facilityTypeService;

    public FacilityTypeController(FacilityTypeService facilityTypeService) {
        super(facilityTypeService, "FacilityType");
        this.facilityTypeService = facilityTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    @Operation(summary = "Get facility type by ID", description = "Retrieves a single facility type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facility type found", content = @Content(schema = @Schema(implementation = FacilityTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Facility type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FacilityTypeDTO> getById(
            @Parameter(description = "Facility type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    @Operation(summary = "Get all facility types (paginated)", description = "Retrieves a paginated list of all facility types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facility types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<FacilityTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    @Operation(summary = "Get all facility types (unpaginated)", description = "Retrieves all facility types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facility types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FacilityTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:MANAGE')")
    @Operation(summary = "Create new facility type", description = "Creates a new facility type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Facility type created successfully", content = @Content(schema = @Schema(implementation = FacilityTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Facility type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FacilityTypeDTO> create(
            @Parameter(description = "Facility type data", required = true) 
            @Valid @RequestBody FacilityTypeDTO dto) {
        FacilityTypeDTO created = facilityTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:MANAGE')")
    @Operation(summary = "Update facility type", description = "Updates an existing facility type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facility type updated successfully", content = @Content(schema = @Schema(implementation = FacilityTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Facility type not found"),
        @ApiResponse(responseCode = "409", description = "Facility type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FacilityTypeDTO> update(
            @Parameter(description = "Facility type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated facility type data", required = true) @Valid @RequestBody FacilityTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:MANAGE')")
    @Operation(summary = "Delete facility type", description = "Deletes a facility type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Facility type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Facility type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete facility type - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Facility type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    @Operation(summary = "Search facility types", description = "Searches facility types by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<FacilityTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    @Operation(summary = "Check if facility type exists", description = "Checks if a facility type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Facility type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    @Operation(summary = "Count facility types", description = "Returns the total number of facility types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facility type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
