/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowEventController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.FlowEventDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowEventService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flow/core/event")
@Tag(name = "Flow Event Management", description = "APIs for managing operational events affecting flow systems")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class FlowEventController extends GenericController<FlowEventDTO, Long> {

    private final FlowEventService flowEventService;
    
    public FlowEventController(FlowEventService flowEventService) {
        super(flowEventService, "FlowEvent");
        this.flowEventService = flowEventService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get flow event by ID", description = "Retrieves a single flow event by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow event found", content = @Content(schema = @Schema(implementation = FlowEventDTO.class))),
        @ApiResponse(responseCode = "404", description = "Flow event not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<FlowEventDTO> getById(
            @Parameter(description = "Flow event ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all flow events (paginated)", description = "Retrieves a paginated list of all flow events")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow events retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Page<FlowEventDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all flow events (unpaginated)", description = "Retrieves all flow events without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow events retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<List<FlowEventDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create flow event", description = "Creates a new flow event")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Flow event created successfully", content = @Content(schema = @Schema(implementation = FlowEventDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:WRITE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:WRITE')")
    public ResponseEntity<FlowEventDTO> create(
            @Parameter(description = "Flow event data", required = true) 
            @Valid @RequestBody FlowEventDTO dto) {
        FlowEventDTO created = flowEventService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update flow event", description = "Updates an existing flow event")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow event updated successfully", content = @Content(schema = @Schema(implementation = FlowEventDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Flow event not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:WRITE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:WRITE')")
    public ResponseEntity<FlowEventDTO> update(
            @Parameter(description = "Flow event ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated flow event data", required = true) @Valid @RequestBody FlowEventDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete flow event", description = "Deletes a flow event permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Flow event deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Flow event not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Flow event ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search flow events", description = "Searches flow events by description and other criteria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Page<FlowEventDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if flow event exists", description = "Checks if a flow event with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Flow event ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count flow events", description = "Returns the total number of flow events")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow event count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/infrastructure/{infrastructureId}")
    @Operation(summary = "Get events by infrastructure", description = "Retrieves all events for a specific infrastructure")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Infrastructure not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<List<FlowEventDTO>> getByInfrastructure(
            @Parameter(description = "Infrastructure ID", required = true, example = "1") 
            @PathVariable Long infrastructureId) {
        log.info("GET /flow/core/event/infrastructure/{} - Getting events by infrastructure", infrastructureId);
        return ResponseEntity.ok(flowEventService.findByInfrastructure(infrastructureId));
    }

    @GetMapping("/severity/{severityId}")
    @Operation(summary = "Get events by severity", description = "Retrieves all events with a specific severity level")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Severity not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<List<FlowEventDTO>> getBySeverity(
            @Parameter(description = "Severity ID", required = true, example = "1") 
            @PathVariable Long severityId) {
        log.info("GET /flow/core/event/severity/{} - Getting events by severity", severityId);
        return ResponseEntity.ok(flowEventService.findBySeverity(severityId));
    }

    @GetMapping("/status/{statusId}")
    @Operation(summary = "Get events by status", description = "Retrieves all events with a specific status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Status not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<List<FlowEventDTO>> getByStatus(
            @Parameter(description = "Status ID", required = true, example = "1") 
            @PathVariable Long statusId) {
        log.info("GET /flow/core/event/status/{} - Getting events by status", statusId);
        return ResponseEntity.ok(flowEventService.findByStatus(statusId));
    }

    @GetMapping("/impact-on-flow")
    @Operation(summary = "Get events by flow impact", description = "Retrieves events filtered by whether they impact flow operations")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<List<FlowEventDTO>> getByImpactOnFlow(
            @Parameter(description = "Impact on flow filter", example = "true") 
            @RequestParam(defaultValue = "true") Boolean impactOnFlow) {
        log.info("GET /flow/core/event/impact-on-flow - Getting events with impact on flow: {}", impactOnFlow);
        return ResponseEntity.ok(flowEventService.findByImpactOnFlow(impactOnFlow));
    }

    @GetMapping("/infrastructure/{infrastructureId}/time-range")
    @Operation(summary = "Get events by infrastructure and time range", description = "Retrieves events for an infrastructure within a time range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid time range parameters"),
        @ApiResponse(responseCode = "404", description = "Infrastructure not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Page<FlowEventDTO>> getByInfrastructureAndTimeRange(
            @Parameter(description = "Infrastructure ID", required = true, example = "1") @PathVariable Long infrastructureId,
            @Parameter(description = "Start time (ISO 8601)", required = true, example = "2026-01-01T00:00:00") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "End time (ISO 8601)", required = true, example = "2026-12-31T23:59:59") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "eventTimestamp") @RequestParam(defaultValue = "eventTimestamp") String sortBy,
            @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/event/infrastructure/{}/time-range - Getting events from {} to {}", 
                 infrastructureId, startTime, endTime);
        return ResponseEntity.ok(flowEventService.findByInfrastructureAndTimeRange(
                infrastructureId, startTime, endTime, buildPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/severity/{severityId}/time-range")
    @Operation(summary = "Get events by severity and time range", description = "Retrieves events with specific severity within a time range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid time range parameters"),
        @ApiResponse(responseCode = "404", description = "Severity not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Page<FlowEventDTO>> getBySeverityAndTimeRange(
            @Parameter(description = "Severity ID", required = true, example = "1") @PathVariable Long severityId,
            @Parameter(description = "Start time (ISO 8601)", required = true, example = "2026-01-01T00:00:00") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "End time (ISO 8601)", required = true, example = "2026-12-31T23:59:59") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "eventTimestamp") @RequestParam(defaultValue = "eventTimestamp") String sortBy,
            @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/event/severity/{}/time-range - Getting events from {} to {}", 
                 severityId, startTime, endTime);
        return ResponseEntity.ok(flowEventService.findBySeverityAndTimeRange(
                severityId, startTime, endTime, buildPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/impact-on-flow/time-range")
    @Operation(summary = "Get events with flow impact by time range", description = "Retrieves events that impact flow operations within a time range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid time range parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_EVENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_EVENT:READ')")
    public ResponseEntity<Page<FlowEventDTO>> getWithImpactOnFlowByTimeRange(
            @Parameter(description = "Start time (ISO 8601)", required = true, example = "2026-01-01T00:00:00") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "End time (ISO 8601)", required = true, example = "2026-12-31T23:59:59") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "eventTimestamp") @RequestParam(defaultValue = "eventTimestamp") String sortBy,
            @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/event/impact-on-flow/time-range - Getting events with impact from {} to {}", 
                 startTime, endTime);
        return ResponseEntity.ok(flowEventService.findWithImpactOnFlowByTimeRange(
                startTime, endTime, buildPageable(page, size, sortBy, sortDir)));
    }
}
