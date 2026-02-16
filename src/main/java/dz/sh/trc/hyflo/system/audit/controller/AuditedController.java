/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuditedController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Audit
 *
 **/

package dz.sh.trc.hyflo.system.audit.controller;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.audit.dto.AuditedDTO;
import dz.sh.trc.hyflo.system.audit.service.AuditedService;
import dz.sh.trc.hyflo.system.audit.service.AuditedService.UserActivitySummary;
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

/**
 * REST Controller for comprehensive audit log management.
 * Provides endpoints for tracking, querying, and analyzing all system operations.
 * Extends GenericController for standard CRUD operations with audit-specific extensions.
 * 
 * Security: All endpoints require appropriate AUDIT authority levels.
 * Performance: Uses pagination for large result sets to prevent memory issues.
 */
@Tag(
    name = "Audit Log Management", 
    description = "Comprehensive audit logging API for tracking all system operations, user activities, and compliance monitoring"
)
@SecurityRequirement(name = "bearer-auth")
@RestController
@RequestMapping("/system/audit/audited")
@Slf4j
public class AuditedController extends GenericController<AuditedDTO, Long> {

    private final AuditedService auditedService;

    public AuditedController(AuditedService auditedService) {
        super(auditedService, "Audited");
        this.auditedService = auditedService;
    }

    // ========== Override GenericController methods with OpenAPI documentation ==========

