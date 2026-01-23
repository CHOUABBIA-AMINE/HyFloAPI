/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowEventRepository
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

import dz.sh.trc.hyflo.flow.core.model.FlowEvent;

@Repository
public interface FlowEventRepository extends JpaRepository<FlowEvent, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    List<FlowEvent> findByInfrastructureId(Long infrastructureId);
    
    List<FlowEvent> findBySeverityId(Long severityId);
    
    List<FlowEvent> findByStatusId(Long statusId);
    
    List<FlowEvent> findByReportedById(Long employeeId);
    
    List<FlowEvent> findByRelatedReadingId(Long readingId);
    
    List<FlowEvent> findByRelatedAlertId(Long alertId);
    
    List<FlowEvent> findByImpactOnFlow(Boolean impactOnFlow);
    
    List<FlowEvent> findByEventTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    List<FlowEvent> findByInfrastructureIdAndEventTimestampBetween(Long infrastructureId, LocalDateTime startTime, LocalDateTime endTime);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT fe FROM FlowEvent fe WHERE "
         + "fe.infrastructure.id = :infrastructureId AND "
         + "fe.eventTimestamp BETWEEN :startTime AND :endTime "
         + "ORDER BY fe.eventTimestamp DESC")
    Page<FlowEvent> findByInfrastructureAndTimeRange(
        @Param("infrastructureId") Long infrastructureId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
    
    @Query("SELECT fe FROM FlowEvent fe WHERE "
         + "fe.severity.id = :severityId AND "
         + "fe.eventTimestamp BETWEEN :startTime AND :endTime "
         + "ORDER BY fe.eventTimestamp DESC")
    Page<FlowEvent> findBySeverityAndTimeRange(
        @Param("severityId") Long severityId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
    
    @Query("SELECT fe FROM FlowEvent fe WHERE "
         + "fe.status.id = :statusId AND "
         + "fe.eventTimestamp BETWEEN :startTime AND :endTime "
         + "ORDER BY fe.eventTimestamp DESC")
    Page<FlowEvent> findByStatusAndTimeRange(
        @Param("statusId") Long statusId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
    
    @Query("SELECT fe FROM FlowEvent fe WHERE "
         + "fe.impactOnFlow = true AND "
         + "fe.eventTimestamp BETWEEN :startTime AND :endTime "
         + "ORDER BY fe.eventTimestamp DESC")
    Page<FlowEvent> findWithImpactOnFlowByTimeRange(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
    
    @Query("SELECT fe FROM FlowEvent fe WHERE "
         + "LOWER(fe.title) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(fe.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<FlowEvent> searchByAnyField(@Param("search") String search, Pageable pageable);
}
