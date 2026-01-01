/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AlloyController
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Common 
 *
 **/

package dz.sh.trc.hyflo.network.common.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.common.dto.AlloyDTO;
import dz.sh.trc.hyflo.network.common.service.AlloyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/common/alloy")
@Slf4j
public class AlloyController extends GenericController<AlloyDTO, Long> {

    @SuppressWarnings("unused")
	private final AlloyService alloyService;

    public AlloyController(AlloyService alloyService) {
        super(alloyService, "Alloy");
        this.alloyService = alloyService;
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    public ResponseEntity<AlloyDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    public ResponseEntity<Page<AlloyDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    public ResponseEntity<List<AlloyDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:ADMIN')")
    public ResponseEntity<AlloyDTO> create(@Valid @RequestBody AlloyDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:ADMIN')")
    public ResponseEntity<AlloyDTO> update(@PathVariable Long id, @Valid @RequestBody AlloyDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    public ResponseEntity<Page<AlloyDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
