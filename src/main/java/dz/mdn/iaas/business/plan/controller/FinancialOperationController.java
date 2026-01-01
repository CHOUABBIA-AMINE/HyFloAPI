/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FinancialOperationController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Plan
 *
 **/

package dz.mdn.iaas.business.plan.controller;

import dz.mdn.iaas.business.plan.dto.FinancialOperationDTO;
import dz.mdn.iaas.business.plan.service.FinancialOperationService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/plan/financialOperation")
@Slf4j
public class FinancialOperationController extends GenericController<FinancialOperationDTO, Long> {

    private final FinancialOperationService financialOperationService;

    public FinancialOperationController(FinancialOperationService financialOperationService) {
        super(financialOperationService, "FinancialOperation");
        this.financialOperationService = financialOperationService;
    }

    @Override
    @PreAuthorize("hasAuthority('FINANCIAL_OPERATION:READ')")
    public ResponseEntity<FinancialOperationDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FINANCIAL_OPERATION:READ')")
    public ResponseEntity<Page<FinancialOperationDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FINANCIAL_OPERATION:READ')")
    public ResponseEntity<List<FinancialOperationDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FINANCIAL_OPERATION:ADMIN')")
    public ResponseEntity<FinancialOperationDTO> create(@Valid @RequestBody FinancialOperationDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FINANCIAL_OPERATION:ADMIN')")
    public ResponseEntity<FinancialOperationDTO> update(@PathVariable Long id, @Valid @RequestBody FinancialOperationDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FINANCIAL_OPERATION:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FINANCIAL_OPERATION:READ')")
    public ResponseEntity<Page<FinancialOperationDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FINANCIAL_OPERATION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FINANCIAL_OPERATION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('FINANCIAL_OPERATION:READ')")
    public ResponseEntity<List<FinancialOperationDTO>> getAllList() {
        log.debug("GET /financial-operation/list");
        return success(financialOperationService.getAll());
    }

    @GetMapping("/year/{year}")
    @PreAuthorize("hasAuthority('FINANCIAL_OPERATION:READ')")
    public ResponseEntity<List<FinancialOperationDTO>> getByYear(@PathVariable Integer year) {
        log.debug("GET /financial-operation/year/{}", year);
        return success(financialOperationService.getByYear(year));
    }
}
