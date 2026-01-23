/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: SeverityController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.common.dto.SeverityDTO;
import dz.sh.trc.hyflo.flow.common.service.SeverityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/common/severity")
@Tag(name = "Severity", description = "Alert and event severity management API")
@Slf4j
public class SeverityController extends GenericController<SeverityDTO, Long> {

    private final SeverityService severityService;
    
    public SeverityController(SeverityService severityService) {
        super(severityService, "Severity");
        this.severityService = severityService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get severity by ID", description = "Retrieve a single severity level by its unique identifier")
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<SeverityDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all severities (paginated)", description = "Retrieve all severity levels with pagination and sorting")
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<Page<SeverityDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all severities (no pagination)", description = "Retrieve all severity levels sorted by code")
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<List<SeverityDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create severity", description = "Create a new severity level with unique code and French designation validation")
    @PreAuthorize("hasAuthority('SEVERITY:ADMIN')")
    public ResponseEntity<SeverityDTO> create(@Valid @RequestBody SeverityDTO dto) {
        return super.create(dto);
    }

    @Override
    @Operation(summary = "Update severity", description = "Update an existing severity level by ID")
    @PreAuthorize("hasAuthority('SEVERITY:ADMIN')")
    public ResponseEntity<SeverityDTO> update(@PathVariable Long id, @Valid @RequestBody SeverityDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete severity", description = "Delete a severity level by ID")
    @PreAuthorize("hasAuthority('SEVERITY:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search severities", description = "Search severity levels across all fields with pagination")
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<Page<SeverityDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if severity exists", description = "Check if a severity level exists by ID")
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count severities", description = "Get total count of severity levels")
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get severity by French designation", description = "Find severity level by its unique French designation")
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<SeverityDTO> getByDesignationFr(@PathVariable String designationFr) {
        log.info("GET /flow/common/severity/designation/{} - Getting severity by French designation", designationFr);
        return ResponseEntity.ok(severityService.findByDesignationFr(designationFr));
    }
}
