/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CompanyTypeController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.type.dto.CompanyTypeDTO;
import dz.sh.trc.hyflo.network.type.service.CompanyTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/type/company")
@Slf4j
public class CompanyTypeController extends GenericController<CompanyTypeDTO, Long> {

    private final CompanyTypeService companyTypeService;

    public CompanyTypeController(CompanyTypeService companyTypeService) {
        super(companyTypeService, "CompanyType");
        this.companyTypeService = companyTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    public ResponseEntity<CompanyTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    public ResponseEntity<Page<CompanyTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    public ResponseEntity<List<CompanyTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:ADMIN')")
    public ResponseEntity<CompanyTypeDTO> create(@Valid @RequestBody CompanyTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:ADMIN')")
    public ResponseEntity<CompanyTypeDTO> update(@PathVariable Long id, @Valid @RequestBody CompanyTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    public ResponseEntity<Page<CompanyTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    protected Page<CompanyTypeDTO> searchByQuery(String query, Pageable pageable) {
        return companyTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
