/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: GroupController
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
import dz.sh.trc.hyflo.system.security.dto.GroupDTO;
import dz.sh.trc.hyflo.system.security.service.GroupService;
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
@RequestMapping("/system/security/group")
@Slf4j
@Tag(name = "Group Management", description = "APIs for managing user groups and their role assignments")
@SecurityRequirement(name = "bearer-auth")
public class GroupController extends GenericController<GroupDTO, Long> {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        super(groupService, "Group");
        this.groupService = groupService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    @Operation(summary = "Get group by ID", description = "Retrieves a single group by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group found", content = @Content(schema = @Schema(implementation = GroupDTO.class))),
        @ApiResponse(responseCode = "404", description = "Group not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GroupDTO> getById(
            @Parameter(description = "Group ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    @Operation(summary = "Get all groups (paginated)", description = "Retrieves a paginated list of all groups")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Groups retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<GroupDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    @Operation(summary = "Get all groups (unpaginated)", description = "Retrieves all groups without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Groups retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<GroupDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:MANAGE')")
    @Operation(summary = "Create a new group", description = "Creates a new user group with validation for unique name")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Group created successfully", content = @Content(schema = @Schema(implementation = GroupDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Group name already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GroupDTO> create(
            @Parameter(description = "Group data", required = true) 
            @Valid @RequestBody GroupDTO dto) {
        GroupDTO created = groupService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:MANAGE')")
    @Operation(summary = "Update group", description = "Updates an existing group including its roles")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group updated successfully", content = @Content(schema = @Schema(implementation = GroupDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Group not found"),
        @ApiResponse(responseCode = "409", description = "Group name already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GroupDTO> update(
            @Parameter(description = "Group ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated group data", required = true) @Valid @RequestBody GroupDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:MANAGE')")
    @Operation(summary = "Delete group", description = "Deletes a group permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Group deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Group not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete group - has assigned users"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Group ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    @Operation(summary = "Search groups", description = "Searches groups by name or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<GroupDTO>> search(
            @Parameter(description = "Search query", example = "admin") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    @Operation(summary = "Check if group exists", description = "Checks if a group with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Group ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    @Operation(summary = "Count groups", description = "Returns the total number of groups in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== ROLE MANAGEMENT ==========

    @PostMapping("/{groupId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('GROUP:MANAGE')")
    @Operation(summary = "Assign role to group", description = "Adds a role to a group (idempotent)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role assigned successfully", content = @Content(schema = @Schema(implementation = GroupDTO.class))),
        @ApiResponse(responseCode = "404", description = "Group or role not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GroupDTO> assignRole(
            @Parameter(description = "Group ID", required = true, example = "1") @PathVariable Long groupId,
            @Parameter(description = "Role ID", required = true, example = "2") @PathVariable Long roleId) {
        log.info("REST request to assign role {} to group {}", roleId, groupId);
        return ResponseEntity.ok(groupService.assignRole(groupId, roleId));
    }

    @DeleteMapping("/{groupId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('GROUP:MANAGE')")
    @Operation(summary = "Remove role from group", description = "Removes a role from a group")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role removed successfully", content = @Content(schema = @Schema(implementation = GroupDTO.class))),
        @ApiResponse(responseCode = "404", description = "Group or role not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GroupDTO> removeRole(
            @Parameter(description = "Group ID", required = true, example = "1") @PathVariable Long groupId,
            @Parameter(description = "Role ID", required = true, example = "2") @PathVariable Long roleId) {
        log.info("REST request to remove role {} from group {}", roleId, groupId);
        return ResponseEntity.ok(groupService.removeRole(groupId, roleId));
    }

    // ========== CUSTOM QUERY OPERATIONS ==========

    @Override
    protected Page<GroupDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return groupService.getAll(pageable);
        }
        log.debug("GET /search?q={} - Searching", query);
        return groupService.globalSearch(query, pageable);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('GROUP:READ')")
    @Operation(summary = "Get group by name", description = "Retrieves a group by its unique name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group found", content = @Content(schema = @Schema(implementation = GroupDTO.class))),
        @ApiResponse(responseCode = "404", description = "Group not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GroupDTO> getByName(
            @Parameter(description = "Group name", required = true, example = "Administrators") 
            @PathVariable String name) {
        log.info("REST request to get Group by name: {}", name);
        return ResponseEntity.ok(groupService.findByName(name));
    }

    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('GROUP:READ')")
    @Operation(summary = "Get groups by role", description = "Retrieves all groups that have a specific role")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Groups retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<GroupDTO>> getByRole(
            @Parameter(description = "Role ID", required = true, example = "2") 
            @PathVariable Long roleId) {
        log.info("REST request to get Groups by role: {}", roleId);
        return ResponseEntity.ok(groupService.findByRole(roleId));
    }

    @GetMapping("/exists/{name}")
    @PreAuthorize("hasAuthority('GROUP:READ')")
    @Operation(summary = "Check if group name exists", description = "Checks if a group with the given name already exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check result returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires GROUP:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Boolean>> checkExists(
            @Parameter(description = "Group name", required = true, example = "Administrators") 
            @PathVariable String name) {
        log.info("REST request to check if Group exists: {}", name);
        boolean exists = groupService.existsByName(name);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
