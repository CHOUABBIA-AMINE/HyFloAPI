/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowConsumedController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.flow.dto.FlowConsumedDTO;
import dz.sh.trc.hyflo.network.flow.service.FlowConsumedService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/network/flow/consumed")
@Slf4j
public class FlowConsumedController extends GenericController<FlowConsumedDTO, Long> {

    //private final FlowConsumedService flowService;

    public FlowConsumedController(FlowConsumedService flowConsumedService) {
        super(flowConsumedService, "FlowConsumed");
        //this.flowService = flowService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<FlowConsumedDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Page<FlowConsumedDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<List<FlowConsumedDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<FlowConsumedDTO> create(@Valid @RequestBody FlowConsumedDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<FlowConsumedDTO> update(@PathVariable Long id, @Valid @RequestBody FlowConsumedDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Page<FlowConsumedDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    /*@Override
    protected Page<FlowConsumedDTO> searchByQuery(String query, Pageable pageable) {
        return flowService.globalSearch(query, pageable);
    }*/

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
