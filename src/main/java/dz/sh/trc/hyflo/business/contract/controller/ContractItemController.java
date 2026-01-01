/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ContractItemController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *	@Type		: Controller
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Controller
 *
 **/

package dz.sh.trc.hyflo.business.contract.controller;

import dz.sh.trc.hyflo.business.contract.dto.ContractItemDTO;
import dz.sh.trc.hyflo.business.contract.service.ContractItemService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/contract/contractItem")
@Slf4j
public class ContractItemController extends GenericController<ContractItemDTO, Long> {

    private final ContractItemService contractItemService;

    public ContractItemController(ContractItemService contractItemService) {
        super(contractItemService, "ContractItem");
        this.contractItemService = contractItemService;
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_ITEM:READ')")
    public ResponseEntity<ContractItemDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_ITEM:READ')")
    public ResponseEntity<Page<ContractItemDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_ITEM:READ')")
    public ResponseEntity<List<ContractItemDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_ITEM:ADMIN')")
    public ResponseEntity<ContractItemDTO> create(@Valid @RequestBody ContractItemDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_ITEM:ADMIN')")
    public ResponseEntity<ContractItemDTO> update(@PathVariable Long id, @Valid @RequestBody ContractItemDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_ITEM:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_ITEM:READ')")
    public ResponseEntity<Page<ContractItemDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_ITEM:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT_ITEM:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('CONTRACT_ITEM:READ')")
    public ResponseEntity<List<ContractItemDTO>> getAllList() {
        log.debug("GET /contract-item/list");
        return success(contractItemService.getAll());
    }

    @GetMapping("/contract/{contractId}")
    @PreAuthorize("hasAuthority('CONTRACT_ITEM:READ')")
    public ResponseEntity<List<ContractItemDTO>> getByContract(@PathVariable Long contractId) {
        log.debug("GET /contract-item/contract/{}", contractId);
        return success(contractItemService.getByContractId(contractId));
    }
}
