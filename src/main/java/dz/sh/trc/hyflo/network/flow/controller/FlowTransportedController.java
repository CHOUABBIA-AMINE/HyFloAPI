/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FlowTransportedController
 *	@CreatedOn	: 12-19-2025
 *	@UpdatedOn	: 12-19-2025
 *
 *	@Type		: Controller
 *	@Layer		: Network / Controller
 *	@Package	: Network / Core
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
import dz.sh.trc.hyflo.network.flow.dto.FlowTransportedDTO;
import dz.sh.trc.hyflo.network.flow.service.FlowTransportedService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/network/flow/transported")
@Slf4j
public class FlowTransportedController extends GenericController<FlowTransportedDTO, Long> {

    //private final FlowTransportedService flowService;

    public FlowTransportedController(FlowTransportedService flowTransportedService) {
        super(flowTransportedService, "FlowTransported");
        //this.flowService = flowService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<FlowTransportedDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Page<FlowTransportedDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<List<FlowTransportedDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<FlowTransportedDTO> create(@Valid @RequestBody FlowTransportedDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<FlowTransportedDTO> update(@PathVariable Long id, @Valid @RequestBody FlowTransportedDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    public ResponseEntity<Page<FlowTransportedDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    /*@Override
    protected Page<FlowTransportedDTO> searchByQuery(String query, Pageable pageable) {
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
