package dz.sh.trc.hyflo.core.system.security.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateUserRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateUserRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.UserResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.UserSummary;
import dz.sh.trc.hyflo.core.system.security.model.User;
import dz.sh.trc.hyflo.core.system.security.service.UserService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/system/users")
@Tag(name = "User API", description = "Endpoints for managing users")
public class UserController extends BaseController<CreateUserRequest, UpdateUserRequest, UserResponse, UserSummary> {

    private final UserService userService;

    public UserController(UserService service) {
        super(service);
        this.userService = service;
    }

    @Override
    protected Page<UserSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not yet implemented");
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Find user by username")
    public ResponseEntity<UserResponse> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PostMapping("/{id}/roles/{roleId}")
    @Operation(summary = "Assign role to user")
    public ResponseEntity<Void> assignRole(@PathVariable Long id, @PathVariable Long roleId) {
        userService.assignRole(id, roleId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    @Operation(summary = "Remove role from user")
    public ResponseEntity<Void> removeRole(@PathVariable Long id, @PathVariable Long roleId) {
        userService.removeRole(id, roleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/groups/{groupId}")
    @Operation(summary = "Assign group to user")
    public ResponseEntity<Void> assignGroup(@PathVariable Long id, @PathVariable Long groupId) {
        userService.assignGroup(id, groupId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/groups/{groupId}")
    @Operation(summary = "Remove group from user")
    public ResponseEntity<Void> removeGroup(@PathVariable Long id, @PathVariable Long groupId) {
        userService.removeGroup(id, groupId);
        return ResponseEntity.noContent().build();
    }
}
