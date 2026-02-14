/**
 *
 *	@Author		: MEDJERAB Abir, CHOUABBIA Amine
 *
 * 	@Name		: PipelineIntelligenceController
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 02-14-2026
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST API for Pipeline Operational Intelligence
 * Provides real-time monitoring, KPIs, and analytics for pipeline operations
 * Extended to support comprehensive PipelineInfoPage functionality
 */
@RestController
@RequestMapping("/flow/intelligence/pipeline")
@Tag(name = "Pipeline Intelligence", description = "Operational intelligence and analytics for pipelines")
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceController {
    
    private final PipelineIntelligenceService intelligenceService;
    
    /**
     * Get comprehensive pipeline information for detail page
     * Aggregates static infrastructure with optional dynamic health data
     */
    @GetMapping("/{pipelineId}/info")
    //@PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    @Operation(
        summary = "Get Pipeline Detailed Information",
        description = "Retrieve comprehensive pipeline information including static infrastructure, " +
                      "linked entities (stations, valves, sensors), and optional current health status. " +
                      "This endpoint is optimized for PipelineInfoPage with caching support."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved pipeline information",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = PipelineInfoDTO.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Pipeline not found"
    )
    @ApiResponse(
        responseCode = "403",
        description = "Access denied - insufficient permissions"
    )
    public ResponseEntity<PipelineInfoDTO> getPipelineInfo(
            @Parameter(description = "Pipeline ID", required = true)
            @PathVariable Long pipelineId,
            
            @Parameter(description = "Include current health status (may increase response time)", 
                       example = "true")
            @RequestParam(required = false, defaultValue = "true") 
            Boolean includeHealth,
            
            @Parameter(description = "Include linked entities (stations, valves, sensors)", 
                       example = "true")
            @RequestParam(required = false, defaultValue = "false") 
            Boolean includeEntities) {
        
        log.info("GET /flow/intelligence/pipeline/{}/info?includeHealth={}&includeEntities={}", 
                 pipelineId, includeHealth, includeEntities);
        
        PipelineInfoDTO info = intelligenceService.getPipelineInfo(pipelineId, includeHealth, includeEntities);
        return ResponseEntity.ok(info);
    }
    
    /**
     * Get real-time operational dashboard data
     * Optimized for frequent updates with short cache TTL
     */
    @GetMapping("/{pipelineId}/dashboard")
    //@PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    @Operation(
        summary = "Get Real-time Pipeline Dashboard",
        description = "Retrieve real-time operational dashboard metrics including current readings, " +
                      "health indicators, alert counts, and quick statistics. Optimized for frequent " +
                      "polling or WebSocket updates (30-second cache)."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved dashboard data",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = PipelineDynamicDashboardDTO.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Pipeline not found"
    )
    @ApiResponse(
        responseCode = "403",
        description = "Access denied - insufficient permissions"
    )
    public ResponseEntity<PipelineDynamicDashboardDTO> getDashboard(
            @Parameter(description = "Pipeline ID", required = true)
            @PathVariable Long pipelineId) {
        
        log.info("GET /flow/intelligence/pipeline/{}/dashboard", pipelineId);
        
        PipelineDynamicDashboardDTO dashboard = intelligenceService.getDashboard(pipelineId);
        return ResponseEntity.ok(dashboard);
    }
    
    /**
     * Get unified timeline of alerts and events
     * Merges alerts and events chronologically with pagination
     */
    @GetMapping("/{pipelineId}/timeline")
    //@PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    @Operation(
        summary = "Get Pipeline Timeline",
        description = "Retrieve unified timeline merging alerts and events chronologically. " +
                      "Supports filtering by date range, severity, and type with pagination. " +
                      "Includes distribution statistics for dashboard visualization."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved timeline",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = PipelineTimelineDTO.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Pipeline not found"
    )
    @ApiResponse(
        responseCode = "403",
        description = "Access denied - insufficient permissions"
    )
    public ResponseEntity<PipelineTimelineDTO> getTimeline(
            @Parameter(description = "Pipeline ID", required = true)
            @PathVariable Long pipelineId,
            
            @Parameter(description = "Start date/time for timeline", 
                       example = "2026-02-07T00:00:00")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
            LocalDateTime from,
            
            @Parameter(description = "End date/time for timeline (defaults to now)", 
                       example = "2026-02-14T23:59:59")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
            LocalDateTime to,
            
            @Parameter(
                description = "Filter by severity",
                schema = @Schema(
                    type = "string",
                    allowableValues = {"CRITICAL", "WARNING", "INFO", "NORMAL"}
                )
            )
            @RequestParam(required = false) 
            String severity,
            
            @Parameter(
                description = "Filter by type",
                schema = @Schema(
                    type = "string",
                    allowableValues = {"ALERT", "EVENT", "OPERATION"}
                )
            )
            @RequestParam(required = false) 
            String type,
            
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false, defaultValue = "0") 
            Integer page,
            
            @Parameter(description = "Items per page", example = "20")
            @RequestParam(required = false, defaultValue = "20") 
            Integer size) {
        
        LocalDateTime fromDate = from != null ? from : LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = to != null ? to : LocalDateTime.now();
        
        log.info("GET /flow/intelligence/pipeline/{}/timeline?from={}&to={}&severity={}&type={}&page={}&size={}",
            pipelineId, fromDate, toDate, severity, type, page, size);
        
        PipelineTimelineDTO timeline = intelligenceService.getTimeline(
            pipelineId, fromDate, toDate, severity, type, page, size);
        
        return ResponseEntity.ok(timeline);
    }
    
    /**
     * Get comprehensive operational overview for a pipeline
     * Includes asset specs, KPIs, slot coverage, and current measurements
     */
    @GetMapping("/{pipelineId}/overview")
    //@PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    @Operation(
        summary = "Get Pipeline Overview",
        description = "Retrieve comprehensive operational overview including asset specifications, " +
                      "current measurements, slot coverage, and key performance indicators. " +
                      "This endpoint provides a complete dashboard view of pipeline operations."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved pipeline overview",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = PipelineOverviewDTO.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Pipeline not found"
    )
    @ApiResponse(
        responseCode = "403",
        description = "Access denied - insufficient permissions"
    )
    public ResponseEntity<PipelineOverviewDTO> getOverview(
            @Parameter(description = "Pipeline ID", required = true)
            @PathVariable Long pipelineId,
            
            @Parameter(description = "Reference date for data aggregation (defaults to today)", 
                       example = "2026-02-07")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate referenceDate) {
        
        LocalDate date = referenceDate != null ? referenceDate : LocalDate.now();
        log.info("GET /flow/intelligence/pipeline/{}/overview?referenceDate={}", pipelineId, date);
        
        PipelineOverviewDTO overview = intelligenceService.getOverview(pipelineId, date);
        return ResponseEntity.ok(overview);
    }
    
    /**
     * Get detailed slot-by-slot coverage for a specific date
     * Shows all 12 reading slots with their status, recordings, and validations
     */
    @GetMapping("/{pipelineId}/slot-coverage")
    //@PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    @Operation(
        summary = "Get Slot Coverage",
        description = "Retrieve detailed status of all 12 reading slots for a specific date. " +
                      "Each slot includes recording status, validation state, measurements, " +
                      "overdue indicators, and operator information. Perfect for daily monitoring."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved slot coverage",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = SlotStatusDTO.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Pipeline not found"
    )
    @ApiResponse(
        responseCode = "403",
        description = "Access denied - insufficient permissions"
    )
    public ResponseEntity<List<SlotStatusDTO>> getSlotCoverage(
            @Parameter(description = "Pipeline ID", required = true)
            @PathVariable Long pipelineId,
            
            @Parameter(description = "Date for slot coverage (defaults to today)", 
                       example = "2026-02-07")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate date) {
        
        LocalDate targetDate = date != null ? date : LocalDate.now();
        log.info("GET /flow/intelligence/pipeline/{}/slot-coverage?date={}", pipelineId, targetDate);
        
        List<SlotStatusDTO> slotCoverage = intelligenceService.getSlotCoverage(pipelineId, targetDate);
        return ResponseEntity.ok(slotCoverage);
    }
    
    /**
     * Get time-series data for historical readings analysis
     * Supports trend analysis, pattern detection, and statistical summaries
     */
    @GetMapping("/{pipelineId}/readings-timeseries")
    //@PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    @Operation(
        summary = "Get Readings Time Series",
        description = "Retrieve historical time-series data for a specific measurement type. " +
                      "Includes data points ordered chronologically with statistical analysis " +
                      "(min, max, avg, median, std deviation). Useful for trend analysis and reporting."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved time-series data",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ReadingsTimeSeriesDTO.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Pipeline not found"
    )
    @ApiResponse(
        responseCode = "403",
        description = "Access denied - insufficient permissions"
    )
    public ResponseEntity<ReadingsTimeSeriesDTO> getReadingsTimeSeries(
            @Parameter(description = "Pipeline ID", required = true)
            @PathVariable Long pipelineId,
            
            @Parameter(description = "Start date of time range (defaults to 7 days ago)", 
                       example = "2026-01-31")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate startDate,
            
            @Parameter(description = "End date of time range (defaults to today)", 
                       example = "2026-02-07")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate endDate,
            
            @Parameter(
                description = "Type of measurement to retrieve",
                schema = @Schema(
                    type = "string",
                    allowableValues = {"PRESSURE", "TEMPERATURE", "FLOW_RATE", "CONTAINED_VOLUME"},
                    defaultValue = "PRESSURE"
                )
            )
            @RequestParam(required = false, defaultValue = "PRESSURE") 
            String measurementType) {
        
        LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(7);
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        
        log.info("GET /flow/intelligence/pipeline/{}/readings-timeseries?startDate={}&endDate={}&measurementType={}",
            pipelineId, start, end, measurementType);
        
        ReadingsTimeSeriesDTO timeSeries = intelligenceService.getReadingsTimeSeries(
            pipelineId, start, end, measurementType);
        
        return ResponseEntity.ok(timeSeries);
    }
}
