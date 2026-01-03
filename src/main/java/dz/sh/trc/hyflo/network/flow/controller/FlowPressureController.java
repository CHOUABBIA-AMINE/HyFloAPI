/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowPressureController
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
import dz.sh.trc.hyflo.network.flow.dto.FlowPressureDTO;
import dz.sh.trc.hyflo.network.flow.service.FlowPressureService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/network/flow/pressure")
@Slf4j
public class FlowPressureController extends GenericController<FlowPressureDTO, Long> {

    //private final FlowPressureService flowService;

    public FlowPressureController(FlowPressureService flowPressureService) {
        super(flowPressureService, "FlowPressure");
        //this.flowService = flowService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FLOW_PRESSURE:READ')")
    public ResponseEntity<FlowPressureDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_PRESSURE:READ')")
    public ResponseEntity<Page<FlowPressureDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_PRESSURE:READ')")
    public ResponseEntity<List<FlowPressureDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_PRESSURE:ADMIN')")
    public ResponseEntity<FlowPressureDTO> create(@Valid @RequestBody FlowPressureDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_PRESSURE:ADMIN')")
    public ResponseEntity<FlowPressureDTO> update(@PathVariable Long id, @Valid @RequestBody FlowPressureDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_PRESSURE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_PRESSURE:READ')")
    public ResponseEntity<Page<FlowPressureDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    /*@Override
    protected Page<FlowPressureDTO> searchByQuery(String query, Pageable pageable) {
        return flowService.globalSearch(query, pageable);
    }*/

    @Override
    @PreAuthorize("hasAuthority('FLOW_PRESSURE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_PRESSURE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
