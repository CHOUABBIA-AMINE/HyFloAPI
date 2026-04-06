/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 *  @Name       : SlotCoverageController
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Workflow
 *
 *  @Description: REST controller for operational slot coverage dashboard.
 *                Provides per-slot, per-structure reading completion status.
 *                Used by operations supervisors to monitor which pipelines
 *                have submitted / approved / missing readings for a given slot.
 *
 *                Primary endpoint is the slot coverage view:
 *                  GET /api/v2/flow/slot-coverage/coverage
 *
 *                Delegates to SlotCoverageService.getSlotCoverage().
 *                Returns SlotCoverageResponseDTO — a rich aggregate with:
 *                  - Total pipeline count vs recorded/submitted/approved/missing
 *                  - Per-pipeline status with actor and timestamp context
 *                  - Recording and validation completion percentages
 *                  - Slot deadline indicator
 *
 *  Phase 4 — Commit 32
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.flow.workflow.service.SlotCoverageService;
import dz.sh.trc.hyflo.intelligence.dto.monitoring.SlotCoverageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/slotCoverage")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Slot Coverage",
     description = "Operational dashboard — per-slot pipeline reading coverage for supervisors")
@SecurityRequirement(name = "bearer-auth")
public class SlotCoverageController {

    private final SlotCoverageService slotCoverageService;

    /**
     * Main operational dashboard endpoint.
     *
     * Returns the complete coverage picture for a date, time slot and managing structure.
     * Includes per-pipeline reading status, actor context, completion percentages,
     * and slot deadline.
     *
     * Used by: operations supervisors, control room dashboards.
     */
    @GetMapping("/coverage")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(
        summary = "Get slot coverage for a date, slot and structure",
        description = "Returns the complete reading coverage status for all pipelines managed by a structure "
                    + "for a given date and operational time slot. "
                    + "Includes: per-pipeline reading existence and validation status, "
                    + "recording and validation completion percentages, "
                    + "slot deadline, and missing pipeline count. "
                    + "This is the primary endpoint for the operational supervisor dashboard."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Slot coverage data returned"),
        @ApiResponse(responseCode = "404", description = "Structure or slot not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<SlotCoverageResponseDTO> getSlotCoverage(
            @Parameter(description = "Reading date, format: yyyy-MM-dd")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Operational time slot number (e.g., 1, 2, 3)")
            @RequestParam Integer slotNumber,
            @Parameter(description = "Managing structure ID (station, terminal, or manifold)")
            @RequestParam Long structureId) {
        log.info("GET /api/v2/flow/slot-coverage/coverage date={} slot={} structureId={}",
                date, slotNumber, structureId);
        return ResponseEntity.ok(
                slotCoverageService.getSlotCoverage(date, slotNumber, structureId));
    }
}
