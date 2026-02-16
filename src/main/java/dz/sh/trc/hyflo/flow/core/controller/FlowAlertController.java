/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowAlertController
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
import dz.sh.trc.hyflo.flow.core.dto.FlowAlertDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowAlertService;
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
@RequestMapping("/flow/core/alert")
@Tag(name = "Flow Alert Management", description = "APIs for managing flow-related alerts and threshold violations")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class FlowAlertController extends GenericController<FlowAlertDTO, Long> {

    private final FlowAlertService flowAlertService;
    
    public FlowAlertController(FlowAlertService flowAlertService) {
        super(flowAlertService, "FlowAlert");
        this.flowAlertService = flowAlertService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get flow alert by ID", description = "Retrieves a single flow alert by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow alert found", content = @Content(schema = @Schema(implementation = FlowAlertDTO.class))),
        @ApiResponse(responseCode = "404", description = "Flow alert not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<FlowAlertDTO> getById(
            @Parameter(description = "Flow alert ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all flow alerts (paginated)", description = "Retrieves a paginated list of all flow alerts")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow alerts retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all flow alerts (unpaginated)", description = "Retrieves all flow alerts without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow alerts retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<List<FlowAlertDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create flow alert", description = "Creates a new flow alert")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Flow alert created successfully", content = @Content(schema = @Schema(implementation = FlowAlertDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:WRITE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:WRITE')")
    public ResponseEntity<FlowAlertDTO> create(
            @Parameter(description = "Flow alert data", required = true) 
            @Valid @RequestBody FlowAlertDTO dto) {
        FlowAlertDTO created = flowAlertService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update flow alert", description = "Updates an existing flow alert")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow alert updated successfully", content = @Content(schema = @Schema(implementation = FlowAlertDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Flow alert not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:WRITE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:WRITE')")
    public ResponseEntity<FlowAlertDTO> update(
            @Parameter(description = "Flow alert ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated flow alert data", required = true) @Valid @RequestBody FlowAlertDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete flow alert", description = "Deletes a flow alert permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Flow alert deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Flow alert not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Flow alert ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search flow alerts", description = "Searches flow alerts by multiple criteria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if flow alert exists", description = "Checks if a flow alert with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Flow alert ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count flow alerts", description = "Returns the total number of flow alerts")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow alert count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/threshold/{thresholdId}")
    @Operation(summary = "Get alerts by threshold", description = "Retrieves all alerts triggered by a specific threshold")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alerts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Threshold not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<List<FlowAlertDTO>> getByThreshold(
            @Parameter(description = "Threshold ID", required = true, example = "1") 
            @PathVariable Long thresholdId) {
        log.info("GET /flow/core/alert/threshold/{} - Getting alerts by threshold", thresholdId);
        return ResponseEntity.ok(flowAlertService.findByThreshold(thresholdId));
    }

    @GetMapping("/status/{statusId}")
    @Operation(summary = "Get alerts by status", description = "Retrieves all alerts with a specific status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alerts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Status not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<List<FlowAlertDTO>> getByStatus(
            @Parameter(description = "Status ID", required = true, example = "1") 
            @PathVariable Long statusId) {
        log.info("GET /flow/core/alert/status/{} - Getting alerts by status", statusId);
        return ResponseEntity.ok(flowAlertService.findByStatus(statusId));
    }

    @GetMapping("/unacknowledged")
    @Operation(summary = "Get unacknowledged alerts", description = "Retrieves all alerts that have not been acknowledged")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Unacknowledged alerts retrieved"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getUnacknowledged(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/core/alert/unacknowledged - Getting unacknowledged alerts");
        return ResponseEntity.ok(flowAlertService.findUnacknowledged(
                buildPageable(page, size, "alertTimestamp", "desc")));
    }

    @GetMapping("/unresolved")
    @Operation(summary = "Get unresolved alerts", description = "Retrieves all alerts that have not been resolved")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Unresolved alerts retrieved"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getUnresolved(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/core/alert/unresolved - Getting unresolved alerts");
        return ResponseEntity.ok(flowAlertService.findUnresolved(
                buildPageable(page, size, "alertTimestamp", "desc")));
    }

    @GetMapping("/pipeline/{pipelineId}/unresolved")
    @Operation(summary = "Get unresolved alerts by pipeline", description = "Retrieves unresolved alerts for a specific pipeline")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Unresolved alerts retrieved"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getUnresolvedByPipeline(
            @Parameter(description = "Pipeline ID", required = true, example = "1") @PathVariable Long pipelineId,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/core/alert/pipeline/{}/unresolved - Getting unresolved alerts for pipeline", pipelineId);
        return ResponseEntity.ok(flowAlertService.findUnresolvedByPipeline(
                pipelineId, buildPageable(page, size, "alertTimestamp", "desc")));
    }

    @GetMapping("/pipeline/{pipelineId}/time-range")
    @Operation(summary = "Get alerts by pipeline and time range", description = "Retrieves alerts for a pipeline within a time range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alerts retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid time range parameters"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getByPipelineAndTimeRange(
            @Parameter(description = "Pipeline ID", required = true, example = "1") @PathVariable Long pipelineId,
            @Parameter(description = "Start time (ISO 8601)", required = true, example = "2026-01-01T00:00:00") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "End time (ISO 8601)", required = true, example = "2026-12-31T23:59:59") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "alertTimestamp") @RequestParam(defaultValue = "alertTimestamp") String sortBy,
            @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/alert/pipeline/{}/time-range - Getting alerts from {} to {}", 
                 pipelineId, startTime, endTime);
        return ResponseEntity.ok(flowAlertService.findByPipelineAndTimeRange(
                pipelineId, startTime, endTime, buildPageable(page, size, sortBy, sortDir)));
    }

    @GetMapping("/status/{statusId}/time-range")
    @Operation(summary = "Get alerts by status and time range", description = "Retrieves alerts with specific status within a time range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alerts retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid time range parameters"),
        @ApiResponse(responseCode = "404", description = "Status not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_ALERT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_ALERT:READ')")
    public ResponseEntity<Page<FlowAlertDTO>> getByStatusAndTimeRange(
            @Parameter(description = "Status ID", required = true, example = "1") @PathVariable Long statusId,
            @Parameter(description = "Start time (ISO 8601)", required = true, example = "2026-01-01T00:00:00") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "End time (ISO 8601)", required = true, example = "2026-12-31T23:59:59") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "alertTimestamp") @RequestParam(defaultValue = "alertTimestamp") String sortBy,
            @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/alert/status/{}/time-range - Getting alerts from {} to {}", 
                 statusId, startTime, endTime);
        return ResponseEntity.ok(flowAlertService.findByStatusAndTimeRange(
                statusId, startTime, endTime, buildPageable(page, size, sortBy, sortDir)));
    }
}
