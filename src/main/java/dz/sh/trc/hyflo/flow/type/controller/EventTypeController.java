/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventTypeController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.type.dto.EventTypeDTO;
import dz.sh.trc.hyflo.flow.type.service.EventTypeService;
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

/**
 * REST Controller for EventType entity.
 * Provides CRUD endpoints and search functionality for operational event type management.
 */
@RestController
@RequestMapping("/flow/type/event")
@Tag(name = "Event Type Management", description = "APIs for managing operational event types")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class EventTypeController extends GenericController<EventTypeDTO, Long> {

    private final EventTypeService eventTypeService;
    
    public EventTypeController(EventTypeService eventTypeService) {
        super(eventTypeService, "EventType");
        this.eventTypeService = eventTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get event type by ID", description = "Retrieves a single event type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event type found", content = @Content(schema = @Schema(implementation = EventTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Event type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<EventTypeDTO> getById(
            @Parameter(description = "Event type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all event types (paginated)", description = "Retrieves a paginated list of all event types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<Page<EventTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all event types (unpaginated)", description = "Retrieves all event types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<List<EventTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create event type", description = "Creates a new event type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Event type created successfully", content = @Content(schema = @Schema(implementation = EventTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Event type code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_TYPE:MANAGE')")
    public ResponseEntity<EventTypeDTO> create(
            @Parameter(description = "Event type data", required = true) 
            @Valid @RequestBody EventTypeDTO dto) {
        EventTypeDTO created = eventTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update event type", description = "Updates an existing event type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event type updated successfully", content = @Content(schema = @Schema(implementation = EventTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Event type not found"),
        @ApiResponse(responseCode = "409", description = "Event type code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_TYPE:MANAGE')")
    public ResponseEntity<EventTypeDTO> update(
            @Parameter(description = "Event type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated event type data", required = true) @Valid @RequestBody EventTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete event type", description = "Deletes an event type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Event type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Event type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete event type - has dependencies (events using this type)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_TYPE:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Event type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search event types", description = "Searches event types by code or designation (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<Page<EventTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if event type exists", description = "Checks if an event type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Event type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count event types", description = "Returns the total number of event types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get event type by code", description = "Retrieves an event type by its unique code")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event type found", content = @Content(schema = @Schema(implementation = EventTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Event type not found with given code"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<EventTypeDTO> getByCode(
            @Parameter(description = "Event type code", required = true, example = "MAINTENANCE") 
            @PathVariable String code) {
        log.info("GET /flow/type/event/code/{} - Getting event type by code", code);
        return ResponseEntity.ok(eventTypeService.findByCode(code));
    }

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get event type by French designation", description = "Retrieves an event type by its unique French designation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event type found", content = @Content(schema = @Schema(implementation = EventTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Event type not found with given designation"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EVENT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<EventTypeDTO> getByDesignationFr(
            @Parameter(description = "French designation", required = true, example = "Maintenance") 
            @PathVariable String designationFr) {
        log.info("GET /flow/type/event/designation/{} - Getting event type by French designation", designationFr);
        return ResponseEntity.ok(eventTypeService.findByDesignationFr(designationFr));
    }
}