    @Operation(
        summary = "Get audit log by ID",
        description = "Retrieve a specific audit log entry by its unique identifier. Returns complete audit information including operation details, user context, and execution results."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Audit log entry found and returned successfully",
            content = @Content(schema = @Schema(implementation = AuditedDTO.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:READ authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Audit log entry not found with the specified ID",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while retrieving audit log",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<AuditedDTO> getById(
        @Parameter(description = "Audit log entry ID", required = true, example = "1") 
        @PathVariable Long id
    ) {
        return super.getById(id);
    }

    @Operation(
        summary = "Get all audit logs with pagination",
        description = "Retrieve a paginated list of all audit log entries. Supports sorting by any field and customizable page sizes for optimal performance."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Audit logs page retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid pagination parameters (page, size, sortBy, sortDir)",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:READ authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while retrieving audit logs",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Page<AuditedDTO>> getAll(
        @Parameter(description = "Page number (0-indexed)", example = "0") 
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Number of records per page", example = "20") 
        @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Field name to sort by", example = "timestamp") 
        @RequestParam(defaultValue = "id") String sortBy,
        @Parameter(description = "Sort direction (asc or desc)", example = "desc") 
        @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Operation(
        summary = "Get all audit logs without pagination",
        description = "Retrieve complete list of all audit log entries. WARNING: This endpoint may return large datasets and should be used cautiously. Consider using paginated version for production use."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Complete audit logs list retrieved successfully",
            content = @Content(schema = @Schema(implementation = List.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:READ authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error or out of memory due to large dataset",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<List<AuditedDTO>> getAll() {
        return super.getAll();
    }

    @Operation(
        summary = "Create new audit log entry",
        description = "Create a new audit log entry to manually record a system operation. Typically used by audit interceptors and system services rather than direct API calls."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Audit log entry created successfully",
            content = @Content(schema = @Schema(implementation = AuditedDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid audit log data - Validation failed on required fields or format",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:MANAGE authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while creating audit log",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @Override
    @PreAuthorize("hasAuthority('AUDIT:MANAGE')")
    public ResponseEntity<AuditedDTO> create(
        @Parameter(description = "Audit log entry to create", required = true) 
        @Valid @org.springframework.web.bind.annotation.RequestBody AuditedDTO dto
    ) {
        return super.create(dto);
    }

    @Operation(
        summary = "Update existing audit log entry",
        description = "Update an existing audit log entry. WARNING: Modifying audit logs should be restricted and logged separately for compliance. Consider immutability requirements."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Audit log entry updated successfully",
            content = @Content(schema = @Schema(implementation = AuditedDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid audit log data - Validation failed",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:MANAGE authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Audit log entry not found with the specified ID",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while updating audit log",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @Override
    @PreAuthorize("hasAuthority('AUDIT:MANAGE')")
    public ResponseEntity<AuditedDTO> update(
        @Parameter(description = "Audit log entry ID", required = true, example = "1") 
        @PathVariable Long id,
        @Parameter(description = "Updated audit log data", required = true) 
        @Valid @org.springframework.web.bind.annotation.RequestBody AuditedDTO dto
    ) {
        return super.update(id, dto);
    }

    @Operation(
        summary = "Delete audit log entry",
        description = "Delete an audit log entry by ID. WARNING: Deleting audit logs violates compliance requirements in most cases. This operation should be heavily restricted."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Audit log entry deleted successfully",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:MANAGE authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Audit log entry not found with the specified ID",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while deleting audit log",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @Override
    @PreAuthorize("hasAuthority('AUDIT:MANAGE')")
    public ResponseEntity<Void> delete(
        @Parameter(description = "Audit log entry ID to delete", required = true, example = "1") 
        @PathVariable Long id
    ) {
        return super.delete(id);
    }

    @Operation(
        summary = "Search audit logs",
        description = "Search audit logs using a query string. Searches across entity names, actions, usernames, descriptions, and other text fields. Supports pagination and sorting."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Search results retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid search parameters or pagination values",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:READ authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred during search operation",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Page<AuditedDTO>> search(
        @Parameter(description = "Search query string", example = "Pipeline") 
        @RequestParam(required = false) String q,
        @Parameter(description = "Page number (0-indexed)", example = "0") 
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Number of records per page", example = "20") 
        @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Field name to sort by", example = "timestamp") 
        @RequestParam(defaultValue = "id") String sortBy,
        @Parameter(description = "Sort direction (asc or desc)", example = "desc") 
        @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Operation(
        summary = "Check if audit log entry exists",
        description = "Check if an audit log entry exists by its ID. Returns boolean result without fetching the full entry data."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Existence check completed successfully",
            content = @Content(schema = @Schema(implementation = Boolean.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:READ authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred during existence check",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Boolean> exists(
        @Parameter(description = "Audit log entry ID to check", required = true, example = "1") 
        @PathVariable Long id
    ) {
        return super.exists(id);
    }

    @Operation(
        summary = "Count total audit log entries",
        description = "Get the total count of all audit log entries in the system. Useful for dashboard statistics and reporting."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Count retrieved successfully",
            content = @Content(schema = @Schema(implementation = Long.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:READ authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while counting records",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== Custom audit-specific endpoints ==========

    @Operation(
        summary = "Get entity audit history",
        description = "Retrieve complete audit trail for a specific entity instance. Returns all operations performed on the entity in chronological order, useful for entity-level compliance tracking."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Entity audit history retrieved successfully",
            content = @Content(schema = @Schema(implementation = List.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid entity name or ID format",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:READ or AUDIT:USER authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No audit history found for specified entity",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while retrieving audit history",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/entity/{entityName}/{entityId}")
    @PreAuthorize("hasAnyAuthority('AUDIT:READ', 'AUDIT:USER')")
    public ResponseEntity<List<AuditedDTO>> getEntityAuditHistory(
        @Parameter(description = "Entity class name", required = true, example = "Pipeline") 
        @PathVariable String entityName,
        @Parameter(description = "Entity instance ID", required = true, example = "1234") 
        @PathVariable Long entityId
    ) {
        log.debug("Getting audit history for entity {}:{}", entityName, entityId);
        List<AuditedDTO> auditHistory = auditedService.getEntityAuditHistory(entityName, entityId);
        return success(auditHistory);
    }

    @Operation(
        summary = "Get user audit history",
        description = "Retrieve paginated audit history for a specific user. Shows all operations performed by the user with sorting and filtering capabilities. Users can view their own history, admins can view any user."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User audit history retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid pagination parameters",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User can only view own history or needs AUDIT:READ authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No audit history found for specified user",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while retrieving user history",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAuthority('AUDIT:READ') or #username == authentication.principal.username")
    public ResponseEntity<Page<AuditedDTO>> getUserAuditHistory(
        @Parameter(description = "Username to query audit history", required = true, example = "achouabbia") 
        @PathVariable String username,
        @Parameter(description = "Page number (0-indexed)", example = "0") 
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Number of records per page", example = "20") 
        @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Field name to sort by", example = "timestamp") 
        @RequestParam(defaultValue = "timestamp") String sortBy,
        @Parameter(description = "Sort direction (asc or desc)", example = "desc") 
        @RequestParam(defaultValue = "desc") String sortDir
    ) {
        log.debug("Getting audit history for user: {}", username);
        Page<AuditedDTO> auditHistory = auditedService.getUserAuditHistory(
            username, 
            buildPageable(page, size, sortBy, sortDir)
        );
        return success(auditHistory);
    }

    @Operation(
        summary = "Get audit logs by date range",
        description = "Retrieve audit logs within a specific date range. Essential for compliance reporting, period analysis, and historical data extraction. Supports pagination for large datasets."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Audit logs within date range retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid date format or date range (startDate must be before endDate)",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:READ authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while retrieving date range logs",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/date-range")
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Page<AuditedDTO>> getAuditLogsByDateRange(
        @Parameter(description = "Start date (inclusive, format: yyyy-MM-dd)", required = true, example = "2026-01-01") 
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @Parameter(description = "End date (inclusive, format: yyyy-MM-dd)", required = true, example = "2026-12-31") 
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
        @Parameter(description = "Page number (0-indexed)", example = "0") 
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Number of records per page", example = "20") 
        @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Field name to sort by", example = "timestamp") 
        @RequestParam(defaultValue = "timestamp") String sortBy,
        @Parameter(description = "Sort direction (asc or desc)", example = "desc") 
        @RequestParam(defaultValue = "desc") String sortDir
    ) {
        log.debug("Getting audit logs between {} and {}", startDate, endDate);
        Page<AuditedDTO> auditLogs = auditedService.getAuditLogsByDateRange(
            startDate, 
            endDate, 
            buildPageable(page, size, sortBy, sortDir)
        );
        return success(auditLogs);
    }

    @Operation(
        summary = "Get failed operations",
        description = "Retrieve all audit logs for operations that failed. Critical for monitoring system health, debugging issues, and identifying recurring problems. Sorted by most recent failures first."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Failed operations retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid pagination parameters",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User lacks AUDIT:READ authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while retrieving failed operations",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/failed")
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Page<AuditedDTO>> getFailedOperations(
        @Parameter(description = "Page number (0-indexed)", example = "0") 
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Number of records per page", example = "20") 
        @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Field name to sort by", example = "timestamp") 
        @RequestParam(defaultValue = "timestamp") String sortBy,
        @Parameter(description = "Sort direction (asc or desc)", example = "desc") 
        @RequestParam(defaultValue = "desc") String sortDir
    ) {
        log.debug("Getting failed operations");
        Page<AuditedDTO> failedOperations = auditedService.getFailedOperations(
            buildPageable(page, size, sortBy, sortDir)
        );
        return success(failedOperations);
    }

    @Operation(
        summary = "Get user activity summary",
        description = "Get aggregated statistics of user activity over a specified number of days. Includes total operations, success/failure rates, most active modules, and operation type breakdown. Useful for user behavior analysis."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User activity summary calculated successfully",
            content = @Content(schema = @Schema(implementation = UserActivitySummary.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid days parameter (must be positive)",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - User can only view own summary or needs AUDIT:READ authority",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No activity found for specified user in the time period",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred while calculating activity summary",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/user/{username}/summary")
    @PreAuthorize("hasAuthority('AUDIT:READ') or #username == authentication.principal.username")
    public ResponseEntity<UserActivitySummary> getUserActivitySummary(
        @Parameter(description = "Username to query activity summary", required = true, example = "achouabbia") 
        @PathVariable String username,
        @Parameter(description = "Number of days to analyze (default: 30)", example = "30") 
        @RequestParam(defaultValue = "30") int days
    ) {
        log.debug("Getting activity summary for user {} over {} days", username, days);
        UserActivitySummary summary = auditedService.getUserActivitySummary(username, days);
        return success(summary);
    }
}
