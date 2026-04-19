/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: BaseService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-10-2025
 *
 *	@Type		: Abstract Class
 *	@Layer		: Template
 *	@Package	: Configuration / Template
 *
 **/

package dz.sh.trc.hyflo.platform.kernel;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public abstract class BaseController<REQ, RES> {

    protected abstract BaseService<REQ, RES> service();

    @PostMapping
    public ResponseEntity<RES> create(@RequestBody @Valid REQ request) {
        return ResponseEntity.ok(service().create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RES> update(@PathVariable Long id,
                                      @RequestBody @Valid REQ request) {
        return ResponseEntity.ok(service().update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RES> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service().getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RES>> getAll() {
        return ResponseEntity.ok(service().getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service().delete(id);
        return ResponseEntity.noContent().build();
    }
}