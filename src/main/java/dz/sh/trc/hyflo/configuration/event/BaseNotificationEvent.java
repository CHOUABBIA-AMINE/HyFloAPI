/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : BaseNotificationEvent
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Abstract Class
 *  @Layer      : Event
 *  @Package    : Configuration / Event
 *
 **/

package dz.sh.trc.hyflo.configuration.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Base class for all notification events in the system
 * Provides common structure for event-driven notifications
 * 
 * All workflow events should extend this class to leverage
 * the generic notification system.
 * 
 * Usage Example:
 * <pre>
 * {@code
 * @Getter
 * @SuperBuilder
 * @ToString(callSuper = true)
 * public class OrderPlacedEvent extends BaseNotificationEvent {
 *     private final Long orderId;
 *     private final String customerName;
 *     private final BigDecimal totalAmount;
 * }
 * }
 * </pre>
 */
@Getter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseNotificationEvent {

    /**
     * The code of the notification type (e.g., "READING_SUBMITTED", "ORDER_PLACED")
     * Must match a NotificationType.code in the database
     */
    private String notificationTypeCode;

    /**
     * The title of the notification
     * Will be displayed prominently in the notification UI
     */
    private String notificationTitle;

    /**
     * The message/body of the notification
     * Can include formatting and multiple lines
     */
    private String notificationMessage;

    /**
     * The ID of the entity related to this notification
     * (e.g., reading ID, order ID, document ID)
     */
    private String relatedEntityId;

    /**
     * The type of the entity related to this notification
     * (e.g., "READING", "ORDER", "DOCUMENT")
     */
    private String relatedEntityType;

    /**
     * List of usernames to receive this notification
     * If null or empty, notification strategy will determine recipients
     */
    private List<String> recipientUsernames;

    /**
     * Role-based targeting: send to all users with this role
     * (e.g., "ROLE_VALIDATOR", "ROLE_ADMIN")
     * If null, role-based targeting is not used
     */
    private String recipientRole;

    /**
     * Notification priority (optional)
     * LOW, NORMAL, HIGH, URGENT
     */
    private NotificationPriority priority;

    /**
     * Additional metadata as key-value pairs (optional)
     * Can be used for custom notification behavior
     */
    private java.util.Map<String, Object> metadata;

    /**
     * Timestamp when the event occurred (ISO 8601 format)
     */
    private String occurredAt;

    /**
     * Notification priority levels
     */
    public enum NotificationPriority {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }

    /**
     * Get recipient usernames (with null-safe empty list)
     */
    public List<String> getRecipientUsernames() {
        return recipientUsernames != null ? recipientUsernames : List.of();
    }

    /**
     * Get metadata (with null-safe empty map)
     */
    public java.util.Map<String, Object> getMetadata() {
        return metadata != null ? metadata : java.util.Map.of();
    }

    /**
     * Check if this notification should be sent to specific users
     */
    public boolean hasSpecificRecipients() {
        return recipientUsernames != null && !recipientUsernames.isEmpty();
    }

    /**
     * Check if this notification should be sent by role
     */
    public boolean hasRoleBasedRecipients() {
        return recipientRole != null && !recipientRole.isEmpty();
    }

    /**
     * Get priority (defaults to NORMAL if not set)
     */
    public NotificationPriority getPriority() {
        return priority != null ? priority : NotificationPriority.NORMAL;
    }
}
