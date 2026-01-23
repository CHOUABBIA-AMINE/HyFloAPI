/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowAlertRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowAlert;

@Repository
public interface FlowAlertRepository extends JpaRepository<FlowAlert, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    List<FlowAlert> findByThresholdId(Long thresholdId);
    
    List<FlowAlert> findByFlowReadingId(Long flowReadingId);
    
    List<FlowAlert> findByStatusId(Long statusId);
    
    List<FlowAlert> findByResolvedById(Long employeeId);
    
    List<FlowAlert> findByAcknowledgedById(Long employeeId);
    
    List<FlowAlert> findByNotificationSent(Boolean sent);
    
    List<FlowAlert> findByAlertTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    List<FlowAlert> findByThresholdIdAndStatusId(Long thresholdId, Long statusId);
    
    List<FlowAlert> findByStatusIdAndAlertTimestampBetween(Long statusId, LocalDateTime startTime, LocalDateTime endTime);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT fa FROM FlowAlert fa WHERE "
         + "fa.status.id = :statusId AND "
         + "fa.alertTimestamp BETWEEN :startTime AND :endTime "
         + "ORDER BY fa.alertTimestamp DESC")
    Page<FlowAlert> findByStatusAndTimeRange(
        @Param("statusId") Long statusId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
    
    @Query("SELECT fa FROM FlowAlert fa WHERE "
         + "fa.threshold.pipeline.id = :pipelineId AND "
         + "fa.alertTimestamp BETWEEN :startTime AND :endTime "
         + "ORDER BY fa.alertTimestamp DESC")
    Page<FlowAlert> findByPipelineAndTimeRange(
        @Param("pipelineId") Long pipelineId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
    
    @Query("SELECT fa FROM FlowAlert fa WHERE "
         + "fa.acknowledgedAt IS NULL "
         + "ORDER BY fa.alertTimestamp DESC")
    Page<FlowAlert> findUnacknowledged(Pageable pageable);
    
    @Query("SELECT fa FROM FlowAlert fa WHERE "
         + "fa.resolvedAt IS NULL "
         + "ORDER BY fa.alertTimestamp DESC")
    Page<FlowAlert> findUnresolved(Pageable pageable);
    
    @Query("SELECT fa FROM FlowAlert fa WHERE "
         + "fa.threshold.pipeline.id = :pipelineId AND "
         + "fa.resolvedAt IS NULL "
         + "ORDER BY fa.alertTimestamp DESC")
    Page<FlowAlert> findUnresolvedByPipeline(
        @Param("pipelineId") Long pipelineId,
        Pageable pageable);
}
