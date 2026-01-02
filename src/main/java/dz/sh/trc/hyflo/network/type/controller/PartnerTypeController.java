/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PartnerTypeController
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
import dz.sh.trc.hyflo.network.type.dto.PartnerTypeDTO;
import dz.sh.trc.hyflo.network.type.service.PartnerTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/type/partner")
@Slf4j
public class PartnerTypeController extends GenericController<PartnerTypeDTO, Long> {

    private final PartnerTypeService partnerTypeService;

    public PartnerTypeController(PartnerTypeService partnerTypeService) {
        super(partnerTypeService, "PartnerType");
        this.partnerTypeService = partnerTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    public ResponseEntity<PartnerTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    public ResponseEntity<Page<PartnerTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    public ResponseEntity<List<PartnerTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:ADMIN')")
    public ResponseEntity<PartnerTypeDTO> create(@Valid @RequestBody PartnerTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:ADMIN')")
    public ResponseEntity<PartnerTypeDTO> update(@PathVariable Long id, @Valid @RequestBody PartnerTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    public ResponseEntity<Page<PartnerTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<PartnerTypeDTO> searchByQuery(String query, Pageable pageable) {
        return partnerTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
