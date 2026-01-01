/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FacilityTypeController
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Type 
 *
 **/

package dz.mdn.iaas.network.type.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.mdn.iaas.configuration.template.GenericController;
import dz.mdn.iaas.network.type.dto.FacilityTypeDTO;
import dz.mdn.iaas.network.type.service.FacilityTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/network/type/facility")
@Slf4j
public class FacilityTypeController extends GenericController<FacilityTypeDTO, Long> {

    @SuppressWarnings("unused")
	private final FacilityTypeService facilityTypeService;

    public FacilityTypeController(FacilityTypeService facilityTypeService) {
        super(facilityTypeService, "FacilityType");
        this.facilityTypeService = facilityTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    public ResponseEntity<FacilityTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    public ResponseEntity<Page<FacilityTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    public ResponseEntity<List<FacilityTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:ADMIN')")
    public ResponseEntity<FacilityTypeDTO> create(@Valid @RequestBody FacilityTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:ADMIN')")
    public ResponseEntity<FacilityTypeDTO> update(@PathVariable Long id, @Valid @RequestBody FacilityTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    public ResponseEntity<Page<FacilityTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FACILITY_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
