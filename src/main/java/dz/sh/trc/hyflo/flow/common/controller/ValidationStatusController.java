/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ValidationStatusController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.common.dto.ValidationStatusDTO;
import dz.sh.trc.hyflo.flow.common.service.ValidationStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flow/common/validationStatus")
@Tag(name = "Validation Status Management", description = "APIs for managing flow data validation statuses")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class ValidationStatusController extends GenericController<ValidationStatusDTO, Long> {

    private final ValidationStatusService validationStatusService;
    
    public ValidationStatusController(ValidationStatusService validationStatusService) {
        super(validationStatusService, "ValidationStatus");
        this.validationStatusService = validationStatusService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get validation status by ID", description = "Retrieves a single validation status by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Validation status found", content = @Content(schema = @Schema(implementation = ValidationStatusDTO.class))),
        @ApiResponse(responseCode = "404", description = "Validation status not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VALIDATION_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<ValidationStatusDTO> getById(
            @Parameter(description = "Validation status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all validation statuses (paginated)", description = "Retrieves a paginated list of all validation statuses")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Validation statuses retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VALIDATION_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<Page<ValidationStatusDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all validation statuses (unpaginated)", description = "Retrieves all validation statuses without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Validation statuses retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VALIDATION_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<List<ValidationStatusDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create validation status", description = "Creates a new validation status with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Validation status created successfully", content = @Content(schema = @Schema(implementation = ValidationStatusDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Validation status code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VALIDATION_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:MANAGE')")
    public ResponseEntity<ValidationStatusDTO> create(
            @Parameter(description = "Validation status data", required = true) 
            @Valid @RequestBody ValidationStatusDTO dto) {
        ValidationStatusDTO created = validationStatusService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update validation status", description = "Updates an existing validation status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Validation status updated successfully", content = @Content(schema = @Schema(implementation = ValidationStatusDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Validation status not found"),
        @ApiResponse(responseCode = "409", description = "Validation status code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VALIDATION_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:MANAGE')")
    public ResponseEntity<ValidationStatusDTO> update(
            @Parameter(description = "Validation status ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated validation status data", required = true) @Valid @RequestBody ValidationStatusDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete validation status", description = "Deletes a validation status permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Validation status deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Validation status not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete validation status - has dependencies (readings using this status)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VALIDATION_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Validation status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search validation statuses", description = "Searches validation statuses by code or designation (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VALIDATION_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<Page<ValidationStatusDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if validation status exists", description = "Checks if a validation status with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VALIDATION_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Validation status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count validation statuses", description = "Returns the total number of validation statuses in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Validation status count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VALIDATION_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get validation status by code", description = "Retrieves a validation status by its unique code (e.g., VALIDATED, PENDING, REJECTED)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Validation status found", content = @Content(schema = @Schema(implementation = ValidationStatusDTO.class))),
        @ApiResponse(responseCode = "404", description = "Validation status not found with given code"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VALIDATION_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<ValidationStatusDTO> getByCode(
            @Parameter(description = "Validation status code", required = true, example = "VALIDATED") 
            @PathVariable String code) {
        log.info("GET /flow/common/validationstatus/code/{} - Getting validation status by code", code);
        return ResponseEntity.ok(validationStatusService.findByCode(code));
    }

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get validation status by French designation", description = "Retrieves a validation status by its unique French designation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Validation status found", content = @Content(schema = @Schema(implementation = ValidationStatusDTO.class))),
        @ApiResponse(responseCode = "404", description = "Validation status not found with given designation"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VALIDATION_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('VALIDATION_STATUS:READ')")
    public ResponseEntity<ValidationStatusDTO> getByDesignationFr(
            @Parameter(description = "French designation", required = true, example = "Validé") 
            @PathVariable String designationFr) {
        log.info("GET /flow/common/validationstatus/designation/{} - Getting validation status by French designation", designationFr);
        return ResponseEntity.ok(validationStatusService.findByDesignationFr(designationFr));
    }
}
