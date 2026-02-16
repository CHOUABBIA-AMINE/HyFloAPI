/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ZoneController
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
import dz.sh.trc.hyflo.general.localization.dto.ZoneDTO;
import dz.sh.trc.hyflo.general.localization.service.ZoneService;
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
@RequestMapping("/general/localization/zone")
@Slf4j
@Tag(name = "Zone Management", description = "APIs for managing zones, areas, or spatial regions")
@SecurityRequirement(name = "bearer-auth")
public class ZoneController extends GenericController<ZoneDTO, Long> {

    @SuppressWarnings("unused")
	private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        super(zoneService, "Zone");
        this.zoneService = zoneService;
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    @Operation(summary = "Get zone by ID", description = "Retrieves a single zone by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Zone found", content = @Content(schema = @Schema(implementation = ZoneDTO.class))),
        @ApiResponse(responseCode = "404", description = "Zone not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ZONE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ZoneDTO> getById(
            @Parameter(description = "Zone ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    @Operation(summary = "Get all zones (paginated)", description = "Retrieves a paginated list of all zones")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Zones retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ZONE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ZoneDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    @Operation(summary = "Get all zones (unpaginated)", description = "Retrieves all zones without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Zones retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ZONE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ZoneDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:MANAGE')")
    @Operation(summary = "Create new zone", description = "Creates a new zone with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Zone created successfully", content = @Content(schema = @Schema(implementation = ZoneDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Zone name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ZONE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ZoneDTO> create(
            @Parameter(description = "Zone data", required = true) 
            @Valid @RequestBody ZoneDTO dto) {
        ZoneDTO created = zoneService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:MANAGE')")
    @Operation(summary = "Update zone", description = "Updates an existing zone")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Zone updated successfully", content = @Content(schema = @Schema(implementation = ZoneDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Zone not found"),
        @ApiResponse(responseCode = "409", description = "Zone name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ZONE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ZoneDTO> update(
            @Parameter(description = "Zone ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated zone data", required = true) @Valid @RequestBody ZoneDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:MANAGE')")
    @Operation(summary = "Delete zone", description = "Deletes a zone permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Zone deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Zone not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete zone - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ZONE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Zone ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    @Operation(summary = "Search zones", description = "Searches zones by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ZONE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ZoneDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    @Operation(summary = "Check if zone exists", description = "Checks if a zone with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ZONE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Zone ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ZONE:READ')")
    @Operation(summary = "Count zones", description = "Returns the total number of zones in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Zone count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ZONE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
