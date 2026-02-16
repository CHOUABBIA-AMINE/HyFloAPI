/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: LocalityController
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
import dz.sh.trc.hyflo.general.localization.dto.LocalityDTO;
import dz.sh.trc.hyflo.general.localization.service.LocalityService;
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
@RequestMapping("/general/localization/locality")
@Slf4j
@Tag(name = "Locality Management", description = "APIs for managing localities, municipalities, and local administrative areas")
@SecurityRequirement(name = "bearer-auth")
public class LocalityController extends GenericController<LocalityDTO, Long> {

    private final LocalityService localityService;

    public LocalityController(LocalityService localityService) {
        super(localityService, "Locality");
        this.localityService = localityService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    @Operation(summary = "Get locality by ID", description = "Retrieves a single locality by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Locality found", content = @Content(schema = @Schema(implementation = LocalityDTO.class))),
        @ApiResponse(responseCode = "404", description = "Locality not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCALITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocalityDTO> getById(
            @Parameter(description = "Locality ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    @Operation(summary = "Get all localities (paginated)", description = "Retrieves a paginated list of all localities")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Localities retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCALITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<LocalityDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    @Operation(summary = "Get all localities (unpaginated)", description = "Retrieves all localities without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Localities retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCALITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LocalityDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:MANAGE')")
    @Operation(summary = "Create new locality", description = "Creates a new locality with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Locality created successfully", content = @Content(schema = @Schema(implementation = LocalityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Locality name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCALITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocalityDTO> create(
            @Parameter(description = "Locality data", required = true) 
            @Valid @RequestBody LocalityDTO dto) {
        LocalityDTO created = localityService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:MANAGE')")
    @Operation(summary = "Update locality", description = "Updates an existing locality")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Locality updated successfully", content = @Content(schema = @Schema(implementation = LocalityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Locality not found"),
        @ApiResponse(responseCode = "409", description = "Locality name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCALITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LocalityDTO> update(
            @Parameter(description = "Locality ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated locality data", required = true) @Valid @RequestBody LocalityDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:MANAGE')")
    @Operation(summary = "Delete locality", description = "Deletes a locality permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Locality deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Locality not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete locality - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCALITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Locality ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    @Operation(summary = "Search localities", description = "Searches localities by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCALITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<LocalityDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    @Operation(summary = "Check if locality exists", description = "Checks if a locality with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCALITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Locality ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    @Operation(summary = "Count localities", description = "Returns the total number of localities in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Locality count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCALITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<LocalityDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return localityService.getAll(pageable);
        }
        return localityService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all localities as list (non-paginated)
     * GET /locality/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    @Operation(summary = "Get all localities as list", description = "Retrieves all localities as a simple list without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Locality list retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCALITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LocalityDTO>> getAllList() {
        log.debug("GET /locality/list - Getting all localities as list");
        List<LocalityDTO> localities = localityService.getAll();
        return success(localities);
    }

    /**
     * Get localities by district ID
     * GET /locality/district/{districtId}
     */
    @GetMapping("/district/{districtId}")
    @PreAuthorize("hasAuthority('LOCALITY:READ')")
    @Operation(summary = "Get localities by district", description = "Retrieves all localities belonging to a specific district")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Localities retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "District not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires LOCALITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LocalityDTO>> getByDistrictId(
            @Parameter(description = "District ID", required = true, example = "1") 
            @PathVariable Long districtId) {
        log.debug("GET /locality/district/{} - Getting localities by district ID", districtId);
        List<LocalityDTO> localities = localityService.getByDistrictId(districtId);
        return success(localities);
    }
}
