/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PartnerController
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
import dz.sh.trc.hyflo.network.common.dto.PartnerDTO;
import dz.sh.trc.hyflo.network.common.service.PartnerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/common/partner")
@Slf4j
public class PartnerController extends GenericController<PartnerDTO, Long> {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        super(partnerService, "Partner");
        this.partnerService = partnerService;
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    public ResponseEntity<PartnerDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    public ResponseEntity<Page<PartnerDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    public ResponseEntity<List<PartnerDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:ADMIN')")
    public ResponseEntity<PartnerDTO> create(@Valid @RequestBody PartnerDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:ADMIN')")
    public ResponseEntity<PartnerDTO> update(@PathVariable Long id, @Valid @RequestBody PartnerDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    public ResponseEntity<Page<PartnerDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<PartnerDTO> searchByQuery(String query, Pageable pageable) {
        return partnerService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
