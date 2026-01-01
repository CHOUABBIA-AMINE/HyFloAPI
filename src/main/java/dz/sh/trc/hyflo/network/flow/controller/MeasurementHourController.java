/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MeasurementHourController
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-12-2025
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
import dz.sh.trc.hyflo.network.flow.dto.MeasurementHourDTO;
import dz.sh.trc.hyflo.network.flow.service.MeasurementHourService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/network/flow/measurementHour")
@Slf4j
public class MeasurementHourController extends GenericController<MeasurementHourDTO, Long> {

    @SuppressWarnings("unused")
	private final MeasurementHourService equipmentTypeService;

    public MeasurementHourController(MeasurementHourService equipmentTypeService) {
        super(equipmentTypeService, "MeasurementHour");
        this.equipmentTypeService = equipmentTypeService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<MeasurementHourDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<Page<MeasurementHourDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<List<MeasurementHourDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:ADMIN')")
    public ResponseEntity<MeasurementHourDTO> create(@Valid @RequestBody MeasurementHourDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:ADMIN')")
    public ResponseEntity<MeasurementHourDTO> update(@PathVariable Long id, @Valid @RequestBody MeasurementHourDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<Page<MeasurementHourDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
