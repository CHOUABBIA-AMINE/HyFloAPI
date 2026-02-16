/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CoordinateController
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
import dz.sh.trc.hyflo.general.localization.dto.CoordinateDTO;
import dz.sh.trc.hyflo.general.localization.service.CoordinateService;
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
@RequestMapping("/general/localization/coordinate")
@Slf4j
@Tag(name = "Coordinate Management", description = "APIs for managing geographic coordinates (latitude/longitude)")
@SecurityRequirement(name = "bearer-auth")
public class CoordinateController extends GenericController<CoordinateDTO, Long> {

    private final CoordinateService coordinateService;

    public CoordinateController(CoordinateService coordinateService) {
        super(coordinateService, "Coordinate");
        this.coordinateService = coordinateService;
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    @Operation(summary = "Get coordinate by ID", description = "Retrieves a single geographic coordinate by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Coordinate found", content = @Content(schema = @Schema(implementation = CoordinateDTO.class))),
        @ApiResponse(responseCode = "404", description = "Coordinate not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COORDINATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CoordinateDTO> getById(
            @Parameter(description = "Coordinate ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    @Operation(summary = "Get all coordinates (paginated)", description = "Retrieves a paginated list of all geographic coordinates")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Coordinates retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COORDINATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<CoordinateDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    @Operation(summary = "Get all coordinates (unpaginated)", description = "Retrieves all geographic coordinates without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Coordinates retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COORDINATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CoordinateDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:MANAGE')")
    @Operation(summary = "Create new coordinate", description = "Creates a new geographic coordinate with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Coordinate created successfully", content = @Content(schema = @Schema(implementation = CoordinateDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed (invalid lat/long values)"),
        @ApiResponse(responseCode = "409", description = "Coordinate already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COORDINATE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CoordinateDTO> create(
            @Parameter(description = "Coordinate data", required = true) 
            @Valid @RequestBody CoordinateDTO dto) {
        CoordinateDTO created = coordinateService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:MANAGE')")
    @Operation(summary = "Update coordinate", description = "Updates an existing geographic coordinate")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Coordinate updated successfully", content = @Content(schema = @Schema(implementation = CoordinateDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Coordinate not found"),
        @ApiResponse(responseCode = "409", description = "Coordinate conflict"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COORDINATE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CoordinateDTO> update(
            @Parameter(description = "Coordinate ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated coordinate data", required = true) @Valid @RequestBody CoordinateDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:MANAGE')")
    @Operation(summary = "Delete coordinate", description = "Deletes a geographic coordinate permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Coordinate deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Coordinate not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete coordinate - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COORDINATE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Coordinate ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    @Operation(summary = "Search coordinates", description = "Searches coordinates by name or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COORDINATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<CoordinateDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    @Operation(summary = "Check if coordinate exists", description = "Checks if a coordinate with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COORDINATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Coordinate ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    @Operation(summary = "Count coordinates", description = "Returns the total number of coordinates in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Coordinate count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COORDINATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/infrastructure/{infrastructureId}")
    @PreAuthorize("hasAuthority('COORDINATE:READ')")
    @Operation(summary = "Get coordinates by infrastructure", description = "Retrieves all geographic coordinates associated with a specific infrastructure")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Coordinates retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Infrastructure not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COORDINATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CoordinateDTO>> getByInfrastructure(
            @Parameter(description = "Infrastructure ID", required = true, example = "1") 
            @PathVariable Long infrastructureId) {
        log.info("GET /network/coordinate/infrastructure/{} - Getting coordinates by infrastructure", infrastructureId);
        return ResponseEntity.ok(coordinateService.findByInfrastructure(infrastructureId));
    }

	/*
	 * @GetMapping("/locality/{localityId}")
	 * 
	 * @PreAuthorize("hasAuthority('COORDINATE:READ')") public
	 * ResponseEntity<List<CoordinateDTO>> getByLocality(@PathVariable Long
	 * localityId) { log.
	 * info("GET /network/coordinate/locality/{} - Getting coordinates by locality",
	 * localityId); return
	 * ResponseEntity.ok(coordinateService.findByLocality(localityId)); }
	 */
}
