/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSystemController
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
import dz.sh.trc.hyflo.network.core.dto.PipelineSystemDTO;
import dz.sh.trc.hyflo.network.core.service.PipelineSystemService;
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
@RequestMapping("/network/core/pipelineSystem")
@Slf4j
@Tag(name = "Pipeline System Management", description = "APIs for managing pipeline systems and networks")
@SecurityRequirement(name = "bearer-auth")
public class PipelineSystemController extends GenericController<PipelineSystemDTO, Long> {

    private final PipelineSystemService pipelineSystemService;

    public PipelineSystemController(PipelineSystemService pipelineSystemService) {
        super(pipelineSystemService, "PipelineSystem");
        this.pipelineSystemService = pipelineSystemService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    @Operation(summary = "Get pipeline system by ID", description = "Retrieves a single pipeline system by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline system found", content = @Content(schema = @Schema(implementation = PipelineSystemDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pipeline system not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SYSTEM:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PipelineSystemDTO> getById(
            @Parameter(description = "Pipeline system ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    @Operation(summary = "Get all pipeline systems (paginated)", description = "Retrieves a paginated list of all pipeline systems")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline systems retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SYSTEM:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PipelineSystemDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    @Operation(summary = "Get all pipeline systems (unpaginated)", description = "Retrieves all pipeline systems without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline systems retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SYSTEM:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PipelineSystemDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:MANAGE')")
    @Operation(summary = "Create new pipeline system", description = "Creates a new pipeline system with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pipeline system created successfully", content = @Content(schema = @Schema(implementation = PipelineSystemDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Pipeline system name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SYSTEM:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PipelineSystemDTO> create(
            @Parameter(description = "Pipeline system data", required = true) 
            @Valid @RequestBody PipelineSystemDTO dto) {
        PipelineSystemDTO created = pipelineSystemService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:MANAGE')")
    @Operation(summary = "Update pipeline system", description = "Updates an existing pipeline system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline system updated successfully", content = @Content(schema = @Schema(implementation = PipelineSystemDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Pipeline system not found"),
        @ApiResponse(responseCode = "409", description = "Pipeline system name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SYSTEM:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PipelineSystemDTO> update(
            @Parameter(description = "Pipeline system ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated pipeline system data", required = true) @Valid @RequestBody PipelineSystemDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:MANAGE')")
    @Operation(summary = "Delete pipeline system", description = "Deletes a pipeline system permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pipeline system deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Pipeline system not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete pipeline system - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SYSTEM:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Pipeline system ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    @Operation(summary = "Search pipeline systems", description = "Searches pipeline systems by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SYSTEM:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PipelineSystemDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    @Operation(summary = "Check if pipeline system exists", description = "Checks if a pipeline system with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SYSTEM:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Pipeline system ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    @Operation(summary = "Count pipeline systems", description = "Returns the total number of pipeline systems in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline system count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SYSTEM:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    @Operation(summary = "Get pipeline systems by product", description = "Retrieves all pipeline systems transporting a specific product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline systems retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SYSTEM:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PipelineSystemDTO>> getByProduct(
            @Parameter(description = "Product ID", required = true, example = "1") 
            @PathVariable Long productId) {
        log.info("REST request to get Pipeline System by product id: {}", productId);
        return ResponseEntity.ok(pipelineSystemService.findByProduct(productId));
    }

    @GetMapping("/status/{operationalStatusId}")
    @PreAuthorize("hasAuthority('PIPELINE_SYSTEM:READ')")
    @Operation(summary = "Get pipeline systems by operational status", description = "Retrieves all pipeline systems with a specific operational status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pipeline systems retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PIPELINE_SYSTEM:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PipelineSystemDTO>> getByOperationalStatus(
            @Parameter(description = "Operational status ID", required = true, example = "1") 
            @PathVariable Long operationalStatusId) {
        log.info("REST request to get Pipeline System by operational status id: {}", operationalStatusId);
        return ResponseEntity.ok(pipelineSystemService.findByOperationalStatus(operationalStatusId));
    }
}
