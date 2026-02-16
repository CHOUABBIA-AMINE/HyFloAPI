/**
 *
 *	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowMonitoringController
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-16-2026 - Enhanced with comprehensive OpenAPI documentation
 * 	@UpdatedOn	: 02-16-2026 - Simplified to single-line descriptions
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: REST controller for flow reading monitoring and analytics.
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

@Tag(
    name = "Flow Monitoring",
    description = "Operational monitoring and analytics for flow readings"
)
@SecurityRequirement(name = "bearer-auth")
@RestController
@RequestMapping("/flow/intelligence/monitoring")
@RequiredArgsConstructor
@Slf4j
public class FlowMonitoringController {

    private final FlowMonitoringService monitoringService;

    @Operation(
        summary = "Get pending validations",
        description = "Retrieves all readings in SUBMITTED status awaiting validation for a specific structure. Useful for supervisors to monitor their validation workload."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pending validations"),
        @ApiResponse(responseCode = "403", description = "Access forbidden"),
        @ApiResponse(responseCode = "404", description = "Structure not found")
    })
    @GetMapping("/pending-validations")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getPendingValidations(
            @Parameter(description = "Structure ID", required = true, example = "12")
            @RequestParam Long structureId,
            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/intelligence/monitoring/pending-validations - Structure: {}", structureId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("recordedAt").ascending());
        return ResponseEntity.ok(monitoringService.findPendingValidationsByStructure(structureId, pageable));
    }

    @Operation(
        summary = "Get overdue readings",
        description = "Identifies readings that are past their slot deadline and not yet validated. Critical for tracking SLA compliance and operational delays."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved overdue readings"),
        @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/overdue-readings")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<Page<FlowReadingDTO>> getOverdueReadings(
            @Parameter(description = "Structure ID", required = true, example = "12")
            @RequestParam Long structureId,
            @Parameter(description = "Reference date (defaults to today)", example = "2026-02-16")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate,
            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        LocalDate targetDate = asOfDate != null ? asOfDate : LocalDate.now();
        log.info("GET /flow/intelligence/monitoring/overdue-readings - Structure: {}, Date: {}", structureId, targetDate);
        Pageable pageable = PageRequest.of(page, size, Sort.by("readingDate").descending());
        return ResponseEntity.ok(monitoringService.findOverdueReadingsByStructure(structureId, targetDate, pageable));
    }

    @Operation(
        summary = "Get daily completion statistics",
        description = "Returns aggregated KPI statistics for reading completion grouped by date. Includes submission rates, validation rates, and completion percentages for management dashboards."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved daily statistics"),
        @ApiResponse(responseCode = "400", description = "Invalid date range (max 90 days)"),
        @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/daily-statistics")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<DailyCompletionStatisticsDTO>> getDailyStatistics(
            @Parameter(description = "Structure ID", required = true, example = "12")
            @RequestParam Long structureId,
            @Parameter(description = "Start date (inclusive)", required = true, example = "2026-02-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (inclusive, max 90 days from start)", required = true, example = "2026-02-16")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/intelligence/monitoring/daily-statistics - Structure: {}, Range: {} to {}", structureId, startDate, endDate);
        return ResponseEntity.ok(monitoringService.getDailyCompletionStatistics(structureId, startDate, endDate));
    }

    @Operation(
        summary = "Get validator workload distribution",
        description = "Analyzes validation workload across validators showing total validations, approvals, rejections, and performance metrics. Useful for workload balancing and capacity planning."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved validator workload"),
        @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/validator-workload")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<ValidatorWorkloadDTO>> getValidatorWorkload(
            @Parameter(description = "Structure ID", required = true, example = "12")
            @RequestParam Long structureId,
            @Parameter(description = "Start date", required = true, example = "2026-02-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2026-02-16")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/intelligence/monitoring/validator-workload - Structure: {}, Range: {} to {}", structureId, startDate, endDate);
        return ResponseEntity.ok(monitoringService.getValidatorWorkloadDistribution(structureId, startDate, endDate));
    }

    @Operation(
        summary = "Get submission trends",
        description = "Analyzes submission patterns over time with configurable aggregation (HOUR, DAY, WEEK, MONTH). Useful for identifying peak hours, shift performance, and operational patterns."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved submission trends"),
        @ApiResponse(responseCode = "400", description = "Invalid groupBy parameter"),
        @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/submission-trends")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<SubmissionTrendDTO>> getSubmissionTrends(
            @Parameter(description = "Structure ID", required = true, example = "12")
            @RequestParam Long structureId,
            @Parameter(description = "Start date", required = true, example = "2026-02-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2026-02-16")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Aggregation level: HOUR, DAY, WEEK, or MONTH", example = "DAY")
            @RequestParam(defaultValue = "DAY") String groupBy) {
        log.info("GET /flow/intelligence/monitoring/submission-trends - Structure: {}, Range: {} to {}, GroupBy: {}", structureId, startDate, endDate, groupBy);
        return ResponseEntity.ok(monitoringService.getSubmissionTrends(structureId, startDate, endDate, groupBy));
    }

    @Operation(
        summary = "Get pipeline coverage",
        description = "Calculates reading coverage percentage for each pipeline over a date range. Shows which pipelines consistently submit readings vs those with gaps. Critical for data quality monitoring."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pipeline coverage"),
        @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/pipeline-coverage")
    @PreAuthorize("hasAuthority('FLOW_READING:READ')")
    public ResponseEntity<List<PipelineCoverageDetailDTO>> getPipelineCoverageByDateRange(
            @Parameter(description = "Structure ID", required = true, example = "12")
            @RequestParam Long structureId,
            @Parameter(description = "Start date", required = true, example = "2026-02-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2026-02-16")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/intelligence/monitoring/pipeline-coverage - Structure: {}, Range: {} to {}", structureId, startDate, endDate);
        return ResponseEntity.ok(monitoringService.getPipelineCoverageByDateRange(structureId, startDate, endDate));
    }
}
