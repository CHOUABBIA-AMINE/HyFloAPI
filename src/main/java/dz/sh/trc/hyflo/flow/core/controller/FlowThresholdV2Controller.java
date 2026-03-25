/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdV2Controller
 *  @CreatedOn  : Phase 4 — Commit 33
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Core
 *
 *  @Description: v2 REST controller for pipeline operating thresholds.
 *                Clean API wrapper over FlowThresholdService.
 *                No entity exposure. No legacy DTO leaks in v2 paths.
 *                Business invariant: only one active threshold per pipeline.
 *                Deactivation of existing active threshold on new activation
 *                is handled inside FlowThresholdService.
 *
 *  Phase 4 — Commit 33
 *
 *  FlowThresholdService public method signatures (confirmed):
 *    createThreshold(FlowThresholdDTO)                         -> FlowThresholdDTO
 *    updateThreshold(Long, FlowThresholdDTO)                   -> FlowThresholdDTO
 *    deleteThreshold(Long)                                      -> void
 *    activateThreshold(Long)                                    -> FlowThresholdDTO
 *    deactivateThreshold(Long)                                  -> FlowThresholdDTO
 *    getThresholdById(Long)                                     -> FlowThresholdDTO
 *    getAllThresholds(int, int, String, String)                  -> Page<FlowThresholdDTO>
 *    getActiveThresholds(int, int)                              -> Page<FlowThresholdDTO>
 *    getThresholdsByPipelineId(Long)                            -> List<FlowThresholdDTO>
 *    getActiveThresholdByPipelineId(Long)                       -> Optional<FlowThresholdDTO>
 *    getActiveThresholdsByStructureId(Long)                     -> List<FlowThresholdDTO>
 *    searchByPipelineCode(String)                               -> List<FlowThresholdDTO>
 *    searchByPipelineCodePattern(String)                        -> List<FlowThresholdDTO>
 *    getAllActiveThresholds()                                    -> List<FlowThresholdDTO>
 *    getAllInactiveThresholds()                                  -> List<FlowThresholdDTO>
 *    getPipelinesWithoutThresholds()                            -> List<Long>
 *    getPipelinesWithoutActiveThresholds()                      -> List<Long>
 *    countPipelinesWithoutThresholds()                          -> long
 *    countActiveThresholds()                                    -> long
 *    hasActiveThreshold(Long)                                   -> boolean
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import dz.sh.trc.hyflo.flow.core.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowThresholdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/flow/thresholds")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Thresholds v2",
     description = "Pipeline operating threshold management — define pressure, temperature, flow-rate and volume envelopes per pipeline")
@SecurityRequirement(name = "bearer-auth")
public class FlowThresholdV2Controller {

    private final FlowThresholdService thresholdService;

