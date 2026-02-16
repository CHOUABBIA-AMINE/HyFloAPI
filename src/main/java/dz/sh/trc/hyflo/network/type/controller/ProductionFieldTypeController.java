/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProductionFieldTypeController
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
import dz.sh.trc.hyflo.network.type.dto.ProductionFieldTypeDTO;
import dz.sh.trc.hyflo.network.type.service.ProductionFieldTypeService;
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
@RequestMapping("/network/type/productionField")
@Slf4j
@Tag(name = "Production Field Type Management", description = "APIs for managing production field type classifications")
@SecurityRequirement(name = "bearer-auth")
public class ProductionFieldTypeController extends GenericController<ProductionFieldTypeDTO, Long> {

    private final ProductionFieldTypeService productionFieldTypeService;

    public ProductionFieldTypeController(ProductionFieldTypeService productionFieldTypeService) {
        super(productionFieldTypeService, "ProductionFieldType");
        this.productionFieldTypeService = productionFieldTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    @Operation(summary = "Get production field type by ID", description = "Retrieves a single production field type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production field type found", content = @Content(schema = @Schema(implementation = ProductionFieldTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Production field type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductionFieldTypeDTO> getById(
            @Parameter(description = "Production field type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    @Operation(summary = "Get all production field types (paginated)", description = "Retrieves a paginated list of all production field types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production field types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ProductionFieldTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    @Operation(summary = "Get all production field types (unpaginated)", description = "Retrieves all production field types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production field types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProductionFieldTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:MANAGE')")
    @Operation(summary = "Create new production field type", description = "Creates a new production field type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Production field type created successfully", content = @Content(schema = @Schema(implementation = ProductionFieldTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Production field type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductionFieldTypeDTO> create(
            @Parameter(description = "Production field type data", required = true) 
            @Valid @RequestBody ProductionFieldTypeDTO dto) {
        ProductionFieldTypeDTO created = productionFieldTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:MANAGE')")
    @Operation(summary = "Update production field type", description = "Updates an existing production field type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production field type updated successfully", content = @Content(schema = @Schema(implementation = ProductionFieldTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Production field type not found"),
        @ApiResponse(responseCode = "409", description = "Production field type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductionFieldTypeDTO> update(
            @Parameter(description = "Production field type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated production field type data", required = true) @Valid @RequestBody ProductionFieldTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:MANAGE')")
    @Operation(summary = "Delete production field type", description = "Deletes a production field type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Production field type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Production field type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete production field type - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Production field type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    @Operation(summary = "Search production field types", description = "Searches production field types by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ProductionFieldTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<ProductionFieldTypeDTO> searchByQuery(String query, Pageable pageable) {
        return productionFieldTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    @Operation(summary = "Check if production field type exists", description = "Checks if a production field type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Production field type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    @Operation(summary = "Count production field types", description = "Returns the total number of production field types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production field type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
