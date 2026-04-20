package dz.sh.trc.hyflo.core.communication.Notification.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.core.communication.Notification.dto.request.SendNotificationRequest;
import dz.sh.trc.hyflo.core.communication.Notification.dto.response.NotificationResponse;
import dz.sh.trc.hyflo.core.communication.Notification.dto.response.NotificationSummary;

/**
 * Service for creating, delivering, and managing notifications.
 * Integrates with LocalizationService for template-based messages.
 */
public interface NotificationService {

    /** Create and persist a notification using localization keys. */
    NotificationResponse send(SendNotificationRequest request);

    /** Convenience: send a workflow-triggered notification. */
    void sendWorkflowNotification(String typeCode, String entityType,
            Long entityId, Long actorEmployeeId, Long recipientUserId);

    /** Get paginated notifications for a specific user. */
    Page<NotificationSummary> getForUser(Long userId, Pageable pageable);

    /** Count unread notifications for a user. */
    long getUnreadCount(Long userId);

    /** Mark a notification as read. */
    void markAsRead(Long notificationId);
}
