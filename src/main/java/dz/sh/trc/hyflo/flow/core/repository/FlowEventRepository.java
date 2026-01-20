/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: FlowEventRepository
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

import dz.sh.trc.hyflo.flow.core.model.FlowEvent;

@Repository
public interface FlowEventRepository extends JpaRepository<FlowEvent, Long>, 
                                            JpaSpecificationExecutor<FlowEvent> {
    
    List<FlowEvent> findByStatus(String status);
    
    List<FlowEvent> findByEventType(String eventType);
    
    @Query("SELECT fe FROM FlowEvent fe WHERE " +
           "fe.infrastructure.id = :infrastructureId " +
           "ORDER BY fe.eventTimestamp DESC")
    List<FlowEvent> findByInfrastructure(@Param("infrastructureId") Long infrastructureId);
    
    @Query("SELECT fe FROM FlowEvent fe WHERE " +
           "fe.eventTimestamp BETWEEN :start AND :end " +
           "ORDER BY fe.eventTimestamp DESC")
    List<FlowEvent> findByDateRange(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);
    
    @Query("SELECT fe FROM FlowEvent fe WHERE " +
           "fe.severity = :severity AND " +
           "fe.status IN :statuses " +
           "ORDER BY fe.eventTimestamp DESC")
    List<FlowEvent> findBySeverityAndStatus(
        @Param("severity") String severity,
        @Param("statuses") List<String> statuses);
    
    @Query("SELECT fe FROM FlowEvent fe WHERE " +
           "fe.impactOnFlow = true AND " +
           "fe.status IN ('OPEN', 'IN_PROGRESS') " +
           "ORDER BY fe.eventTimestamp DESC")
    List<FlowEvent> findActiveEventsWithFlowImpact();
}
