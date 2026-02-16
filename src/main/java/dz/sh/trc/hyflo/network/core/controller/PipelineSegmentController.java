/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSegmentController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.core.dto.PipelineSegmentDTO;
import dz.sh.trc.hyflo.network.core.service.PipelineSegmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/core/pipelineSegment")
@Slf4j
@Tag(name = "Pipeline Segment Management", description = "APIs for managing pipeline segments and sections")
@SecurityRequirement(name = "bearer-auth")
public class PipelineSegmentController extends GenericController<PipelineSegmentDTO, Long> {

    private final PipelineSegmentService pipelineSegmentService;

    public PipelineSegmentController(PipelineSegmentService pipelineSegmentService) {
        super(pipelineSegmentService, "PipelineSegment");
        this.pipelineSegmentService = pipelineSegmentService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    @Operation(summary = "Get pipeline segment by ID", description = "Retrieves a single pipeline segment by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline segment found", content = @Content(schema = @Schema(implementation = PipelineSegmentDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pipeline segment not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SEGMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PipelineSegmentDTO> getById(
            @Parameter(description = "Pipeline segment ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    @Operation(summary = "Get all pipeline segments (paginated)", description = "Retrieves a paginated list of all pipeline segments")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline segments retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SEGMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PipelineSegmentDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    @Operation(summary = "Get all pipeline segments (unpaginated)", description = "Retrieves all pipeline segments without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline segments retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SEGMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PipelineSegmentDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:MANAGE')")
    @Operation(summary = "Create new pipeline segment", description = "Creates a new pipeline segment with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pipeline segment created successfully", content = @Content(schema = @Schema(implementation = PipelineSegmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Pipeline segment name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SEGMENT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PipelineSegmentDTO> create(
            @Parameter(description = "Pipeline segment data", required = true) 
            @Valid @RequestBody PipelineSegmentDTO dto) {
        PipelineSegmentDTO created = pipelineSegmentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:MANAGE')")
    @Operation(summary = "Update pipeline segment", description = "Updates an existing pipeline segment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline segment updated successfully", content = @Content(schema = @Schema(implementation = PipelineSegmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Pipeline segment not found"),
        @ApiResponse(responseCode = "409", description = "Pipeline segment name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SEGMENT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PipelineSegmentDTO> update(
            @Parameter(description = "Pipeline segment ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated pipeline segment data", required = true) @Valid @RequestBody PipelineSegmentDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:MANAGE')")
    @Operation(summary = "Delete pipeline segment", description = "Deletes a pipeline segment permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pipeline segment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Pipeline segment not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete pipeline segment - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SEGMENT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Pipeline segment ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    @Operation(summary = "Search pipeline segments", description = "Searches pipeline segments by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SEGMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PipelineSegmentDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    @Operation(summary = "Check if pipeline segment exists", description = "Checks if a pipeline segment with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SEGMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Pipeline segment ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    @Operation(summary = "Count pipeline segments", description = "Returns the total number of pipeline segments in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline segment count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SEGMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('PIPELINE_SEGMENT:READ')")
    @Operation(summary = "Get segments by pipeline", description = "Retrieves all segments for a specific pipeline")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline segments retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SEGMENT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PipelineSegmentDTO>> getByPipeline(
            @Parameter(description = "Pipeline ID", required = true, example = "1") 
            @PathVariable Long pipelineId) {
        log.info("REST request to get PipelineSegments by pipeline id: {}", pipelineId);
        return ResponseEntity.ok(pipelineSegmentService.findByPipeline(pipelineId));
    }
}
