/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: RoleController
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.security.dto.RoleDTO;
import dz.sh.trc.hyflo.system.security.service.RoleService;
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
@RequestMapping("/system/security/role")
@Slf4j
@Tag(name = "Role Management", description = "APIs for managing roles and their permissions")
@SecurityRequirement(name = "bearer-auth")
public class RoleController extends GenericController<RoleDTO, Long> {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        super(roleService, "Role");
        this.roleService = roleService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    @Operation(summary = "Get role by ID", description = "Retrieves a single role by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role found", content = @Content(schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RoleDTO> getById(
            @Parameter(description = "Role ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    @Operation(summary = "Get all roles (paginated)", description = "Retrieves a paginated list of all roles")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<RoleDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    @Operation(summary = "Get all roles (unpaginated)", description = "Retrieves all roles without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<RoleDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:MANAGE')")
    @Operation(summary = "Create a new role", description = "Creates a new role with validation for unique name")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Role created successfully", content = @Content(schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Role name already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RoleDTO> create(
            @Parameter(description = "Role data", required = true) 
            @Valid @RequestBody RoleDTO dto) {
        RoleDTO created = roleService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:MANAGE')")
    @Operation(summary = "Update role", description = "Updates an existing role including its permissions")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role updated successfully", content = @Content(schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "409", description = "Role name already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RoleDTO> update(
            @Parameter(description = "Role ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated role data", required = true) @Valid @RequestBody RoleDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:MANAGE')")
    @Operation(summary = "Delete role", description = "Deletes a role permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete role - assigned to users or groups"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Role ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    @Operation(summary = "Search roles", description = "Searches roles by name or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<RoleDTO>> search(
            @Parameter(description = "Search query", example = "admin") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    @Operation(summary = "Check if role exists", description = "Checks if a role with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Role ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    @Operation(summary = "Count roles", description = "Returns the total number of roles in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== PERMISSION MANAGEMENT ==========

    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE:MANAGE')")
    @Operation(summary = "Assign permission to role", description = "Adds a permission to a role (idempotent)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permission assigned successfully", content = @Content(schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Role or permission not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RoleDTO> assignPermission(
            @Parameter(description = "Role ID", required = true, example = "1") @PathVariable Long roleId,
            @Parameter(description = "Permission ID", required = true, example = "5") @PathVariable Long permissionId) {
        log.info("REST request to assign permission {} to role {}", permissionId, roleId);
        return ResponseEntity.ok(roleService.assignPermission(roleId, permissionId));
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE:MANAGE')")
    @Operation(summary = "Remove permission from role", description = "Removes a permission from a role")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permission removed successfully", content = @Content(schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Role or permission not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RoleDTO> removePermission(
            @Parameter(description = "Role ID", required = true, example = "1") @PathVariable Long roleId,
            @Parameter(description = "Permission ID", required = true, example = "5") @PathVariable Long permissionId) {
        log.info("REST request to remove permission {} from role {}", permissionId, roleId);
        return ResponseEntity.ok(roleService.removePermission(roleId, permissionId));
    }

    // ========== CUSTOM QUERY OPERATIONS ==========

    @Override
    protected Page<RoleDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return roleService.getAll(pageable);
        }
        log.debug("GET /search?q={} - Searching", query);
        return roleService.globalSearch(query, pageable);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('ROLE:READ')")
    @Operation(summary = "Get role by name", description = "Retrieves a role by its unique name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role found", content = @Content(schema = @Schema(implementation = RoleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RoleDTO> getByName(
            @Parameter(description = "Role name", required = true, example = "ROLE_ADMIN") 
            @PathVariable String name) {
        log.info("REST request to get Role by name: {}", name);
        return ResponseEntity.ok(roleService.findByName(name));
    }

    @GetMapping("/permission/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE:READ')")
    @Operation(summary = "Get roles by permission", description = "Retrieves all roles that have a specific permission")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Permission not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<RoleDTO>> getByPermission(
            @Parameter(description = "Permission ID", required = true, example = "5") 
            @PathVariable Long permissionId) {
        log.info("REST request to get Roles by permission: {}", permissionId);
        return ResponseEntity.ok(roleService.findByPermission(permissionId));
    }

    @GetMapping("/exists/{name}")
    @PreAuthorize("hasAuthority('ROLE:READ')")
    @Operation(summary = "Check if role name exists", description = "Checks if a role with the given name already exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check result returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ROLE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Boolean>> checkExists(
            @Parameter(description = "Role name", required = true, example = "ROLE_ADMIN") 
            @PathVariable String name) {
        log.info("REST request to check if Role exists: {}", name);
        boolean exists = roleService.existsByName(name);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
