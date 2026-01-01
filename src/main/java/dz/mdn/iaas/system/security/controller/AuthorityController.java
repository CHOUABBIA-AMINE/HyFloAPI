/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AuthorityController
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
import dz.mdn.iaas.system.security.dto.AuthorityDTO;
import dz.mdn.iaas.system.security.service.AuthorityService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/security/authority")
@Slf4j
public class AuthorityController extends GenericController<AuthorityDTO, Long> {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        super(authorityService, "Authority");
        this.authorityService = authorityService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    public ResponseEntity<AuthorityDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    public ResponseEntity<Page<AuthorityDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    public ResponseEntity<List<AuthorityDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:ADMIN')")
    public ResponseEntity<AuthorityDTO> create(@Valid @RequestBody AuthorityDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:ADMIN')")
    public ResponseEntity<AuthorityDTO> update(@PathVariable Long id, @Valid @RequestBody AuthorityDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    public ResponseEntity<Page<AuthorityDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM QUERY OPERATIONS ==========

    @GetMapping("/by-name/{name}")
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    public ResponseEntity<AuthorityDTO> getByName(@PathVariable String name) {
        log.info("REST request to get Authority by name: {}", name);
        return ResponseEntity.ok(authorityService.findByName(name));
    }

    @GetMapping("/by-type/{type}")
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    public ResponseEntity<List<AuthorityDTO>> getByType(@PathVariable String type) {
        log.info("REST request to get Authorities by type: {}", type);
        return ResponseEntity.ok(authorityService.findByType(type));
    }

    @GetMapping("/exists/{name}")
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    public ResponseEntity<Map<String, Boolean>> checkExists(@PathVariable String name) {
        log.info("REST request to check if Authority exists: {}", name);
        boolean exists = authorityService.existsByName(name);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
