/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EquipmentController
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Common 
 *
 **/

package dz.mdn.iaas.network.core.controller;

import dz.mdn.iaas.configuration.template.GenericController;
import dz.mdn.iaas.network.core.dto.EquipmentDTO;
import dz.mdn.iaas.network.core.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/core/equipment")
@Slf4j
public class EquipmentController extends GenericController<EquipmentDTO, Long> {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        super(equipmentService, "Equipment");
        this.equipmentService = equipmentService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    public ResponseEntity<EquipmentDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    public ResponseEntity<Page<EquipmentDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    public ResponseEntity<List<EquipmentDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:ADMIN')")
    public ResponseEntity<EquipmentDTO> create(@Valid @RequestBody EquipmentDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:ADMIN')")
    public ResponseEntity<EquipmentDTO> update(@PathVariable Long id, @Valid @RequestBody EquipmentDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    public ResponseEntity<Page<EquipmentDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/facility/{facilityId}")
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    public ResponseEntity<List<EquipmentDTO>> getByFacility(@PathVariable Long facilityId) {
        log.info("REST request to get Equipment by facility id: {}", facilityId);
        return ResponseEntity.ok(equipmentService.findByFacility(facilityId));
    }

    @GetMapping("/type/{typeId}")
    @PreAuthorize("hasAuthority('EQUIPMENT:READ')")
    public ResponseEntity<List<EquipmentDTO>> getByEquipmentType(@PathVariable Long typeId) {
        log.info("REST request to get Equipment by equipment type id: {}", typeId);
        return ResponseEntity.ok(equipmentService.findByEquipmentType(typeId));
    }
}
