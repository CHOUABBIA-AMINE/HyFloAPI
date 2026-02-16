/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: LocationController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.general.localization.dto.LocationDTO;
import dz.sh.trc.hyflo.general.localization.service.LocationService;
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
@RequestMapping("/general/localization/location")
@Slf4j
@Tag(name = "Location Management", description = "APIs for managing specific geographic locations and addresses")
@SecurityRequirement(name = "bearer-auth")
public class LocationController extends GenericController<LocationDTO, Long> {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        super(locationService, "Location");
        this.locationService = locationService;
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    @Operation(summary = "Get location by ID", description = "Retrieves a single geographic location by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Location found", content = @Content(schema = @Schema(implementation = LocationDTO.class))),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocationDTO> getById(
            @Parameter(description = "Location ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    @Operation(summary = "Get all locations (paginated)", description = "Retrieves a paginated list of all geographic locations")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Locations retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<LocationDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    @Operation(summary = "Get all locations (unpaginated)", description = "Retrieves all geographic locations without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Locations retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LocationDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:MANAGE')")
    @Operation(summary = "Create new location", description = "Creates a new geographic location with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Location created successfully", content = @Content(schema = @Schema(implementation = LocationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Location already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCATION:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocationDTO> create(
            @Parameter(description = "Location data", required = true) 
            @Valid @RequestBody LocationDTO dto) {
        LocationDTO created = locationService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:MANAGE')")
    @Operation(summary = "Update location", description = "Updates an existing geographic location")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Location updated successfully", content = @Content(schema = @Schema(implementation = LocationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "409", description = "Location conflict"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCATION:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocationDTO> update(
            @Parameter(description = "Location ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated location data", required = true) @Valid @RequestBody LocationDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:MANAGE')")
    @Operation(summary = "Delete location", description = "Deletes a geographic location permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Location deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Location not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete location - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCATION:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Location ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    @Operation(summary = "Search locations", description = "Searches locations by name, address, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<LocationDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    @Operation(summary = "Check if location exists", description = "Checks if a location with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Location ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    @Operation(summary = "Count locations", description = "Returns the total number of locations in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Location count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/locality/{localityId}")
    @PreAuthorize("hasAuthority('LOCATION:READ')")
    @Operation(summary = "Get locations by locality", description = "Retrieves all locations within a specific locality")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Locations retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Locality not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LocationDTO>> getByLocality(
            @Parameter(description = "Locality ID", required = true, example = "1") 
            @PathVariable Long localityId) {
        log.info("GET /network/location/locality/{} - Getting locations by locality", localityId);
        return ResponseEntity.ok(locationService.findByLocality(localityId));
    }
}
