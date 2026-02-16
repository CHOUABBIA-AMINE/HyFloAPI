/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: OperationalStatusController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.network.common.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.service.OperationalStatusService;
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
@RequestMapping("/network/common/operationalStatus")
@Slf4j
@Tag(name = "Operational Status Management", description = "APIs for managing operational status types")
@SecurityRequirement(name = "bearer-auth")
public class OperationalStatusController extends GenericController<OperationalStatusDTO, Long> {

    @SuppressWarnings("unused")
	private final OperationalStatusService operationalStatusService;

    public OperationalStatusController(OperationalStatusService operationalStatusService) {
        super(operationalStatusService, "OperationalStatus");
        this.operationalStatusService = operationalStatusService;
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    @Operation(summary = "Get operational status by ID", description = "Retrieves a single operational status by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operational status found", content = @Content(schema = @Schema(implementation = OperationalStatusDTO.class))),
        @ApiResponse(responseCode = "404", description = "Operational status not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATIONAL_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<OperationalStatusDTO> getById(
            @Parameter(description = "Operational status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    @Operation(summary = "Get all operational statuses (paginated)", description = "Retrieves a paginated list of all operational statuses")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operational statuses retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATIONAL_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<OperationalStatusDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    @Operation(summary = "Get all operational statuses (unpaginated)", description = "Retrieves all operational statuses without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operational statuses retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATIONAL_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<OperationalStatusDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:MANAGE')")
    @Operation(summary = "Create new operational status", description = "Creates a new operational status with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Operational status created successfully", content = @Content(schema = @Schema(implementation = OperationalStatusDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Operational status name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATIONAL_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<OperationalStatusDTO> create(
            @Parameter(description = "Operational status data", required = true) 
            @Valid @RequestBody OperationalStatusDTO dto) {
        OperationalStatusDTO created = operationalStatusService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:MANAGE')")
    @Operation(summary = "Update operational status", description = "Updates an existing operational status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operational status updated successfully", content = @Content(schema = @Schema(implementation = OperationalStatusDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Operational status not found"),
        @ApiResponse(responseCode = "409", description = "Operational status name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATIONAL_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<OperationalStatusDTO> update(
            @Parameter(description = "Operational status ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated operational status data", required = true) @Valid @RequestBody OperationalStatusDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:MANAGE')")
    @Operation(summary = "Delete operational status", description = "Deletes an operational status permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Operational status deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Operational status not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete operational status - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATIONAL_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Operational status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    @Operation(summary = "Search operational statuses", description = "Searches operational statuses by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATIONAL_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<OperationalStatusDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    @Operation(summary = "Check if operational status exists", description = "Checks if an operational status with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATIONAL_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Operational status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    @Operation(summary = "Count operational statuses", description = "Returns the total number of operational statuses in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operational status count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATIONAL_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
