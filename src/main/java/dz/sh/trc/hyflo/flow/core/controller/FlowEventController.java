/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventController
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Core
 *
 *  @Description: REST controller for FlowEvent queries.
 *                FlowEventFacade is used externally by flow.intelligence —
 *                this controller exposes the query layer via HTTP.
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import dz.sh.trc.hyflo.flow.core.dto.FlowEventReadDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowEventQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flow/events")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Events", description = "Query endpoints for FlowEvent data")
public class FlowEventController {

    private final FlowEventQueryService flowEventQueryService;

    @GetMapping
    @Operation(summary = "Get all flow events (paginated)")
    public ResponseEntity<Page<FlowEventReadDTO>> getAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(flowEventQueryService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get flow event by ID")
    public ResponseEntity<FlowEventReadDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(flowEventQueryService.findById(id));
    }

    @GetMapping("/infrastructure/{infrastructureId}")
    @Operation(summary = "Get flow events by infrastructure ID")
    public ResponseEntity<List<FlowEventReadDTO>> getByInfrastructure(
            @PathVariable Long infrastructureId) {
        return ResponseEntity.ok(flowEventQueryService.findByInfrastructure(infrastructureId));
    }

    @GetMapping("/severity/{severityId}")
    @Operation(summary = "Get flow events by severity ID")
    public ResponseEntity<List<FlowEventReadDTO>> getBySeverity(
            @PathVariable Long severityId) {
        return ResponseEntity.ok(flowEventQueryService.findBySeverity(severityId));
    }

    @GetMapping("/status/{statusId}")
    @Operation(summary = "Get flow events by status ID")
    public ResponseEntity<List<FlowEventReadDTO>> getByStatus(
            @PathVariable Long statusId) {
        return ResponseEntity.ok(flowEventQueryService.findByStatus(statusId));
    }

    @GetMapping("/impact")
    @Operation(summary = "Get flow events by impact on flow flag")
    public ResponseEntity<List<FlowEventReadDTO>> getByImpactOnFlow(
            @RequestParam Boolean impactOnFlow) {
        return ResponseEntity.ok(flowEventQueryService.findByImpactOnFlow(impactOnFlow));
    }

    @GetMapping("/time-range")
    @Operation(summary = "Get flow events by time range")
    public ResponseEntity<List<FlowEventReadDTO>> getByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(flowEventQueryService.findByTimeRange(start, end));
    }

    @GetMapping("/infrastructure/{infrastructureId}/time-range")
    @Operation(summary = "Get flow events by infrastructure and time range (paginated)")
    public ResponseEntity<Page<FlowEventReadDTO>> getByInfrastructureAndTimeRange(
            @PathVariable Long infrastructureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(
                flowEventQueryService.findByInfrastructureAndTimeRange(infrastructureId, start, end, pageable));
    }

    @GetMapping("/severity/{severityId}/time-range")
    @Operation(summary = "Get flow events by severity and time range (paginated)")
    public ResponseEntity<Page<FlowEventReadDTO>> getBySeverityAndTimeRange(
            @PathVariable Long severityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(
                flowEventQueryService.findBySeverityAndTimeRange(severityId, start, end, pageable));
    }

    @GetMapping("/status/{statusId}/time-range")
    @Operation(summary = "Get flow events by status and time range (paginated)")
    public ResponseEntity<Page<FlowEventReadDTO>> getByStatusAndTimeRange(
            @PathVariable Long statusId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(
                flowEventQueryService.findByStatusAndTimeRange(statusId, start, end, pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Global search across flow events")
    public ResponseEntity<Page<FlowEventReadDTO>> search(
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(flowEventQueryService.globalSearch(query, pageable));
    }
}
