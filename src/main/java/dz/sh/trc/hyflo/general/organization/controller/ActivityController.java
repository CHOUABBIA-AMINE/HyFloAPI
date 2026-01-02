/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ActivityController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.general.organization.dto.ActivityDTO;
import dz.sh.trc.hyflo.general.organization.service.ActivityService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/general/organization/activity")
@Slf4j
public class ActivityController extends GenericController<ActivityDTO, Long> {

    @SuppressWarnings("unused")
	private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        super(activityService, "Activity");
        this.activityService = activityService;
    }

    @Override
    @PreAuthorize("hasAuthority('ACTIVITY:READ')")
    public ResponseEntity<ActivityDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ACTIVITY:READ')")
    public ResponseEntity<Page<ActivityDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ACTIVITY:READ')")
    public ResponseEntity<List<ActivityDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ACTIVITY:ADMIN')")
    public ResponseEntity<ActivityDTO> create(@Valid @RequestBody ActivityDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ACTIVITY:ADMIN')")
    public ResponseEntity<ActivityDTO> update(@PathVariable Long id, @Valid @RequestBody ActivityDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ACTIVITY:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ACTIVITY:READ')")
    public ResponseEntity<Page<ActivityDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ACTIVITY:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ACTIVITY:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
