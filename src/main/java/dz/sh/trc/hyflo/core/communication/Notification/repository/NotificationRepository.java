/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationRepository
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : System / Notification
 *
 **/

package dz.sh.trc.hyflo.core.communication.Notification.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.core.communication.Notification.model.Notification;
import dz.sh.trc.hyflo.core.system.security.model.User;

/**
 * Repository for Notification entity
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByRecipientOrderByCreatedAtDesc(User recipient, Pageable pageable);

    List<Notification> findByRecipientAndIsReadFalseOrderByCreatedAtDesc(User recipient);

    Long countByRecipientAndIsReadFalse(User recipient);

    List<Notification> findByRecipientAndCreatedAtAfterOrderByCreatedAtDesc(
            User recipient, 
            LocalDateTime since
    );

    @Query("DELETE FROM Notification n WHERE n.isRead = true AND n.readAt < :cutoffDate")
    void deleteOldReadNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);
}
