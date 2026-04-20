package dz.sh.trc.hyflo.core.communication.Notification.dto.request;

import java.util.Map;

/**
 * Request DTO for sending a notification.
 * Uses localization keys instead of hardcoded strings.
 */
public record SendNotificationRequest(
    String notificationTypeCode,              // e.g. "READING_SUBMITTED"
    String titleKey,                           // localization key for title
    String messageKey,                         // localization key for message
    Map<String, String> placeholders,          // {actorName}, {readingId}, etc.
    String relatedEntityId,                    // e.g. "42"
    String relatedEntityType,                  // e.g. "FLOW_READING"
    Long recipientUserId                       // FK to User
) {}
