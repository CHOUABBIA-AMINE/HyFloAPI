/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: GroupController
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
import dz.mdn.iaas.system.security.dto.GroupDTO;
import dz.mdn.iaas.system.security.service.GroupService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/security/group")
@Slf4j
public class GroupController extends GenericController<GroupDTO, Long> {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        super(groupService, "Group");
        this.groupService = groupService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    public ResponseEntity<GroupDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    public ResponseEntity<Page<GroupDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    public ResponseEntity<List<GroupDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:ADMIN')")
    public ResponseEntity<GroupDTO> create(@Valid @RequestBody GroupDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:ADMIN')")
    public ResponseEntity<GroupDTO> update(@PathVariable Long id, @Valid @RequestBody GroupDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    public ResponseEntity<Page<GroupDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== ROLE MANAGEMENT ==========

    @PostMapping("/{groupId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('GROUP:ADMIN')")
    public ResponseEntity<GroupDTO> assignRole(
            @PathVariable Long groupId,
            @PathVariable Long roleId) {
        log.info("REST request to assign role {} to group {}", roleId, groupId);
        return ResponseEntity.ok(groupService.assignRole(groupId, roleId));
    }

    @DeleteMapping("/{groupId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('GROUP:ADMIN')")
    public ResponseEntity<GroupDTO> removeRole(
            @PathVariable Long groupId,
            @PathVariable Long roleId) {
        log.info("REST request to remove role {} from group {}", roleId, groupId);
        return ResponseEntity.ok(groupService.removeRole(groupId, roleId));
    }

    // ========== CUSTOM QUERY OPERATIONS ==========

    @GetMapping("/by-name/{name}")
    @PreAuthorize("hasAuthority('GROUP:READ')")
    public ResponseEntity<GroupDTO> getByName(@PathVariable String name) {
        log.info("REST request to get Group by name: {}", name);
        return ResponseEntity.ok(groupService.findByName(name));
    }

    @GetMapping("/by-role/{roleId}")
    @PreAuthorize("hasAuthority('GROUP:READ')")
    public ResponseEntity<List<GroupDTO>> getByRole(@PathVariable Long roleId) {
        log.info("REST request to get Groups by role: {}", roleId);
        return ResponseEntity.ok(groupService.findByRole(roleId));
    }

    @GetMapping("/exists/{name}")
    @PreAuthorize("hasAuthority('GROUP:READ')")
    public ResponseEntity<Map<String, Boolean>> checkExists(@PathVariable String name) {
        log.info("REST request to check if Group exists: {}", name);
        boolean exists = groupService.existsByName(name);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
