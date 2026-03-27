/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdController
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: moved from flow.core.controller → flow.common.controller
 *                             Mapping updated: /flow/threshold → /flow/common/threshold
 *                             All imports updated to flow.common.*
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.controller;

import dz.sh.trc.hyflo.flow.common.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.common.service.FlowThresholdCommandService;
import dz.sh.trc.hyflo.flow.common.service.FlowThresholdQueryService;
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
@RequestMapping("/flow/common/threshold")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Common — Thresholds",
     description = "Pipeline operating threshold management (create, update, activate, deactivate, query)")
@SecurityRequirement(name = "bearer-auth")
public class FlowThresholdController {

    private final FlowThresholdCommandService commandService;
    private final FlowThresholdQueryService   queryService;

    // =========================================================
    // QUERY ENDPOINTS
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get threshold by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Threshold found"),
            @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<FlowThresholdDTO> getById(
            @Parameter(description = "Threshold ID") @PathVariable Long id) {
        log.debug("GET /flow/common/threshold/{}", id);
        return ResponseEntity.ok(queryService.getThresholdById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "List all thresholds (paginated)")
    public ResponseEntity<Page<FlowThresholdDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(queryService.getAllThresholds(page, size, sortBy, sortDir));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "List all active thresholds (paginated)")
    public ResponseEntity<Page<FlowThresholdDTO>> getActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(queryService.getActiveThresholds(page, size));
    }

    @GetMapping("/active/all")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all active thresholds (unpaginated)")
    public ResponseEntity<List<FlowThresholdDTO>> getAllActive() {
        return ResponseEntity.ok(queryService.getAllActiveThresholds());
    }

    @GetMapping("/inactive/all")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all inactive thresholds (unpaginated)")
    public ResponseEntity<List<FlowThresholdDTO>> getAllInactive() {
        return ResponseEntity.ok(queryService.getAllInactiveThresholds());
    }

    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all thresholds for a pipeline")
    public ResponseEntity<List<FlowThresholdDTO>> getByPipeline(@PathVariable Long pipelineId) {
        return ResponseEntity.ok(queryService.getThresholdsByPipelineId(pipelineId));
    }

    @GetMapping("/pipeline/{pipelineId}/active")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get the active threshold for a pipeline")
    public ResponseEntity<Optional<FlowThresholdDTO>> getActiveByPipeline(@PathVariable Long pipelineId) {
        return ResponseEntity.ok(queryService.getActiveThresholdByPipelineId(pipelineId));
    }

    @GetMapping("/structure/{structureId}/active")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get active thresholds for all pipelines managed by a structure")
    public ResponseEntity<List<FlowThresholdDTO>> getActiveByStructure(@PathVariable Long structureId) {
        return ResponseEntity.ok(queryService.getActiveThresholdsByStructureId(structureId));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Search thresholds by pipeline code")
    public ResponseEntity<List<FlowThresholdDTO>> searchByPipelineCode(
            @RequestParam String pipelineCode) {
        return ResponseEntity.ok(queryService.searchByPipelineCode(pipelineCode));
    }

    @GetMapping("/search/pattern")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Search thresholds by pipeline code pattern (LIKE)")
    public ResponseEntity<List<FlowThresholdDTO>> searchByPattern(@RequestParam String pattern) {
        return ResponseEntity.ok(queryService.searchByPipelineCodePattern(pattern));
    }

    @GetMapping("/coverage/pipelines-without")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get pipeline IDs without any threshold")
    public ResponseEntity<List<Long>> getPipelinesWithoutThresholds() {
        return ResponseEntity.ok(queryService.getPipelinesWithoutThresholds());
    }

    @GetMapping("/coverage/pipelines-without-active")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get pipeline IDs without an active threshold")
    public ResponseEntity<List<Long>> getPipelinesWithoutActive() {
        return ResponseEntity.ok(queryService.getPipelinesWithoutActiveThresholds());
    }

    // =========================================================
    // COMMAND ENDPOINTS
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Create a new threshold",
               description = "Creates a threshold for a pipeline. If active=true, deactivates the existing active threshold.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Threshold created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Pipeline not found")
    })
    public ResponseEntity<FlowThresholdDTO> create(
            @Valid @RequestBody FlowThresholdDTO dto) {
        log.debug("POST /flow/common/threshold - create");
        return ResponseEntity.status(HttpStatus.CREATED).body(commandService.createThreshold(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Update an existing threshold")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Threshold updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<FlowThresholdDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FlowThresholdDTO dto) {
        log.debug("PUT /flow/common/threshold/{}", id);
        return ResponseEntity.ok(commandService.updateThreshold(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('THRESHOLD:DELETE')")
    @Operation(summary = "Delete a threshold (hard delete)",
               description = "Hard delete. Prefer deactivate for audit trail preservation.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Threshold deleted"),
            @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("DELETE /flow/common/threshold/{}", id);
        commandService.deleteThreshold(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Activate a threshold",
               description = "Activates the specified threshold. Deactivates the currently active threshold for the same pipeline.")
    public ResponseEntity<FlowThresholdDTO> activate(@PathVariable Long id) {
        log.debug("PATCH /flow/common/threshold/{}/activate", id);
        return ResponseEntity.ok(commandService.activateThreshold(id));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Deactivate a threshold",
               description = "Deactivates the specified threshold.")
    public ResponseEntity<FlowThresholdDTO> deactivate(@PathVariable Long id) {
        log.debug("PATCH /flow/common/threshold/{}/deactivate", id);
        return ResponseEntity.ok(commandService.deactivateThreshold(id));
    }
}
