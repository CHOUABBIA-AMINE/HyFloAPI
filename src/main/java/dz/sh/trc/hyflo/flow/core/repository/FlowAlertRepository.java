/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: FlowAlertRepository
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowAlert;

@Repository
public interface FlowAlertRepository extends JpaRepository<FlowAlert, Long>, 
                                             JpaSpecificationExecutor<FlowAlert> {
    
    List<FlowAlert> findByStatus(String status);
    
    @Query("SELECT fa FROM FlowAlert fa WHERE fa.status IN :statuses ORDER BY fa.alertTimestamp DESC")
    List<FlowAlert> findByStatusIn(@Param("statuses") List<String> statuses);
    
    @Query("SELECT fa FROM FlowAlert fa WHERE " +
           "fa.threshold.infrastructure.id = :infrastructureId AND " +
           "fa.status IN :statuses " +
           "ORDER BY fa.alertTimestamp DESC")
    List<FlowAlert> findByInfrastructureAndStatus(
        @Param("infrastructureId") Long infrastructureId,
        @Param("statuses") List<String> statuses);
    
    @Query("SELECT fa FROM FlowAlert fa WHERE " +
           "fa.alertTimestamp BETWEEN :start AND :end " +
           "ORDER BY fa.alertTimestamp DESC")
    List<FlowAlert> findByDateRange(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);
    
    @Query("SELECT fa FROM FlowAlert fa WHERE " +
           "fa.threshold.severity = :severity AND " +
           "fa.status IN :statuses " +
           "ORDER BY fa.alertTimestamp DESC")
    List<FlowAlert> findBySeverityAndStatus(
        @Param("severity") String severity,
        @Param("statuses") List<String> statuses);
    
    @Query("SELECT fa FROM FlowAlert fa WHERE " +
           "fa.notificationSent = false AND " +
           "fa.status = 'NEW'")
    List<FlowAlert> findPendingNotifications();
}
