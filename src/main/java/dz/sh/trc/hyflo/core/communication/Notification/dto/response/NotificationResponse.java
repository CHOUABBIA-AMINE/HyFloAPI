package dz.sh.trc.hyflo.core.communication.Notification.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    String title,
    String message,
    String relatedEntityId,
    String relatedEntityType,
    Boolean isRead,
    LocalDateTime readAt,
    LocalDateTime createdAt,
    Long typeId,
    String typeCode,
    String typeDesignationFr,
    Long recipientId
) {}
