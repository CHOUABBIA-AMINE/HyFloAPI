/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: OperationTypeController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.type.dto.OperationTypeDTO;
import dz.sh.trc.hyflo.flow.type.service.OperationTypeService;
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

/**
 * REST Controller for OperationType entity.
 * Provides CRUD endpoints and search functionality for flow operation type management.
 */
@RestController
@RequestMapping("/flow/type/operation")
@Tag(name = "Operation Type Management", description = "APIs for managing flow operation types")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class OperationTypeController extends GenericController<OperationTypeDTO, Long> {

    private final OperationTypeService operationTypeService;
    
    public OperationTypeController(OperationTypeService operationTypeService) {
        super(operationTypeService, "OperationType");
        this.operationTypeService = operationTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get operation type by ID", description = "Retrieves a single operation type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operation type found", content = @Content(schema = @Schema(implementation = OperationTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Operation type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<OperationTypeDTO> getById(
            @Parameter(description = "Operation type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all operation types (paginated)", description = "Retrieves a paginated list of all operation types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operation types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<Page<OperationTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all operation types (unpaginated)", description = "Retrieves all operation types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operation types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<List<OperationTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create operation type", description = "Creates a new operation type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Operation type created successfully", content = @Content(schema = @Schema(implementation = OperationTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Operation type code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATION_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('OPERATION_TYPE:MANAGE')")
    public ResponseEntity<OperationTypeDTO> create(
            @Parameter(description = "Operation type data", required = true) 
            @Valid @RequestBody OperationTypeDTO dto) {
        OperationTypeDTO created = operationTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update operation type", description = "Updates an existing operation type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operation type updated successfully", content = @Content(schema = @Schema(implementation = OperationTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Operation type not found"),
        @ApiResponse(responseCode = "409", description = "Operation type code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATION_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('OPERATION_TYPE:MANAGE')")
    public ResponseEntity<OperationTypeDTO> update(
            @Parameter(description = "Operation type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated operation type data", required = true) @Valid @RequestBody OperationTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete operation type", description = "Deletes an operation type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Operation type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Operation type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete operation type - has dependencies (operations using this type)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATION_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('OPERATION_TYPE:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Operation type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search operation types", description = "Searches operation types by code or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<Page<OperationTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if operation type exists", description = "Checks if an operation type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Operation type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count operation types", description = "Returns the total number of operation types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operation type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get operation type by code", description = "Retrieves an operation type by its unique code (e.g., PRODUCED, TRANSPORTED, CONSUMED)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operation type found", content = @Content(schema = @Schema(implementation = OperationTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Operation type not found with given code"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires OPERATION_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<OperationTypeDTO> getByCode(
            @Parameter(description = "Operation type code", required = true, example = "PRODUCED") 
            @PathVariable String code) {
        log.info("GET /flow/type/operation/code/{} - Getting operation type by code", code);
        return ResponseEntity.ok(operationTypeService.findByCode(code));
    }
}
