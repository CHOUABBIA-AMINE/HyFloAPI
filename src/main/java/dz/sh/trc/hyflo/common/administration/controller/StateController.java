/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: StateController
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Administration
 *
 **/

package dz.sh.trc.hyflo.common.administration.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.common.administration.dto.StateDTO;
import dz.sh.trc.hyflo.common.administration.service.StateService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * State REST Controller - Extends GenericController
 * Provides standard CRUD endpoints plus state-specific operations
 */
@RestController
@RequestMapping("/common/administration/state")
@Slf4j
public class StateController extends GenericController<StateDTO, Long> {

    private final StateService stateService;

    public StateController(StateService stateService) {
        super(stateService, "State");
        this.stateService = stateService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    public ResponseEntity<StateDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    public ResponseEntity<Page<StateDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    public ResponseEntity<List<StateDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:ADMIN')")
    public ResponseEntity<StateDTO> create(@Valid @RequestBody StateDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:ADMIN')")
    public ResponseEntity<StateDTO> update(@PathVariable Long id, @Valid @RequestBody StateDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    public ResponseEntity<Page<StateDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<StateDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return stateService.getAll(pageable);
        }
        return stateService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all states as list (non-paginated)
     * GET /state/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('STATE:READ')")
    public ResponseEntity<List<StateDTO>> getAllList() {
        log.debug("GET /state/list - Getting all states as list");
        List<StateDTO> states = stateService.getAll();
        return success(states);
    }
}
