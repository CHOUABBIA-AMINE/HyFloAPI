/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationWebSocketService
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Service
 *  @Package    : System / Notification / WebSocket
 *
 **/

package dz.sh.trc.hyflo.configuration.websocket;

import dz.sh.trc.hyflo.system.notification.core.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for sending notifications via WebSocket
 * 
 * Provides methods to send notifications to:
 * - Specific users
 * - All users (broadcast)
 * - Role-based groups
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Send notification to a specific user
     * 
     * @param username Target username
     * @param notification Notification DTO to send
     */
    public void sendToUser(String username, NotificationDTO notification) {
        log.debug("Sending notification to user: {} - {}", username, notification.getTitle());
        
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                notification
        );
    }

    /**
     * Send notification count update to a specific user
     * 
     * @param username Target username
     * @param unreadCount Current unread notification count
     */
    public void sendUnreadCountToUser(String username, Long unreadCount) {
        log.debug("Sending unread count to user: {} - count: {}", username, unreadCount);
        
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications/count",
                unreadCount
        );
    }

    /**
     * Broadcast notification to all connected users
     * (Use sparingly - only for system-wide announcements)
     * 
     * @param notification Notification DTO to broadcast
     */
    public void broadcastToAll(NotificationDTO notification) {
        log.info("Broadcasting notification to all users: {}", notification.getTitle());
        
        messagingTemplate.convertAndSend(
                "/topic/notifications",
                notification
        );
    }

    /**
     * Send notification to all users with a specific role
     * (Implementation requires active session tracking)
     * 
     * @param role Target role (e.g., "ROLE_VALIDATOR")
     * @param notification Notification DTO to send
     */
    public void sendToRole(String role, NotificationDTO notification) {
        log.debug("Sending notification to role: {} - {}", role, notification.getTitle());
        
        // Send to role-specific topic
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + role.toLowerCase().replace("role_", ""),
                notification
        );
    }
}
