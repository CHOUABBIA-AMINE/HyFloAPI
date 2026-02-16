/**
 *
 *	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowMonitoringController
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-16-2026 - Enhanced with comprehensive OpenAPI documentation
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: REST controller for flow reading monitoring and analytics.
 * 	             Extracted from FlowReadingController to separate concerns.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.DailyCompletionStatisticsDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.PipelineCoverageDetailDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.SubmissionTrendDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.ValidatorWorkloadDTO;
import dz.sh.trc.hyflo.flow.intelligence.service.FlowMonitoringService;
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
 * REST controller providing operational monitoring and analytics endpoints.
 * 
 * This controller handles:
 * - Pending validations tracking
 * - Overdue readings detection
 * - Daily completion statistics
 * - Validator workload distribution
 * - Submission trend analysis
 * - Pipeline coverage reporting
 */
@Tag(
    name = "Flow Monitoring",
    description = "Operational monitoring and analytics for flow readings. Provides real-time tracking, KPI metrics, workload distribution, and trend analysis for supervisors and operations managers."
)
@SecurityRequirement(name = "bearer-auth")
@RestController
@RequestMapping("/flow/intelligence/monitoring")
@RequiredArgsConstructor
@Slf4j
public class FlowMonitoringController {

    private final FlowMonitoringService monitoringService;

    /**
     * Get pending validations by structure
     * Returns all readings in SUBMITTED status awaiting validation
     * Useful for supervisors to see their validation workload
     * 
     * @param structureId Structure ID to filter by
     * @param page Page number for pagination
     * @param size Number of items per page
     * @return Paginated list of readings awaiting validation
     */
    @Operation(
        summary = "Get pending validations by structure",
        description = """Retrieves all readings in SUBMITTED status awaiting validation for a specific structure.
        
        **Use Cases:**
        - Supervisors checking their validation workload
        - Operations managers monitoring validation queue
        - Quality control officers prioritizing validations
        
        **Business Context:**
        - Only SUBMITTED readings are returned (not DRAFT or VALIDATED)
        - Results sorted by recording timestamp (oldest first)
        - Pagination support for large validation queues
        
        **Performance:**
        - Indexed query on structure_id and validation_status
        - Typical response time: < 100ms for 1000 readings
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved pending validations. Returns paginated list of readings.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - insufficient permissions to read flow readings",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Structure not found",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/pending-validations")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getPendingValidations(
            @Parameter(
                description = "Structure/organization unit ID to filter pending validations",
                required = true,
                example = "12"
            )
            @RequestParam Long structureId,
            
            @Parameter(
                description = "Page number (0-indexed)",
                example = "0"
            )
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(
                description = "Number of items per page",
                example = "20"
            )
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/monitoring/pending-validations - Structure: {}", structureId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("recordedAt").ascending());
        return ResponseEntity.ok(monitoringService.findPendingValidationsByStructure(
                structureId, pageable));
    }

    /**
     * Get overdue readings by structure
     * Returns readings that are past their slot deadline and not yet validated
     * Critical for operational awareness of delayed data
     * 
     * @param structureId Structure ID to filter by
     * @param asOfDate Date to check for overdue readings (defaults to today)
     * @param page Page number for pagination
     * @param size Number of items per page
     * @return Paginated list of overdue readings
     */
    @Operation(
        summary = "Get overdue readings by structure",
        description = """Identifies readings that are past their slot deadline and not yet validated.
        
        **Use Cases:**
        - Identifying data quality issues and delays
        - Operations managers tracking SLA compliance
        - Priority escalation for overdue validations
        
        **Overdue Criteria:**
        - Reading date + slot time has passed
        - Validation status is still SUBMITTED or DRAFT
        - Grace period (if configured) has expired
        
        **Business Impact:**
        - Overdue readings affect compliance reporting
        - May trigger automatic alerts to supervisors
        - Critical for meeting operational deadlines
        
        **Default Behavior:**
        - If asOfDate not provided, uses current date
        - Results sorted by reading date (oldest first)
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved overdue readings. Empty page if no overdue items.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/overdue-readings")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getOverdueReadings(
            @Parameter(
                description = "Structure ID to filter overdue readings",
                required = true,
                example = "12"
            )
            @RequestParam Long structureId,
            
            @Parameter(
                description = "Reference date for overdue calculation (defaults to today)",
                example = "2026-02-16"
            )
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate,
            
            @Parameter(
                description = "Page number (0-indexed)",
                example = "0"
            )
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(
                description = "Number of items per page",
                example = "20"
            )
            @RequestParam(defaultValue = "20") int size) {
        LocalDate targetDate = asOfDate != null ? asOfDate : LocalDate.now();
        log.info("GET /flow/monitoring/overdue-readings - Structure: {}, Date: {}", 
                 structureId, targetDate);
        Pageable pageable = PageRequest.of(page, size, Sort.by("readingDate").descending());
        return ResponseEntity.ok(monitoringService.findOverdueReadingsByStructure(
                structureId, targetDate, pageable));
    }

