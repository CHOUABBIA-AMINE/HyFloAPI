/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StationTypeController
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
import dz.sh.trc.hyflo.network.type.dto.StationTypeDTO;
import dz.sh.trc.hyflo.network.type.service.StationTypeService;
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
@RequestMapping("/network/type/station")
@Slf4j
@Tag(name = "Station Type Management", description = "APIs for managing station type classifications")
@SecurityRequirement(name = "bearer-auth")
public class StationTypeController extends GenericController<StationTypeDTO, Long> {

    private final StationTypeService stationTypeService;

    public StationTypeController(StationTypeService stationTypeService) {
        super(stationTypeService, "StationType");
        this.stationTypeService = stationTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    @Operation(summary = "Get station type by ID", description = "Retrieves a single station type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Station type found", content = @Content(schema = @Schema(implementation = StationTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Station type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StationTypeDTO> getById(
            @Parameter(description = "Station type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    @Operation(summary = "Get all station types (paginated)", description = "Retrieves a paginated list of all station types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Station types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StationTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    @Operation(summary = "Get all station types (unpaginated)", description = "Retrieves all station types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Station types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StationTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:MANAGE')")
    @Operation(summary = "Create new station type", description = "Creates a new station type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Station type created successfully", content = @Content(schema = @Schema(implementation = StationTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Station type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StationTypeDTO> create(
            @Parameter(description = "Station type data", required = true) 
            @Valid @RequestBody StationTypeDTO dto) {
        StationTypeDTO created = stationTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:MANAGE')")
    @Operation(summary = "Update station type", description = "Updates an existing station type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Station type updated successfully", content = @Content(schema = @Schema(implementation = StationTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Station type not found"),
        @ApiResponse(responseCode = "409", description = "Station type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StationTypeDTO> update(
            @Parameter(description = "Station type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated station type data", required = true) @Valid @RequestBody StationTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:MANAGE')")
    @Operation(summary = "Delete station type", description = "Deletes a station type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Station type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Station type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete station type - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Station type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    @Operation(summary = "Search station types", description = "Searches station types by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StationTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<StationTypeDTO> searchByQuery(String query, Pageable pageable) {
        return stationTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    @Operation(summary = "Check if station type exists", description = "Checks if a station type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Station type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATION_TYPE:READ')")
    @Operation(summary = "Count station types", description = "Returns the total number of station types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Station type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
