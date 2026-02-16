/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowThresholdController
 * 	@CreatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-16-2026 - Enhanced with comprehensive OpenAPI documentation
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

@RestController
@RequestMapping("/flow/core/threshold")
@Tag(name = "Flow Threshold Management", description = "APIs for managing pipeline operating thresholds with activation control and compliance tracking")
@SecurityRequirement(name = "bearer-auth")
@RequiredArgsConstructor
public class FlowThresholdController {
    
    private final FlowThresholdService flowThresholdService;
    
    // ========== CREATE ==========
    
    @PostMapping
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:WRITE')")
    @Operation(
        summary = "Create threshold",
        description = "Create a new operating threshold for a pipeline. " +
            "If active=true, will deactivate any existing active threshold for the same pipeline."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Threshold created successfully", 
                     content = @Content(schema = @Schema(implementation = FlowThresholdDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "409", description = "Validation conflict - duplicate or invalid threshold configuration"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:WRITE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FlowThresholdDTO> createThreshold(
        @Parameter(description = "Threshold configuration data", required = true)
        @Valid @RequestBody FlowThresholdDTO dto
    ) {
        FlowThresholdDTO created = flowThresholdService.createThreshold(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // ========== READ ==========
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(summary = "Get threshold by ID", description = "Retrieves a specific threshold by its identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold found", 
                     content = @Content(schema = @Schema(implementation = FlowThresholdDTO.class))),
        @ApiResponse(responseCode = "404", description = "Threshold not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FlowThresholdDTO> getThresholdById(
        @Parameter(description = "Threshold ID", required = true, example = "1")
        @PathVariable Long id
    ) {
        FlowThresholdDTO threshold = flowThresholdService.getThresholdById(id);
        return ResponseEntity.ok(threshold);
    }
    
    @GetMapping("/pipeline/{pipelineId}/active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Get active threshold for pipeline",
        description = "Retrieves the currently active threshold for a specific pipeline"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active threshold found", 
                     content = @Content(schema = @Schema(implementation = FlowThresholdDTO.class))),
        @ApiResponse(responseCode = "404", description = "No active threshold found for this pipeline"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FlowThresholdDTO> getActiveThresholdByPipelineId(
        @Parameter(description = "Pipeline ID", required = true, example = "101")
        @PathVariable Long pipelineId
    ) {
        return flowThresholdService.getActiveThresholdByPipelineId(pipelineId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Get all thresholds for pipeline",
        description = "Retrieves all thresholds (active and inactive) for a specific pipeline"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Thresholds retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FlowThresholdDTO>> getThresholdsByPipelineId(
        @Parameter(description = "Pipeline ID", required = true, example = "101")
        @PathVariable Long pipelineId
    ) {
        List<FlowThresholdDTO> thresholds = flowThresholdService.getThresholdsByPipelineId(pipelineId);
        return ResponseEntity.ok(thresholds);
    }
    
    @GetMapping("/structure/{structureId}/active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Get active thresholds by structure",
        description = "Retrieves all active thresholds for pipelines managed by a structure (station, terminal, manifold)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Thresholds retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Structure not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FlowThresholdDTO>> getActiveThresholdsByStructureId(
        @Parameter(description = "Structure ID", required = true, example = "5")
        @PathVariable Long structureId
    ) {
        List<FlowThresholdDTO> thresholds = flowThresholdService.getActiveThresholdsByStructureId(structureId);
        return ResponseEntity.ok(thresholds);
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Get all thresholds (paginated)",
        description = "Retrieves all thresholds with pagination and sorting"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Thresholds retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
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
    
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Get all active thresholds (paginated)",
        description = "Retrieves only active thresholds with pagination"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active thresholds retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
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
    
    @GetMapping("/active/all")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Get all active thresholds",
        description = "Retrieves all active thresholds without pagination"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active thresholds retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FlowThresholdDTO>> getAllActiveThresholds() {
        List<FlowThresholdDTO> thresholds = flowThresholdService.getAllActiveThresholds();
        return ResponseEntity.ok(thresholds);
    }
    
    @GetMapping("/inactive/all")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Get all inactive thresholds",
        description = "Retrieves all inactive thresholds without pagination"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inactive thresholds retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FlowThresholdDTO>> getAllInactiveThresholds() {
        List<FlowThresholdDTO> thresholds = flowThresholdService.getAllInactiveThresholds();
        return ResponseEntity.ok(thresholds);
    }
    
    @GetMapping("/search/code/{pipelineCode}")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Search by pipeline code",
        description = "Find thresholds by exact pipeline code match"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FlowThresholdDTO>> searchByPipelineCode(
        @Parameter(description = "Pipeline code", required = true, example = "OZ-24-101")
        @PathVariable String pipelineCode
    ) {
        List<FlowThresholdDTO> thresholds = flowThresholdService.searchByPipelineCode(pipelineCode);
        return ResponseEntity.ok(thresholds);
    }
    
    @GetMapping("/search/pattern")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Search by pipeline code pattern",
        description = "Find thresholds by pipeline code pattern (e.g., 'OZ%' for all OZ pipelines)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search pattern"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<FlowThresholdDTO>> searchByPipelineCodePattern(
        @Parameter(description = "Pipeline code pattern", required = true, example = "OZ%")
        @RequestParam String pattern
    ) {
        List<FlowThresholdDTO> thresholds = flowThresholdService.searchByPipelineCodePattern(pattern);
        return ResponseEntity.ok(thresholds);
    }
    
    // ========== OPERATIONAL QUERIES ==========
    
    @GetMapping("/compliance/missing")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Get pipelines without thresholds",
        description = "Find active pipelines that lack threshold configuration (compliance check)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline IDs retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Long>> getPipelinesWithoutThresholds() {
        List<Long> pipelineIds = flowThresholdService.getPipelinesWithoutThresholds();
        return ResponseEntity.ok(pipelineIds);
    }
    
    @GetMapping("/compliance/missing-active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Get pipelines without active thresholds",
        description = "Find active pipelines that lack an active threshold configuration"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline IDs retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Long>> getPipelinesWithoutActiveThresholds() {
        List<Long> pipelineIds = flowThresholdService.getPipelinesWithoutActiveThresholds();
        return ResponseEntity.ok(pipelineIds);
    }
    
    @GetMapping("/compliance/missing/count")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Count pipelines without thresholds",
        description = "Get count of active pipelines lacking threshold configuration"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> countPipelinesWithoutThresholds() {
        long count = flowThresholdService.countPipelinesWithoutThresholds();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Count active thresholds",
        description = "Get total count of active thresholds"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> countActiveThresholds() {
        long count = flowThresholdService.countActiveThresholds();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/pipeline/{pipelineId}/has-active")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:READ')")
    @Operation(
        summary = "Check if pipeline has active threshold",
        description = "Check whether a pipeline has an active threshold configured"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check completed successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> hasActiveThreshold(
        @Parameter(description = "Pipeline ID", required = true, example = "101")
        @PathVariable Long pipelineId
    ) {
        boolean hasActive = flowThresholdService.hasActiveThreshold(pipelineId);
        return ResponseEntity.ok(hasActive);
    }
    
    // ========== UPDATE ==========
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:WRITE')")
    @Operation(
        summary = "Update threshold",
        description = "Update an existing threshold. If setting active=true, will deactivate other active thresholds for the same pipeline."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold updated successfully", 
                     content = @Content(schema = @Schema(implementation = FlowThresholdDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
        @ApiResponse(responseCode = "404", description = "Threshold not found"),
        @ApiResponse(responseCode = "409", description = "Validation conflict - duplicate or invalid threshold configuration"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:WRITE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FlowThresholdDTO> updateThreshold(
        @Parameter(description = "Threshold ID", required = true, example = "1")
        @PathVariable Long id,
        
        @Parameter(description = "Updated threshold data", required = true)
        @Valid @RequestBody FlowThresholdDTO dto
    ) {
        FlowThresholdDTO updated = flowThresholdService.updateThreshold(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:MANAGE')")
    @Operation(
        summary = "Activate threshold",
        description = "Activate a threshold. Will deactivate any other active threshold for the same pipeline."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold activated successfully", 
                     content = @Content(schema = @Schema(implementation = FlowThresholdDTO.class))),
        @ApiResponse(responseCode = "404", description = "Threshold not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FlowThresholdDTO> activateThreshold(
        @Parameter(description = "Threshold ID", required = true, example = "1")
        @PathVariable Long id
    ) {
        FlowThresholdDTO activated = flowThresholdService.activateThreshold(id);
        return ResponseEntity.ok(activated);
    }
    
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:MANAGE')")
    @Operation(
        summary = "Deactivate threshold",
        description = "Deactivate a threshold without deleting it (preserves configuration for audit trail)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Threshold deactivated successfully", 
                     content = @Content(schema = @Schema(implementation = FlowThresholdDTO.class))),
        @ApiResponse(responseCode = "404", description = "Threshold not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FlowThresholdDTO> deactivateThreshold(
        @Parameter(description = "Threshold ID", required = true, example = "1")
        @PathVariable Long id
    ) {
        FlowThresholdDTO deactivated = flowThresholdService.deactivateThreshold(id);
        return ResponseEntity.ok(deactivated);
    }
    
    // ========== DELETE ==========
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW_THRESHOLD:MANAGE')")
    @Operation(
        summary = "Delete threshold",
        description = "Permanently delete a threshold. WARNING: This is irreversible. Consider deactivation instead."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Threshold deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Threshold not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_THRESHOLD:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteThreshold(
        @Parameter(description = "Threshold ID", required = true, example = "1")
        @PathVariable Long id
    ) {
        flowThresholdService.deleteThreshold(id);
        return ResponseEntity.noContent().build();
    }
}
