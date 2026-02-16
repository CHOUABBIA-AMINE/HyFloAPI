/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProcessingPlantController
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
import dz.sh.trc.hyflo.network.core.dto.ProcessingPlantDTO;
import dz.sh.trc.hyflo.network.core.service.ProcessingPlantService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/core/processingPlant")
@Slf4j
@Tag(name = "Processing Plant Management", description = "APIs for managing processing plants and facilities")
@SecurityRequirement(name = "bearer-auth")
public class ProcessingPlantController extends GenericController<ProcessingPlantDTO, Long> {

    private final ProcessingPlantService processingPlantService;

    public ProcessingPlantController(ProcessingPlantService processingPlantService) {
        super(processingPlantService, "ProcessingPlant");
        this.processingPlantService = processingPlantService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT:READ')")
    @Operation(summary = "Get processing plant by ID", description = "Retrieves a single processing plant by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Processing plant found", content = @Content(schema = @Schema(implementation = ProcessingPlantDTO.class))),
        @ApiResponse(responseCode = "404", description = "Processing plant not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProcessingPlantDTO> getById(
            @Parameter(description = "Processing plant ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT:READ')")
    @Operation(summary = "Get all processing plants (paginated)", description = "Retrieves a paginated list of all processing plants")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Processing plants retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ProcessingPlantDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT:READ')")
    @Operation(summary = "Get all processing plants (unpaginated)", description = "Retrieves all processing plants without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Processing plants retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProcessingPlantDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT:MANAGE')")
    @Operation(summary = "Create new processing plant", description = "Creates a new processing plant with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Processing plant created successfully", content = @Content(schema = @Schema(implementation = ProcessingPlantDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Processing plant name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProcessingPlantDTO> create(
            @Parameter(description = "Processing plant data", required = true) 
            @Valid @RequestBody ProcessingPlantDTO dto) {
        ProcessingPlantDTO created = processingPlantService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT:MANAGE')")
    @Operation(summary = "Update processing plant", description = "Updates an existing processing plant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Processing plant updated successfully", content = @Content(schema = @Schema(implementation = ProcessingPlantDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Processing plant not found"),
        @ApiResponse(responseCode = "409", description = "Processing plant name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProcessingPlantDTO> update(
            @Parameter(description = "Processing plant ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated processing plant data", required = true) @Valid @RequestBody ProcessingPlantDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT:MANAGE')")
    @Operation(summary = "Delete processing plant", description = "Deletes a processing plant permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Processing plant deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Processing plant not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete processing plant - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Processing plant ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT:READ')")
    @Operation(summary = "Search processing plants", description = "Searches processing plants by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ProcessingPlantDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<ProcessingPlantDTO> searchByQuery(String query, Pageable pageable) {
        return processingPlantService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT:READ')")
    @Operation(summary = "Check if processing plant exists", description = "Checks if a processing plant with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Processing plant ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT:READ')")
    @Operation(summary = "Count processing plants", description = "Returns the total number of processing plants in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Processing plant count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
