/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: VendorTypeController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.type.dto.VendorTypeDTO;
import dz.sh.trc.hyflo.network.type.service.VendorTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/type/vendor")
@Slf4j
public class VendorTypeController extends GenericController<VendorTypeDTO, Long> {

    private final VendorTypeService vendorTypeService;

    public VendorTypeController(VendorTypeService vendorTypeService) {
        super(vendorTypeService, "VendorType");
        this.vendorTypeService = vendorTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    public ResponseEntity<VendorTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    public ResponseEntity<Page<VendorTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    public ResponseEntity<List<VendorTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:MANAGE')")
    public ResponseEntity<VendorTypeDTO> create(@Valid @RequestBody VendorTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:MANAGE')")
    public ResponseEntity<VendorTypeDTO> update(@PathVariable Long id, @Valid @RequestBody VendorTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    public ResponseEntity<Page<VendorTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<VendorTypeDTO> searchByQuery(String query, Pageable pageable) {
        return vendorTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