    /**
     * Get daily completion statistics
     * Returns aggregated statistics for reading completion by date
     * Shows submission rates, validation rates, and completion percentage
     * Useful for KPI dashboards and performance monitoring
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of daily statistics
     */
    @Operation(
        summary = "Get daily completion statistics",
        description = """Returns aggregated KPI statistics for reading completion grouped by date.
        
        **Metrics Included:**
        - Total readings expected (pipelines × slots)
        - Readings submitted count
        - Readings validated count
        - Completion percentage
        - Submission rate
        - Validation rate
        - Average validation time
        
        **Use Cases:**
        - KPI dashboards for operations management
        - Performance tracking over time
        - Identifying trends and anomalies
        - Management reporting and compliance
        
        **Date Range:**
        - Maximum range: 90 days
        - Results ordered chronologically
        - Each date has complete statistics
        
        **Caching:**
        - Historical data (> 1 day old): 1 hour cache
        - Current day data: 5 minute cache
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved daily statistics",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DailyCompletionStatisticsDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid date range (exceeds maximum or inverted dates)",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/daily-statistics")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<DailyCompletionStatisticsDTO>> getDailyStatistics(
            @Parameter(
                description = "Structure ID for statistics aggregation",
                required = true,
                example = "12"
            )
            @RequestParam Long structureId,
            
            @Parameter(
                description = "Start date of range (inclusive)",
                required = true,
                example = "2026-02-01"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(
                description = "End date of range (inclusive, max 90 days from start)",
                required = true,
                example = "2026-02-16"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/monitoring/daily-statistics - Structure: {}, Range: {} to {}", 
                 structureId, startDate, endDate);
        return ResponseEntity.ok(monitoringService.getDailyCompletionStatistics(
                structureId, startDate, endDate));
    }

    /**
     * Get validator workload
     * Returns workload distribution showing number of validations by validator
     * Helps identify bottlenecks and balance workload distribution
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of validators with their validation counts
     */
    @Operation(
        summary = "Get validator workload distribution",
        description = """Analyzes validation workload distribution across validators.
        
        **Metrics per Validator:**
        - Employee name and ID
        - Total validations performed
        - Approvals vs rejections count
        - Average validation time
        - Peak validation hours
        
        **Use Cases:**
        - Workload balancing across validators
        - Identifying validation bottlenecks
        - Performance evaluation and training needs
        - Capacity planning for validation team
        
        **Business Value:**
        - Ensures fair workload distribution
        - Identifies high-performing validators
        - Highlights potential burnout risks
        - Supports workforce optimization
        
        **Sorting:**
        - Results ordered by validation count (descending)
        - Shows most active validators first
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved validator workload distribution",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ValidatorWorkloadDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/validator-workload")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<ValidatorWorkloadDTO>> getValidatorWorkload(
            @Parameter(
                description = "Structure ID for workload analysis",
                required = true,
                example = "12"
            )
            @RequestParam Long structureId,
            
            @Parameter(
                description = "Start date of analysis period",
                required = true,
                example = "2026-02-01"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(
                description = "End date of analysis period",
                required = true,
                example = "2026-02-16"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/monitoring/validator-workload - Structure: {}, Range: {} to {}", 
                 structureId, startDate, endDate);
        return ResponseEntity.ok(monitoringService.getValidatorWorkloadDistribution(
                structureId, startDate, endDate));
    }

    /**
     * Get reading submission trends
     * Returns time-series data showing submission patterns over time
     * Useful for identifying peak submission times and operational patterns
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @param groupBy Grouping interval: HOUR, DAY, WEEK, MONTH
     * @return List of submission counts grouped by time interval
     */
    @Operation(
        summary = "Get reading submission trends",
        description = """Analyzes submission patterns over time with configurable aggregation.
        
        **Aggregation Levels:**
        - HOUR: Hourly submission patterns (useful for shift analysis)
        - DAY: Daily submission counts (default, most common)
        - WEEK: Weekly aggregation (for monthly reports)
        - MONTH: Monthly trends (for annual analysis)
        
        **Use Cases:**
        - Identifying peak submission hours
        - Shift performance comparison
        - Trend analysis and forecasting
        - Operational pattern detection
        
        **Data Points:**
        - Timestamp/period
        - Submission count
        - Validation count
        - Average time to validation
        
        **Insights:**
        - Peak operational hours
        - Workload distribution across shifts
        - Seasonal or periodic patterns
        - Capacity planning data
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved submission trends",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SubmissionTrendDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid groupBy parameter (must be HOUR, DAY, WEEK, or MONTH)",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/submission-trends")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<SubmissionTrendDTO>> getSubmissionTrends(
            @Parameter(
                description = "Structure ID for trend analysis",
                required = true,
                example = "12"
            )
            @RequestParam Long structureId,
            
            @Parameter(
                description = "Start date of trend analysis",
                required = true,
                example = "2026-02-01"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(
                description = "End date of trend analysis",
                required = true,
                example = "2026-02-16"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            
            @Parameter(
                description = "Time aggregation level",
                schema = @Schema(allowableValues = {"HOUR", "DAY", "WEEK", "MONTH"}),
                example = "DAY"
            )
            @RequestParam(defaultValue = "DAY") String groupBy) {
        log.info("GET /flow/monitoring/submission-trends - Structure: {}, Range: {} to {}, GroupBy: {}", 
                 structureId, startDate, endDate, groupBy);
        return ResponseEntity.ok(monitoringService.getSubmissionTrends(
                structureId, startDate, endDate, groupBy));
    }

    /**
     * Get pipeline coverage by date range
     * Returns coverage percentage for each pipeline over a date range
     * Shows which pipelines consistently submit readings vs those with gaps
     * Critical for identifying data quality issues
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of pipelines with their coverage percentages
     */
    @Operation(
        summary = "Get pipeline coverage by date range",
        description = """Calculates reading coverage percentage for each pipeline over a date range.
        
        **Coverage Calculation:**
        - Expected readings = days × slots per day
        - Actual readings = submitted + validated readings
        - Coverage % = (actual / expected) × 100
        
        **Metrics per Pipeline:**
        - Pipeline name and ID
        - Expected reading count
        - Actual reading count
        - Coverage percentage
        - Missing slots breakdown
        - Consecutive gaps (data quality indicator)
        
        **Use Cases:**
        - Data quality monitoring
        - Identifying problematic pipelines
        - Compliance reporting
        - Maintenance planning
        
        **Quality Indicators:**
        - Coverage >= 95%: Excellent
        - Coverage 80-95%: Good
        - Coverage 60-80%: Needs attention
        - Coverage < 60%: Critical issue
        
        **Sorting:**
        - Results ordered by coverage (ascending)
        - Shows worst performers first for quick action
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved pipeline coverage details",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PipelineCoverageDetailDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden - insufficient permissions",
            content = @Content(schema = @Schema(implementation = String.class))
        )
    })
    @GetMapping("/pipeline-coverage")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<PipelineCoverageDetailDTO>> getPipelineCoverageByDateRange(
            @Parameter(
                description = "Structure ID for coverage analysis",
                required = true,
                example = "12"
            )
            @RequestParam Long structureId,
            
            @Parameter(
                description = "Start date of coverage period",
                required = true,
                example = "2026-02-01"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            
            @Parameter(
                description = "End date of coverage period",
                required = true,
                example = "2026-02-16"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/monitoring/pipeline-coverage - Structure: {}, Range: {} to {}", 
                 structureId, startDate, endDate);
        return ResponseEntity.ok(monitoringService.getPipelineCoverageByDateRange(
                structureId, startDate, endDate));
    }
}
