/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationController
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : System / Notification / Core
 *
 **/

package dz.sh.trc.hyflo.system.notification.core.controller;

import dz.sh.trc.hyflo.system.notification.core.dto.NotificationDTO;
import dz.sh.trc.hyflo.system.notification.core.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Notification Management
 */
@Tag(name = "Notifications", description = "Notification management endpoints")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
@RestController
@RequestMapping("/system/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get my notifications")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationDTO>> getMyNotifications(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        log.debug("Fetching notifications with pagination: {}", pageable);
        return ResponseEntity.ok(notificationService.getMyNotifications(pageable));
    }

    @Operation(summary = "Get unread notifications")
    @GetMapping("/unread")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() {
        log.debug("Fetching unread notifications");
        return ResponseEntity.ok(notificationService.getMyUnreadNotifications());
    }

    @Operation(summary = "Get unread count")
    @GetMapping("/unread/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        return ResponseEntity.ok(Map.of("unreadCount", notificationService.getUnreadCount()));
    }

    @Operation(summary = "Mark as read")
    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        log.debug("Marking notification {} as read", id);
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @Operation(summary = "Mark all as read")
    @PutMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAllAsRead() {
        log.debug("Marking all notifications as read");
        notificationService.markAllAsRead();
        return ResponseEntity.noContent().build();
    }
}
