/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ValidationStatusController
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
import dz.sh.trc.hyflo.flow.common.dto.ValidationStatusDTO;
import dz.sh.trc.hyflo.flow.common.service.ValidationStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/common/validationstatus")
@Tag(name = "Validation Status", description = "Flow validation status management API")
@Slf4j
public class ValidationStatusController extends GenericController<ValidationStatusDTO, Long> {

    private final ValidationStatusService validationStatusService;
    
    public ValidationStatusController(ValidationStatusService validationStatusService) {
        super(validationStatusService, "ValidationStatus");
        this.validationStatusService = validationStatusService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get validation status by ID", description = "Retrieve a single validation status by its unique identifier")
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<ValidationStatusDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all validation statuses (paginated)", description = "Retrieve all validation statuses with pagination and sorting")
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<Page<ValidationStatusDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all validation statuses (no pagination)", description = "Retrieve all validation statuses sorted by code")
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<List<ValidationStatusDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create validation status", description = "Create a new validation status with unique code and French designation validation")
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:ADMIN')")
    public ResponseEntity<ValidationStatusDTO> create(@Valid @RequestBody ValidationStatusDTO dto) {
        return super.create(dto);
    }

    @Override
    @Operation(summary = "Update validation status", description = "Update an existing validation status by ID")
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:ADMIN')")
    public ResponseEntity<ValidationStatusDTO> update(@PathVariable Long id, @Valid @RequestBody ValidationStatusDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete validation status", description = "Delete a validation status by ID")
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search validation statuses", description = "Search validation statuses across all fields with pagination")
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<Page<ValidationStatusDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if validation status exists", description = "Check if a validation status exists by ID")
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count validation statuses", description = "Get total count of validation statuses")
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get validation status by code", description = "Find validation status by its unique code (VALIDATED, PENDING, etc.)")
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<ValidationStatusDTO> getByCode(@PathVariable String code) {
        log.info("GET /flow/common/validationstatus/code/{} - Getting validation status by code", code);
        return ResponseEntity.ok(validationStatusService.findByCode(code));
    }

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get validation status by French designation", description = "Find validation status by its unique French designation")
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<ValidationStatusDTO> getByDesignationFr(@PathVariable String designationFr) {
        log.info("GET /flow/common/validationstatus/designation/{} - Getting validation status by French designation", designationFr);
        return ResponseEntity.ok(validationStatusService.findByDesignationFr(designationFr));
    }
}
