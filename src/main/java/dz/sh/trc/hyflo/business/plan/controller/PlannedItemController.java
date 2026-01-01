/**
 *	
 *	@author		: CHOUABBIA 

 *	@Name		: PlannedItemController
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Plan
 *
 **/

package dz.sh.trc.hyflo.business.plan.controller;

import dz.sh.trc.hyflo.business.plan.dto.PlannedItemDTO;
import dz.sh.trc.hyflo.business.plan.service.PlannedItemService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/plan/plannedItem")
@Slf4j
public class PlannedItemController extends GenericController<PlannedItemDTO, Long> {

    private final PlannedItemService plannedItemService;

    public PlannedItemController(PlannedItemService plannedItemService) {
        super(plannedItemService, "PlannedItem");
        this.plannedItemService = plannedItemService;
    }

    @Override
    @PreAuthorize("hasAuthority('PLANNED_ITEM:READ')")
    public ResponseEntity<PlannedItemDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PLANNED_ITEM:READ')")
    public ResponseEntity<Page<PlannedItemDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PLANNED_ITEM:READ')")
    public ResponseEntity<List<PlannedItemDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PLANNED_ITEM:ADMIN')")
    public ResponseEntity<PlannedItemDTO> create(@Valid @RequestBody PlannedItemDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PLANNED_ITEM:ADMIN')")
    public ResponseEntity<PlannedItemDTO> update(@PathVariable Long id, @Valid @RequestBody PlannedItemDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PLANNED_ITEM:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PLANNED_ITEM:READ')")
    public ResponseEntity<Page<PlannedItemDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PLANNED_ITEM:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PLANNED_ITEM:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('PLANNED_ITEM:READ')")
    public ResponseEntity<List<PlannedItemDTO>> getAllList() {
        log.debug("GET /planned-item/list");
        return success(plannedItemService.getAll());
    }

    @GetMapping("/year/{year}")
    @PreAuthorize("hasAuthority('PLANNED_ITEM:READ')")
    public ResponseEntity<List<PlannedItemDTO>> getByYear(@PathVariable String year) {
        log.debug("GET /planned-item/year/{}", year);
        return success(plannedItemService.getByYear(year));
    }
}
