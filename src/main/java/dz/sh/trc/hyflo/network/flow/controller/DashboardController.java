/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DashboardController
 *	@CreatedOn	: 12-27-2025
 *	@Updated	: 12-27-2025
 *
 *	@Type		: Controller
 *	@Layer		: Controller
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.network.flow.dto.DailyTrendDTO;
import dz.sh.trc.hyflo.network.flow.dto.DashboardSummaryDTO;
import dz.sh.trc.hyflo.network.flow.dto.PipelineStatusDTO;
import dz.sh.trc.hyflo.network.flow.service.DashboardService;

/**
 * Dashboard Controller
 * Provides REST API endpoints for flow monitoring dashboard
 * 
 * Base URL: /iaas/api/network/flow/dashboard
 * 
 * Endpoints:
 * - GET /summary - Get overall dashboard summary
 * - GET /pipelines - Get all pipeline statuses
 * - GET /pipeline/{id} - Get specific pipeline status
 * - GET /trends - Get daily trend data for charts
 */
@RestController
@RequestMapping("/network/flow/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * GET /network/flow/dashboard/summary
     * 
     * Get comprehensive dashboard summary including:
     * - Infrastructure counts
     * - Today's volume and pressure metrics
     * - Transported vs estimated comparison
     * - Status breakdown (on target, below, above, offline)
     * - Monthly summary
     * 
     * @return DashboardSummaryDTO with all dashboard metrics
     */
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        try {
            DashboardSummaryDTO summary = dashboardService.getDashboardSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /network/flow/dashboard/pipelines
     * GET /network/flow/dashboard/pipelines?date=2025-12-27
     * 
     * Get status for all pipelines for a specific date
     * Includes:
     * - Latest volume and pressure readings
     * - Daily accumulated volume
     * - Transported vs estimated comparison
     * - Status indicators
     * 
     * @param date Optional date parameter (format: yyyy-MM-dd). Defaults to today
     * @return List of PipelineStatusDTO
     */
    @GetMapping("/pipelines")
    public ResponseEntity<List<PipelineStatusDTO>> getPipelineStatuses(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate date) {
        try {
            LocalDate targetDate = date != null ? date : LocalDate.now();
            List<PipelineStatusDTO> statuses = dashboardService.getPipelineStatuses(targetDate);
            return ResponseEntity.ok(statuses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /network/flow/dashboard/pipeline/{id}
     * GET /network/flow/dashboard/pipeline/{id}?date=2025-12-27
     * 
     * Get detailed status for a specific pipeline
     * 
     * @param id Pipeline ID
     * @param date Optional date parameter (format: yyyy-MM-dd). Defaults to today
     * @return PipelineStatusDTO for the specified pipeline
     */
    @GetMapping("/pipeline/{id}")
    public ResponseEntity<PipelineStatusDTO> getPipelineStatus(
            @PathVariable Long id,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate date) {
        try {
            LocalDate targetDate = date != null ? date : LocalDate.now();
            PipelineStatusDTO status = dashboardService.getPipelineStatus(id, targetDate);
            return ResponseEntity.ok(status);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /network/flow/dashboard/trends
     * GET /network/flow/dashboard/trends?startDate=2025-12-20&endDate=2025-12-27
     * 
     * Get daily trend data for charts
     * Used for volume comparison charts and pressure trends
     * 
     * @param startDate Optional start date (format: yyyy-MM-dd). Defaults to 7 days ago
     * @param endDate Optional end date (format: yyyy-MM-dd). Defaults to today
     * @return List of DailyTrendDTO with daily metrics
     */
    @GetMapping("/trends")
    public ResponseEntity<List<DailyTrendDTO>> getDailyTrends(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate startDate,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate endDate) {
        try {
            LocalDate start = startDate != null ? startDate : LocalDate.now().minusDays(7);
            LocalDate end = endDate != null ? endDate : LocalDate.now();
            
            List<DailyTrendDTO> trends = dashboardService.getDailyTrend(start, end);
            return ResponseEntity.ok(trends);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
