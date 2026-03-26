/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowThresholdController
 * 	@CreatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-16-2026 - Enhanced with comprehensive OpenAPI documentation
 * 	@UpdatedOn	: 03-26-2026 - H10: Marked @Deprecated(forRemoval=true)
 *
 * 	@Type		: Controller
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 *  @Deprecated Phase 4 — H10
 *  This controller is superseded by FlowThresholdV2Controller at /api/v2/flow/thresholds.
 *  Retained only for transitional compile safety and legacy client backward-compatibility.
 *  Scheduled for removal: Phase 8 cleanup.
 *  DO NOT add new endpoints here.
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.flow.core.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowThresholdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @deprecated Superseded by FlowThresholdV2Controller (/api/v2/flow/thresholds).
 * Retained for transitional compile safety. Scheduled for removal in Phase 8 cleanup.
 */
@Deprecated(since = "v2-phase4", forRemoval = true)
@RestController
@RequestMapping("/flow/core/threshold")
@Tag(name = "Flow Threshold Management (Legacy)", description = "[DEPRECATED] Use /api/v2/flow/thresholds instead.")
@SecurityRequirement(name = "bearer-auth")
@RequiredArgsConstructor
public class FlowThresholdController {

    private final FlowThresholdService flowThresholdService;

    @PostMapping
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:WRITE')")
    @Operation(summary = "[DEPRECATED] Create threshold — use /api/v2/flow/thresholds")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Threshold created successfully",
                     content = @Content(schema = @Schema(implementation = FlowThresholdDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:WRITE authority")
    })
    public ResponseEntity<FlowThresholdDTO> createThreshold(
        @Valid @RequestBody FlowThresholdDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flowThresholdService.createThreshold(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Get threshold by ID — use /api/v2/flow/thresholds/{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold found",
                     content = @Content(schema = @Schema(implementation = FlowThresholdDTO.class))),
        @ApiResponse(responseCode = "404", description = "Threshold not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority")
    })
    public ResponseEntity<FlowThresholdDTO> getThresholdById(@PathVariable Long id) {
        return ResponseEntity.ok(flowThresholdService.getThresholdById(id));
    }

    @GetMapping("/pipeline/{pipelineId}/active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Get active threshold for pipeline")
    public ResponseEntity<FlowThresholdDTO> getActiveThresholdByPipelineId(@PathVariable Long pipelineId) {
        return flowThresholdService.getActiveThresholdByPipelineId(pipelineId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Get all thresholds for pipeline")
    public ResponseEntity<List<FlowThresholdDTO>> getThresholdsByPipelineId(@PathVariable Long pipelineId) {
        return ResponseEntity.ok(flowThresholdService.getThresholdsByPipelineId(pipelineId));
    }

    @GetMapping("/structure/{structureId}/active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Get active thresholds by structure")
    public ResponseEntity<List<FlowThresholdDTO>> getActiveThresholdsByStructureId(@PathVariable Long structureId) {
        return ResponseEntity.ok(flowThresholdService.getActiveThresholdsByStructureId(structureId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Get all thresholds (paginated)")
    public ResponseEntity<Page<FlowThresholdDTO>> getAllThresholds(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(flowThresholdService.getAllThresholds(page, size, sortBy, sortDirection));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Get all active thresholds (paginated)")
    public ResponseEntity<Page<FlowThresholdDTO>> getActiveThresholds(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(flowThresholdService.getActiveThresholds(page, size));
    }

    @GetMapping("/active/all")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Get all active thresholds")
    public ResponseEntity<List<FlowThresholdDTO>> getAllActiveThresholds() {
        return ResponseEntity.ok(flowThresholdService.getAllActiveThresholds());
    }

    @GetMapping("/inactive/all")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Get all inactive thresholds")
    public ResponseEntity<List<FlowThresholdDTO>> getAllInactiveThresholds() {
        return ResponseEntity.ok(flowThresholdService.getAllInactiveThresholds());
    }

    @GetMapping("/search/code/{pipelineCode}")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Search by pipeline code")
    public ResponseEntity<List<FlowThresholdDTO>> searchByPipelineCode(
        @Parameter(description = "Pipeline code", required = true, example = "OZ-24-101")
        @PathVariable String pipelineCode) {
        return ResponseEntity.ok(flowThresholdService.searchByPipelineCode(pipelineCode));
    }

    @GetMapping("/search/pattern")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Search by pipeline code pattern")
    public ResponseEntity<List<FlowThresholdDTO>> searchByPipelineCodePattern(
        @RequestParam String pattern) {
        return ResponseEntity.ok(flowThresholdService.searchByPipelineCodePattern(pattern));
    }

    @GetMapping("/compliance/missing")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Get pipelines without thresholds")
    public ResponseEntity<List<Long>> getPipelinesWithoutThresholds() {
        return ResponseEntity.ok(flowThresholdService.getPipelinesWithoutThresholds());
    }

    @GetMapping("/compliance/missing-active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Get pipelines without active thresholds")
    public ResponseEntity<List<Long>> getPipelinesWithoutActiveThresholds() {
        return ResponseEntity.ok(flowThresholdService.getPipelinesWithoutActiveThresholds());
    }

    @GetMapping("/compliance/missing/count")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Count pipelines without thresholds")
    public ResponseEntity<Long> countPipelinesWithoutThresholds() {
        return ResponseEntity.ok(flowThresholdService.countPipelinesWithoutThresholds());
    }

    @GetMapping("/count/active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Count active thresholds")
    public ResponseEntity<Long> countActiveThresholds() {
        return ResponseEntity.ok(flowThresholdService.countActiveThresholds());
    }

    @GetMapping("/pipeline/{pipelineId}/has-active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "[DEPRECATED] Check if pipeline has active threshold")
    public ResponseEntity<Boolean> hasActiveThreshold(@PathVariable Long pipelineId) {
        return ResponseEntity.ok(flowThresholdService.hasActiveThreshold(pipelineId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:WRITE')")
    @Operation(summary = "[DEPRECATED] Update threshold — use /api/v2/flow/thresholds/{id}")
    public ResponseEntity<FlowThresholdDTO> updateThreshold(
        @PathVariable Long id,
        @Valid @RequestBody FlowThresholdDTO dto) {
        return ResponseEntity.ok(flowThresholdService.updateThreshold(id, dto));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:MANAGE')")
    @Operation(summary = "[DEPRECATED] Activate threshold")
    public ResponseEntity<FlowThresholdDTO> activateThreshold(@PathVariable Long id) {
        return ResponseEntity.ok(flowThresholdService.activateThreshold(id));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:MANAGE')")
    @Operation(summary = "[DEPRECATED] Deactivate threshold")
    public ResponseEntity<FlowThresholdDTO> deactivateThreshold(@PathVariable Long id) {
        return ResponseEntity.ok(flowThresholdService.deactivateThreshold(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:MANAGE')")
    @Operation(summary = "[DEPRECATED] Delete threshold — use /api/v2/flow/thresholds/{id}")
    public ResponseEntity<Void> deleteThreshold(@PathVariable Long id) {
        flowThresholdService.deleteThreshold(id);
        return ResponseEntity.noContent().build();
    }
}
