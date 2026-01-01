/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ItemStatusController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Plan
 *
 **/

package dz.mdn.iaas.business.plan.controller;

import dz.mdn.iaas.business.plan.dto.ItemStatusDTO;
import dz.mdn.iaas.business.plan.service.ItemStatusService;
import dz.mdn.iaas.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/plan/itemStatus")
@Slf4j
public class ItemStatusController extends GenericController<ItemStatusDTO, Long> {

    private final ItemStatusService itemStatusService;

    public ItemStatusController(ItemStatusService itemStatusService) {
        super(itemStatusService, "ItemStatus");
        this.itemStatusService = itemStatusService;
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_STATUS:READ')")
    public ResponseEntity<ItemStatusDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_STATUS:READ')")
    public ResponseEntity<Page<ItemStatusDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_STATUS:READ')")
    public ResponseEntity<List<ItemStatusDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_STATUS:ADMIN')")
    public ResponseEntity<ItemStatusDTO> create(@Valid @RequestBody ItemStatusDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_STATUS:ADMIN')")
    public ResponseEntity<ItemStatusDTO> update(@PathVariable Long id, @Valid @RequestBody ItemStatusDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_STATUS:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_STATUS:READ')")
    public ResponseEntity<Page<ItemStatusDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_STATUS:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_STATUS:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ITEM_STATUS:READ')")
    public ResponseEntity<List<ItemStatusDTO>> getAllList() {
        log.debug("GET /item-status/list");
        return success(itemStatusService.getAll());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ITEM_STATUS:READ')")
    public ResponseEntity<List<ItemStatusDTO>> getActiveStatuses() {
        log.debug("GET /item-status/active");
        return success(itemStatusService.getActiveStatuses());
    }
}
