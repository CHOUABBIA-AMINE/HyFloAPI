/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProductionFieldController
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
import dz.sh.trc.hyflo.network.core.dto.ProductionFieldDTO;
import dz.sh.trc.hyflo.network.core.service.ProductionFieldService;
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
@RequestMapping("/network/core/productionField")
@Slf4j
@Tag(name = "Production Field Management", description = "APIs for managing oil & gas production fields")
@SecurityRequirement(name = "bearer-auth")
public class ProductionFieldController extends GenericController<ProductionFieldDTO, Long> {

    private final ProductionFieldService productionFieldService;

    public ProductionFieldController(ProductionFieldService productionFieldService) {
        super(productionFieldService, "ProductionField");
        this.productionFieldService = productionFieldService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    @Operation(summary = "Get production field by ID", description = "Retrieves a single production field by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production field found", content = @Content(schema = @Schema(implementation = ProductionFieldDTO.class))),
        @ApiResponse(responseCode = "404", description = "Production field not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductionFieldDTO> getById(
            @Parameter(description = "Production field ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    @Operation(summary = "Get all production fields (paginated)", description = "Retrieves a paginated list of all production fields")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production fields retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ProductionFieldDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    @Operation(summary = "Get all production fields (unpaginated)", description = "Retrieves all production fields without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production fields retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProductionFieldDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:MANAGE')")
    @Operation(summary = "Create new production field", description = "Creates a new production field with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Production field created successfully", content = @Content(schema = @Schema(implementation = ProductionFieldDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Production field name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductionFieldDTO> create(
            @Parameter(description = "Production field data", required = true) 
            @Valid @RequestBody ProductionFieldDTO dto) {
        ProductionFieldDTO created = productionFieldService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:MANAGE')")
    @Operation(summary = "Update production field", description = "Updates an existing production field")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production field updated successfully", content = @Content(schema = @Schema(implementation = ProductionFieldDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Production field not found"),
        @ApiResponse(responseCode = "409", description = "Production field name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ProductionFieldDTO> update(
            @Parameter(description = "Production field ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated production field data", required = true) @Valid @RequestBody ProductionFieldDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:MANAGE')")
    @Operation(summary = "Delete production field", description = "Deletes a production field permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Production field deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Production field not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete production field - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Production field ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    @Operation(summary = "Search production fields", description = "Searches production fields by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<ProductionFieldDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<ProductionFieldDTO> searchByQuery(String query, Pageable pageable) {
        return productionFieldService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    @Operation(summary = "Check if production field exists", description = "Checks if a production field with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Production field ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    @Operation(summary = "Count production fields", description = "Returns the total number of production fields in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production field count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/plant/{plantId}")
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    @Operation(summary = "Get production fields by processing plant", description = "Retrieves all production fields connected to a specific processing plant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production fields retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProductionFieldDTO>> getByProcessingPlant(
            @Parameter(description = "Processing plant ID", required = true, example = "1") 
            @PathVariable Long plantId) {
        log.info("REST request to get productionField by processing plant id: {}", plantId);
        return ResponseEntity.ok(productionFieldService.findByProcessingPlant(plantId));
    }

    @GetMapping("/status/{statusId}")
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    @Operation(summary = "Get production fields by operational status", description = "Retrieves all production fields with a specific operational status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production fields retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProductionFieldDTO>> getByOperationalStatus(
            @Parameter(description = "Operational status ID", required = true, example = "1") 
            @PathVariable Long statusId) {
        log.info("REST request to get productionField by operational status id: {}", statusId);
        return ResponseEntity.ok(productionFieldService.findByOperationalStatus(statusId));
    }

    @GetMapping("/location/{locationId}")
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    @Operation(summary = "Get production fields by location", description = "Retrieves all production fields in a specific location")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production fields retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProductionFieldDTO>> getByLocation(
            @Parameter(description = "Location ID", required = true, example = "1") 
            @PathVariable Long locationId) {
        log.info("REST request to get productionField by location id: {}", locationId);
        return ResponseEntity.ok(productionFieldService.findByLocation(locationId));
    }

    @GetMapping("/type/{typeId}")
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    @Operation(summary = "Get production fields by type", description = "Retrieves all production fields of a specific type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Production fields retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PRODUCTION_FIELD:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProductionFieldDTO>> getByProductionFieldType(
            @Parameter(description = "Production field type ID", required = true, example = "1") 
            @PathVariable Long typeId) {
        log.info("REST request to get productionField by production field type id: {}", typeId);
        return ResponseEntity.ok(productionFieldService.findByProductionFieldType(typeId));
    }
}
