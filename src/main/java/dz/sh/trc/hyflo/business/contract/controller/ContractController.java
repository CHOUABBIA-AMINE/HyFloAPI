/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ContractController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *	@Type		: Controller
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Controller
 *
 **/

package dz.sh.trc.hyflo.business.contract.controller;

import dz.sh.trc.hyflo.business.contract.dto.ContractDTO;
import dz.sh.trc.hyflo.business.contract.service.ContractService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/contract/contract")
@Slf4j
public class ContractController extends GenericController<ContractDTO, Long> {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        super(contractService, "Contract");
        this.contractService = contractService;
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT:READ')")
    public ResponseEntity<ContractDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT:READ')")
    public ResponseEntity<Page<ContractDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT:READ')")
    public ResponseEntity<List<ContractDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT:ADMIN')")
    public ResponseEntity<ContractDTO> create(@Valid @RequestBody ContractDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT:ADMIN')")
    public ResponseEntity<ContractDTO> update(@PathVariable Long id, @Valid @RequestBody ContractDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT:READ')")
    public ResponseEntity<Page<ContractDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<ContractDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return contractService.getAll(pageable);
        }
        return contractService.globalSearch(query, pageable);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('CONTRACT:READ')")
    public ResponseEntity<List<ContractDTO>> getAllList() {
        log.debug("GET /contract/list");
        return success(contractService.getAll());
    }
}
