/**
 *
 *	@Author		: MEDJERAB Abir, CHOUABBIA Amine
 *
 * 	@Name		: PipelineIntelligenceController
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 02-14-2026
 * 	@UpdatedOn	: 02-16-2026 - Enhanced with comprehensive OpenAPI documentation
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST API for Pipeline Operational Intelligence
 * Provides real-time monitoring, KPIs, and analytics for pipeline operations
 * Extended to support comprehensive PipelineInfoPage functionality
 */
@Tag(
    name = "Pipeline Intelligence",
    description = "Operational intelligence and analytics for individual pipelines. Provides real-time dashboards, historical trends, timeline views, and comprehensive operational insights."
)
@SecurityRequirement(name = "bearer-auth")
@RestController
@RequestMapping("/flow/intelligence/pipeline")
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceController {
    
    private final PipelineIntelligenceService intelligenceService;
    
    /**
     * Get comprehensive pipeline information for detail page
     * Aggregates static infrastructure with optional dynamic health data
     */
    @Operation(
        summary = "Get pipeline detailed information",
        description = """Retrieves comprehensive pipeline information including static infrastructure, linked entities, and optional health status.
        
        **Information Included:**
        - Pipeline specifications (name, code, length, diameter, capacity)
        - Infrastructure details (stations, valves, measurement points)
        - Optional: Current health metrics (pressure, flow rate, temperature)
        - Optional: Linked entities (sensors, equipment, maintenance records)
        
        **Use Cases:**
        - Pipeline detail page population
        - Infrastructure inventory
        - Maintenance planning context
        - Operations reference documentation
        
        **Performance:**
        - Static data: 5-minute cache
        - Health data (if included): 30-second cache
        - Entities data: 10-minute cache
        
        **Optimization:**
        - Set includeHealth=false for faster response when health not needed
        - Set includeEntities=false to reduce payload size
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved pipeline information",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PipelineInfoDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pipeline not found with provided ID",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/{pipelineId}/info")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<PipelineInfoDTO> getPipelineInfo(
            @Parameter(
                description = "Unique pipeline identifier",
                required = true,
                example = "101"
            )
            @PathVariable Long pipelineId,
            
            @Parameter(
                description = "Include current health status (may increase response time by 50-100ms)",
                example = "true"
            )
            @RequestParam(required = false, defaultValue = "true") Boolean includeHealth,
            
            @Parameter(
                description = "Include linked entities like stations, valves, and sensors",
                example = "false"
            )
            @RequestParam(required = false, defaultValue = "false") Boolean includeEntities) {
        
        log.info("GET /flow/intelligence/pipeline/{}/info?includeHealth={}&includeEntities={}", 
                 pipelineId, includeHealth, includeEntities);
        
        PipelineInfoDTO info = intelligenceService.getPipelineInfo(pipelineId, includeHealth, includeEntities);
        return ResponseEntity.ok(info);
    }
    
    /**
     * Get real-time operational dashboard data
     * Optimized for frequent updates with short cache TTL
     */
    @Operation(
        summary = "Get real-time pipeline dashboard",
        description = """Retrieves real-time operational dashboard metrics optimized for frequent polling.
        
        **Dashboard Components:**
        - Current readings (pressure, temperature, flow rate)
        - Health indicators (operational status, alerts count)
        - Quick statistics (today's readings, completion rate)
        - Recent alerts (last 5 critical/warning alerts)
        - Trend indicators (increasing/decreasing/stable)
        
        **Use Cases:**
        - Control room dashboards
        - Mobile operator apps
        - Real-time monitoring displays
        - Operations center screens
        
        **Update Strategy:**
        - Polling interval: 30 seconds recommended
        - Cache TTL: 30 seconds
        - WebSocket upgrade available for push notifications
        
        **Performance:**
        - Response time: < 50ms (cached)
        - Payload size: ~2KB
        - Concurrent requests: 100+ supported
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved dashboard data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PipelineDynamicDashboardDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pipeline not found",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/{pipelineId}/dashboard")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<PipelineDynamicDashboardDTO> getDashboard(
            @Parameter(
                description = "Pipeline ID for dashboard data",
                required = true,
                example = "101"
            )
            @PathVariable Long pipelineId) {
        
        log.info("GET /flow/intelligence/pipeline/{}/dashboard", pipelineId);
        
        PipelineDynamicDashboardDTO dashboard = intelligenceService.getDashboard(pipelineId);
        return ResponseEntity.ok(dashboard);
    }
    
    /**
     * Get unified timeline of alerts and events
     * Merges alerts and events chronologically with pagination
     */
    @Operation(
        summary = "Get pipeline timeline",
        description = """Retrieves unified chronological timeline merging alerts and operational events.
        
        **Timeline Items:**
        - System alerts (CRITICAL, WARNING, INFO)
        - Operational events (maintenance, calibration, shutdowns)
        - Reading submissions and validations
        - Configuration changes
        
        **Filtering:**
        - Date/time range (default: last 7 days)
        - Severity level (CRITICAL, WARNING, INFO, NORMAL)
        - Type (ALERT, EVENT, OPERATION)
        
        **Distribution Statistics:**
        - Count by severity
        - Count by type
        - Count by day of week
        - Useful for dashboard visualizations
        
        **Use Cases:**
        - Incident investigation
        - Operations audit trail
        - Pattern analysis
        - Compliance reporting
        
        **Pagination:**
        - Default: 20 items per page
        - Maximum: 100 items per page
        - Sorted chronologically (newest first)
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved timeline",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PipelineTimelineDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pipeline not found",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/{pipelineId}/timeline")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<PipelineTimelineDTO> getTimeline(
            @Parameter(
                description = "Pipeline ID for timeline",
                required = true,
                example = "101"
            )
            @PathVariable Long pipelineId,
            
            @Parameter(
                description = "Start date/time for timeline (defaults to 7 days ago)",
                example = "2026-02-09T00:00:00"
            )
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            
            @Parameter(
                description = "End date/time for timeline (defaults to now)",
                example = "2026-02-16T23:59:59"
            )
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            
            @Parameter(
                description = "Filter by severity level",
                schema = @Schema(allowableValues = {"CRITICAL", "WARNING", "INFO", "NORMAL"})
            )
            @RequestParam(required = false) String severity,
            
            @Parameter(
                description = "Filter by item type",
                schema = @Schema(allowableValues = {"ALERT", "EVENT", "OPERATION"})
            )
            @RequestParam(required = false) String type,
            
            @Parameter(
                description = "Page number (0-indexed)",
                example = "0"
            )
            @RequestParam(required = false, defaultValue = "0") Integer page,
            
            @Parameter(
                description = "Items per page (max 100)",
                example = "20"
            )
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        
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
    @Operation(
        summary = "Get pipeline operational overview",
        description = """Retrieves comprehensive operational overview providing complete dashboard view.
        
        **Overview Components:**
        - Asset specifications (capacity, dimensions, product)
        - Current measurements (latest readings)
        - Slot coverage for reference date (all 12 slots)
        - Key performance indicators:
          * Today's completion rate
          * Week's average completion
          * Month's coverage percentage
          * Outstanding validations
        
        **Use Cases:**
        - Pipeline overview page
        - Operations summary reports
        - Management dashboards
        - Daily briefing screens
        
        **Reference Date:**
        - Defaults to current date
        - Can specify historical date for reports
        - Affects slot coverage and daily metrics
        
        **Caching:**
        - Historical dates (not today): 1 hour cache
        - Current date: 5 minute cache
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved pipeline overview",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PipelineOverviewDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pipeline not found",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/{pipelineId}/overview")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<PipelineOverviewDTO> getOverview(
            @Parameter(
                description = "Pipeline ID for overview",
                required = true,
                example = "101"
            )
            @PathVariable Long pipelineId,
            
            @Parameter(
                description = "Reference date for metrics (defaults to today)",
                example = "2026-02-16"
            )
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referenceDate) {
        
        LocalDate date = referenceDate != null ? referenceDate : LocalDate.now();
        log.info("GET /flow/intelligence/pipeline/{}/overview?referenceDate={}", pipelineId, date);
        
        PipelineOverviewDTO overview = intelligenceService.getOverview(pipelineId, date);
        return ResponseEntity.ok(overview);
    }
    
    /**
     * Get detailed slot-by-slot coverage for a specific date
     * Shows all 12 reading slots with their status, recordings, and validations
     */
    @Operation(
        summary = "Get slot coverage",
        description = """Retrieves detailed status of all 12 reading slots for a specific date.
        
        **Slot Information (per slot):**
        - Slot number and time range
        - Recording status (RECORDED, PENDING, MISSING, OVERDUE)
        - Validation status (VALIDATED, SUBMITTED, DRAFT)
        - Actual measurements (if recorded)
        - Operator information (recorder, validator)
        - Timestamps (recorded at, validated at)
        - Overdue indicator (past deadline)
        
        **Use Cases:**
        - Daily monitoring checklist
        - Operator handover reports
        - Completion tracking
        - Shift performance review
        
        **Status Indicators:**
        - RECORDED + VALIDATED: ✓ Complete
        - RECORDED + SUBMITTED: ⏳ Awaiting validation
        - RECORDED + DRAFT: ✎ Draft only
        - PENDING: ○ Not yet recorded
        - OVERDUE: ⚠ Past deadline
        
        **Date Default:**
        - If not specified, uses current date
        - Useful for today's monitoring
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved slot coverage (always returns 12 slots)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SlotStatusDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pipeline not found",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/{pipelineId}/slot-coverage")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<List<SlotStatusDTO>> getSlotCoverage(
            @Parameter(
                description = "Pipeline ID for slot coverage",
                required = true,
                example = "101"
            )
            @PathVariable Long pipelineId,
            
            @Parameter(
                description = "Date for slot coverage (defaults to today)",
                example = "2026-02-16"
            )
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        LocalDate targetDate = date != null ? date : LocalDate.now();
        log.info("GET /flow/intelligence/pipeline/{}/slot-coverage?date={}", pipelineId, targetDate);
        
        List<SlotStatusDTO> slotCoverage = intelligenceService.getSlotCoverage(pipelineId, targetDate);
        return ResponseEntity.ok(slotCoverage);
    }
    
    /**
     * Get time-series data for historical readings analysis
     * Supports trend analysis, pattern detection, and statistical summaries
     */
    @Operation(
        summary = "Get readings time series",
        description = """Retrieves historical time-series data for specific measurement type with statistical analysis.
        
        **Measurement Types:**
        - PRESSURE: Pressure readings (bar, psi)
        - TEMPERATURE: Temperature readings (°C, °F)
        - FLOW_RATE: Flow rate readings (m³/h, bbl/day)
        - CONTAINED_VOLUME: Volume readings (m³, bbl)
        
        **Time Series Data:**
        - Chronologically ordered data points
        - Timestamp and value pairs
        - Validation status per point
        - Gap indicators for missing data
        
        **Statistical Summary:**
        - Minimum value
        - Maximum value
        - Average (mean)
        - Median
        - Standard deviation
        - Count of readings
        
        **Use Cases:**
        - Trend analysis and forecasting
        - Anomaly detection
        - Performance monitoring
        - Regulatory reporting
        - Pattern recognition
        
        **Date Range:**
        - Default: Last 7 days
        - Maximum: 365 days
        - Recommended: 30 days or less for performance
        
        **Export:**
        - Data suitable for CSV export
        - Compatible with charting libraries
        - Statistical summary for Excel pivot tables
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved time-series data with statistics",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReadingsTimeSeriesDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid measurement type or date range",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Pipeline not found",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/{pipelineId}/readings-timeseries")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
    public ResponseEntity<ReadingsTimeSeriesDTO> getReadingsTimeSeries(
            @Parameter(
                description = "Pipeline ID for time-series data",
                required = true,
                example = "101"
            )
            @PathVariable Long pipelineId,
            
            @Parameter(
                description = "Start date of time range (defaults to 7 days ago)",
                example = "2026-02-09"
            )
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(
                description = "End date of time range (defaults to today)",
                example = "2026-02-16"
            )
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            
            @Parameter(
                description = "Type of measurement to retrieve",
                schema = @Schema(
                    allowableValues = {"PRESSURE", "TEMPERATURE", "FLOW_RATE", "CONTAINED_VOLUME"}
                ),
                example = "PRESSURE"
            )
            @RequestParam(required = false, defaultValue = "PRESSURE") String measurementType) {
        
        LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(7);
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        
        log.info("GET /flow/intelligence/pipeline/{}/readings-timeseries?startDate={}&endDate={}&measurementType={}",
            pipelineId, start, end, measurementType);
        
        ReadingsTimeSeriesDTO timeSeries = intelligenceService.getReadingsTimeSeries(
            pipelineId, start, end, measurementType);
        
        return ResponseEntity.ok(timeSeries);
    }
}
