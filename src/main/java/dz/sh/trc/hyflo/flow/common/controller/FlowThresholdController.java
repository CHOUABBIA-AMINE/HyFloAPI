/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdController
 *  @CreatedOn  : 03-26-2026
 *  @MovedOn    : 03-28-2026 — refactor: flow.core.controller → flow.common.controller
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Common
 *
 *  @Description: REST controller for FlowThreshold CRUD and lifecycle.
 *                Moved to /flow/common/threshold per HyFlo v2 endpoint normalization.
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flow/common/threshold")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Common — Thresholds",
     description = "Pipeline operating threshold configuration and lifecycle management")
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
        return ResponseEntity.ok(queryService.getThresholdById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "List all thresholds (paginated)")
    public ResponseEntity<Page<FlowThresholdDTO>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(queryService.getAllThresholds(page, size, sortBy, sortDirection));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "List all active thresholds (paginated)")
    public ResponseEntity<Page<FlowThresholdDTO>> getActive(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(queryService.getActiveThresholds(page, size));
    }

    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all thresholds for a pipeline (active + historical)")
    public ResponseEntity<List<FlowThresholdDTO>> getByPipeline(
            @PathVariable Long pipelineId) {
        return ResponseEntity.ok(queryService.getThresholdsByPipelineId(pipelineId));
    }

    @GetMapping("/pipeline/{pipelineId}/active")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get active threshold for a pipeline")
    public ResponseEntity<Optional<FlowThresholdDTO>> getActivForPipeline(
            @PathVariable Long pipelineId) {
        return ResponseEntity.ok(queryService.getActiveThresholdByPipelineId(pipelineId));
    }

    @GetMapping("/structure/{structureId}/active")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get active thresholds for all pipelines under a structure")
    public ResponseEntity<List<FlowThresholdDTO>> getActiveByStructure(
            @PathVariable Long structureId) {
        return ResponseEntity.ok(queryService.getActiveThresholdsByStructureId(structureId));
    }

    @GetMapping("/coverage/missing")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get pipeline IDs with no threshold configured")
    public ResponseEntity<List<Long>> getMissingCoverage() {
        return ResponseEntity.ok(queryService.getPipelinesWithoutThresholds());
    }

    @GetMapping("/coverage/missing-active")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get pipeline IDs with no ACTIVE threshold")
    public ResponseEntity<List<Long>> getMissingActiveCoverage() {
        return ResponseEntity.ok(queryService.getPipelinesWithoutActiveThresholds());
    }

    // =========================================================
    // COMMAND ENDPOINTS
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Create a new threshold for a pipeline")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Threshold created"),
        @ApiResponse(responseCode = "400", description = "Invalid range configuration"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found")
    })
    public ResponseEntity<FlowThresholdDTO> create(
            @Valid @RequestBody FlowThresholdDTO dto) {
        log.info("POST /flow/common/threshold — create for pipelineId={}", dto.getPipelineId());
        return ResponseEntity.status(HttpStatus.CREATED).body(commandService.createThreshold(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Update an existing threshold")
    public ResponseEntity<FlowThresholdDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FlowThresholdDTO dto) {
        log.info("PUT /flow/common/threshold/{}", id);
        return ResponseEntity.ok(commandService.updateThreshold(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('THRESHOLD:DELETE')")
    @Operation(summary = "Hard-delete a threshold (prefer deactivate)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("DELETE /flow/common/threshold/{}", id);
        commandService.deleteThreshold(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Activate a threshold (deactivates the current active threshold for same pipeline)")
    public ResponseEntity<FlowThresholdDTO> activate(@PathVariable Long id) {
        log.info("POST /flow/common/threshold/{}/activate", id);
        return ResponseEntity.ok(commandService.activateThreshold(id));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('THRESHOLD:WRITE')")
    @Operation(summary = "Deactivate a threshold")
    public ResponseEntity<FlowThresholdDTO> deactivate(@PathVariable Long id) {
        log.info("POST /flow/common/threshold/{}/deactivate", id);
        return ResponseEntity.ok(commandService.deactivateThreshold(id));
    }
}
