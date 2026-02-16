/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineController
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
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.service.PipelineService;
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
@RequestMapping("/network/core/pipeline")
@Slf4j
@Tag(name = "Pipeline Management", description = "APIs for managing pipelines and their configurations")
@SecurityRequirement(name = "bearer-auth")
public class PipelineController extends GenericController<PipelineDTO, Long> {

    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService) {
        super(pipelineService, "Pipeline");
        this.pipelineService = pipelineService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    @Operation(summary = "Get pipeline by ID", description = "Retrieves a single pipeline by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline found", content = @Content(schema = @Schema(implementation = PipelineDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PipelineDTO> getById(
            @Parameter(description = "Pipeline ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    @Operation(summary = "Get all pipelines (paginated)", description = "Retrieves a paginated list of all pipelines")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipelines retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PipelineDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    @Operation(summary = "Get all pipelines (unpaginated)", description = "Retrieves all pipelines without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipelines retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PipelineDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:MANAGE')")
    @Operation(summary = "Create new pipeline", description = "Creates a new pipeline with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pipeline created successfully", content = @Content(schema = @Schema(implementation = PipelineDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Pipeline name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PipelineDTO> create(
            @Parameter(description = "Pipeline data", required = true) 
            @Valid @RequestBody PipelineDTO dto) {
        PipelineDTO created = pipelineService.create(dto);  
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:MANAGE')")
    @Operation(summary = "Update pipeline", description = "Updates an existing pipeline")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline updated successfully", content = @Content(schema = @Schema(implementation = PipelineDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "409", description = "Pipeline name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PipelineDTO> update(
            @Parameter(description = "Pipeline ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated pipeline data", required = true) @Valid @RequestBody PipelineDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:MANAGE')")
    @Operation(summary = "Delete pipeline", description = "Deletes a pipeline permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pipeline deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete pipeline - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Pipeline ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    @Operation(summary = "Search pipelines", description = "Searches pipelines by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PipelineDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    @Operation(summary = "Check if pipeline exists", description = "Checks if a pipeline with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Pipeline ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    @Operation(summary = "Count pipelines", description = "Returns the total number of pipelines in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/system/{systemId}")
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    @Operation(summary = "Get pipelines by system", description = "Retrieves all pipelines in a specific pipeline system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipelines retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PipelineDTO>> getByPipelineSystem(
            @Parameter(description = "Pipeline system ID", required = true, example = "1") 
            @PathVariable Long systemId) {
        log.info("REST request to get Pipeline by pipeline system id: {}", systemId);
        return ResponseEntity.ok(pipelineService.findByPipelineSystem(systemId));
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    @Operation(summary = "Get pipelines by owner", description = "Retrieves all pipelines owned by a specific entity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipelines retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PipelineDTO>> getByOwner(
            @Parameter(description = "Owner ID", required = true, example = "1") 
            @PathVariable Long ownerId) {
        log.info("REST request to get Pipeline by owner structure id: {}", ownerId);
        return ResponseEntity.ok(pipelineService.findByOwner(ownerId));
    }

    @GetMapping("/manager/{managerId}")
    @PreAuthorize("hasAuthority('PIPELINE:READ')")
    @Operation(summary = "Get pipelines by manager", description = "Retrieves all pipelines managed by a specific entity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipelines retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PipelineDTO>> getByManager(
            @Parameter(description = "Manager ID", required = true, example = "1") 
            @PathVariable Long managerId) {
        log.info("REST request to get Pipeline by manager structure id: {}", managerId);
        return ResponseEntity.ok(pipelineService.findByManager(managerId));
    }
}
