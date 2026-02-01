/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationEventListener
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Event Listener
 *  @Package    : System / Notification / Event
 *
 **/

package dz.sh.trc.hyflo.configuration.event;

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
 * Event listener for creating and sending notifications
 * Handles Reading workflow events (Submitted, Validated, Rejected)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationWebSocketService webSocketService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final NotificationTypeRepository notificationTypeRepository;

    /**
     * Handle ReadingSubmittedEvent
     * Creates notifications for all Validators (users with ROLE_VALIDATOR)
     */
    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleReadingSubmittedEvent(ReadingSubmittedEvent event) {
        log.info("Processing ReadingSubmittedEvent for reading ID: {}", event.getReadingId());
        
        try {
            // Lookup notification type
            NotificationType readingSubmittedType = notificationTypeRepository
                    .findByCode("READING_SUBMITTED")
                    .orElseThrow(() -> new IllegalStateException(
                        "NotificationType 'READING_SUBMITTED' not found in database. " +
                        "Please run database initialization script."
                    ));
            
            // Find all enabled Validators (direct role or via groups)
            // CHANGED: Using the new repository method
            List<User> validators = userRepository.findAllEnabledUsersByRoleName("ROLE_VALIDATOR");
            
            if (validators.isEmpty()) {
                log.warn("No validators found to notify for reading ID: {}", event.getReadingId());
                return;
            }
            
            log.debug("Found {} validators to notify", validators.size());
            
            // Create notification for each Validator
            for (User validator : validators) {
                Notification notification = Notification.builder()
                        .title("New Reading Awaiting Validation")
                        .message(String.format(
                                "Reader %s submitted a new operational reading for %s",
                                event.getSubmittedBy(),
                                event.getReadingIdentifier()
                        ))
                        .type(readingSubmittedType)
                        .recipient(validator)
                        .relatedEntityId(event.getReadingId().toString())
                        .relatedEntityType("READING")
                        .isRead(false)
                        .build();
                
                Notification savedNotification = notificationService.createNotification(notification);
                NotificationDTO dto = NotificationDTO.fromEntity(savedNotification);
                
                // Send real-time notification via WebSocket
                webSocketService.sendToUser(validator.getUsername(), dto);
                
                // Update unread count
                Long unreadCount = notificationService.getUnreadCountForUser(validator);
                webSocketService.sendUnreadCountToUser(validator.getUsername(), unreadCount);
                
                log.debug("Notification sent to validator: {}", validator.getUsername());
            }
            
            log.info("Successfully processed ReadingSubmittedEvent for reading ID: {}", 
                    event.getReadingId());
            
        } catch (Exception e) {
            log.error("Error processing ReadingSubmittedEvent for reading ID: {}", 
                    event.getReadingId(), e);
        }
    }

    /**
     * Handle ReadingValidatedEvent
     * Creates notification for the original Reader who submitted the reading
     */
    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleReadingValidatedEvent(ReadingValidatedEvent event) {
        log.info("Processing ReadingValidatedEvent for reading ID: {}", event.getReadingId());
        
        try {
            // Lookup notification type
            NotificationType readingValidatedType = notificationTypeRepository
                    .findByCode("READING_VALIDATED")
                    .orElseThrow(() -> new IllegalStateException(
                        "NotificationType 'READING_VALIDATED' not found in database"
                    ));
            
            // Find the original submitter (Reader)
            User originalSubmitter = userRepository.findByUsername(event.getOriginalSubmitter())
                    .orElseThrow(() -> new IllegalStateException(
                        "Original submitter not found: " + event.getOriginalSubmitter()
                    ));
            
            // Build message
            String message = String.format(
                    "Your reading for %s has been validated by %s",
                    event.getReadingIdentifier(),
                    event.getValidatedBy()
            );
            
            if (event.getComment() != null && !event.getComment().isEmpty()) {
                message += "\n\nComment: " + event.getComment();
            }
            
            // Create notification
            Notification notification = Notification.builder()
                    .title("Reading Validated")
                    .message(message)
                    .type(readingValidatedType)
                    .recipient(originalSubmitter)
                    .relatedEntityId(event.getReadingId().toString())
                    .relatedEntityType("READING")
                    .isRead(false)
                    .build();
            
            Notification savedNotification = notificationService.createNotification(notification);
            NotificationDTO dto = NotificationDTO.fromEntity(savedNotification);
            
            // Send real-time notification via WebSocket
            webSocketService.sendToUser(originalSubmitter.getUsername(), dto);
            
            // Update unread count
            Long unreadCount = notificationService.getUnreadCountForUser(originalSubmitter);
            webSocketService.sendUnreadCountToUser(originalSubmitter.getUsername(), unreadCount);
            
            log.info("Successfully processed ReadingValidatedEvent for reading ID: {}", 
                    event.getReadingId());
            
        } catch (Exception e) {
            log.error("Error processing ReadingValidatedEvent for reading ID: {}", 
                    event.getReadingId(), e);
        }
    }

    /**
     * Handle ReadingRejectedEvent
     * Creates notification for the original Reader who submitted the reading
     */
    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleReadingRejectedEvent(ReadingRejectedEvent event) {
        log.info("Processing ReadingRejectedEvent for reading ID: {}", event.getReadingId());
        
        try {
            // Lookup notification type
            NotificationType readingRejectedType = notificationTypeRepository
                    .findByCode("READING_REJECTED")
                    .orElseThrow(() -> new IllegalStateException(
                        "NotificationType 'READING_REJECTED' not found in database"
                    ));
            
            // Find the original submitter (Reader)
            User originalSubmitter = userRepository.findByUsername(event.getOriginalSubmitter())
                    .orElseThrow(() -> new IllegalStateException(
                        "Original submitter not found: " + event.getOriginalSubmitter()
                    ));
            
            // Build message
            String message = String.format(
                    "Your reading for %s has been rejected by %s\n\nReason: %s",
                    event.getReadingIdentifier(),
                    event.getRejectedBy(),
                    event.getRejectionReason()
            );
            
            // Create notification
            Notification notification = Notification.builder()
                    .title("Reading Rejected")
                    .message(message)
                    .type(readingRejectedType)
                    .recipient(originalSubmitter)
                    .relatedEntityId(event.getReadingId().toString())
                    .relatedEntityType("READING")
                    .isRead(false)
                    .build();
            
            Notification savedNotification = notificationService.createNotification(notification);
            NotificationDTO dto = NotificationDTO.fromEntity(savedNotification);
            
            // Send real-time notification via WebSocket
            webSocketService.sendToUser(originalSubmitter.getUsername(), dto);
            
            // Update unread count
            Long unreadCount = notificationService.getUnreadCountForUser(originalSubmitter);
            webSocketService.sendUnreadCountToUser(originalSubmitter.getUsername(), unreadCount);
            
            log.info("Successfully processed ReadingRejectedEvent for reading ID: {}", 
                    event.getReadingId());
            
        } catch (Exception e) {
            log.error("Error processing ReadingRejectedEvent for reading ID: {}", 
                    event.getReadingId(), e);
        }
    }
}
