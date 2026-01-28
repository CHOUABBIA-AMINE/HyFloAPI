/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CountryController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.general.localization.dto.CountryDTO;
import dz.sh.trc.hyflo.general.localization.service.CountryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/general/localization/country")
@Slf4j
public class CountryController extends GenericController<CountryDTO, Long> {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        super(countryService, "Country");
        this.countryService = countryService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    public ResponseEntity<CountryDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    public ResponseEntity<Page<CountryDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    public ResponseEntity<List<CountryDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:MANAGE')")
    public ResponseEntity<CountryDTO> create(@Valid @RequestBody CountryDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:MANAGE')")
    public ResponseEntity<CountryDTO> update(@PathVariable Long id, @Valid @RequestBody CountryDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    public ResponseEntity<Page<CountryDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<CountryDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return countryService.getAll(pageable);
        }
        return countryService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all countries as list (non-paginated)
     * GET /country/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    public ResponseEntity<List<CountryDTO>> getAllList() {
        log.debug("GET /country/list - Getting all countries as list");
        List<CountryDTO> countries = countryService.getAll();
        return success(countries);
    }
}
