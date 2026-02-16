/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DistrictController
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
import dz.sh.trc.hyflo.general.localization.dto.DistrictDTO;
import dz.sh.trc.hyflo.general.localization.service.DistrictService;
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
@RequestMapping("/general/localization/district")
@Slf4j
@Tag(name = "District Management", description = "APIs for managing administrative districts and regions")
@SecurityRequirement(name = "bearer-auth")
public class DistrictController extends GenericController<DistrictDTO, Long> {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        super(districtService, "District");
        this.districtService = districtService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    @Operation(summary = "Get district by ID", description = "Retrieves a single administrative district by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "District found", content = @Content(schema = @Schema(implementation = DistrictDTO.class))),
        @ApiResponse(responseCode = "404", description = "District not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DISTRICT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DistrictDTO> getById(
            @Parameter(description = "District ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    @Operation(summary = "Get all districts (paginated)", description = "Retrieves a paginated list of all administrative districts")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Districts retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DISTRICT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<DistrictDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    @Operation(summary = "Get all districts (unpaginated)", description = "Retrieves all administrative districts without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Districts retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DISTRICT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<DistrictDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:MANAGE')")
    @Operation(summary = "Create new district", description = "Creates a new administrative district with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "District created successfully", content = @Content(schema = @Schema(implementation = DistrictDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "District name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DISTRICT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DistrictDTO> create(
            @Parameter(description = "District data", required = true) 
            @Valid @RequestBody DistrictDTO dto) {
        DistrictDTO created = districtService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:MANAGE')")
    @Operation(summary = "Update district", description = "Updates an existing administrative district")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "District updated successfully", content = @Content(schema = @Schema(implementation = DistrictDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "District not found"),
        @ApiResponse(responseCode = "409", description = "District name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DISTRICT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DistrictDTO> update(
            @Parameter(description = "District ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated district data", required = true) @Valid @RequestBody DistrictDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:MANAGE')")
    @Operation(summary = "Delete district", description = "Deletes an administrative district permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "District deleted successfully"),
        @ApiResponse(responseCode = "404", description = "District not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete district - has dependencies (localities)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DISTRICT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "District ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    @Operation(summary = "Search districts", description = "Searches districts by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DISTRICT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<DistrictDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    @Operation(summary = "Check if district exists", description = "Checks if a district with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DISTRICT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "District ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    @Operation(summary = "Count districts", description = "Returns the total number of districts in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "District count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DISTRICT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<DistrictDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return districtService.getAll(pageable);
        }
        return districtService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all districts as list (non-paginated)
     * GET /district/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    @Operation(summary = "Get all districts as list", description = "Retrieves all administrative districts as a simple list without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "District list retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DISTRICT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<DistrictDTO>> getAllList() {
        log.debug("GET /district/list - Getting all districts as list");
        List<DistrictDTO> districts = districtService.getAll();
        return success(districts);
    }

    /**
     * Get districts by state ID
     * GET /district/state/{stateId}
     */
    @GetMapping("/state/{stateId}")
    @PreAuthorize("hasAuthority('DISTRICT:READ')")
    @Operation(summary = "Get districts by state", description = "Retrieves all districts belonging to a specific state")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Districts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "State not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DISTRICT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<DistrictDTO>> getByStateId(
            @Parameter(description = "State ID", required = true, example = "1") 
            @PathVariable Long stateId) {
        log.debug("GET /district/state/{} - Getting districts by state ID", stateId);
        List<DistrictDTO> districts = districtService.getByStateId(stateId);
        return success(districts);
    }
}
