package dz.sh.trc.hyflo.core.system.security.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateRoleRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateRoleRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.RoleResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.RoleSummary;
import dz.sh.trc.hyflo.core.system.security.model.Role;
import dz.sh.trc.hyflo.core.system.security.service.RoleService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/system/roles")
@Tag(name = "Role API", description = "Endpoints for managing roles")
public class RoleController extends BaseController<CreateRoleRequest, UpdateRoleRequest, RoleResponse, RoleSummary, Role, Long> {

    private final RoleService roleService;

    public RoleController(RoleService service) {
        super(service);
        this.roleService = service;
    }

    @Override
    protected Page<RoleSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not yet implemented");
    }

    @PostMapping("/{id}/permissions/{permissionId}")
    @Operation(summary = "Assign permission to role")
    public ResponseEntity<Void> assignPermission(@PathVariable Long id, @PathVariable Long permissionId) {
        roleService.assignPermission(id, permissionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permissions/{permissionId}")
    @Operation(summary = "Remove permission from role")
    public ResponseEntity<Void> removePermission(@PathVariable Long id, @PathVariable Long permissionId) {
        roleService.removePermission(id, permissionId);
        return ResponseEntity.noContent().build();
    }
}
