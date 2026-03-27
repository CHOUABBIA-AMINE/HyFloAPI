/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAnomalyController
 *  @CreatedOn  : 03-25-2026
 *  @MovedOn    : 03-28-2026 — refactor: flow.core.controller → flow.intelligence.controller
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.flow.intelligence.dto.FlowAnomalyReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.service.FlowAnomalyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/intelligence/anomaly")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Intelligence — Anomalies",
     description = "Query anomalies detected by the intelligence engine")
@SecurityRequirement(name = "bearer-auth")
public class FlowAnomalyController {

    private final FlowAnomalyService anomalyService;

    @GetMapping
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "List all anomalies (paginated)")
    public ResponseEntity<Page<FlowAnomalyReadDTO>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(anomalyService.getAll(pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Search anomalies by keyword")
    public ResponseEntity<Page<FlowAnomalyReadDTO>> search(
            @RequestParam(required = false) String query,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(anomalyService.searchByQuery(query, pageable));
    }

    @GetMapping("/reading/{readingId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get anomalies by source reading ID")
    public ResponseEntity<List<FlowAnomalyReadDTO>> getByReadingId(
            @PathVariable Long readingId) {
        return ResponseEntity.ok(anomalyService.getByReadingId(readingId));
    }

    @GetMapping("/segment/{segmentId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get anomalies by pipeline segment ID")
    public ResponseEntity<List<FlowAnomalyReadDTO>> getBySegmentId(
            @PathVariable Long segmentId) {
        return ResponseEntity.ok(anomalyService.getByPipelineSegmentId(segmentId));
    }
}
