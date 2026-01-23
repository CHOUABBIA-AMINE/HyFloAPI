/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventTypeController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.controller;

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
import dz.sh.trc.hyflo.flow.type.dto.EventTypeDTO;
import dz.sh.trc.hyflo.flow.type.service.EventTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for EventType entity.
 * Provides CRUD endpoints and search functionality for operational event type management.
 */
@RestController
@RequestMapping("/flow/type/event")
@Tag(name = "Event Type", description = "Operational event type management API")
@Slf4j
public class EventTypeController extends GenericController<EventTypeDTO, Long> {

    private final EventTypeService eventTypeService;
    
    public EventTypeController(EventTypeService eventTypeService) {
        super(eventTypeService, "EventType");
        this.eventTypeService = eventTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get event type by ID", description = "Retrieve a single event type by its unique identifier")
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<EventTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all event types (paginated)", description = "Retrieve all event types with pagination and sorting")
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<Page<EventTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all event types (no pagination)", description = "Retrieve all event types sorted by code")
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<List<EventTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create event type", description = "Create a new event type with unique code and French designation validation")
    @PreAuthorize("hasAuthority('EVENT_TYPE:ADMIN')")
    public ResponseEntity<EventTypeDTO> create(@Valid @RequestBody EventTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @Operation(summary = "Update event type", description = "Update an existing event type by ID")
    @PreAuthorize("hasAuthority('EVENT_TYPE:ADMIN')")
    public ResponseEntity<EventTypeDTO> update(@PathVariable Long id, @Valid @RequestBody EventTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete event type", description = "Delete an event type by ID")
    @PreAuthorize("hasAuthority('EVENT_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search event types", description = "Search event types across all fields with pagination")
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<Page<EventTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if event type exists", description = "Check if an event type exists by ID")
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count event types", description = "Get total count of event types")
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get event type by code", description = "Find event type by its unique code")
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<EventTypeDTO> getByCode(@PathVariable String code) {
        log.info("GET /flow/type/event/code/{} - Getting event type by code", code);
        return ResponseEntity.ok(eventTypeService.findByCode(code));
    }

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get event type by French designation", description = "Find event type by its unique French designation")
    @PreAuthorize("hasAuthority('EVENT_TYPE:READ')")
    public ResponseEntity<EventTypeDTO> getByDesignationFr(@PathVariable String designationFr) {
        log.info("GET /flow/type/event/designation/{} - Getting event type by French designation", designationFr);
        return ResponseEntity.ok(eventTypeService.findByDesignationFr(designationFr));
    }
}
