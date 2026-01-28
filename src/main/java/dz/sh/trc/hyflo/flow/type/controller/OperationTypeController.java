/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: OperationTypeController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.type.dto.OperationTypeDTO;
import dz.sh.trc.hyflo.flow.type.service.OperationTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for OperationType entity.
 * Provides CRUD endpoints and search functionality for flow operation type management.
 */
@RestController
@RequestMapping("/flow/type/operation")
@Tag(name = "Operation Type", description = "Flow operation type management API")
@Slf4j
public class OperationTypeController extends GenericController<OperationTypeDTO, Long> {

    private final OperationTypeService operationTypeService;
    
    public OperationTypeController(OperationTypeService operationTypeService) {
        super(operationTypeService, "OperationType");
        this.operationTypeService = operationTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get operation type by ID", description = "Retrieve a single operation type by its unique identifier")
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<OperationTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all operation types (paginated)", description = "Retrieve all operation types with pagination and sorting")
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<Page<OperationTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all operation types (no pagination)", description = "Retrieve all operation types sorted by code")
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<List<OperationTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create operation type", description = "Create a new operation type with unique code validation")
    @PreAuthorize("hasAuthority('OPERATION_TYPE:MANAGE')")
    public ResponseEntity<OperationTypeDTO> create(@Valid @RequestBody OperationTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @Operation(summary = "Update operation type", description = "Update an existing operation type by ID")
    @PreAuthorize("hasAuthority('OPERATION_TYPE:MANAGE')")
    public ResponseEntity<OperationTypeDTO> update(@PathVariable Long id, @Valid @RequestBody OperationTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete operation type", description = "Delete an operation type by ID")
    @PreAuthorize("hasAuthority('OPERATION_TYPE:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search operation types", description = "Search operation types across all fields with pagination")
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<Page<OperationTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if operation type exists", description = "Check if an operation type exists by ID")
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count operation types", description = "Get total count of operation types")
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get operation type by code", description = "Find operation type by its unique code (PRODUCED, TRANSPORTED, CONSUMED)")
    @PreAuthorize("hasAuthority('OPERATION_TYPE:READ')")
    public ResponseEntity<OperationTypeDTO> getByCode(@PathVariable String code) {
        log.info("GET /flow/type/operation/code/{} - Getting operation type by code", code);
        return ResponseEntity.ok(operationTypeService.findByCode(code));
    }
}
