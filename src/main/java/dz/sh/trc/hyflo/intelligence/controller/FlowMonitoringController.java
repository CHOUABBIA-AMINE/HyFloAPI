/**
 *
 *	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowMonitoringController
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 03-29-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: REST controller for flow reading monitoring and analytics.
 *
 **/

package dz.sh.trc.hyflo.intelligence.controller;

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

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.intelligence.dto.analytics.DailyCompletionStatisticsDTO;
import dz.sh.trc.hyflo.intelligence.dto.analytics.PipelineCoverageDetailDTO;
import dz.sh.trc.hyflo.intelligence.dto.analytics.SubmissionTrendDTO;
import dz.sh.trc.hyflo.intelligence.dto.analytics.ValidatorWorkloadDTO;
import dz.sh.trc.hyflo.intelligence.service.FlowMonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
        description = "Retrieves all readings in SUBMITTED status awaiting validation for a specific structure."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pending validations"),
        @ApiResponse(responseCode = "403", description = "Access forbidden"),
        @ApiResponse(responseCode = "404", description = "Structure not found")
    })
    @GetMapping("/pending-validations")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<Page<FlowReadingReadDTO>> getPendingValidations(
            @Parameter(description = "Structure ID", required = true, example = "12")
            @RequestParam Long structureId,
            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/intelligence/monitoring/pending-validations - Structure: {}", structureId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("recordedAt").ascending());
        return ResponseEntity.ok(monitoringService.findPendingValidations(structureId, pageable));
    }

    @Operation(
        summary = "Get overdue readings",
        description = "Identifies readings past their slot deadline and not yet validated."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved overdue readings"),
        @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/overdue-readings")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<Page<FlowReadingReadDTO>> getOverdueReadings(
            @Parameter(description = "Structure ID", required = true, example = "12")
            @RequestParam Long structureId,
            @Parameter(description = "Reference date (defaults to today)", example = "2026-02-16")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate,
            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        LocalDate targetDate = asOfDate != null ? asOfDate : LocalDate.now();
        log.info("GET /flow/intelligence/monitoring/overdue-readings - Structure: {}, Date: {}",
                structureId, targetDate);
        Pageable pageable = PageRequest.of(page, size, Sort.by("readingDate").descending());
        return ResponseEntity.ok(monitoringService.findOverdueReadings(structureId, targetDate, pageable));
    }

    @Operation(
        summary = "Get daily completion statistics",
        description = "Returns aggregated KPI statistics for reading completion grouped by date."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved daily statistics"),
        @ApiResponse(responseCode = "400", description = "Invalid date range (max 90 days)"),
        @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/daily-statistics")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<List<DailyCompletionStatisticsDTO>> getDailyStatistics(
            @Parameter(description = "Pipeline ID", required = true, example = "5")
            @RequestParam Long pipelineId,
            @Parameter(description = "Start date", required = true, example = "2026-02-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2026-02-28")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/intelligence/monitoring/daily-statistics - Pipeline: {}, {} to {}",
                pipelineId, startDate, endDate);
        return ResponseEntity.ok(
                monitoringService.getDailyCompletionStatistics(pipelineId, startDate, endDate));
    }

    @Operation(
        summary = "Get validator workload",
        description = "Returns distribution of pending validation work across all validators."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved validator workload"),
        @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/validator-workload")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<List<ValidatorWorkloadDTO>> getValidatorWorkload() {
        log.info("GET /flow/intelligence/monitoring/validator-workload");
        return ResponseEntity.ok(monitoringService.getValidatorWorkload());
    }

    @Operation(
        summary = "Get submission trend",
        description = "Returns daily submission counts over a date range for trend analysis."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved submission trend"),
        @ApiResponse(responseCode = "400", description = "Invalid date range (max 90 days)"),
        @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/submission-trend")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<List<SubmissionTrendDTO>> getSubmissionTrend(
            @Parameter(description = "Start date", required = true, example = "2026-02-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2026-02-28")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/intelligence/monitoring/submission-trend - {} to {}", startDate, endDate);
        return ResponseEntity.ok(monitoringService.getSubmissionTrend(startDate, endDate));
    }

    @Operation(
        summary = "Get pipeline coverage detail",
        description = "Returns per-slot coverage detail for a pipeline over a date range."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pipeline coverage"),
        @ApiResponse(responseCode = "400", description = "Invalid date range (max 90 days)"),
        @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping("/pipeline-coverage")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<List<PipelineCoverageDetailDTO>> getPipelineCoverage(
            @Parameter(description = "Pipeline ID", required = true, example = "5")
            @RequestParam Long pipelineId,
            @Parameter(description = "Start date", required = true, example = "2026-02-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2026-02-28")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/intelligence/monitoring/pipeline-coverage - Pipeline: {}, {} to {}",
                pipelineId, startDate, endDate);
        return ResponseEntity.ok(
                monitoringService.getPipelineCoverageDetail(pipelineId, startDate, endDate));
    }
}
