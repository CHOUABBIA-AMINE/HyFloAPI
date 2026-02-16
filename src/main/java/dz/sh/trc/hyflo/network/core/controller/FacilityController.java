/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FacilityController
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
import dz.sh.trc.hyflo.network.core.dto.FacilityDTO;
import dz.sh.trc.hyflo.network.core.service.FacilityService;
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
@RequestMapping("/network/core/facility")
@Slf4j
@Tag(name = "Facility Management", description = "APIs for managing oil & gas facilities")
@SecurityRequirement(name = "bearer-auth")
public class FacilityController extends GenericController<FacilityDTO, Long> {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        super(facilityService, "Facility");
        this.facilityService = facilityService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    @Operation(summary = "Get facility by ID", description = "Retrieves a single facility by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facility found", content = @Content(schema = @Schema(implementation = FacilityDTO.class))),
        @ApiResponse(responseCode = "404", description = "Facility not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FacilityDTO> getById(
            @Parameter(description = "Facility ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    @Operation(summary = "Get all facilities (paginated)", description = "Retrieves a paginated list of all facilities")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facilities retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<FacilityDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    @Operation(summary = "Get all facilities (unpaginated)", description = "Retrieves all facilities without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facilities retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FacilityDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:MANAGE')")
    @Operation(summary = "Create new facility", description = "Creates a new facility with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Facility created successfully", content = @Content(schema = @Schema(implementation = FacilityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Facility name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FacilityDTO> create(
            @Parameter(description = "Facility data", required = true) 
            @Valid @RequestBody FacilityDTO dto) {
        FacilityDTO created = facilityService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:MANAGE')")
    @Operation(summary = "Update facility", description = "Updates an existing facility")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facility updated successfully", content = @Content(schema = @Schema(implementation = FacilityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Facility not found"),
        @ApiResponse(responseCode = "409", description = "Facility name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FacilityDTO> update(
            @Parameter(description = "Facility ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated facility data", required = true) @Valid @RequestBody FacilityDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:MANAGE')")
    @Operation(summary = "Delete facility", description = "Deletes a facility permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Facility deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Facility not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete facility - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Facility ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    @Operation(summary = "Search facilities", description = "Searches facilities by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<FacilityDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    @Operation(summary = "Check if facility exists", description = "Checks if a facility with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Facility ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    @Operation(summary = "Count facilities", description = "Returns the total number of facilities in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facility count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/vendor/{vendorId}")
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    @Operation(summary = "Get facilities by vendor", description = "Retrieves all facilities from a specific vendor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facilities retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FacilityDTO>> getByVendor(
            @Parameter(description = "Vendor ID", required = true, example = "1") 
            @PathVariable Long vendorId) {
        log.info("REST request to get Facilities by vendor: {}", vendorId);
        return ResponseEntity.ok(facilityService.findByVendor(vendorId));
    }

    @GetMapping("/operationalStatus/{operationalStatusId}")
    @PreAuthorize("hasAuthority('FACILITY:READ')")
    @Operation(summary = "Get facilities by operational status", description = "Retrieves all facilities with a specific operational status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facilities retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FACILITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FacilityDTO>> getByOperationalStatus(
            @Parameter(description = "Operational status ID", required = true, example = "1") 
            @PathVariable Long operationalStatusId) {
        log.info("REST request to get Facilities by operational status: {}", operationalStatusId);
        return ResponseEntity.ok(facilityService.findByOperationalStatus(operationalStatusId));
    }
}
