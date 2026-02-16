/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventStatusController
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
import dz.sh.trc.hyflo.flow.common.dto.EventStatusDTO;
import dz.sh.trc.hyflo.flow.common.service.EventStatusService;
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
@RequestMapping("/flow/common/eventStatus")
@Tag(name = "Event Status Management", description = "APIs for managing operational event statuses")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class EventStatusController extends GenericController<EventStatusDTO, Long> {

    private final EventStatusService eventStatusService;
    
    public EventStatusController(EventStatusService eventStatusService) {
        super(eventStatusService, "EventStatus");
        this.eventStatusService = eventStatusService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get event status by ID", description = "Retrieves a single event status by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event status found", content = @Content(schema = @Schema(implementation = EventStatusDTO.class))),
        @ApiResponse(responseCode = "404", description = "Event status not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<EventStatusDTO> getById(
            @Parameter(description = "Event status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all event statuses (paginated)", description = "Retrieves a paginated list of all event statuses")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event statuses retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<Page<EventStatusDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all event statuses (unpaginated)", description = "Retrieves all event statuses without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event statuses retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<List<EventStatusDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create event status", description = "Creates a new event status with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Event status created successfully", content = @Content(schema = @Schema(implementation = EventStatusDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Event status code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_STATUS:MANAGE')")
    public ResponseEntity<EventStatusDTO> create(
            @Parameter(description = "Event status data", required = true) 
            @Valid @RequestBody EventStatusDTO dto) {
        EventStatusDTO created = eventStatusService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update event status", description = "Updates an existing event status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event status updated successfully", content = @Content(schema = @Schema(implementation = EventStatusDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Event status not found"),
        @ApiResponse(responseCode = "409", description = "Event status code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_STATUS:MANAGE')")
    public ResponseEntity<EventStatusDTO> update(
            @Parameter(description = "Event status ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated event status data", required = true) @Valid @RequestBody EventStatusDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete event status", description = "Deletes an event status permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Event status deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Event status not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete event status - has dependencies (events using this status)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_STATUS:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_STATUS:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Event status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search event statuses", description = "Searches event statuses by code or designation (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<Page<EventStatusDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if event status exists", description = "Checks if an event status with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Event status ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count event statuses", description = "Returns the total number of event statuses in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event status count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get event status by French designation", description = "Retrieves an event status by its unique French designation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event status found", content = @Content(schema = @Schema(implementation = EventStatusDTO.class))),
        @ApiResponse(responseCode = "404", description = "Event status not found with given designation"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_STATUS:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<EventStatusDTO> getByDesignationFr(
            @Parameter(description = "French designation", required = true, example = "En cours") 
            @PathVariable String designationFr) {
        log.info("GET /flow/common/eventstatus/designation/{} - Getting event status by French designation", designationFr);
        return ResponseEntity.ok(eventStatusService.findByDesignationFr(designationFr));
    }
}
