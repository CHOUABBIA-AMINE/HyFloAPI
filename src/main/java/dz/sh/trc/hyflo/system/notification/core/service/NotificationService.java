/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationService
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Service
 *  @Package    : System / Notification
 *
 **/

package dz.sh.trc.hyflo.system.notification.core.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.system.notification.core.dto.NotificationDTO;
import dz.sh.trc.hyflo.system.notification.core.model.Notification;
import dz.sh.trc.hyflo.system.notification.core.repository.NotificationRepository;
import dz.sh.trc.hyflo.system.security.model.User;
import dz.sh.trc.hyflo.system.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for Notification management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationService extends GenericService<Notification, NotificationDTO, Long> {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    protected JpaRepository<Notification, Long> getRepository() {
        return notificationRepository;
    }

    @Override
    protected NotificationDTO toDTO(Notification entity) {
        return NotificationDTO.fromEntity(entity);
    }

    @Override
    protected Notification toEntity(NotificationDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Notification entity, NotificationDTO dto) {
        dto.updateEntity(entity);
    }

    @Transactional
    public Notification createNotification(Notification notification) {
        log.debug("Creating notification: {}", notification.getTitle());
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public Page<NotificationDTO> getMyNotifications(Pageable pageable) {
        User currentUser = getCurrentUser();
        Page<Notification> notifications = notificationRepository
                .findByRecipientOrderByCreatedAtDesc(currentUser, pageable);
        return notifications.map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getMyUnreadNotifications() {
        User currentUser = getCurrentUser();
        List<Notification> unreadNotifications = notificationRepository
                .findByRecipientAndIsReadFalseOrderByCreatedAtDesc(currentUser);
        return unreadNotifications.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long getUnreadCount() {
        User currentUser = getCurrentUser();
        return notificationRepository.countByRecipientAndIsReadFalse(currentUser);
    }

    @Transactional
    public NotificationDTO markAsRead(Long notificationId) {
        User currentUser = getCurrentUser();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with ID: " + notificationId
                ));
        
        if (!notification.getRecipient().getId().equals(currentUser.getId())) {
            throw new SecurityException("Cannot mark another user's notification as read");
        }
        
        notification.markAsRead();
        Notification updated = notificationRepository.save(notification);
        log.debug("Notification {} marked as read by user {}", notificationId, currentUser.getUsername());
        return toDTO(updated);
    }

    @Transactional
    public void markAllAsRead() {
        User currentUser = getCurrentUser();
        List<Notification> unreadNotifications = notificationRepository
                .findByRecipientAndIsReadFalseOrderByCreatedAtDesc(currentUser);
        unreadNotifications.forEach(Notification::markAsRead);
        notificationRepository.saveAll(unreadNotifications);
        log.info("Marked {} notifications as read for user {}", 
                unreadNotifications.size(), currentUser.getUsername());
    }

    @Transactional
    public void deleteOldReadNotifications(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        notificationRepository.deleteOldReadNotifications(cutoffDate);
        log.info("Deleted read notifications older than {}", cutoffDate);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    @Override
    protected String getEntityName() {
        return "Notification";
    }
    
    /**
     * Get unread count for specific user (for WebSocket updates)
     */
    @Transactional(readOnly = true)
    public Long getUnreadCountForUser(User user) {
        return notificationRepository.countByRecipientAndIsReadFalse(user);
    }

}
