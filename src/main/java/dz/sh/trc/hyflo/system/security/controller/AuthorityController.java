/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuthorityController
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.security.dto.AuthorityDTO;
import dz.sh.trc.hyflo.system.security.service.AuthorityService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

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
    @PreAuthorize("hasAuthority('AUTHORITY:MANAGE')")
    public ResponseEntity<AuthorityDTO> create(@Valid @RequestBody AuthorityDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:MANAGE')")
    public ResponseEntity<AuthorityDTO> update(@PathVariable Long id, @Valid @RequestBody AuthorityDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:MANAGE')")
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

    @Override
    protected Page<AuthorityDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return authorityService.getAll(pageable);
        }
        log.debug("GET /search?q={} - Searching", query);
        return authorityService.globalSearch(query, pageable);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    public ResponseEntity<AuthorityDTO> getByName(@PathVariable String name) {
        log.info("REST request to get Authority by name: {}", name);
        return ResponseEntity.ok(authorityService.findByName(name));
    }

    @GetMapping("/type/{type}")
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
