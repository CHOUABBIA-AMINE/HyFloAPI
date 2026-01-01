/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: BudgetTypeController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Plan
 *
 **/

package dz.mdn.iaas.business.plan.controller;

import dz.mdn.iaas.business.plan.dto.BudgetTypeDTO;
import dz.mdn.iaas.business.plan.service.BudgetTypeService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/plan/budgetType")
@Slf4j
public class BudgetTypeController extends GenericController<BudgetTypeDTO, Long> {

    private final BudgetTypeService budgetTypeService;

    public BudgetTypeController(BudgetTypeService budgetTypeService) {
        super(budgetTypeService, "BudgetType");
        this.budgetTypeService = budgetTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_TYPE:READ')")
    public ResponseEntity<BudgetTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_TYPE:READ')")
    public ResponseEntity<Page<BudgetTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_TYPE:READ')")
    public ResponseEntity<List<BudgetTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_TYPE:ADMIN')")
    public ResponseEntity<BudgetTypeDTO> create(@Valid @RequestBody BudgetTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_TYPE:ADMIN')")
    public ResponseEntity<BudgetTypeDTO> update(@PathVariable Long id, @Valid @RequestBody BudgetTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_TYPE:READ')")
    public ResponseEntity<Page<BudgetTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('BUDGET_TYPE:READ')")
    public ResponseEntity<List<BudgetTypeDTO>> getAllList() {
        log.debug("GET /budget-type/list");
        return success(budgetTypeService.getAll());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('BUDGET_TYPE:READ')")
    public ResponseEntity<List<BudgetTypeDTO>> getActiveTypes() {
        log.debug("GET /budget-type/active");
        return success(budgetTypeService.getActiveTypes());
    }

}