    // =========================================================
    // QUERY ENDPOINTS
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "Get threshold by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold found"),
        @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<FlowThresholdDTO> getById(
            @Parameter(description = "Threshold ID") @PathVariable Long id) {
        return ResponseEntity.ok(thresholdService.getThresholdById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "List all thresholds (paginated)",
               description = "Returns all thresholds with pagination and optional sort field/direction.")
    public ResponseEntity<Page<FlowThresholdDTO>> getAll(
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "20")  int size,
            @RequestParam(defaultValue = "id")  String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(thresholdService.getAllThresholds(page, size, sortBy, sortDirection));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "List all active thresholds (paginated)")
    public ResponseEntity<Page<FlowThresholdDTO>> getActivePaginated(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(thresholdService.getActiveThresholds(page, size));
    }

    @GetMapping("/active/all")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "List all active thresholds (unpaginated)",
               description = "Returns all active thresholds without pagination. Use for configuration exports.")
    public ResponseEntity<List<FlowThresholdDTO>> getAllActive() {
        return ResponseEntity.ok(thresholdService.getAllActiveThresholds());
    }

    @GetMapping("/inactive/all")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "List all inactive thresholds",
               description = "Returns all deactivated (historical) thresholds for audit purposes.")
    public ResponseEntity<List<FlowThresholdDTO>> getAllInactive() {
        return ResponseEntity.ok(thresholdService.getAllInactiveThresholds());
    }

    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "Get all thresholds for a pipeline",
               description = "Returns all threshold versions (active and historical) for a pipeline.")
    public ResponseEntity<List<FlowThresholdDTO>> getByPipeline(
            @PathVariable Long pipelineId) {
        return ResponseEntity.ok(thresholdService.getThresholdsByPipelineId(pipelineId));
    }

    @GetMapping("/pipeline/{pipelineId}/active")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "Get active threshold for a pipeline",
               description = "Returns the currently active threshold for a pipeline, if any.")
    public ResponseEntity<FlowThresholdDTO> getActiveByPipeline(
            @PathVariable Long pipelineId) {
        Optional<FlowThresholdDTO> result = thresholdService.getActiveThresholdByPipelineId(pipelineId);
        return result.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pipeline/{pipelineId}/exists")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "Check if pipeline has an active threshold")
    public ResponseEntity<Boolean> hasActiveThreshold(
            @PathVariable Long pipelineId) {
        return ResponseEntity.ok(thresholdService.hasActiveThreshold(pipelineId));
    }

    @GetMapping("/structure/{structureId}/active")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "Get all active thresholds for a structure",
               description = "Returns active thresholds for all pipelines managed by a structure (station, terminal, manifold).")
    public ResponseEntity<List<FlowThresholdDTO>> getActiveByStructure(
            @PathVariable Long structureId) {
        return ResponseEntity.ok(thresholdService.getActiveThresholdsByStructureId(structureId));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "Search thresholds by exact pipeline code")
    public ResponseEntity<List<FlowThresholdDTO>> searchByPipelineCode(
            @Parameter(description = "Exact pipeline code") @RequestParam String code) {
        return ResponseEntity.ok(thresholdService.searchByPipelineCode(code));
    }

    @GetMapping("/search/pattern")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "Search thresholds by pipeline code pattern",
               description = "Supports SQL LIKE patterns (e.g., OZ% for all Oran-zone pipelines).")
    public ResponseEntity<List<FlowThresholdDTO>> searchByPipelineCodePattern(
            @Parameter(description = "Pipeline code LIKE pattern (e.g., OZ%)") @RequestParam String pattern) {
        return ResponseEntity.ok(thresholdService.searchByPipelineCodePattern(pattern));
    }

    @GetMapping("/stats/pipelines-without-thresholds")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "Get pipeline IDs with no threshold configured")
    public ResponseEntity<List<Long>> getPipelinesWithoutThresholds() {
        return ResponseEntity.ok(thresholdService.getPipelinesWithoutThresholds());
    }

    @GetMapping("/stats/pipelines-without-active-thresholds")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "Get pipeline IDs with no active threshold")
    public ResponseEntity<List<Long>> getPipelinesWithoutActiveThresholds() {
        return ResponseEntity.ok(thresholdService.getPipelinesWithoutActiveThresholds());
    }

    @GetMapping("/stats/count-pipelines-without-thresholds")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "Count pipelines with no threshold configured")
    public ResponseEntity<Long> countPipelinesWithoutThresholds() {
        return ResponseEntity.ok(thresholdService.countPipelinesWithoutThresholds());
    }

    @GetMapping("/stats/count-active")
    @PreAuthorize("hasAuthority('THRESHOLD:READ')")
    @Operation(summary = "Count total active thresholds")
    public ResponseEntity<Long> countActiveThresholds() {
        return ResponseEntity.ok(thresholdService.countActiveThresholds());
    }

    // =========================================================
    // COMMAND ENDPOINTS
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Create a new threshold for a pipeline",
               description = "Creates a threshold defining pressure, temperature, flow-rate, and volume operating envelopes. "
                           + "If active=true, any existing active threshold for the same pipeline is automatically deactivated.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Threshold created"),
        @ApiResponse(responseCode = "400", description = "Validation failed (min >= max)"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found")
    })
    public ResponseEntity<FlowThresholdDTO> create(
            @Valid @RequestBody FlowThresholdDTO dto) {
        log.info("POST /api/v2/flow/thresholds - create for pipelineId={}", dto.getPipelineId());
        return ResponseEntity.status(HttpStatus.CREATED).body(thresholdService.createThreshold(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Update an existing threshold",
               description = "Updates threshold operating parameters. "
                           + "If activating an inactive threshold, existing active threshold for the pipeline is deactivated.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold updated"),
        @ApiResponse(responseCode = "400", description = "Validation failed (min >= max)"),
        @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<FlowThresholdDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FlowThresholdDTO dto) {
        return ResponseEntity.ok(thresholdService.updateThreshold(id, dto));
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Activate a threshold",
               description = "Activates an inactive threshold. Deactivates the currently active threshold for the same pipeline if one exists.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold activated"),
        @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<FlowThresholdDTO> activate(@PathVariable Long id) {
        log.info("POST /api/v2/flow/thresholds/{}/activate", id);
        return ResponseEntity.ok(thresholdService.activateThreshold(id));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Deactivate a threshold",
               description = "Deactivates an active threshold. Pipeline will have no active threshold until another is activated.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold deactivated"),
        @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<FlowThresholdDTO> deactivate(@PathVariable Long id) {
        log.info("POST /api/v2/flow/thresholds/{}/deactivate", id);
        return ResponseEntity.ok(thresholdService.deactivateThreshold(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('THRESHOLD:DELETE')")
    @Operation(summary = "Delete a threshold (hard delete)",
               description = "Permanently deletes a threshold. WARNING: This is a hard delete with no audit trail. "
                           + "Prefer deactivation to preserve configuration history.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Threshold deleted"),
        @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("DELETE /api/v2/flow/thresholds/{} - hard delete requested", id);
        thresholdService.deleteThreshold(id);
        return ResponseEntity.noContent().build();
    }
}
