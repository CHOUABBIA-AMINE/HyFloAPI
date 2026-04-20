package dz.sh.trc.hyflo.core.communication.Notification.dto.response;

import java.time.LocalDateTime;

public record NotificationSummary(
    Long id,
    String title,
    Boolean isRead,
    LocalDateTime createdAt,
    String typeCode
) {}
