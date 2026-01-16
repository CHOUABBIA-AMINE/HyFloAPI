/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProductionFieldController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.core.dto.ProductionFieldDTO;
import dz.sh.trc.hyflo.network.core.service.ProductionFieldService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/network/core/productionField")
@Slf4j
public class ProductionFieldController extends GenericController<ProductionFieldDTO, Long> {

    private final ProductionFieldService productionFieldService;

    public ProductionFieldController(ProductionFieldService productionFieldService) {
        super(productionFieldService, "ProductionField");
        this.productionFieldService = productionFieldService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    public ResponseEntity<ProductionFieldDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    public ResponseEntity<Page<ProductionFieldDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    public ResponseEntity<List<ProductionFieldDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:ADMIN')")
    public ResponseEntity<ProductionFieldDTO> create(@Valid @RequestBody ProductionFieldDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:ADMIN')")
    public ResponseEntity<ProductionFieldDTO> update(@PathVariable Long id, @Valid @RequestBody ProductionFieldDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    public ResponseEntity<Page<ProductionFieldDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<ProductionFieldDTO> searchByQuery(String query, Pageable pageable) {
        return productionFieldService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
    
    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/plant/{plantId}")
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    public ResponseEntity<List<ProductionFieldDTO>> getByProcessingPlant(@PathVariable Long plantId) {
        log.info("REST request to get productionField by processing plant id: {}", plantId);
        return ResponseEntity.ok(productionFieldService.findByProcessingPlant(plantId));
    }

    @GetMapping("/status/{statusId}")
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    public ResponseEntity<List<ProductionFieldDTO>> getByOperationalStatus(@PathVariable Long statusId) {
        log.info("REST request to get productionField by operational status id: {}", statusId);
        return ResponseEntity.ok(productionFieldService.findByOperationalStatus(statusId));
    }

    @GetMapping("/location/{locationId}")
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    public ResponseEntity<List<ProductionFieldDTO>> getByLocation(@PathVariable Long locationId) {
        log.info("REST request to get PipproductionFieldeline by location id: {}", locationId);
        return ResponseEntity.ok(productionFieldService.findByLocation(locationId));
    }

    @GetMapping("/type/{typeId}")
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD:READ')")
    public ResponseEntity<List<ProductionFieldDTO>> getByProductionFieldType(@PathVariable Long typeId) {
        log.info("REST request to get productionField by production field type id: {}", typeId);
        return ResponseEntity.ok(productionFieldService.findByProductionFieldType(typeId));
    }
}
