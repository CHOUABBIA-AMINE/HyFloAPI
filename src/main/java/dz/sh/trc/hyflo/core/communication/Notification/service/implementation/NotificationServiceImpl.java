package dz.sh.trc.hyflo.core.communication.Notification.service.implementation;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.core.communication.Notification.dto.request.SendNotificationRequest;
import dz.sh.trc.hyflo.core.communication.Notification.dto.response.NotificationResponse;
import dz.sh.trc.hyflo.core.communication.Notification.dto.response.NotificationSummary;
import dz.sh.trc.hyflo.core.communication.Notification.mapper.NotificationMapper;
import dz.sh.trc.hyflo.core.communication.Notification.model.Notification;
import dz.sh.trc.hyflo.core.communication.Notification.repository.NotificationRepository;
import dz.sh.trc.hyflo.core.communication.Notification.service.NotificationService;
import dz.sh.trc.hyflo.core.communication.type.model.NotificationType;
import dz.sh.trc.hyflo.core.communication.type.repository.NotificationTypeRepository;
import dz.sh.trc.hyflo.core.system.security.model.User;
import dz.sh.trc.hyflo.core.system.security.repository.UserRepository;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import dz.sh.trc.hyflo.platform.localization.service.LocalizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final LocalizationService localizationService;

    @Override
    @Transactional
    public NotificationResponse send(SendNotificationRequest request) {
        // Resolve notification type by code
        NotificationType type = notificationTypeRepository.findByCode(request.notificationTypeCode())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "NotificationType not found: " + request.notificationTypeCode()));

        // Resolve recipient
        User recipient = userRepository.findById(request.recipientUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + request.recipientUserId()));

        // Translate title and message using localization keys
        Map<String, String> placeholders = request.placeholders() != null ? request.placeholders() : Map.of();
        String title = localizationService.translate(request.titleKey(), "FR", placeholders);
        String message = localizationService.translate(request.messageKey(), "FR", placeholders);

        // Build and persist notification
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .relatedEntityId(request.relatedEntityId())
                .relatedEntityType(request.relatedEntityType())
                .type(type)
                .recipient(recipient)
                .build();

        notification = notificationRepository.save(notification);
        log.debug("Notification sent: type={}, recipient={}, entity={}/{}",
                request.notificationTypeCode(), request.recipientUserId(),
                request.relatedEntityType(), request.relatedEntityId());

        return notificationMapper.toResponse(notification);
    }

    @Override
    @Transactional
    public void sendWorkflowNotification(String typeCode, String entityType,
            Long entityId, Long actorEmployeeId, Long recipientUserId) {
        if (recipientUserId == null) {
            log.debug("Skipping notification: no recipient for workflow event type={}", typeCode);
            return;
        }

        String titleKey = "NOTIFICATION_TITLE_" + typeCode;
        String messageKey = "NOTIFICATION_MSG_" + typeCode;

        Map<String, String> placeholders = Map.of(
                "entityType", entityType != null ? entityType : "",
                "entityId", entityId != null ? entityId.toString() : "",
                "actorId", actorEmployeeId != null ? actorEmployeeId.toString() : ""
        );

        send(new SendNotificationRequest(typeCode, titleKey, messageKey,
                placeholders, entityId != null ? entityId.toString() : null,
                entityType, recipientUserId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationSummary> getForUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user, pageable)
                .map(notificationMapper::toSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        return notificationRepository.countByRecipientAndIsReadFalse(user);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found: " + notificationId));
        notification.markAsRead();
        notificationRepository.save(notification);
    }
}
