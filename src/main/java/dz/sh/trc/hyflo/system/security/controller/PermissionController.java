/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PermissionController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.security.dto.PermissionDTO;
import dz.sh.trc.hyflo.system.security.service.PermissionService;
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

@RestController
@RequestMapping("/system/security/permission")
@Slf4j
@Tag(name = "Permission Management", description = "APIs for managing granular permissions (resource:action combinations)")
@SecurityRequirement(name = "bearer-auth")
public class PermissionController extends GenericController<PermissionDTO, Long> {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        super(permissionService, "Permission");
        this.permissionService = permissionService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    @Operation(summary = "Get permission by ID", description = "Retrieves a single permission by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permission found", content = @Content(schema = @Schema(implementation = PermissionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Permission not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PermissionDTO> getById(
            @Parameter(description = "Permission ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    @Operation(summary = "Get all permissions (paginated)", description = "Retrieves a paginated list of all permissions")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PermissionDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    @Operation(summary = "Get all permissions (unpaginated)", description = "Retrieves all permissions without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PermissionDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:MANAGE')")
    @Operation(summary = "Create a new permission", description = "Creates a new permission with validation for unique name")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Permission created successfully", content = @Content(schema = @Schema(implementation = PermissionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Permission name already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PermissionDTO> create(
            @Parameter(description = "Permission data", required = true) 
            @Valid @RequestBody PermissionDTO dto) {
        PermissionDTO created = permissionService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:MANAGE')")
    @Operation(summary = "Update permission", description = "Updates an existing permission")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permission updated successfully", content = @Content(schema = @Schema(implementation = PermissionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Permission not found"),
        @ApiResponse(responseCode = "409", description = "Permission name already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PermissionDTO> update(
            @Parameter(description = "Permission ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated permission data", required = true) @Valid @RequestBody PermissionDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:MANAGE')")
    @Operation(summary = "Delete permission", description = "Deletes a permission permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Permission deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Permission not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete permission - assigned to roles"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Permission ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    @Operation(summary = "Search permissions", description = "Searches permissions by name, resource, or action (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PermissionDTO>> search(
            @Parameter(description = "Search query", example = "read") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    @Operation(summary = "Check if permission exists", description = "Checks if a permission with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Permission ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    @Operation(summary = "Count permissions", description = "Returns the total number of permissions in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permission count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM QUERY OPERATIONS ==========

    @Override
    protected Page<PermissionDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return permissionService.getAll(pageable);
        }
        log.debug("GET /search?q={} - Searching", query);
        return permissionService.globalSearch(query, pageable);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    @Operation(summary = "Get permission by name", description = "Retrieves a permission by its unique name (e.g., 'USER:READ')")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permission found", content = @Content(schema = @Schema(implementation = PermissionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Permission not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PermissionDTO> getByName(
            @Parameter(description = "Permission name", required = true, example = "USER:READ") 
            @PathVariable String name) {
        log.info("REST request to get Permission by name: {}", name);
        return ResponseEntity.ok(permissionService.findByName(name));
    }

    @GetMapping("/resource/{resource}")
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    @Operation(summary = "Get permissions by resource", description = "Retrieves all permissions for a specific resource (e.g., 'USER')")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PermissionDTO>> getByResource(
            @Parameter(description = "Resource name", required = true, example = "USER") 
            @PathVariable String resource) {
        log.info("REST request to get Permissions by resource: {}", resource);
        return ResponseEntity.ok(permissionService.findByResource(resource));
    }

    @GetMapping("/action/{action}")
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    @Operation(summary = "Get permissions by action", description = "Retrieves all permissions for a specific action (e.g., 'READ', 'MANAGE')")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PermissionDTO>> getByAction(
            @Parameter(description = "Action name", required = true, example = "READ") 
            @PathVariable String action) {
        log.info("REST request to get Permissions by action: {}", action);
        return ResponseEntity.ok(permissionService.findByAction(action));
    }

    @GetMapping("/resource-action")
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    @Operation(summary = "Get permissions by resource and action", description = "Retrieves permissions matching both resource and action")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid or missing resource/action parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PermissionDTO>> getByResourceAndAction(
            @Parameter(description = "Resource name", required = true, example = "USER") @RequestParam String resource,
            @Parameter(description = "Action name", required = true, example = "READ") @RequestParam String action) {
        log.info("REST request to get Permissions by resource: {} and action: {}", resource, action);
        return ResponseEntity.ok(permissionService.findByResourceAndAction(resource, action));
    }

    @GetMapping("/exists/{name}")
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    @Operation(summary = "Check if permission name exists", description = "Checks if a permission with the given name already exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check result returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERMISSION:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Boolean>> checkExists(
            @Parameter(description = "Permission name", required = true, example = "USER:READ") 
            @PathVariable String name) {
        log.info("REST request to check if Permission exists: {}", name);
        boolean exists = permissionService.existsByName(name);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
