/**
 *
 *	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowMonitoringController
 * 	@CreatedOn	: 02-10-2026
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
@RestController
@RequestMapping("/flow/intelligence/monitoring")
@Tag(name = "Flow Monitoring", description = "Operational monitoring and analytics for flow readings")
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
    @GetMapping("/pending-validations")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    @Operation(
        summary = "Get pending validations by structure",
        description = "Returns readings awaiting validation filtered by structure"
    )
    public ResponseEntity<Page<FlowReadingDTO>> getPendingValidations(
            @RequestParam Long structureId,
            @RequestParam(defaultValue = "0") int page,
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
    @GetMapping("/overdue-readings")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    @Operation(
        summary = "Get overdue readings by structure",
        description = "Returns readings past their deadline that are not yet validated"
    )
    public ResponseEntity<Page<FlowReadingDTO>> getOverdueReadings(
            @RequestParam Long structureId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate,
            @RequestParam(defaultValue = "0") int page,
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
    @GetMapping("/daily-statistics")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    @Operation(
        summary = "Get daily completion statistics",
        description = "Returns aggregated statistics for reading completion by date"
    )
    public ResponseEntity<List<DailyCompletionStatisticsDTO>> getDailyStatistics(
            @RequestParam Long structureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
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
    @GetMapping("/validator-workload")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    @Operation(
        summary = "Get validator workload",
        description = "Returns validation distribution across validators"
    )
    public ResponseEntity<List<ValidatorWorkloadDTO>> getValidatorWorkload(
            @RequestParam Long structureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
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
    @GetMapping("/submission-trends")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    @Operation(
        summary = "Get reading submission trends",
        description = "Returns time-series data showing submission patterns"
    )
    public ResponseEntity<List<SubmissionTrendDTO>> getSubmissionTrends(
            @RequestParam Long structureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
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
    @GetMapping("/pipeline-coverage")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    @Operation(
        summary = "Get pipeline coverage by date range",
        description = "Returns coverage percentage for each pipeline over a date range"
    )
    public ResponseEntity<List<PipelineCoverageDetailDTO>> getPipelineCoverageByDateRange(
            @RequestParam Long structureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/monitoring/pipeline-coverage - Structure: {}, Range: {} to {}", 
                 structureId, startDate, endDate);
        return ResponseEntity.ok(monitoringService.getPipelineCoverageByDateRange(
                structureId, startDate, endDate));
    }
}
