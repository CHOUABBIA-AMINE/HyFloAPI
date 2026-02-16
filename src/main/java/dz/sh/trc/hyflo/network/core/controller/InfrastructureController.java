/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: InfrastructureController
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
import dz.sh.trc.hyflo.network.core.dto.InfrastructureDTO;
import dz.sh.trc.hyflo.network.core.service.InfrastructureService;
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
@RequestMapping("/network/core/infrastructure")
@Slf4j
@Tag(name = "Infrastructure Management", description = "APIs for managing pipeline infrastructure components")
@SecurityRequirement(name = "bearer-auth")
public class InfrastructureController extends GenericController<InfrastructureDTO, Long> {

    private final InfrastructureService infrastructureService;

    public InfrastructureController(InfrastructureService infrastructureService) {
        super(infrastructureService, "Infrastructure");
        this.infrastructureService = infrastructureService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    @Operation(summary = "Get infrastructure by ID", description = "Retrieves a single infrastructure component by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Infrastructure found", content = @Content(schema = @Schema(implementation = InfrastructureDTO.class))),
        @ApiResponse(responseCode = "404", description = "Infrastructure not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires INFRASTRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<InfrastructureDTO> getById(
            @Parameter(description = "Infrastructure ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    @Operation(summary = "Get all infrastructure (paginated)", description = "Retrieves a paginated list of all infrastructure components")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Infrastructure retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires INFRASTRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<InfrastructureDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    @Operation(summary = "Get all infrastructure (unpaginated)", description = "Retrieves all infrastructure components without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Infrastructure retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires INFRASTRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<InfrastructureDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:MANAGE')")
    @Operation(summary = "Create new infrastructure", description = "Creates a new infrastructure component with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Infrastructure created successfully", content = @Content(schema = @Schema(implementation = InfrastructureDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Infrastructure name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires INFRASTRUCTURE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<InfrastructureDTO> create(
            @Parameter(description = "Infrastructure data", required = true) 
            @Valid @RequestBody InfrastructureDTO dto) {
        InfrastructureDTO created = infrastructureService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:MANAGE')")
    @Operation(summary = "Update infrastructure", description = "Updates an existing infrastructure component")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Infrastructure updated successfully", content = @Content(schema = @Schema(implementation = InfrastructureDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Infrastructure not found"),
        @ApiResponse(responseCode = "409", description = "Infrastructure name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires INFRASTRUCTURE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<InfrastructureDTO> update(
            @Parameter(description = "Infrastructure ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated infrastructure data", required = true) @Valid @RequestBody InfrastructureDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:MANAGE')")
    @Operation(summary = "Delete infrastructure", description = "Deletes an infrastructure component permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Infrastructure deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Infrastructure not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete infrastructure - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires INFRASTRUCTURE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Infrastructure ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    @Operation(summary = "Search infrastructure", description = "Searches infrastructure by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires INFRASTRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<InfrastructureDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<InfrastructureDTO> searchByQuery(String query, Pageable pageable) {
        return infrastructureService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    @Operation(summary = "Check if infrastructure exists", description = "Checks if an infrastructure component with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires INFRASTRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Infrastructure ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    @Operation(summary = "Count infrastructure", description = "Returns the total number of infrastructure components in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Infrastructure count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires INFRASTRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAuthority('INFRASTRUCTURE:READ')")
    @Operation(summary = "Get infrastructure by owner", description = "Retrieves all infrastructure owned by a specific entity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Infrastructure retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires INFRASTRUCTURE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<InfrastructureDTO>> getByOwnerId(
            @Parameter(description = "Owner ID", required = true, example = "1") 
            @PathVariable Long ownerId) {
        log.info("REST request to get Infrastructure by owner: {}", ownerId);
        return ResponseEntity.ok(infrastructureService.findByOwner(ownerId));
    }
}
