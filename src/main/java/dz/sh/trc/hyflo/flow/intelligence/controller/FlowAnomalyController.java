/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAnomalyController
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Intelligence
 *
 *  @Description: REST controller for flow anomalies.
 *                Exposed at /flow/intelligence/anomaly per HyFlo v2 architecture.
 *                Replaces deprecated flow.core.controller.FlowAnomalyController.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.controller;

import java.util.List;

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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/intelligence/anomaly")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Intelligence — Anomalies",
     description = "Query flow anomalies detected by the intelligence engine")
@SecurityRequirement(name = "bearer-auth")
public class FlowAnomalyController {

    private final FlowAnomalyService flowAnomalyService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get anomaly by ID")
    public ResponseEntity<FlowAnomalyReadDTO> getById(
            @Parameter(description = "Anomaly ID") @PathVariable Long id) {
        log.debug("GET /flow/intelligence/anomaly/{}", id);
        return ResponseEntity.ok(flowAnomalyService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "List all anomalies (paginated)")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("GET /flow/intelligence/anomaly page={} size={}", page, size);
        return ResponseEntity.ok(flowAnomalyService.findAll(
                org.springframework.data.domain.PageRequest.of(
                        page, size,
                        "desc".equalsIgnoreCase(sortDir)
                                ? org.springframework.data.domain.Sort.Direction.DESC
                                : org.springframework.data.domain.Sort.Direction.ASC,
                        sortBy)));
    }

    @GetMapping("/reading/{readingId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get anomalies by reading ID")
    public ResponseEntity<List<FlowAnomalyReadDTO>> getByReadingId(
            @Parameter(description = "Reading ID", required = true) @PathVariable Long readingId) {
        log.debug("GET /flow/intelligence/anomaly/reading/{}", readingId);
        return ResponseEntity.ok(flowAnomalyService.getByReadingId(readingId));
    }

    @GetMapping("/segment/{segmentId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get anomalies by pipeline segment ID")
    public ResponseEntity<List<FlowAnomalyReadDTO>> getBySegmentId(
            @Parameter(description = "Segment ID", required = true) @PathVariable Long segmentId) {
        log.debug("GET /flow/intelligence/anomaly/segment/{}", segmentId);
        return ResponseEntity.ok(flowAnomalyService.getByPipelineSegmentId(segmentId));
    }
}
