/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: RoleController
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
import dz.sh.trc.hyflo.system.security.dto.RoleDTO;
import dz.sh.trc.hyflo.system.security.service.RoleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/system/security/role")
@Slf4j
public class RoleController extends GenericController<RoleDTO, Long> {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        super(roleService, "Role");
        this.roleService = roleService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    public ResponseEntity<RoleDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    public ResponseEntity<Page<RoleDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    public ResponseEntity<List<RoleDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:MANAGE')")
    public ResponseEntity<RoleDTO> create(@Valid @RequestBody RoleDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:MANAGE')")
    public ResponseEntity<RoleDTO> update(@PathVariable Long id, @Valid @RequestBody RoleDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    public ResponseEntity<Page<RoleDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== PERMISSION MANAGEMENT ==========

    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE:MANAGE')")
    public ResponseEntity<RoleDTO> assignPermission(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        log.info("REST request to assign permission {} to role {}", permissionId, roleId);
        return ResponseEntity.ok(roleService.assignPermission(roleId, permissionId));
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE:MANAGE')")
    public ResponseEntity<RoleDTO> removePermission(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
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
    public ResponseEntity<RoleDTO> getByName(@PathVariable String name) {
        log.info("REST request to get Role by name: {}", name);
        return ResponseEntity.ok(roleService.findByName(name));
    }

    @GetMapping("/permission/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE:READ')")
    public ResponseEntity<List<RoleDTO>> getByPermission(@PathVariable Long permissionId) {
        log.info("REST request to get Roles by permission: {}", permissionId);
        return ResponseEntity.ok(roleService.findByPermission(permissionId));
    }

    @GetMapping("/exists/{name}")
    @PreAuthorize("hasAuthority('ROLE:READ')")
    public ResponseEntity<Map<String, Boolean>> checkExists(@PathVariable String name) {
        log.info("REST request to check if Role exists: {}", name);
        boolean exists = roleService.existsByName(name);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
