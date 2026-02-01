/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationTypeRepository
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : System / Notification
 *
 **/

package dz.sh.trc.hyflo.system.notification.type.repository;

import dz.sh.trc.hyflo.system.notification.type.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for NotificationType entity
 */
@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {

    /**
     * Find notification type by code
     */
    Optional<NotificationType> findByCode(String code);

    /**
     * Find all active notification types
     */
    List<NotificationType> findByActiveTrue();

    /**
     * Check if notification type exists by code
     */
    boolean existsByCode(String code);

    /**
     * Global search across all text fields
     */
    @Query("""
        SELECT nt FROM NotificationType nt 
        WHERE LOWER(nt.code) LIKE LOWER(:searchTerm)
           OR LOWER(nt.designationAr) LIKE LOWER(:searchTerm)
           OR LOWER(nt.designationEn) LIKE LOWER(:searchTerm)
           OR LOWER(nt.designationFr) LIKE LOWER(:searchTerm)
           OR LOWER(nt.description) LIKE LOWER(:searchTerm)
        """)
    Page<NotificationType> globalSearch(@Param("searchTerm") String searchTerm, Pageable pageable);
}
