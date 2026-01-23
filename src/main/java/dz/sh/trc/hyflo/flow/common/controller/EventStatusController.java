/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventStatusController
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
import dz.sh.trc.hyflo.flow.common.dto.EventStatusDTO;
import dz.sh.trc.hyflo.flow.common.service.EventStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/common/eventstatus")
@Tag(name = "Event Status", description = "Operational event status management API")
@Slf4j
public class EventStatusController extends GenericController<EventStatusDTO, Long> {

    private final EventStatusService eventStatusService;
    
    public EventStatusController(EventStatusService eventStatusService) {
        super(eventStatusService, "EventStatus");
        this.eventStatusService = eventStatusService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get event status by ID", description = "Retrieve a single event status by its unique identifier")
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<EventStatusDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all event statuses (paginated)", description = "Retrieve all event statuses with pagination and sorting")
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<Page<EventStatusDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all event statuses (no pagination)", description = "Retrieve all event statuses sorted by code")
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<List<EventStatusDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create event status", description = "Create a new event status with unique code and French designation validation")
    @PreAuthorize("hasAuthority('EVENT_STATUS:ADMIN')")
    public ResponseEntity<EventStatusDTO> create(@Valid @RequestBody EventStatusDTO dto) {
        return super.create(dto);
    }

    @Override
    @Operation(summary = "Update event status", description = "Update an existing event status by ID")
    @PreAuthorize("hasAuthority('EVENT_STATUS:ADMIN')")
    public ResponseEntity<EventStatusDTO> update(@PathVariable Long id, @Valid @RequestBody EventStatusDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete event status", description = "Delete an event status by ID")
    @PreAuthorize("hasAuthority('EVENT_STATUS:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search event statuses", description = "Search event statuses across all fields with pagination")
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<Page<EventStatusDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if event status exists", description = "Check if an event status exists by ID")
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count event statuses", description = "Get total count of event statuses")
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get event status by French designation", description = "Find event status by its unique French designation")
    @PreAuthorize("hasAuthority('EVENT_STATUS:READ')")
    public ResponseEntity<EventStatusDTO> getByDesignationFr(@PathVariable String designationFr) {
        log.info("GET /flow/common/eventstatus/designation/{} - Getting event status by French designation", designationFr);
        return ResponseEntity.ok(eventStatusService.findByDesignationFr(designationFr));
    }
}
