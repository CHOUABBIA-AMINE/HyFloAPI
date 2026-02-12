/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineIntelligenceController
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineOverviewDTO;
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
 */
@RestController
@RequestMapping("/flow/intelligence/pipeline")
@Tag(name = "Pipeline Intelligence", description = "Operational intelligence and analytics for pipelines")
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceController {
    
    private final PipelineIntelligenceService intelligenceService;
    
    /**
     * Get comprehensive operational overview for a pipeline
     * Includes asset specs, KPIs, slot coverage, and current measurements
     */
    @GetMapping("/{pipelineId}/overview")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
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
        log.info("GET /api/v1/flow/intelligence/pipelines/{}/overview?referenceDate={}", pipelineId, date);
        
        PipelineOverviewDTO overview = intelligenceService.getOverview(pipelineId, date);
        return ResponseEntity.ok(overview);
    }
    
    /**
     * Get detailed slot-by-slot coverage for a specific date
     * Shows all 12 reading slots with their status, recordings, and validations
     */
    @GetMapping("/{pipelineId}/slot-coverage")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
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
        log.info("GET /api/v1/flow/intelligence/pipelines/{}/slot-coverage?date={}", pipelineId, targetDate);
        
        List<SlotStatusDTO> slotCoverage = intelligenceService.getSlotCoverage(pipelineId, targetDate);
        return ResponseEntity.ok(slotCoverage);
    }
    
    /**
     * Get time-series data for historical readings analysis
     * Supports trend analysis, pattern detection, and statistical summaries
     */
    @GetMapping("/{pipelineId}/readings-timeseries")
    @PreAuthorize("hasAnyAuthority('FLOW_READ', 'FLOW_WRITE', 'FLOW_VALIDATE')")
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
        
        log.info("GET /api/v1/flow/intelligence/pipelines/{}/readings-timeseries?startDate={}&endDate={}&measurementType={}",
            pipelineId, start, end, measurementType);
        
        ReadingsTimeSeriesDTO timeSeries = intelligenceService.getReadingsTimeSeries(
            pipelineId, start, end, measurementType);
        
        return ResponseEntity.ok(timeSeries);
    }
}
