/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ContractStepController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *	@Type		: Controller
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Controller
 *
 **/

package dz.mdn.iaas.business.contract.controller;

import dz.mdn.iaas.business.contract.dto.ContractStepDTO;
import dz.mdn.iaas.business.contract.service.ContractStepService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/contract/contractStep")
@Slf4j
public class ContractStepController extends GenericController<ContractStepDTO, Long> {

    private final ContractStepService contractStepService;

    public ContractStepController(ContractStepService contractStepService) {
        super(contractStepService, "ContractStep");
        this.contractStepService = contractStepService;
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_STEP:READ')")
    public ResponseEntity<ContractStepDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_STEP:READ')")
    public ResponseEntity<Page<ContractStepDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_STEP:READ')")
    public ResponseEntity<List<ContractStepDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_STEP:ADMIN')")
    public ResponseEntity<ContractStepDTO> create(@Valid @RequestBody ContractStepDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_STEP:ADMIN')")
    public ResponseEntity<ContractStepDTO> update(@PathVariable Long id, @Valid @RequestBody ContractStepDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_STEP:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_STEP:READ')")
    public ResponseEntity<Page<ContractStepDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_STEP:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_STEP:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('CONTRACT_STEP:READ')")
    public ResponseEntity<List<ContractStepDTO>> getAllList() {
        log.debug("GET /contract-step/list");
        return success(contractStepService.getAll());
    }

    @GetMapping("/phase/{phaseId}")
    @PreAuthorize("hasAuthority('CONTRACT_STEP:READ')")
    public ResponseEntity<List<ContractStepDTO>> getByPhase(@PathVariable Long phaseId) {
        log.debug("GET /contract-step/phase/{}", phaseId);
        return success(contractStepService.getByPhaseId(phaseId));
    }
}
