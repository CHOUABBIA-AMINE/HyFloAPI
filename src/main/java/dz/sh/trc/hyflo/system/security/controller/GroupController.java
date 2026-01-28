/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: GroupController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-12-2025
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
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

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
    @PreAuthorize("hasAuthority('GROUP:MANAGE')")
    public ResponseEntity<GroupDTO> create(@Valid @RequestBody GroupDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:MANAGE')")
    public ResponseEntity<GroupDTO> update(@PathVariable Long id, @Valid @RequestBody GroupDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('GROUP:MANAGE')")
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
    @PreAuthorize("hasAuthority('GROUP:MANAGE')")
    public ResponseEntity<GroupDTO> assignRole(
            @PathVariable Long groupId,
            @PathVariable Long roleId) {
        log.info("REST request to assign role {} to group {}", roleId, groupId);
        return ResponseEntity.ok(groupService.assignRole(groupId, roleId));
    }

    @DeleteMapping("/{groupId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('GROUP:MANAGE')")
    public ResponseEntity<GroupDTO> removeRole(
            @PathVariable Long groupId,
            @PathVariable Long roleId) {
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
    public ResponseEntity<GroupDTO> getByName(@PathVariable String name) {
        log.info("REST request to get Group by name: {}", name);
        return ResponseEntity.ok(groupService.findByName(name));
    }

    @GetMapping("/role/{roleId}")
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
