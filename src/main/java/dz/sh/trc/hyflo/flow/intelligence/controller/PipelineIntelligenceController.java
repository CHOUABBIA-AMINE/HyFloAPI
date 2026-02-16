/**
 *
 *	@Author		: MEDJERAB Abir, CHOUABBIA Amine
 *
 * 	@Name		: PipelineIntelligenceController
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 02-14-2026
 * 	@UpdatedOn	: 02-16-2026 - Enhanced with comprehensive OpenAPI documentation
 * 	@UpdatedOn	: 02-16-2026 - Simplified to single-line descriptions
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineDynamicDashboardDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineInfoDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineOverviewDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineTimelineDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.ReadingsTimeSeriesDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.SlotStatusDTO;
import dz.sh.trc.hyflo.flow.intelligence.service.PipelineIntelligenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(
    name = "Pipeline Intelligence",
    description = "Operational intelligence and analytics for individual pipelines"
)
@SecurityRequirement(name = "bearer-auth")
@RestController
@RequestMapping("/flow/intelligence/pipeline")
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceController {
    
    private final PipelineIntelligenceService intelligenceService;
    
    @Operation(
        summary = "Get pipeline detailed information",
        description = "Retrieves comprehensive pipeline information including static infrastructure, linked entities, and optional health status for detail page population."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pipeline information"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{pipelineId}/info")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<PipelineInfoDTO> getPipelineInfo(
            @Parameter(description = "Pipeline ID", required = true, example = "101")
            @PathVariable Long pipelineId,
            @Parameter(description = "Include current health status", example = "true")
            @RequestParam(required = false, defaultValue = "true") Boolean includeHealth,
            @Parameter(description = "Include linked entities (stations, valves, sensors)", example = "false")
            @RequestParam(required = false, defaultValue = "false") Boolean includeEntities) {
        log.info("GET /flow/intelligence/pipeline/{}/info", pipelineId);
        PipelineInfoDTO info = intelligenceService.getPipelineInfo(pipelineId, includeHealth, includeEntities);
        return ResponseEntity.ok(info);
    }
    
    @Operation(
        summary = "Get real-time pipeline dashboard",
        description = "Retrieves real-time operational dashboard metrics optimized for frequent polling. Includes current readings, health indicators, and quick statistics."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved dashboard data"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{pipelineId}/dashboard")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<PipelineDynamicDashboardDTO> getDashboard(
            @Parameter(description = "Pipeline ID", required = true, example = "101")
            @PathVariable Long pipelineId) {
        log.info("GET /flow/intelligence/pipeline/{}/dashboard", pipelineId);
        PipelineDynamicDashboardDTO dashboard = intelligenceService.getDashboard(pipelineId);
        return ResponseEntity.ok(dashboard);
    }
    
    @Operation(
        summary = "Get pipeline timeline",
        description = "Retrieves unified chronological timeline merging alerts and operational events with filtering and pagination support."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved timeline"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{pipelineId}/timeline")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<PipelineTimelineDTO> getTimeline(
            @Parameter(description = "Pipeline ID", required = true, example = "101")
            @PathVariable Long pipelineId,
            @Parameter(description = "Start date/time (defaults to 7 days ago)", example = "2026-02-09T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Parameter(description = "End date/time (defaults to now)", example = "2026-02-16T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @Parameter(description = "Filter by severity: CRITICAL, WARNING, INFO, NORMAL")
            @RequestParam(required = false) String severity,
            @Parameter(description = "Filter by type: ALERT, EVENT, OPERATION")
            @RequestParam(required = false) String type,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Items per page (max 100)", example = "20")
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        LocalDateTime fromDate = from != null ? from : LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = to != null ? to : LocalDateTime.now();
        log.info("GET /flow/intelligence/pipeline/{}/timeline", pipelineId);
        PipelineTimelineDTO timeline = intelligenceService.getTimeline(
            pipelineId, fromDate, toDate, severity, type, page, size);
        return ResponseEntity.ok(timeline);
    }
    
    @Operation(
        summary = "Get pipeline operational overview",
        description = "Retrieves comprehensive operational overview including asset specs, KPIs, slot coverage, and current measurements for dashboard display."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pipeline overview"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{pipelineId}/overview")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<PipelineOverviewDTO> getOverview(
            @Parameter(description = "Pipeline ID", required = true, example = "101")
            @PathVariable Long pipelineId,
            @Parameter(description = "Reference date for metrics (defaults to today)", example = "2026-02-16")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referenceDate) {
        LocalDate date = referenceDate != null ? referenceDate : LocalDate.now();
        log.info("GET /flow/intelligence/pipeline/{}/overview", pipelineId);
        PipelineOverviewDTO overview = intelligenceService.getOverview(pipelineId, date);
        return ResponseEntity.ok(overview);
    }
    
    @Operation(
        summary = "Get slot coverage",
        description = "Retrieves detailed status of all 12 reading slots for a specific date with recording status, validation status, and operator information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved slot coverage (12 slots)"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{pipelineId}/slot-coverage")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<List<SlotStatusDTO>> getSlotCoverage(
            @Parameter(description = "Pipeline ID", required = true, example = "101")
            @PathVariable Long pipelineId,
            @Parameter(description = "Date for slot coverage (defaults to today)", example = "2026-02-16")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        log.info("GET /flow/intelligence/pipeline/{}/slot-coverage", pipelineId);
        List<SlotStatusDTO> slotCoverage = intelligenceService.getSlotCoverage(pipelineId, targetDate);
        return ResponseEntity.ok(slotCoverage);
    }
    
    @Operation(
        summary = "Get readings time series",
        description = "Retrieves historical time-series data for specific measurement type with statistical analysis. Supports trend analysis and pattern detection."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved time-series data"),
        @ApiResponse(responseCode = "400", description = "Invalid measurement type or date range"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{pipelineId}/readings-timeseries")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<ReadingsTimeSeriesDTO> getReadingsTimeSeries(
            @Parameter(description = "Pipeline ID", required = true, example = "101")
            @PathVariable Long pipelineId,
            @Parameter(description = "Start date (defaults to 7 days ago)", example = "2026-02-09")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (defaults to today)", example = "2026-02-16")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Measurement type: PRESSURE, TEMPERATURE, FLOW_RATE, CONTAINED_VOLUME", example = "PRESSURE")
            @RequestParam(required = false, defaultValue = "PRESSURE") String measurementType) {
        LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(7);
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        log.info("GET /flow/intelligence/pipeline/{}/readings-timeseries", pipelineId);
        ReadingsTimeSeriesDTO timeSeries = intelligenceService.getReadingsTimeSeries(
            pipelineId, start, end, measurementType);
        return ResponseEntity.ok(timeSeries);
    }
}
