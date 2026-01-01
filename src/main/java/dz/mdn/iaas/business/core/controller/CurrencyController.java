/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: CurrencyController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Core
 *
 **/

package dz.mdn.iaas.business.core.controller;

import dz.mdn.iaas.business.core.dto.CurrencyDTO;
import dz.mdn.iaas.business.core.service.CurrencyService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Currency REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus currency-specific operations
 * 
 * Inherited Endpoints:
 * - POST   /currency                 Create currency
 * - GET    /currency/{id}            Get by ID
 * - GET    /currency                 Get all (paginated)
 * - GET    /currency/all             Get all (non-paginated)
 * - PUT    /currency/{id}            Update currency
 * - DELETE /currency/{id}            Delete currency
 * - GET    /currency/search?q=...    Global search
 * - GET    /currency/{id}/exists     Check existence
 * - GET    /currency/count           Total count
 */
@RestController
@RequestMapping("/business/core/currency")
@Slf4j
public class CurrencyController extends GenericController<CurrencyDTO, Long> {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        super(currencyService, "Currency");
        this.currencyService = currencyService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<CurrencyDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<Page<CurrencyDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<List<CurrencyDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('CURRENCY:ADMIN')")
    public ResponseEntity<CurrencyDTO> create(@Valid @RequestBody CurrencyDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CURRENCY:ADMIN')")
    public ResponseEntity<CurrencyDTO> update(@PathVariable Long id, @Valid @RequestBody CurrencyDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('CURRENCY:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<Page<CurrencyDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<CurrencyDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return currencyService.getAll(pageable);
        }
        return currencyService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all currencies without pagination (custom implementation)
     * GET /currency/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<List<CurrencyDTO>> getAllList() {
        log.debug("GET /currency/list - Getting all currencies as list");
        List<CurrencyDTO> currencies = currencyService.getAll();
        return success(currencies);
    }

    /**
     * Search currencies by designation
     * GET /currency/search/designation?q=...
     */
    @GetMapping("/search/designation")
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<Page<CurrencyDTO>> searchByDesignation(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "designationFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("GET /currency/search/designation?q={}", q);
        
        Pageable pageable = buildPageable(page, size, sortBy, sortDir);
        Page<CurrencyDTO> results = currencyService.searchByDesignation(q, pageable);
        
        return success(results);
    }

    /**
     * Search currencies by acronym
     * GET /currency/search/acronym?q=...
     */
    @GetMapping("/search/acronym")
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<Page<CurrencyDTO>> searchByAcronym(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "code") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("GET /currency/search/acronym?q={}", q);
        
        Pageable pageable = buildPageable(page, size, sortBy, sortDir);
        Page<CurrencyDTO> results = currencyService.searchByAcronym(q, pageable);
        
        return success(results);
    }

    // ========== VALIDATION ENDPOINTS ==========

    /**
     * Check if currency exists by code
     * GET /currency/exists/code/{code}
     */
    @GetMapping("/exists/code/{code}")
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        log.debug("GET /currency/exists/code/{} - Checking code existence", code);
        boolean exists = currencyService.existsByCode(code);
        return success(exists);
    }

    /**
     * Check if currency exists by Arabic designation
     * GET /currency/exists/designation-ar/{designationAr}
     */
    @GetMapping("/exists/designation-ar/{designationAr}")
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<Boolean> existsByDesignationAr(@PathVariable String designationAr) {
        log.debug("GET /currency/exists/designation-ar/{}", designationAr);
        boolean exists = currencyService.existsByDesignationAr(designationAr);
        return success(exists);
    }

    /**
     * Check if currency exists by English designation
     * GET /currency/exists/designation-en/{designationEn}
     */
    @GetMapping("/exists/designation-en/{designationEn}")
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<Boolean> existsByDesignationEn(@PathVariable String designationEn) {
        log.debug("GET /currency/exists/designation-en/{}", designationEn);
        boolean exists = currencyService.existsByDesignationEn(designationEn);
        return success(exists);
    }

    /**
     * Check if currency exists by French designation
     * GET /currency/exists/designation-fr/{designationFr}
     */
    @GetMapping("/exists/designation-fr/{designationFr}")
    @PreAuthorize("hasAuthority('CURRENCY:READ')")
    public ResponseEntity<Boolean> existsByDesignationFr(@PathVariable String designationFr) {
        log.debug("GET /currency/exists/designation-fr/{}", designationFr);
        boolean exists = currencyService.existsByDesignationFr(designationFr);
        return success(exists);
    }
}
