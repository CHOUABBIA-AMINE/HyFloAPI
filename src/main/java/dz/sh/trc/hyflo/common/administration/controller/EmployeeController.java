/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EmployeeController
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Administration
 *
 **/

package dz.sh.trc.hyflo.common.administration.controller;

import dz.sh.trc.hyflo.common.administration.dto.EmployeeDTO;
import dz.sh.trc.hyflo.common.administration.service.EmployeeService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Employee REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus employee-specific operations
 */
@RestController
@RequestMapping("/common/administration/employee")
@Slf4j
public class EmployeeController extends GenericController<EmployeeDTO, Long> {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        super(employeeService, "Employee");
        this.employeeService = employeeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    public ResponseEntity<Page<EmployeeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    public ResponseEntity<List<EmployeeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:ADMIN')")
    public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:ADMIN')")
    public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    public ResponseEntity<Page<EmployeeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EMPLOYEE:READ')")
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
    public ResponseEntity<List<EmployeeDTO>> getByStructureId(@PathVariable Long structureId) {
        log.debug("GET /employee/structure/{} - Getting employees by structure ID", structureId);
        List<EmployeeDTO> employees = employeeService.getByStructureId(structureId);
        return success(employees);
    }
}
