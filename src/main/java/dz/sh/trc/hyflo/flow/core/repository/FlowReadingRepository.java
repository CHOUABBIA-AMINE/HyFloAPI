/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingRepository
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
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowReading;

@Repository
public interface FlowReadingRepository extends JpaRepository<FlowReading, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    List<FlowReading> findByPipelineId(Long pipelineId);
    
    List<FlowReading> findByRecordedById(Long employeeId);
    
    List<FlowReading> findByValidatedById(Long employeeId);
    
    List<FlowReading> findByValidationStatusId(Long validationStatusId);
    
    List<FlowReading> findByPipelineIdAndRecordedAtBetween(Long pipelineId, LocalDateTime startTime, LocalDateTime endTime);
    
    Optional<FlowReading> findByPipelineIdAndRecordedAt(Long pipelineId, LocalDateTime recordedAt);
    
    boolean existsByPipelineIdAndRecordedAt(Long pipelineId, LocalDateTime recordedAt);
    
    List<FlowReading> findByRecordedAtBetween(LocalDateTime startTime, LocalDateTime endTime);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT fr FROM FlowReading fr WHERE "
         + "fr.pipeline.id = :pipelineId AND "
         + "fr.recordedAt BETWEEN :startTime AND :endTime "
         + "ORDER BY fr.recordedAt DESC")
    Page<FlowReading> findByPipelineAndTimeRange(
        @Param("pipelineId") Long pipelineId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable);
    
    @Query("SELECT fr FROM FlowReading fr WHERE "
         + "fr.pipeline.id = :pipelineId AND "
         + "fr.validationStatus.id = :statusId "
         + "ORDER BY fr.recordedAt DESC")
    Page<FlowReading> findByPipelineAndValidationStatus(
        @Param("pipelineId") Long pipelineId,
        @Param("statusId") Long statusId,
        Pageable pageable);
    
    @Query("SELECT fr FROM FlowReading fr WHERE "
         + "fr.pipeline.id = :pipelineId "
         + "ORDER BY fr.recordedAt DESC")
    Page<FlowReading> findLatestByPipeline(
        @Param("pipelineId") Long pipelineId,
        Pageable pageable);
}
