/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: AlertStatusController
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
import dz.sh.trc.hyflo.flow.common.dto.AlertStatusDTO;
import dz.sh.trc.hyflo.flow.common.service.AlertStatusService;
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
@RequestMapping("/flow/common/alertStatus")
@Tag(name = "Alert Status Management", description = "APIs for managing flow alert statuses and their states")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class AlertStatusController extends GenericController<AlertStatusDTO, Long> {

    private final AlertStatusService alertStatusService;
    
    public AlertStatusController(AlertStatusService alertStatusService) {
        super(alertStatusService, "AlertStatus");
        this.alertStatusService = alertStatusService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get alert status by ID", description = "Retrieves a single alert status by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alert status found", content = @Content(schema = @Schema(implementation = AlertStatusDTO.class))),
        @ApiResponse(responseCode = "404", description = "Alert status not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALERT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<AlertStatusDTO> getById(
            @Parameter(description = "Alert status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all alert statuses (paginated)", description = "Retrieves a paginated list of all alert statuses")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alert statuses retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALERT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<Page<AlertStatusDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all alert statuses (unpaginated)", description = "Retrieves all alert statuses without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alert statuses retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALERT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<List<AlertStatusDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create alert status", description = "Creates a new alert status with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Alert status created successfully", content = @Content(schema = @Schema(implementation = AlertStatusDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Alert status code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALERT_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ALERT_STATUS:MANAGE')")
    public ResponseEntity<AlertStatusDTO> create(
            @Parameter(description = "Alert status data", required = true) 
            @Valid @RequestBody AlertStatusDTO dto) {
        AlertStatusDTO created = alertStatusService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update alert status", description = "Updates an existing alert status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alert status updated successfully", content = @Content(schema = @Schema(implementation = AlertStatusDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Alert status not found"),
        @ApiResponse(responseCode = "409", description = "Alert status code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALERT_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ALERT_STATUS:MANAGE')")
    public ResponseEntity<AlertStatusDTO> update(
            @Parameter(description = "Alert status ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated alert status data", required = true) @Valid @RequestBody AlertStatusDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete alert status", description = "Deletes an alert status permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Alert status deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Alert status not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete alert status - has dependencies (alerts using this status)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALERT_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ALERT_STATUS:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Alert status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search alert statuses", description = "Searches alert statuses by code or designation (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALERT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<Page<AlertStatusDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if alert status exists", description = "Checks if an alert status with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALERT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Alert status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count alert statuses", description = "Returns the total number of alert statuses in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alert status count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALERT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get alert status by French designation", description = "Retrieves an alert status by its unique French designation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alert status found", content = @Content(schema = @Schema(implementation = AlertStatusDTO.class))),
        @ApiResponse(responseCode = "404", description = "Alert status not found with given designation"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALERT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<AlertStatusDTO> getByDesignationFr(
            @Parameter(description = "French designation", required = true, example = "Actif") 
            @PathVariable String designationFr) {
        log.info("GET /flow/common/alertstatus/designation/{} - Getting alert status by French designation", designationFr);
        return ResponseEntity.ok(alertStatusService.findByDesignationFr(designationFr));
    }
}
