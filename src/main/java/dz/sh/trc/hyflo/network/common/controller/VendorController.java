/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: VendorController
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Controller
 *	@Layer		: Network / Controller
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.network.common.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.common.dto.VendorDTO;
import dz.sh.trc.hyflo.network.common.service.VendorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/common/vendor")
@Slf4j
public class VendorController extends GenericController<VendorDTO, Long> {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        super(vendorService, "Vendor");
        this.vendorService = vendorService;
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR:READ')")
    public ResponseEntity<VendorDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR:READ')")
    public ResponseEntity<Page<VendorDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR:READ')")
    public ResponseEntity<List<VendorDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR:ADMIN')")
    public ResponseEntity<VendorDTO> create(@Valid @RequestBody VendorDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR:ADMIN')")
    public ResponseEntity<VendorDTO> update(@PathVariable Long id, @Valid @RequestBody VendorDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR:READ')")
    public ResponseEntity<Page<VendorDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<VendorDTO> searchByQuery(String query, Pageable pageable) {
        return vendorService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
