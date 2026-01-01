/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: UserController
 *	@CreatedOn	: 11-18-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.controller;

import dz.mdn.iaas.configuration.template.GenericController;
import dz.mdn.iaas.system.security.dto.ResetPasswordRequest;
import dz.mdn.iaas.system.security.dto.UserDTO;
import dz.mdn.iaas.system.security.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/security/user")
@Slf4j
@Validated
public class UserController extends GenericController<UserDTO, Long> {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService, "User");
        this.userService = userService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    public ResponseEntity<Page<UserDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    public ResponseEntity<List<UserDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    public ResponseEntity<Page<UserDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('USER:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== USER SEARCH OPERATIONS ==========

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('USER:ADMIN') or #username == authentication.principal.username")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        log.info("REST request to get User by username: {}", username);
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        log.info("REST request to get User by email: {}", email);
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsernameExists(@PathVariable String username) {
        log.info("REST request to check if username exists: {}", username);
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@PathVariable String email) {
        log.info("REST request to check if email exists: {}", email);
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // ========== PASSWORD MANAGEMENT ==========

    @PostMapping("/reset-password")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("REST request to reset password for user: {}", request.getUsername());
        userService.resetPassword(request.getUsername(), request.getNewPassword());
        return ResponseEntity.ok(Map.of(
            "message", "Password reset successfully",
            "username", request.getUsername()
        ));
    }

    // ========== ROLE MANAGEMENT ==========

    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<UserDTO> assignRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        log.info("REST request to assign role {} to user {}", roleId, userId);
        return ResponseEntity.ok(userService.assignRole(userId, roleId));
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<UserDTO> removeRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        log.info("REST request to remove role {} from user {}", roleId, userId);
        return ResponseEntity.ok(userService.removeRole(userId, roleId));
    }

    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable Long roleId) {
        log.info("REST request to get users by role: {}", roleId);
        return ResponseEntity.ok(userService.findByRole(roleId));
    }

    // ========== GROUP MANAGEMENT ==========

    @PostMapping("/{userId}/groups/{groupId}")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<UserDTO> assignGroup(
            @PathVariable Long userId,
            @PathVariable Long groupId) {
        log.info("REST request to assign group {} to user {}", groupId, userId);
        return ResponseEntity.ok(userService.assignGroup(userId, groupId));
    }

    @DeleteMapping("/{userId}/groups/{groupId}")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<UserDTO> removeGroup(
            @PathVariable Long userId,
            @PathVariable Long groupId) {
        log.info("REST request to remove group {} from user {}", groupId, userId);
        return ResponseEntity.ok(userService.removeGroup(userId, groupId));
    }

    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsersByGroup(@PathVariable Long groupId) {
        log.info("REST request to get users by group: {}", groupId);
        return ResponseEntity.ok(userService.findByGroup(groupId));
    }

    // ========== ACCOUNT STATUS MANAGEMENT ==========

    @PutMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<UserDTO> enableUser(@PathVariable Long id) {
        log.info("REST request to enable user: {}", id);
        return ResponseEntity.ok(userService.enableUser(id));
    }

    @PutMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<UserDTO> disableUser(@PathVariable Long id) {
        log.info("REST request to disable user: {}", id);
        return ResponseEntity.ok(userService.disableUser(id));
    }

    @PutMapping("/{id}/lock")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<UserDTO> lockUser(@PathVariable Long id) {
        log.info("REST request to lock user: {}", id);
        return ResponseEntity.ok(userService.lockUser(id));
    }

    @PutMapping("/{id}/unlock")
    @PreAuthorize("hasAuthority('USER:ADMIN')")
    public ResponseEntity<UserDTO> unlockUser(@PathVariable Long id) {
        log.info("REST request to unlock user: {}", id);
        return ResponseEntity.ok(userService.unlockUser(id));
    }
}
