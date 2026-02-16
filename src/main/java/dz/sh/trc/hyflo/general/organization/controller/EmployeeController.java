/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: EmployeeController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.service.EmployeeService;
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

/**
 * Employee REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus employee-specific operations
 */
@RestController
@RequestMapping("/general/organization/employee")
@Slf4j
@Tag(name = "Employee Management", description = "APIs for managing company employees and workforce")
@SecurityRequirement(name = "bearer-auth")
public class EmployeeController extends GenericController<EmployeeDTO, Long> {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        super(employeeService, "Employee");
        this.employeeService = employeeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    @Operation(summary = "Get employee by ID", description = "Retrieves a single employee by their unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee found", content = @Content(schema = @Schema(implementation = EmployeeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EMPLOYEE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EmployeeDTO> getById(
            @Parameter(description = "Employee ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    @Operation(summary = "Get all employees (paginated)", description = "Retrieves a paginated list of all employees")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EMPLOYEE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<EmployeeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    @Operation(summary = "Get all employees (unpaginated)", description = "Retrieves all employees without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EMPLOYEE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EmployeeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:MANAGE')")
    @Operation(summary = "Create new employee", description = "Creates a new employee with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Employee created successfully", content = @Content(schema = @Schema(implementation = EmployeeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Employee number/email already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EMPLOYEE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EmployeeDTO> create(
            @Parameter(description = "Employee data", required = true) 
            @Valid @RequestBody EmployeeDTO dto) {
        EmployeeDTO created = employeeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:MANAGE')")
    @Operation(summary = "Update employee", description = "Updates an existing employee")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee updated successfully", content = @Content(schema = @Schema(implementation = EmployeeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "409", description = "Employee number/email already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EMPLOYEE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EmployeeDTO> update(
            @Parameter(description = "Employee ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated employee data", required = true) @Valid @RequestBody EmployeeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:MANAGE')")
    @Operation(summary = "Delete employee", description = "Deletes an employee permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete employee - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EMPLOYEE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Employee ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    @Operation(summary = "Search employees", description = "Searches employees by name, number, email, or job title (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EMPLOYEE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<EmployeeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    @Operation(summary = "Check if employee exists", description = "Checks if an employee with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EMPLOYEE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Employee ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    @Operation(summary = "Count employees", description = "Returns the total number of employees in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EMPLOYEE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<EmployeeDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return employeeService.getAll(pageable);
        }
        return employeeService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all employees as list (non-paginated)
     * GET /employee/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    @Operation(summary = "Get all employees as list", description = "Retrieves all employees as a simple list without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee list retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EMPLOYEE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EmployeeDTO>> getAllList() {
        log.debug("GET /employee/list - Getting all employees as list");
        List<EmployeeDTO> employees = employeeService.getAll();
        return success(employees);
    }

    /**
     * Get employees by structure ID
     * GET /employee/structure/{structureId}
     */
    @GetMapping("/structure/{structureId}")
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    @Operation(summary = "Get employees by structure", description = "Retrieves all employees belonging to a specific organizational structure")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Structure not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires EMPLOYEE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EmployeeDTO>> getByStructureId(
            @Parameter(description = "Structure ID", required = true, example = "1") 
            @PathVariable Long structureId) {
        log.debug("GET /employee/structure/{} - Getting employees by structure ID", structureId);
        List<EmployeeDTO> employees = employeeService.getByStructureId(structureId);
        return success(employees);
    }
}
