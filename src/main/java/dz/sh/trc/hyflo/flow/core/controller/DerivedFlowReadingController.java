/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 *  @Name       : DerivedFlowReadingController
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Core
 *
 *  @Description: Read-only REST controller for derived flow readings.
 *                Derived readings are system-computed from direct readings
 *                via SegmentDistributionService (triggered on approval).
 *                User-facing access is read-only; generation is system-driven.
 *                Uses DerivedFlowReadingQueryService exclusively.
 *
 *  Phase 4 — Commit 28
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.service.DerivedFlowReadingQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/flow/derivedReading")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Derived Flow Readings",
     description = "System-computed derived readings — segment-level flow calculations derived from approved pipeline readings")
@SecurityRequirement(name = "bearer-auth")
public class DerivedFlowReadingController {

    private final DerivedFlowReadingQueryService queryService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get derived reading by ID",
               description = "Returns a single derived flow reading by its system identifier.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Derived reading found"),
        @ApiResponse(responseCode = "404", description = "Derived reading not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<DerivedFlowReadingReadDTO> getById(
            @Parameter(description = "Derived reading ID") @PathVariable Long id) {
        log.debug("GET /api/v2/flow/derived-readings/{}", id);
        return ResponseEntity.ok(queryService.getById(id));
    }

    @GetMapping("/source/{sourceReadingId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get derived readings by source reading",
               description = "Returns all derived readings computed from a specific approved direct flow reading.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Derived readings returned"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<DerivedFlowReadingReadDTO>> getBySourceReading(
            @Parameter(description = "Source FlowReading ID") @PathVariable Long sourceReadingId) {
        log.debug("GET /api/v2/flow/derived-readings/source/{}", sourceReadingId);
        return ResponseEntity.ok(queryService.getBySourceReading(sourceReadingId));
    }

    @GetMapping("/segment/{segmentId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get derived readings by pipeline segment",
               description = "Returns all derived readings associated with a specific pipeline segment.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Derived readings returned"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<DerivedFlowReadingReadDTO>> getBySegment(
            @Parameter(description = "Pipeline segment ID") @PathVariable Long segmentId) {
        log.debug("GET /api/v2/flow/derived-readings/segment/{}", segmentId);
        return ResponseEntity.ok(queryService.getBySegment(segmentId));
    }

    @GetMapping("/segment/{segmentId}/range")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get derived readings by segment and date range",
               description = "Returns segment-level derived readings within a date interval. Used for segment performance monitoring.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Derived readings returned"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<DerivedFlowReadingReadDTO>> getBySegmentAndDateRange(
            @PathVariable Long segmentId,
            @Parameter(description = "Start date (inclusive), format: yyyy-MM-dd")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date (inclusive), format: yyyy-MM-dd")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        log.debug("GET /api/v2/flow/derived-readings/segment/{}/range from={} to={}", segmentId, from, to);
        return ResponseEntity.ok(queryService.getBySegmentAndDateRange(segmentId, from, to));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get derived readings by date range (all segments)",
               description = "Returns all derived readings within a date range across all pipeline segments.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Derived readings returned"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<DerivedFlowReadingReadDTO>> getByDateRange(
            @Parameter(description = "Start date (inclusive), format: yyyy-MM-dd")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date (inclusive), format: yyyy-MM-dd")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        log.debug("GET /api/v2/flow/derived-readings/date-range from={} to={}", from, to);
        return ResponseEntity.ok(queryService.getByDateRange(from, to));
    }
}
