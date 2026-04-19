/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowIncidentRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Monitoring
 *
 **/

package dz.sh.trc.hyflo.core.flow.monitoring.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowIncident;

@Repository
public interface FlowIncidentRepository extends JpaRepository<FlowIncident, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    List<FlowIncident> findByInfrastructureId(Long infrastructureId);
    
    List<FlowIncident> findBySeverityId(Long severityId);
    
    List<FlowIncident> findByStatusId(Long statusId);
    
    List<FlowIncident> findByReportedById(Long employeeId);
    
    List<FlowIncident> findByRelatedReadingId(Long readingId);
    
    List<FlowIncident> findByRelatedAlertId(Long alertId);
    
    List<FlowIncident> findByImpactOnFlow(Boolean impactOnFlow);
    
    List<FlowIncident> findByEventTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    List<FlowIncident> findByInfrastructureIdAndEventTimestampBetween(Long infrastructureId, LocalDateTime startTime, LocalDateTime endTime);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT fe FROM FlowIncident fe WHERE "
         + "fe.infrastructure.id = :infrastructureId AND "
         + "fe.eventTimestamp BETWEEN :startTime AND :endTime "
         + "ORDER BY fe.eventTimestamp DESC")
    Page<FlowIncident> findByInfrastructureAndTimeRange(
        @Param("infrastructureId") Long infrastructureId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
    
    @Query("SELECT fe FROM FlowIncident fe WHERE "
         + "fe.severity.id = :severityId AND "
         + "fe.eventTimestamp BETWEEN :startTime AND :endTime "
         + "ORDER BY fe.eventTimestamp DESC")
    Page<FlowIncident> findBySeverityAndTimeRange(
        @Param("severityId") Long severityId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
    
    @Query("SELECT fe FROM FlowIncident fe WHERE "
         + "fe.status.id = :statusId AND "
         + "fe.eventTimestamp BETWEEN :startTime AND :endTime "
         + "ORDER BY fe.eventTimestamp DESC")
    Page<FlowIncident> findByStatusAndTimeRange(
        @Param("statusId") Long statusId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
    
    @Query("SELECT fe FROM FlowIncident fe WHERE "
         + "fe.impactOnFlow = true AND "
         + "fe.eventTimestamp BETWEEN :startTime AND :endTime "
         + "ORDER BY fe.eventTimestamp DESC")
    Page<FlowIncident> findWithImpactOnFlowByTimeRange(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
    
    @Query("SELECT fe FROM FlowIncident fe WHERE "
         + "LOWER(fe.title) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(fe.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<FlowIncident> searchByAnyField(@Param("search") String search, Pageable pageable);
}
