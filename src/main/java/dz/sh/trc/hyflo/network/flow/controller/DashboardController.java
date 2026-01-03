/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DashboardController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
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

@RestController
@RequestMapping("/network/flow/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        try {
            DashboardSummaryDTO summary = dashboardService.getDashboardSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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
