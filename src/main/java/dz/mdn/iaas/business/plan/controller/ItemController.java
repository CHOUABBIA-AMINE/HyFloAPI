/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ItemController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Plan
 *
 **/

package dz.mdn.iaas.business.plan.controller;

import dz.mdn.iaas.business.plan.dto.ItemDTO;
import dz.mdn.iaas.business.plan.service.ItemService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/plan/item")
@Slf4j
public class ItemController extends GenericController<ItemDTO, Long> {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        super(itemService, "Item");
        this.itemService = itemService;
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM:READ')")
    public ResponseEntity<ItemDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM:READ')")
    public ResponseEntity<Page<ItemDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM:READ')")
    public ResponseEntity<List<ItemDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM:ADMIN')")
    public ResponseEntity<ItemDTO> create(@Valid @RequestBody ItemDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM:ADMIN')")
    public ResponseEntity<ItemDTO> update(@PathVariable Long id, @Valid @RequestBody ItemDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM:READ')")
    public ResponseEntity<Page<ItemDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ITEM:READ')")
    public ResponseEntity<List<ItemDTO>> getAllList() {
        log.debug("GET /item/list");
        return success(itemService.getAll());
    }
}
