/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: SeverityController
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
import dz.sh.trc.hyflo.flow.common.dto.SeverityDTO;
import dz.sh.trc.hyflo.flow.common.service.SeverityService;
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
@RequestMapping("/flow/common/severity")
@Tag(name = "Severity Management", description = "APIs for managing alert and event severity levels")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class SeverityController extends GenericController<SeverityDTO, Long> {

    private final SeverityService severityService;
    
    public SeverityController(SeverityService severityService) {
        super(severityService, "Severity");
        this.severityService = severityService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get severity by ID", description = "Retrieves a single severity level by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Severity found", content = @Content(schema = @Schema(implementation = SeverityDTO.class))),
        @ApiResponse(responseCode = "404", description = "Severity not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires SEVERITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<SeverityDTO> getById(
            @Parameter(description = "Severity ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all severities (paginated)", description = "Retrieves a paginated list of all severity levels")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Severities retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires SEVERITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<Page<SeverityDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all severities (unpaginated)", description = "Retrieves all severity levels without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Severities retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires SEVERITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<List<SeverityDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create severity", description = "Creates a new severity level with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Severity created successfully", content = @Content(schema = @Schema(implementation = SeverityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Severity code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires SEVERITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('SEVERITY:MANAGE')")
    public ResponseEntity<SeverityDTO> create(
            @Parameter(description = "Severity data", required = true) 
            @Valid @RequestBody SeverityDTO dto) {
        SeverityDTO created = severityService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update severity", description = "Updates an existing severity level")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Severity updated successfully", content = @Content(schema = @Schema(implementation = SeverityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Severity not found"),
        @ApiResponse(responseCode = "409", description = "Severity code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires SEVERITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('SEVERITY:MANAGE')")
    public ResponseEntity<SeverityDTO> update(
            @Parameter(description = "Severity ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated severity data", required = true) @Valid @RequestBody SeverityDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete severity", description = "Deletes a severity level permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Severity deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Severity not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete severity - has dependencies (alerts/events using this severity)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires SEVERITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('SEVERITY:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Severity ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search severities", description = "Searches severities by code or designation (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires SEVERITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<Page<SeverityDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if severity exists", description = "Checks if a severity with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires SEVERITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Severity ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count severities", description = "Returns the total number of severities in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Severity count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires SEVERITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get severity by French designation", description = "Retrieves a severity level by its unique French designation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Severity found", content = @Content(schema = @Schema(implementation = SeverityDTO.class))),
        @ApiResponse(responseCode = "404", description = "Severity not found with given designation"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires SEVERITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('SEVERITY:READ')")
    public ResponseEntity<SeverityDTO> getByDesignationFr(
            @Parameter(description = "French designation", required = true, example = "Critique") 
            @PathVariable String designationFr) {
        log.info("GET /flow/common/severity/designation/{} - Getting severity by French designation", designationFr);
        return ResponseEntity.ok(severityService.findByDesignationFr(designationFr));
    }
}
