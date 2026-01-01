/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: OperationalStatusController
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Common 
 *
 **/

package dz.mdn.iaas.network.common.controller;

import dz.mdn.iaas.configuration.template.GenericController;
import dz.mdn.iaas.network.common.dto.OperationalStatusDTO;
import dz.mdn.iaas.network.common.service.OperationalStatusService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/common/operationalStatus")
@Slf4j
public class OperationalStatusController extends GenericController<OperationalStatusDTO, Long> {

    @SuppressWarnings("unused")
	private final OperationalStatusService operationalStatusService;

    public OperationalStatusController(OperationalStatusService operationalStatusService) {
        super(operationalStatusService, "OperationalStatus");
        this.operationalStatusService = operationalStatusService;
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    public ResponseEntity<OperationalStatusDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    public ResponseEntity<Page<OperationalStatusDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    public ResponseEntity<List<OperationalStatusDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:ADMIN')")
    public ResponseEntity<OperationalStatusDTO> create(@Valid @RequestBody OperationalStatusDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:ADMIN')")
    public ResponseEntity<OperationalStatusDTO> update(@PathVariable Long id, @Valid @RequestBody OperationalStatusDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    public ResponseEntity<Page<OperationalStatusDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('OPERATIONAL_STATUS:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
