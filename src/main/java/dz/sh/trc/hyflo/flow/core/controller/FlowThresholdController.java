/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowThresholdController
 * 	@CreatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-03-2026
 *
 * 	@Type		: Controller
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 * 	@Description:
 * 	REST controller for managing pipeline operating thresholds.
 * 	Provides CRUD operations, activation/deactivation, and search capabilities.
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import dz.sh.trc.hyflo.flow.core.dto.entity.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowThresholdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/flow/core/threshold")
@Tag(name = "Flow Thresholds", description = "Pipeline operating threshold management")
@RequiredArgsConstructor
public class FlowThresholdController {
    
    private final FlowThresholdService flowThresholdService;
    
    // ========== CREATE ==========
    
    /**
     * Create a new threshold for a pipeline
     */
    @PostMapping
    @Operation(
        summary = "Create threshold",
        description = "Create a new operating threshold for a pipeline. " +
            "If active=true, will deactivate any existing active threshold for the same pipeline."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Threshold created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "409", description = "Validation failed")
    })
    public ResponseEntity<FlowThresholdDTO> createThreshold(
        @Valid @RequestBody FlowThresholdDTO dto
    ) {
        FlowThresholdDTO created = flowThresholdService.createThreshold(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // ========== READ ==========
    
    /**
     * Get threshold by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get threshold by ID", description = "Retrieve a specific threshold by its identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold found"),
        @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<FlowThresholdDTO> getThresholdById(
        @Parameter(description = "Threshold ID", required = true, example = "1")
        @PathVariable Long id
    ) {
        FlowThresholdDTO threshold = flowThresholdService.getThresholdById(id);
        return ResponseEntity.ok(threshold);
    }
    
    /**
     * Get active threshold for a pipeline
     */
    @GetMapping("/pipeline/{pipelineId}/active")
    @Operation(
        summary = "Get active threshold for pipeline",
        description = "Retrieve the currently active threshold for a specific pipeline"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active threshold found"),
        @ApiResponse(responseCode = "404", description = "No active threshold found for this pipeline")
    })
    public ResponseEntity<FlowThresholdDTO> getActiveThresholdByPipelineId(
        @Parameter(description = "Pipeline ID", required = true, example = "101")
        @PathVariable Long pipelineId
    ) {
        return flowThresholdService.getActiveThresholdByPipelineId(pipelineId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get all thresholds for a pipeline
     */
    @GetMapping("/pipeline/{pipelineId}")
    @Operation(
        summary = "Get all thresholds for pipeline",
        description = "Retrieve all thresholds (active and inactive) for a specific pipeline"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Thresholds retrieved successfully")
    })
    public ResponseEntity<List<FlowThresholdDTO>> getThresholdsByPipelineId(
        @Parameter(description = "Pipeline ID", required = true, example = "101")
        @PathVariable Long pipelineId
    ) {
        List<FlowThresholdDTO> thresholds = flowThresholdService.getThresholdsByPipelineId(pipelineId);
        return ResponseEntity.ok(thresholds);
    }
    
    /**
     * Get all active thresholds for a structure
     */
    @GetMapping("/structure/{structureId}/active")
    @Operation(
        summary = "Get active thresholds by structure",
        description = "Retrieve all active thresholds for pipelines managed by a structure (station, terminal, manifold)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Thresholds retrieved successfully")
    })
    public ResponseEntity<List<FlowThresholdDTO>> getActiveThresholdsByStructureId(
        @Parameter(description = "Structure ID", required = true, example = "5")
        @PathVariable Long structureId
    ) {
        List<FlowThresholdDTO> thresholds = flowThresholdService.getActiveThresholdsByStructureId(structureId);
        return ResponseEntity.ok(thresholds);
    }
    
    /**
     * Get all thresholds with pagination
     */
    @GetMapping
    @Operation(
        summary = "Get all thresholds (paginated)",
        description = "Retrieve all thresholds with pagination and sorting"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Thresholds retrieved successfully")
    })
    public ResponseEntity<Page<FlowThresholdDTO>> getAllThresholds(
        @Parameter(description = "Page number (0-indexed)", example = "0")
        @RequestParam(defaultValue = "0") int page,
        
        @Parameter(description = "Page size", example = "20")
        @RequestParam(defaultValue = "20") int size,
        
        @Parameter(description = "Sort field", example = "id")
        @RequestParam(required = false) String sortBy,
        
        @Parameter(description = "Sort direction (ASC/DESC)", example = "ASC")
        @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        Page<FlowThresholdDTO> thresholds = flowThresholdService.getAllThresholds(
            page, size, sortBy, sortDirection
        );
        return ResponseEntity.ok(thresholds);
    }
    
    /**
     * Get all active thresholds with pagination
     */
    @GetMapping("/active")
    @Operation(
        summary = "Get all active thresholds (paginated)",
        description = "Retrieve only active thresholds with pagination"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active thresholds retrieved successfully")
    })
    public ResponseEntity<Page<FlowThresholdDTO>> getActiveThresholds(
        @Parameter(description = "Page number (0-indexed)", example = "0")
        @RequestParam(defaultValue = "0") int page,
        
        @Parameter(description = "Page size", example = "20")
        @RequestParam(defaultValue = "20") int size
    ) {
        Page<FlowThresholdDTO> thresholds = flowThresholdService.getActiveThresholds(page, size);
        return ResponseEntity.ok(thresholds);
    }
    
    /**
     * Get all active thresholds (no pagination)
     */
    @GetMapping("/active/all")
    @Operation(
        summary = "Get all active thresholds",
        description = "Retrieve all active thresholds without pagination"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active thresholds retrieved successfully")
    })
    public ResponseEntity<List<FlowThresholdDTO>> getAllActiveThresholds() {
        List<FlowThresholdDTO> thresholds = flowThresholdService.getAllActiveThresholds();
        return ResponseEntity.ok(thresholds);
    }
    
    /**
     * Get all inactive thresholds
     */
    @GetMapping("/inactive/all")
    @Operation(
        summary = "Get all inactive thresholds",
        description = "Retrieve all inactive thresholds"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inactive thresholds retrieved successfully")
    })
    public ResponseEntity<List<FlowThresholdDTO>> getAllInactiveThresholds() {
        List<FlowThresholdDTO> thresholds = flowThresholdService.getAllInactiveThresholds();
        return ResponseEntity.ok(thresholds);
    }
    
    /**
     * Search thresholds by pipeline code
     */
    @GetMapping("/search/code/{pipelineCode}")
    @Operation(
        summary = "Search by pipeline code",
        description = "Find thresholds by exact pipeline code match"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<List<FlowThresholdDTO>> searchByPipelineCode(
        @Parameter(description = "Pipeline code", required = true, example = "OZ-24-101")
        @PathVariable String pipelineCode
    ) {
        List<FlowThresholdDTO> thresholds = flowThresholdService.searchByPipelineCode(pipelineCode);
        return ResponseEntity.ok(thresholds);
    }
    
    /**
     * Search thresholds by pipeline code pattern
     */
    @GetMapping("/search/pattern")
    @Operation(
        summary = "Search by pipeline code pattern",
        description = "Find thresholds by pipeline code pattern (e.g., 'OZ%' for all OZ pipelines)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<List<FlowThresholdDTO>> searchByPipelineCodePattern(
        @Parameter(description = "Pipeline code pattern", required = true, example = "OZ%")
        @RequestParam String pattern
    ) {
        List<FlowThresholdDTO> thresholds = flowThresholdService.searchByPipelineCodePattern(pattern);
        return ResponseEntity.ok(thresholds);
    }
    
    // ========== OPERATIONAL QUERIES ==========
    
    /**
     * Get pipelines without any threshold configuration
     */
    @GetMapping("/compliance/missing")
    @Operation(
        summary = "Get pipelines without thresholds",
        description = "Find active pipelines that lack threshold configuration (compliance check)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline IDs retrieved successfully")
    })
    public ResponseEntity<List<Long>> getPipelinesWithoutThresholds() {
        List<Long> pipelineIds = flowThresholdService.getPipelinesWithoutThresholds();
        return ResponseEntity.ok(pipelineIds);
    }
    
    /**
     * Get pipelines without active threshold
     */
    @GetMapping("/compliance/missing-active")
    @Operation(
        summary = "Get pipelines without active thresholds",
        description = "Find active pipelines that lack an active threshold configuration"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline IDs retrieved successfully")
    })
    public ResponseEntity<List<Long>> getPipelinesWithoutActiveThresholds() {
        List<Long> pipelineIds = flowThresholdService.getPipelinesWithoutActiveThresholds();
        return ResponseEntity.ok(pipelineIds);
    }
    
    /**
     * Count pipelines without thresholds
     */
    @GetMapping("/compliance/missing/count")
    @Operation(
        summary = "Count pipelines without thresholds",
        description = "Get count of active pipelines lacking threshold configuration"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<Long> countPipelinesWithoutThresholds() {
        long count = flowThresholdService.countPipelinesWithoutThresholds();
        return ResponseEntity.ok(count);
    }
    
    /**
     * Count active thresholds
     */
    @GetMapping("/count/active")
    @Operation(
        summary = "Count active thresholds",
        description = "Get total count of active thresholds"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<Long> countActiveThresholds() {
        long count = flowThresholdService.countActiveThresholds();
        return ResponseEntity.ok(count);
    }
    
    /**
     * Check if pipeline has active threshold
     */
    @GetMapping("/pipeline/{pipelineId}/has-active")
    @Operation(
        summary = "Check if pipeline has active threshold",
        description = "Check whether a pipeline has an active threshold configured"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    public ResponseEntity<Boolean> hasActiveThreshold(
        @Parameter(description = "Pipeline ID", required = true, example = "101")
        @PathVariable Long pipelineId
    ) {
        boolean hasActive = flowThresholdService.hasActiveThreshold(pipelineId);
        return ResponseEntity.ok(hasActive);
    }
    
    // ========== UPDATE ==========
    
    /**
     * Update threshold
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update threshold",
        description = "Update an existing threshold. If setting active=true, will deactivate other active thresholds for the same pipeline."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Threshold not found"),
        @ApiResponse(responseCode = "409", description = "Validation failed")
    })
    public ResponseEntity<FlowThresholdDTO> updateThreshold(
        @Parameter(description = "Threshold ID", required = true, example = "1")
        @PathVariable Long id,
        
        @Valid @RequestBody FlowThresholdDTO dto
    ) {
        FlowThresholdDTO updated = flowThresholdService.updateThreshold(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Activate threshold
     */
    @PatchMapping("/{id}/activate")
    @Operation(
        summary = "Activate threshold",
        description = "Activate a threshold. Will deactivate any other active threshold for the same pipeline."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold activated successfully"),
        @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<FlowThresholdDTO> activateThreshold(
        @Parameter(description = "Threshold ID", required = true, example = "1")
        @PathVariable Long id
    ) {
        FlowThresholdDTO activated = flowThresholdService.activateThreshold(id);
        return ResponseEntity.ok(activated);
    }
    
    /**
     * Deactivate threshold
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(
        summary = "Deactivate threshold",
        description = "Deactivate a threshold without deleting it (preserves configuration for audit trail)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<FlowThresholdDTO> deactivateThreshold(
        @Parameter(description = "Threshold ID", required = true, example = "1")
        @PathVariable Long id
    ) {
        FlowThresholdDTO deactivated = flowThresholdService.deactivateThreshold(id);
        return ResponseEntity.ok(deactivated);
    }
    
    // ========== DELETE ==========
    
    /**
     * Delete threshold
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete threshold",
        description = "Permanently delete a threshold. WARNING: This is irreversible. Consider deactivation instead."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Threshold deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Threshold not found")
    })
    public ResponseEntity<Void> deleteThreshold(
        @Parameter(description = "Threshold ID", required = true, example = "1")
        @PathVariable Long id
    ) {
        flowThresholdService.deleteThreshold(id);
        return ResponseEntity.noContent().build();
    }
}
