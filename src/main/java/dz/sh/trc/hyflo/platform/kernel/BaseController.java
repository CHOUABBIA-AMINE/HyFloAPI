package dz.sh.trc.hyflo.platform.kernel;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseController<REQ_C, REQ_U, RES, SUM> {

    protected final BaseService<REQ_C, REQ_U, RES, SUM> service;

    protected BaseController(BaseService<REQ_C, REQ_U, RES, SUM> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RES> create(@RequestBody @Valid REQ_C request) {
        log.debug("REST request to save entity : {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RES> update(@PathVariable Long id,
                                      @RequestBody @Valid REQ_U request) {
        log.debug("REST request to update entity : {}, {}", id, request);
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RES> getById(@PathVariable Long id) {
        log.debug("REST request to get entity : {}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RES>> getAll() {
        log.debug("REST request to get all entities");
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping
    public ResponseEntity<Page<SUM>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("REST request to get a page of entities");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        log.debug("REST request to check if entity exists : {}", id);
        return ResponseEntity.ok(service.existsById(id));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        log.debug("REST request to count entities");
        return ResponseEntity.ok(service.count());
    }

    @PostMapping("/search")
    public ResponseEntity<Page<SUM>> search(@RequestParam(required = false) String criteria, Pageable pageable) {
        log.debug("REST request to search entities with criteria: {}", criteria);
        return ResponseEntity.ok(performSearch(criteria, pageable));
    }
    
    protected abstract Page<SUM> performSearch(String search, Pageable pageable);

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete entity : {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}