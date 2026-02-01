/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : GenericNotificationEventListener
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Event
 *  @Package    : Configuration / Event
 *
 **/

package dz.sh.trc.hyflo.configuration.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.websocket.NotificationWebSocketService;
import dz.sh.trc.hyflo.system.notification.core.dto.NotificationDTO;
import dz.sh.trc.hyflo.system.notification.core.model.Notification;
import dz.sh.trc.hyflo.system.notification.core.service.NotificationService;
import dz.sh.trc.hyflo.system.notification.type.model.NotificationType;
import dz.sh.trc.hyflo.system.notification.type.repository.NotificationTypeRepository;
import dz.sh.trc.hyflo.system.security.model.User;
import dz.sh.trc.hyflo.system.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Generic event listener for all notification events
 * Handles any event that extends BaseNotificationEvent
 * 
 * This listener provides a unified notification processing pipeline:
 * 1. Resolve recipients (by username, role, or custom logic)
 * 2. Create notifications in database
 * 3. Send real-time WebSocket notifications
 * 4. Update unread counts
 * 
 * Supports multiple notification strategies:
 * - Direct user targeting (via usernames)
 * - Role-based targeting (all users with a specific role)
 * - Custom recipient resolution (override in subclasses)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GenericNotificationEventListener {

    private final NotificationWebSocketService webSocketService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final NotificationTypeRepository notificationTypeRepository;

    /**
     * Handle any event that extends BaseNotificationEvent
     * Processes the event and creates appropriate notifications
     * 
     * @param event The notification event to process
     */
    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleNotificationEvent(BaseNotificationEvent event) {
        String eventType = event.getClass().getSimpleName();
        log.info("Processing {} for entity: {} (ID: {})", 
                eventType, event.getRelatedEntityType(), event.getRelatedEntityId());
        
        try {
            // 1. Lookup notification type
            NotificationType notificationType = resolveNotificationType(event);
            
            // 2. Resolve recipients
            List<User> recipients = resolveRecipients(event);
            
            if (recipients.isEmpty()) {
                log.warn("No recipients found for {} (entity: {}, ID: {})", 
                        eventType, event.getRelatedEntityType(), event.getRelatedEntityId());
                return;
            }
            
            log.debug("Found {} recipients for {}", recipients.size(), eventType);
            
            // 3. Create and send notification to each recipient
            for (User recipient : recipients) {
                createAndSendNotification(event, notificationType, recipient);
            }
            
            log.info("Successfully processed {} for entity: {} (ID: {})", 
                    eventType, event.getRelatedEntityType(), event.getRelatedEntityId());
            
        } catch (Exception e) {
            log.error("Error processing {} for entity: {} (ID: {})", 
                    eventType, event.getRelatedEntityType(), event.getRelatedEntityId(), e);
            // Don't propagate exception - notification failures shouldn't break business logic
        }
    }

    /**
     * Resolve the NotificationType from the database
     * Uses the notificationTypeCode from the event
     * 
     * @param event The notification event
     * @return NotificationType entity
     * @throws IllegalStateException if notification type not found
     */
    private NotificationType resolveNotificationType(BaseNotificationEvent event) {
        return notificationTypeRepository
                .findByCode(event.getNotificationTypeCode())
                .orElseThrow(() -> new IllegalStateException(
                    String.format("NotificationType '%s' not found in database. " +
                            "Please ensure notification types are initialized.",
                            event.getNotificationTypeCode())
                ));
    }

    /**
     * Resolve recipients for the notification
     * Supports multiple strategies:
     * 1. Specific usernames (event.getRecipientUsernames())
     * 2. Role-based (event.getRecipientRole())
     * 3. Custom logic (can be overridden)
     * 
     * @param event The notification event
     * @return List of User entities who should receive the notification
     */
    protected List<User> resolveRecipients(BaseNotificationEvent event) {
        List<User> recipients = new ArrayList<>();
        
        // Strategy 1: Specific usernames
        if (event.hasSpecificRecipients()) {
            for (String username : event.getRecipientUsernames()) {
                userRepository.findByUsername(username).ifPresent(recipients::add);
            }
            return recipients;
        }
        
        // Strategy 2: Role-based
        if (event.hasRoleBasedRecipients()) {
            recipients = userRepository.findAllEnabledUsersByRoleName(event.getRecipientRole());
            return recipients;
        }
        
        // Strategy 3: Custom resolution (can be overridden in subclasses)
        recipients = resolveCustomRecipients(event);
        
        return recipients;
    }

    /**
     * Custom recipient resolution logic
     * Override this method in subclasses for domain-specific recipient logic
     * 
     * Example:
     * - For an OrderShippedEvent, return the customer who placed the order
     * - For a DocumentExpiredEvent, return all users with access to that document
     * 
     * @param event The notification event
     * @return List of users (empty by default)
     */
    protected List<User> resolveCustomRecipients(BaseNotificationEvent event) {
        log.debug("No custom recipient resolution defined for {}", 
                event.getClass().getSimpleName());
        return List.of();
    }

    /**
     * Create notification in database and send via WebSocket
     * 
     * @param event The notification event
     * @param notificationType The notification type
     * @param recipient The user to receive the notification
     */
    private void createAndSendNotification(
            BaseNotificationEvent event,
            NotificationType notificationType,
            User recipient) {
        
        try {
            // Create notification entity
            Notification notification = Notification.builder()
                    .title(event.getNotificationTitle())
                    .message(event.getNotificationMessage())
                    .type(notificationType)
                    .recipient(recipient)
                    .relatedEntityId(event.getRelatedEntityId())
                    .relatedEntityType(event.getRelatedEntityType())
                    .isRead(false)
                    .build();
            
            // Save to database
            Notification savedNotification = notificationService.createNotification(notification);
            NotificationDTO dto = NotificationDTO.fromEntity(savedNotification);
            
            // Send real-time notification via WebSocket
            webSocketService.sendToUser(recipient.getUsername(), dto);
            
            // Update unread count
            Long unreadCount = notificationService.getUnreadCountForUser(recipient);
            webSocketService.sendUnreadCountToUser(recipient.getUsername(), unreadCount);
            
            log.debug("Notification sent to user: {} (event: {})", 
                    recipient.getUsername(), event.getClass().getSimpleName());
            
        } catch (Exception e) {
            log.error("Failed to create/send notification to user: {} (event: {})", 
                    recipient.getUsername(), event.getClass().getSimpleName(), e);
            // Continue processing other recipients even if one fails
        }
    }
}
