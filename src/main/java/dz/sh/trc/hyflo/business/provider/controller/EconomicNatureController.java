/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: EconomicNatureController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Controller
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Controller
 *
 **/

package dz.sh.trc.hyflo.business.provider.controller;

import dz.sh.trc.hyflo.business.provider.dto.EconomicNatureDTO;
import dz.sh.trc.hyflo.business.provider.service.EconomicNatureService;
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
@RequestMapping("/business/provider/economicNature")
@Slf4j
public class EconomicNatureController extends GenericController<EconomicNatureDTO, Long> {

    private final EconomicNatureService economicNatureService;

    public EconomicNatureController(EconomicNatureService economicNatureService) {
        super(economicNatureService, "EconomicNature");
        this.economicNatureService = economicNatureService;
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_NATURE:READ')")
    public ResponseEntity<EconomicNatureDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_NATURE:READ')")
    public ResponseEntity<Page<EconomicNatureDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_NATURE:READ')")
    public ResponseEntity<List<EconomicNatureDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_NATURE:ADMIN')")
    public ResponseEntity<EconomicNatureDTO> create(@Valid @RequestBody EconomicNatureDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_NATURE:ADMIN')")
    public ResponseEntity<EconomicNatureDTO> update(@PathVariable Long id, @Valid @RequestBody EconomicNatureDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_NATURE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_NATURE:READ')")
    public ResponseEntity<Page<EconomicNatureDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_NATURE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ECONOMIC_NATURE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<EconomicNatureDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return economicNatureService.getAll(pageable);
        }
        return economicNatureService.globalSearch(query, pageable);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ECONOMIC_NATURE:READ')")
    public ResponseEntity<List<EconomicNatureDTO>> getAllList() {
        log.debug("GET /economic-nature/list");
        return success(economicNatureService.getAll());
    }
}
