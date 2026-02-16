/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: UserController
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.security.dto.ResetPasswordRequest;
import dz.sh.trc.hyflo.system.security.dto.UserDTO;
import dz.sh.trc.hyflo.system.security.service.UserService;
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
@RequestMapping("/system/security/user")
@Slf4j
@Validated
@Tag(name = "User Management", description = "APIs for managing users, roles, groups, and account status")
@SecurityRequirement(name = "bearer-auth")
public class UserController extends GenericController<UserDTO, Long> {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService, "User");
        this.userService = userService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Get user by ID", description = "Retrieves a single user by their unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:READ authority")
    })
    public ResponseEntity<UserDTO> getById(
            @Parameter(description = "User ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Get all users (paginated)", description = "Retrieves a paginated list of all users")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:READ authority")
    })
    public ResponseEntity<Page<UserDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Get all users (unpaginated)", description = "Retrieves all users without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:READ authority")
    })
    public ResponseEntity<List<UserDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Create a new user", description = "Creates a new user account with password hashing and validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or username/email already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<UserDTO> create(
            @Parameter(description = "User data", required = true) 
            @Valid @RequestBody UserDTO dto) {
        UserDTO created = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:MANAGE') or #id == authentication.principal.id")
    @Operation(summary = "Update user", description = "Updates an existing user. Users can update themselves, admins can update anyone")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input or username/email already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<UserDTO> update(
            @Parameter(description = "User ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated user data", required = true) @Valid @RequestBody UserDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Delete user", description = "Deletes a user account permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "User ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Search users", description = "Searches users by username or email (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:READ authority")
    })
    public ResponseEntity<Page<UserDTO>> search(
            @Parameter(description = "Search query", example = "john") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Check if user exists", description = "Checks if a user with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:READ authority")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "User ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Count users", description = "Returns the total number of users in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:READ authority")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== USER SEARCH OPERATIONS ==========

    @Override
    protected Page<UserDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return userService.getAll(pageable);
        }
        log.debug("GET /search?q={} - Searching", query);
        return userService.globalSearch(query, pageable);
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('USER:MANAGE') or #username == authentication.principal.username")
    @Operation(summary = "Get user by username", description = "Retrieves a user by their username. Users can access their own data")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<UserDTO> getUserByUsername(
            @Parameter(description = "Username", required = true, example = "john.doe") 
            @PathVariable String username) {
        log.info("REST request to get User by username: {}", username);
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Get user by email", description = "Retrieves a user by their email address")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<UserDTO> getUserByEmail(
            @Parameter(description = "Email address", required = true, example = "john.doe@example.com") 
            @PathVariable String email) {
        log.info("REST request to get User by email: {}", email);
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/exists/username/{username}")
    @Operation(summary = "Check if username exists", description = "Checks if a username is already taken (for registration validation)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check result returned")
    })
    public ResponseEntity<Map<String, Boolean>> checkUsernameExists(
            @Parameter(description = "Username to check", required = true, example = "john.doe") 
            @PathVariable String username) {
        log.info("REST request to check if username exists: {}", username);
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @GetMapping("/exists/email/{email}")
    @Operation(summary = "Check if email exists", description = "Checks if an email is already registered (for registration validation)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check result returned")
    })
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(
            @Parameter(description = "Email to check", required = true, example = "john.doe@example.com") 
            @PathVariable String email) {
        log.info("REST request to check if email exists: {}", email);
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // ========== PASSWORD MANAGEMENT ==========

    @PostMapping("/reset-password")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Reset user password", description = "Resets a user's password and restores account security flags")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password reset successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid password format"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<Map<String, String>> resetPassword(
            @Parameter(description = "Password reset request", required = true) 
            @Valid @RequestBody ResetPasswordRequest request) {
        log.info("REST request to reset password for user: {}", request.getUsername());
        userService.resetPassword(request.getUsername(), request.getNewPassword());
        return ResponseEntity.ok(Map.of(
            "message", "Password reset successfully",
            "username", request.getUsername()
        ));
    }

    // ========== ROLE MANAGEMENT ==========

    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Assign role to user", description = "Assigns a role to a user and invalidates security cache")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role assigned successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User or role not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<UserDTO> assignRole(
            @Parameter(description = "User ID", required = true, example = "1") @PathVariable Long userId,
            @Parameter(description = "Role ID", required = true, example = "2") @PathVariable Long roleId) {
        log.info("REST request to assign role {} to user {}", roleId, userId);
        return ResponseEntity.ok(userService.assignRole(userId, roleId));
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Remove role from user", description = "Removes a role from a user and invalidates security cache")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role removed successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User or role not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<UserDTO> removeRole(
            @Parameter(description = "User ID", required = true, example = "1") @PathVariable Long userId,
            @Parameter(description = "Role ID", required = true, example = "2") @PathVariable Long roleId) {
        log.info("REST request to remove role {} from user {}", roleId, userId);
        return ResponseEntity.ok(userService.removeRole(userId, roleId));
    }

    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Get users by role", description = "Retrieves all users who have a specific role")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<List<UserDTO>> getUsersByRole(
            @Parameter(description = "Role ID", required = true, example = "2") 
            @PathVariable Long roleId) {
        log.info("REST request to get users by role: {}", roleId);
        return ResponseEntity.ok(userService.findByRole(roleId));
    }

    // ========== GROUP MANAGEMENT ==========

    @PostMapping("/{userId}/groups/{groupId}")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Assign group to user", description = "Adds a user to a group and invalidates security cache")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group assigned successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User or group not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<UserDTO> assignGroup(
            @Parameter(description = "User ID", required = true, example = "1") @PathVariable Long userId,
            @Parameter(description = "Group ID", required = true, example = "3") @PathVariable Long groupId) {
        log.info("REST request to assign group {} to user {}", groupId, userId);
        return ResponseEntity.ok(userService.assignGroup(userId, groupId));
    }

    @DeleteMapping("/{userId}/groups/{groupId}")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Remove group from user", description = "Removes a user from a group and invalidates security cache")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group removed successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User or group not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<UserDTO> removeGroup(
            @Parameter(description = "User ID", required = true, example = "1") @PathVariable Long userId,
            @Parameter(description = "Group ID", required = true, example = "3") @PathVariable Long groupId) {
        log.info("REST request to remove group {} from user {}", groupId, userId);
        return ResponseEntity.ok(userService.removeGroup(userId, groupId));
    }

    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Get users by group", description = "Retrieves all users who belong to a specific group")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Group not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<List<UserDTO>> getUsersByGroup(
            @Parameter(description = "Group ID", required = true, example = "3") 
            @PathVariable Long groupId) {
        log.info("REST request to get users by group: {}", groupId);
        return ResponseEntity.ok(userService.findByGroup(groupId));
    }

    // ========== ACCOUNT STATUS MANAGEMENT ==========

    @PutMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Enable user account", description = "Enables a disabled user account")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User enabled successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<UserDTO> enableUser(
            @Parameter(description = "User ID", required = true, example = "1") 
            @PathVariable Long id) {
        log.info("REST request to enable user: {}", id);
        return ResponseEntity.ok(userService.enableUser(id));
    }

    @PutMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Disable user account", description = "Disables a user account (prevents login)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User disabled successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<UserDTO> disableUser(
            @Parameter(description = "User ID", required = true, example = "1") 
            @PathVariable Long id) {
        log.info("REST request to disable user: {}", id);
        return ResponseEntity.ok(userService.disableUser(id));
    }

    @PutMapping("/{id}/lock")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Lock user account", description = "Locks a user account (e.g., after failed login attempts)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User locked successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<UserDTO> lockUser(
            @Parameter(description = "User ID", required = true, example = "1") 
            @PathVariable Long id) {
        log.info("REST request to lock user: {}", id);
        return ResponseEntity.ok(userService.lockUser(id));
    }

    @PutMapping("/{id}/unlock")
    @PreAuthorize("hasAuthority('USER:MANAGE')")
    @Operation(summary = "Unlock user account", description = "Unlocks a locked user account")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User unlocked successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires USER:MANAGE authority")
    })
    public ResponseEntity<UserDTO> unlockUser(
            @Parameter(description = "User ID", required = true, example = "1") 
            @PathVariable Long id) {
        log.info("REST request to unlock user: {}", id);
        return ResponseEntity.ok(userService.unlockUser(id));
    }
}
