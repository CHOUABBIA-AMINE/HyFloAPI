/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PersonController
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Administration
 *
 **/

package dz.sh.trc.hyflo.common.administration.controller;

import dz.sh.trc.hyflo.common.administration.dto.PersonDTO;
import dz.sh.trc.hyflo.common.administration.service.PersonService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Person REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for Person management
 */
@RestController
@RequestMapping("/common/administration/person")
@Slf4j
public class PersonController extends GenericController<PersonDTO, Long> {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        super(personService, "Person");
        this.personService = personService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    public ResponseEntity<PersonDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    public ResponseEntity<Page<PersonDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    public ResponseEntity<List<PersonDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:ADMIN')")
    public ResponseEntity<PersonDTO> create(@Valid @RequestBody PersonDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:ADMIN')")
    public ResponseEntity<PersonDTO> update(@PathVariable Long id, @Valid @RequestBody PersonDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    public ResponseEntity<Page<PersonDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<PersonDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return personService.getAll(pageable);
        }
        return personService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all persons as list (non-paginated)
     * GET /person/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('PERSON:READ')")
    public ResponseEntity<List<PersonDTO>> getAllList() {
        log.debug("GET /person/list - Getting all persons as list");
        List<PersonDTO> persons = personService.getAll();
        return success(persons);
    }
}
