/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationTypeController
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : System / Notification / Type
 *
 **/

package dz.sh.trc.hyflo.system.notification.type.controller;

import java.util.List;
import java.util.Map;

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
import dz.sh.trc.hyflo.system.notification.type.dto.NotificationTypeDTO;
import dz.sh.trc.hyflo.system.notification.type.service.NotificationTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for NotificationType Management
 */
@Tag(name = "Notification Types", description = "Notification type management endpoints")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/system/notification/type")
@Slf4j
public class NotificationTypeController extends GenericController<NotificationTypeDTO, Long> {

    private final NotificationTypeService notificationTypeService;

    public NotificationTypeController(NotificationTypeService notificationTypeService) {
        super(notificationTypeService, "NotificationType");
        this.notificationTypeService = notificationTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION_TYPE:READ')")
    public ResponseEntity<NotificationTypeDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION_TYPE:READ')")
    public ResponseEntity<Page<NotificationTypeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "code") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION_TYPE:READ')")
    public ResponseEntity<List<NotificationTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION_TYPE:MANAGE')")
    public ResponseEntity<NotificationTypeDTO> create(@Valid @RequestBody NotificationTypeDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION_TYPE:MANAGE')")
    public ResponseEntity<NotificationTypeDTO> update(@PathVariable Long id, @Valid @RequestBody NotificationTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION_TYPE:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION_TYPE:READ')")
    public ResponseEntity<Page<NotificationTypeDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "code") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION_TYPE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION_TYPE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<NotificationTypeDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return notificationTypeService.getAll(pageable);
        }
        return notificationTypeService.globalSearch(query, pageable);
    }

    @Operation(summary = "Get all active notification types")
    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationTypeDTO>> getAllActive() {
        log.debug("GET /system/notification/type/active");
        return success(notificationTypeService.getAllActive());
    }

    @Operation(summary = "Get notification type by code")
    @GetMapping("/code/{code}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationTypeDTO> getByCode(@PathVariable String code) {
        log.debug("GET /system/notification/type/code/{}", code);
        return success(notificationTypeService.getByCode(code));
    }

    @Operation(summary = "Check if notification type exists by code")
    @GetMapping("/code/{code}/exists")
    @PreAuthorize("hasAuthority('NOTIFICATION_TYPE:READ')")
    public ResponseEntity<Map<String, Boolean>> existsByCode(@PathVariable String code) {
        log.debug("GET /system/notification/type/code/{}/exists", code);
        return success(Map.of("exists", notificationTypeService.existsByCode(code)));
    }
}
