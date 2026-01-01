/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: BudgetModificationController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Plan
 *
 **/

package dz.mdn.iaas.business.plan.controller;

import dz.mdn.iaas.business.plan.dto.BudgetModificationDTO;
import dz.mdn.iaas.business.plan.service.BudgetModificationService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/plan/budgetModification")
@Slf4j
public class BudgetModificationController extends GenericController<BudgetModificationDTO, Long> {

    private final BudgetModificationService budgetModificationService;

    public BudgetModificationController(BudgetModificationService budgetModificationService) {
        super(budgetModificationService, "BudgetModification");
        this.budgetModificationService = budgetModificationService;
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_MODIFICATION:READ')")
    public ResponseEntity<BudgetModificationDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_MODIFICATION:READ')")
    public ResponseEntity<Page<BudgetModificationDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_MODIFICATION:READ')")
    public ResponseEntity<List<BudgetModificationDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_MODIFICATION:ADMIN')")
    public ResponseEntity<BudgetModificationDTO> create(@Valid @RequestBody BudgetModificationDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_MODIFICATION:ADMIN')")
    public ResponseEntity<BudgetModificationDTO> update(@PathVariable Long id, @Valid @RequestBody BudgetModificationDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_MODIFICATION:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_MODIFICATION:READ')")
    public ResponseEntity<Page<BudgetModificationDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_MODIFICATION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('BUDGET_MODIFICATION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('BUDGET_MODIFICATION:READ')")
    public ResponseEntity<List<BudgetModificationDTO>> getAllList() {
        log.debug("GET /budget-modification/list");
        return success(budgetModificationService.getAll());
    }

    @GetMapping("/year/{year}")
    @PreAuthorize("hasAuthority('BUDGET_MODIFICATION:READ')")
    public ResponseEntity<List<BudgetModificationDTO>> getByYear(@PathVariable Integer year) {
        log.debug("GET /budget-modification/year/{}", year);
        return success(budgetModificationService.getByYear(year));
    }
}
