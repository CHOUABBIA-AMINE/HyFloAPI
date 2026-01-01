/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ActivityController
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.network.common.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.common.dto.ActivityDTO;
import dz.sh.trc.hyflo.network.common.service.ActivityService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/common/activity")
@Slf4j
public class ActivityController extends GenericController<ActivityDTO, Long> {

    @SuppressWarnings("unused")
	private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        super(activityService, "Activity");
        this.activityService = activityService;
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<ActivityDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<Page<ActivityDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<List<ActivityDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:ADMIN')")
    public ResponseEntity<ActivityDTO> create(@Valid @RequestBody ActivityDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:ADMIN')")
    public ResponseEntity<ActivityDTO> update(@PathVariable Long id, @Valid @RequestBody ActivityDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<Page<ActivityDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('REGION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
