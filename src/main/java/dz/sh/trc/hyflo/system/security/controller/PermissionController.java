/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PermissionController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.security.dto.PermissionDTO;
import dz.sh.trc.hyflo.system.security.service.PermissionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/security/permission")
@Slf4j
public class PermissionController extends GenericController<PermissionDTO, Long> {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        super(permissionService, "Permission");
        this.permissionService = permissionService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<PermissionDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<Page<PermissionDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<List<PermissionDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:ADMIN')")
    public ResponseEntity<PermissionDTO> create(@Valid @RequestBody PermissionDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:ADMIN')")
    public ResponseEntity<PermissionDTO> update(@PathVariable Long id, @Valid @RequestBody PermissionDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<Page<PermissionDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM QUERY OPERATIONS ==========

    @GetMapping("/by-name/{name}")
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<PermissionDTO> getByName(@PathVariable String name) {
        log.info("REST request to get Permission by name: {}", name);
        return ResponseEntity.ok(permissionService.findByName(name));
    }

    @GetMapping("/by-resource/{resource}")
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<List<PermissionDTO>> getByResource(@PathVariable String resource) {
        log.info("REST request to get Permissions by resource: {}", resource);
        return ResponseEntity.ok(permissionService.findByResource(resource));
    }

    @GetMapping("/by-action/{action}")
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<List<PermissionDTO>> getByAction(@PathVariable String action) {
        log.info("REST request to get Permissions by action: {}", action);
        return ResponseEntity.ok(permissionService.findByAction(action));
    }

    @GetMapping("/by-resource-action")
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<List<PermissionDTO>> getByResourceAndAction(
            @RequestParam String resource,
            @RequestParam String action) {
        log.info("REST request to get Permissions by resource: {} and action: {}", resource, action);
        return ResponseEntity.ok(permissionService.findByResourceAndAction(resource, action));
    }

    @GetMapping("/exists/{name}")
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<Map<String, Boolean>> checkExists(@PathVariable String name) {
        log.info("REST request to check if Permission exists: {}", name);
        boolean exists = permissionService.existsByName(name);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
