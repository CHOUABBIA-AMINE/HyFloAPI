/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ItemDistributionController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Plan
 *
 **/

package dz.sh.trc.hyflo.business.plan.controller;

import dz.sh.trc.hyflo.business.plan.dto.ItemDistributionDTO;
import dz.sh.trc.hyflo.business.plan.service.ItemDistributionService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/plan/itemDistribution")
@Slf4j
public class ItemDistributionController extends GenericController<ItemDistributionDTO, Long> {

    private final ItemDistributionService itemDistributionService;

    public ItemDistributionController(ItemDistributionService itemDistributionService) {
        super(itemDistributionService, "ItemDistribution");
        this.itemDistributionService = itemDistributionService;
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_DISTRIBUTION:READ')")
    public ResponseEntity<ItemDistributionDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_DISTRIBUTION:READ')")
    public ResponseEntity<Page<ItemDistributionDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_DISTRIBUTION:READ')")
    public ResponseEntity<List<ItemDistributionDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_DISTRIBUTION:ADMIN')")
    public ResponseEntity<ItemDistributionDTO> create(@Valid @RequestBody ItemDistributionDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_DISTRIBUTION:ADMIN')")
    public ResponseEntity<ItemDistributionDTO> update(@PathVariable Long id, @Valid @RequestBody ItemDistributionDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_DISTRIBUTION:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_DISTRIBUTION:READ')")
    public ResponseEntity<Page<ItemDistributionDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_DISTRIBUTION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ITEM_DISTRIBUTION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ITEM_DISTRIBUTION:READ')")
    public ResponseEntity<List<ItemDistributionDTO>> getAllList() {
        log.debug("GET /item-distribution/list");
        return success(itemDistributionService.getAll());
    }

    @GetMapping("/item/{itemId}")
    @PreAuthorize("hasAuthority('ITEM_DISTRIBUTION:READ')")
    public ResponseEntity<List<ItemDistributionDTO>> getByItem(@PathVariable Long itemId) {
        log.debug("GET /item-distribution/item/{}", itemId);
        return success(itemDistributionService.getByItemId(itemId));
    }
}
