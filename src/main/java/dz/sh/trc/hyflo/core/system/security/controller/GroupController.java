package dz.sh.trc.hyflo.core.system.security.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateGroupRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateGroupRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.GroupResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.GroupSummary;
import dz.sh.trc.hyflo.core.system.security.model.Group;
import dz.sh.trc.hyflo.core.system.security.service.GroupService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/system/groups")
@Tag(name = "Group API", description = "Endpoints for managing groups")
public class GroupController extends BaseController<CreateGroupRequest, UpdateGroupRequest, GroupResponse, GroupSummary, Group, Long> {

    private final GroupService groupService;

    public GroupController(GroupService service) {
        super(service);
        this.groupService = service;
    }

    @Override
    protected Page<GroupSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not yet implemented");
    }

    @PostMapping("/{id}/roles/{roleId}")
    @Operation(summary = "Assign role to group")
    public ResponseEntity<Void> assignRole(@PathVariable Long id, @PathVariable Long roleId) {
        groupService.assignRole(id, roleId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    @Operation(summary = "Remove role from group")
    public ResponseEntity<Void> removeRole(@PathVariable Long id, @PathVariable Long roleId) {
        groupService.removeRole(id, roleId);
        return ResponseEntity.noContent().build();
    }
}
