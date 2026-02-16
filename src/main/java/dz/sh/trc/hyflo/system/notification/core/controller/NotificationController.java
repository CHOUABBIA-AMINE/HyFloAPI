/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationController
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-16-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : System / Notification
 *
 **/

package dz.sh.trc.hyflo.system.notification.core.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.notification.core.dto.NotificationDTO;
import dz.sh.trc.hyflo.system.notification.core.service.NotificationService;
import dz.sh.trc.hyflo.system.security.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * REST Controller for user notification management and real-time delivery.
 * Supports notification CRUD, user-specific queries, read status tracking, and SSE streaming.
 */
@Tag(
    name = "Notification Management",
    description = "Comprehensive notification API for real-time user notifications, read status tracking, and notification delivery"
)
@SecurityRequirement(name = "bearer-auth")
@RestController
@RequestMapping("/system/notification/notifications")
@Slf4j
public class NotificationController extends GenericController<NotificationDTO, Long> {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        super(notificationService, "Notification");
        this.notificationService = notificationService;
    }

    @Operation(
        summary = "Get notification by ID",
        description = "Retrieve a specific notification by its unique identifier. Returns complete notification details including type and recipient information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification found", content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION:READ')")
    public ResponseEntity<NotificationDTO> getById(
        @Parameter(description = "Notification ID", required = true, example = "1") @PathVariable Long id
    ) {
        return super.getById(id);
    }

    @Operation(
        summary = "Get all notifications with pagination",
        description = "Retrieve paginated list of all notifications. Supports sorting and filtering for efficient data retrieval."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notifications retrieved", content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION:READ')")
    public ResponseEntity<Page<NotificationDTO>> getAll(
        @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Sort field", example = "createdAt") @RequestParam(defaultValue = "id") String sortBy,
        @Parameter(description = "Sort direction", example = "desc") @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Operation(
        summary = "Create new notification",
        description = "Create a new notification for delivery to a user. Automatically triggers real-time notification delivery if user is online."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notification created", content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid notification data", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION:MANAGE')")
    public ResponseEntity<NotificationDTO> create(@Valid @RequestBody NotificationDTO dto) {
        return super.create(dto);
    }

    @Operation(
        summary = "Update notification",
        description = "Update an existing notification. Commonly used for updating read status or related entity information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification updated", content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid notification data", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION:MANAGE')")
    public ResponseEntity<NotificationDTO> update(@PathVariable Long id, @Valid @RequestBody NotificationDTO dto) {
        return super.update(id, dto);
    }

    @Operation(
        summary = "Delete notification",
        description = "Delete a notification by ID. Permanently removes the notification from the system."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notification deleted", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('NOTIFICATION:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Operation(
        summary = "Get current user notifications",
        description = "Retrieve paginated notifications for the authenticated user. Returns notifications sorted by creation date (newest first) with read/unread status."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User notifications retrieved", content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "User not authenticated", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/my-notifications")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationDTO>> getMyNotifications(
        @AuthenticationPrincipal User currentUser,
        @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size
    ) {
        log.debug("Getting notifications for user: {}", currentUser.getUsername());
        Page<NotificationDTO> notifications = notificationService.getNotificationsForUser(
            currentUser.getId(), 
            buildPageable(page, size, "createdAt", "desc")
        );
        return success(notifications);
    }

    @Operation(
        summary = "Get unread notifications count",
        description = "Get the count of unread notifications for the authenticated user. Useful for displaying notification badges."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Unread count retrieved", content = @Content(schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "403", description = "User not authenticated", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getUnreadCount(@AuthenticationPrincipal User currentUser) {
        log.debug("Getting unread count for user: {}", currentUser.getUsername());
        Long unreadCount = notificationService.getUnreadCount(currentUser.getId());
        return success(unreadCount);
    }

    @Operation(
        summary = "Mark notification as read",
        description = "Mark a specific notification as read and set the readAt timestamp. Users can only mark their own notifications as read."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notification marked as read", content = @Content(schema = @Schema(implementation = NotificationDTO.class))),
        @ApiResponse(responseCode = "403", description = "Cannot mark another user's notification", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PatchMapping("/{id}/mark-read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationDTO> markAsRead(
        @Parameter(description = "Notification ID", required = true, example = "1") @PathVariable Long id,
        @AuthenticationPrincipal User currentUser
    ) {
        log.debug("Marking notification {} as read for user: {}", id, currentUser.getUsername());
        NotificationDTO notification = notificationService.markAsRead(id, currentUser.getId());
        return success(notification);
    }

    @Operation(
        summary = "Mark all notifications as read",
        description = "Mark all unread notifications for the authenticated user as read. Bulk operation for clearing notification badges."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All notifications marked as read", content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "403", description = "User not authenticated", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PatchMapping("/mark-all-read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationDTO>> markAllAsRead(@AuthenticationPrincipal User currentUser) {
        log.debug("Marking all notifications as read for user: {}", currentUser.getUsername());
        List<NotificationDTO> notifications = notificationService.markAllAsRead(currentUser.getId());
        return success(notifications);
    }
}
