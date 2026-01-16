/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProductionFieldTypeController
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
import dz.sh.trc.hyflo.network.type.dto.ProductionFieldTypeDTO;
import dz.sh.trc.hyflo.network.type.service.ProductionFieldTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/type/productionField")
@Slf4j
public class ProductionFieldTypeController extends GenericController<ProductionFieldTypeDTO, Long> {

    private final ProductionFieldTypeService productionFieldTypeService;

    public ProductionFieldTypeController(ProductionFieldTypeService productionFieldTypeService) {
        super(productionFieldTypeService, "ProductionFieldType");
        this.productionFieldTypeService = productionFieldTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    public ResponseEntity<ProductionFieldTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    public ResponseEntity<Page<ProductionFieldTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    public ResponseEntity<List<ProductionFieldTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:ADMIN')")
    public ResponseEntity<ProductionFieldTypeDTO> create(@Valid @RequestBody ProductionFieldTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:ADMIN')")
    public ResponseEntity<ProductionFieldTypeDTO> update(@PathVariable Long id, @Valid @RequestBody ProductionFieldTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    public ResponseEntity<Page<ProductionFieldTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<ProductionFieldTypeDTO> searchByQuery(String query, Pageable pageable) {
        return productionFieldTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PRODUCTION_FIELD_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
