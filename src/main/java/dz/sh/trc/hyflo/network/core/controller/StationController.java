/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StationController
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
import dz.sh.trc.hyflo.network.core.dto.StationDTO;
import dz.sh.trc.hyflo.network.core.service.StationService;
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
@RequestMapping("/network/core/station")
@Slf4j
@Tag(name = "Station Management", description = "APIs for managing pumping and compressor stations")
@SecurityRequirement(name = "bearer-auth")
public class StationController extends GenericController<StationDTO, Long> {

    private final StationService stationService;

    public StationController(StationService stationService) {
        super(stationService, "Station");
        this.stationService = stationService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    @Operation(summary = "Get station by ID", description = "Retrieves a single station by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Station found", content = @Content(schema = @Schema(implementation = StationDTO.class))),
        @ApiResponse(responseCode = "404", description = "Station not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StationDTO> getById(
            @Parameter(description = "Station ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    @Operation(summary = "Get all stations (paginated)", description = "Retrieves a paginated list of all stations")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stations retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StationDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    @Operation(summary = "Get all stations (unpaginated)", description = "Retrieves all stations without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stations retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StationDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:MANAGE')")
    @Operation(summary = "Create new station", description = "Creates a new station with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Station created successfully", content = @Content(schema = @Schema(implementation = StationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Station name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StationDTO> create(
            @Parameter(description = "Station data", required = true) 
            @Valid @RequestBody StationDTO dto) {
        StationDTO created = stationService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:MANAGE')")
    @Operation(summary = "Update station", description = "Updates an existing station")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Station updated successfully", content = @Content(schema = @Schema(implementation = StationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Station not found"),
        @ApiResponse(responseCode = "409", description = "Station name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StationDTO> update(
            @Parameter(description = "Station ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated station data", required = true) @Valid @RequestBody StationDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:MANAGE')")
    @Operation(summary = "Delete station", description = "Deletes a station permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Station deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Station not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete station - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Station ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    @Operation(summary = "Search stations", description = "Searches stations by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StationDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<StationDTO> searchByQuery(String query, Pageable pageable) {
        return stationService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    @Operation(summary = "Check if station exists", description = "Checks if a station with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Station ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION:READ')")
    @Operation(summary = "Count stations", description = "Returns the total number of stations in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Station count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
