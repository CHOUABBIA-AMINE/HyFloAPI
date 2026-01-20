/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowThresholdRepository
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;

@Repository
public interface FlowThresholdRepository extends JpaRepository<FlowThreshold, Long>, 
                                                 JpaSpecificationExecutor<FlowThreshold> {
    
    List<FlowThreshold> findByIsActive(Boolean isActive);
    
    @Query("SELECT ft FROM FlowThreshold ft WHERE " +
           "ft.infrastructure.id = :infrastructureId AND ft.isActive = true")
    List<FlowThreshold> findActiveByInfrastructure(@Param("infrastructureId") Long infrastructureId);
    
    @Query("SELECT ft FROM FlowThreshold ft WHERE " +
           "ft.infrastructure.id = :infrastructureId AND " +
           "ft.measurementType.code = :measurementTypeCode AND " +
           "ft.parameter = :parameter AND " +
           "ft.isActive = true")
    List<FlowThreshold> findActiveByInfrastructureAndType(
        @Param("infrastructureId") Long infrastructureId,
        @Param("measurementTypeCode") String measurementTypeCode,
        @Param("parameter") String parameter);
    
    @Query("SELECT ft FROM FlowThreshold ft WHERE ft.severity = :severity AND ft.isActive = true")
    List<FlowThreshold> findActiveBySeverity(@Param("severity") String severity);
}
