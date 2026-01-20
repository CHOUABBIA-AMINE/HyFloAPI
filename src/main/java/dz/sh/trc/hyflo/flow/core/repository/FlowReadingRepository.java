/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowReadingRepository
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
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

import dz.sh.trc.hyflo.flow.core.model.FlowReading;

@Repository
public interface FlowReadingRepository extends JpaRepository<FlowReading, Long>, 
                                               JpaSpecificationExecutor<FlowReading> {
    
    @Query("SELECT fr FROM FlowReading fr WHERE fr.validationStatus.code = :statusCode")
    List<FlowReading> findByValidationStatus(@Param("statusCode") String statusCode);
    
    @Query("SELECT fr FROM FlowReading fr " +
           "JOIN fr.recordedBy e " +
           "JOIN e.job j " +
           "JOIN j.structure s " +
           "WHERE s.id = :structureId AND fr.validationStatus.code = 'SUBMITTED'")
    List<FlowReading> findPendingValidationByStructure(@Param("structureId") Long structureId);
    
    List<FlowReading> findByReadingTimestampBetween(
        LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT fr FROM FlowReading fr WHERE " +
           "fr.infrastructure.id = :infrastructureId AND " +
           "fr.readingTimestamp BETWEEN :start AND :end " +
           "ORDER BY fr.readingTimestamp ASC")
    List<FlowReading> findByInfrastructureAndDateRange(
        @Param("infrastructureId") Long infrastructureId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);
    
    @Query("SELECT fr FROM FlowReading fr WHERE " +
           "fr.recordedBy.id = :employeeId AND " +
           "fr.readingTimestamp BETWEEN :start AND :end " +
           "ORDER BY fr.readingTimestamp DESC")
    List<FlowReading> findByOperatorAndDateRange(
        @Param("employeeId") Long employeeId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);
    
    @Query("SELECT fr FROM FlowReading fr " +
           "JOIN fr.recordedBy e " +
           "JOIN e.job j " +
           "JOIN j.structure s " +
           "WHERE s.id = :structureId AND " +
           "fr.readingTimestamp BETWEEN :start AND :end " +
           "ORDER BY fr.readingTimestamp DESC")
    List<FlowReading> findByStructureAndDateRange(
        @Param("structureId") Long structureId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);
    
    @Query("SELECT fr FROM FlowReading fr " +
           "JOIN fr.recordedBy e " +
           "JOIN e.job j " +
           "JOIN j.structure s " +
           "WHERE s.id = :structureId AND fr.validationStatus.code = :status")
    List<FlowReading> findByStructureAndStatus(
        @Param("structureId") Long structureId,
        @Param("status") String status);
    
    @Query("SELECT fr FROM FlowReading fr WHERE " +
           "fr.infrastructure.id = :infrastructureId AND " +
           "fr.measurementType.code IN ('ENTRY', 'EXIT') AND " +
           "fr.readingTimestamp BETWEEN :start AND :end AND " +
           "fr.validationStatus.code = 'VALIDATED' " +
           "ORDER BY fr.measurementType.code, fr.readingTimestamp ASC")
    List<FlowReading> findValidatedFlowsForBalance(
        @Param("infrastructureId") Long infrastructureId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);
    
    @Query("SELECT fr FROM FlowReading fr WHERE " +
           "fr.qualityFlag.code IN :flags AND " +
           "fr.readingTimestamp BETWEEN :start AND :end")
    List<FlowReading> findByQualityFlagsAndDateRange(
        @Param("flags") List<String> flags,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);
    
    @Query("SELECT COUNT(fr) > 0 FROM FlowReading fr WHERE " +
           "fr.infrastructure.id = :infrastructureId AND " +
           "fr.measurementType.id = :measurementTypeId AND " +
           "fr.readingTimestamp = :timestamp AND " +
           "(:excludeId IS NULL OR fr.id != :excludeId)")
    boolean existsDuplicateReading(
        @Param("infrastructureId") Long infrastructureId,
        @Param("measurementTypeId") Long measurementTypeId,
        @Param("timestamp") LocalDateTime timestamp,
        @Param("excludeId") Long excludeId);
}
