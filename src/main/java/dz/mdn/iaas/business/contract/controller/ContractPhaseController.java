/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ContractPhaseController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *	@Type		: Controller
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Controller
 *
 **/

package dz.mdn.iaas.business.contract.controller;

import dz.mdn.iaas.business.contract.dto.ContractPhaseDTO;
import dz.mdn.iaas.business.contract.service.ContractPhaseService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/contract/contractPhase")
@Slf4j
public class ContractPhaseController extends GenericController<ContractPhaseDTO, Long> {

    private final ContractPhaseService contractPhaseService;

    public ContractPhaseController(ContractPhaseService contractPhaseService) {
        super(contractPhaseService, "ContractPhase");
        this.contractPhaseService = contractPhaseService;
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_PHASE:READ')")
    public ResponseEntity<ContractPhaseDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_PHASE:READ')")
    public ResponseEntity<Page<ContractPhaseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_PHASE:READ')")
    public ResponseEntity<List<ContractPhaseDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_PHASE:ADMIN')")
    public ResponseEntity<ContractPhaseDTO> create(@Valid @RequestBody ContractPhaseDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_PHASE:ADMIN')")
    public ResponseEntity<ContractPhaseDTO> update(@PathVariable Long id, @Valid @RequestBody ContractPhaseDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_PHASE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_PHASE:READ')")
    public ResponseEntity<Page<ContractPhaseDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_PHASE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_PHASE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('CONTRACT_PHASE:READ')")
    public ResponseEntity<List<ContractPhaseDTO>> getAllList() {
        log.debug("GET /contract-phase/list");
        return success(contractPhaseService.getAll());
    }
}
