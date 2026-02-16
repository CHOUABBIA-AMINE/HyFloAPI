/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProcessingPlantTypeController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.type.dto.ProcessingPlantTypeDTO;
import dz.sh.trc.hyflo.network.type.service.ProcessingPlantTypeService;
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
@RequestMapping("/network/type/processingPlant")
@Slf4j
@Tag(name = "Processing Plant Type Management", description = "APIs for managing processing plant type classifications")
@SecurityRequirement(name = "bearer-auth")
public class ProcessingPlantTypeController extends GenericController<ProcessingPlantTypeDTO, Long> {

    private final ProcessingPlantTypeService processingPlantTypeService;

    public ProcessingPlantTypeController(ProcessingPlantTypeService processingPlantTypeService) {
        super(processingPlantTypeService, "ProcessingPlantType");
        this.processingPlantTypeService = processingPlantTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT_TYPE:READ')")
    @Operation(summary = "Get processing plant type by ID", description = "Retrieves a single processing plant type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Processing plant type found", content = @Content(schema = @Schema(implementation = ProcessingPlantTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Processing plant type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProcessingPlantTypeDTO> getById(
            @Parameter(description = "Processing plant type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT_TYPE:READ')")
    @Operation(summary = "Get all processing plant types (paginated)", description = "Retrieves a paginated list of all processing plant types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Processing plant types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ProcessingPlantTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT_TYPE:READ')")
    @Operation(summary = "Get all processing plant types (unpaginated)", description = "Retrieves all processing plant types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Processing plant types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProcessingPlantTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT_TYPE:MANAGE')")
    @Operation(summary = "Create new processing plant type", description = "Creates a new processing plant type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Processing plant type created successfully", content = @Content(schema = @Schema(implementation = ProcessingPlantTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Processing plant type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProcessingPlantTypeDTO> create(
            @Parameter(description = "Processing plant type data", required = true) 
            @Valid @RequestBody ProcessingPlantTypeDTO dto) {
        ProcessingPlantTypeDTO created = processingPlantTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT_TYPE:MANAGE')")
    @Operation(summary = "Update processing plant type", description = "Updates an existing processing plant type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Processing plant type updated successfully", content = @Content(schema = @Schema(implementation = ProcessingPlantTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Processing plant type not found"),
        @ApiResponse(responseCode = "409", description = "Processing plant type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProcessingPlantTypeDTO> update(
            @Parameter(description = "Processing plant type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated processing plant type data", required = true) @Valid @RequestBody ProcessingPlantTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT_TYPE:MANAGE')")
    @Operation(summary = "Delete processing plant type", description = "Deletes a processing plant type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Processing plant type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Processing plant type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete processing plant type - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Processing plant type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT_TYPE:READ')")
    @Operation(summary = "Search processing plant types", description = "Searches processing plant types by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ProcessingPlantTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<ProcessingPlantTypeDTO> searchByQuery(String query, Pageable pageable) {
        return processingPlantTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT_TYPE:READ')")
    @Operation(summary = "Check if processing plant type exists", description = "Checks if a processing plant type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Processing plant type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PROCESSING_PLANT_TYPE:READ')")
    @Operation(summary = "Count processing plant types", description = "Returns the total number of processing plant types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Processing plant type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PROCESSING_PLANT_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
