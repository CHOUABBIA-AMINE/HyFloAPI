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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import jakarta.persistence.LockModeType;

@Repository
public interface FlowReadingRepository extends JpaRepository<FlowReading, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    List<FlowReading> findByPipelineId(Long pipelineId);
    
    List<FlowReading> findByRecordedById(Long employeeId);
    
    List<FlowReading> findByValidatedById(Long employeeId);
    
    List<FlowReading> findByValidationStatusId(Long validationStatusId);
    
    List<FlowReading> findByReadingSlotId(Long readingSlotId);
    
    List<FlowReading> findByPipelineIdAndReadingDateBetween(Long pipelineId, LocalDate startDate, LocalDate endDate);
    
    List<FlowReading> findByPipelineIdAndReadingSlotIdAndReadingDateBetween(Long pipelineId, Long readingSlotId, LocalDate startDate, LocalDate endDate);
    
    List<FlowReading> findByPipelineIdAndRecordedAtBetween(Long pipelineId, LocalDateTime startTime, LocalDateTime endTime);
    
    Optional<FlowReading> findByPipelineIdAndRecordedAt(Long pipelineId, LocalDateTime recordedAt);
    
    boolean existsByPipelineIdAndRecordedAt(Long pipelineId, LocalDateTime recordedAt);
    
    List<FlowReading> findByRecordedAtBetween(LocalDateTime startTime, LocalDateTime endTime);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT fr FROM FlowReading fr WHERE "
         + "fr.pipeline.id = :pipelineId AND "
         + "fr.readingDate BETWEEN :startDate AND :endDate "
         + "ORDER BY fr.readingDate DESC")
    Page<FlowReading> findByPipelineAndReadingDateRange(
        @Param("pipelineId") Long pipelineId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);
    
    @Query("SELECT fr FROM FlowReading fr WHERE "
         + "fr.pipeline.id = :pipelineId AND "
         + "fr.readingSlot.id = :readingSlotId AND "
         + "fr.readingDate BETWEEN :startDate AND :endDate "
         + "ORDER BY fr.readingDate DESC")
    Page<FlowReading> findByPipelineAndReadingSlotAndReadingDateRange(
        @Param("pipelineId") Long pipelineId,
        @Param("readingSlotId") Long readingSlotId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);
    
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
    Page<FlowReading> findByPipelineAndValidationStatus(@Param("pipelineId") Long pipelineId, @Param("statusId") Long statusId, Pageable pageable);
    
    @Query("SELECT fr FROM FlowReading fr WHERE "
         + "fr.validationStatus.id = :statusId "
         + "ORDER BY fr.recordedAt DESC")
    Page<FlowReading> findByValidationStatus(@Param("statusId") Long statusId, Pageable pageable);
    
    @Query("SELECT fr FROM FlowReading fr WHERE "
         + "fr.pipeline.id = :pipelineId "
         + "ORDER BY fr.recordedAt DESC")
    Page<FlowReading> findLatestByPipeline(@Param("pipelineId") Long pipelineId, Pageable pageable);
    
    /**
     * Slot coverage query - shows all pipelines with LEFT JOIN to readings
     */
    @Query("""
        SELECT 
            p.id as pipelineId,
            p.code as pipelineCode,
            p.name as pipelineName,
            fr.id as readingId,
            COALESCE(vs.code, 'NOT_RECORDED') as validationStatusCode,
            fr.recordedAt as recordedAt,
            fr.validatedAt as validatedAt,
            CONCAT(rec.firstName, ' ', rec.lastName) as recordedByName,
            CONCAT(val.firstName, ' ', val.lastName) as validatedByName,
            CASE WHEN fr.id IS NOT NULL THEN true ELSE false END as hasReading
        FROM Pipeline p
        LEFT JOIN FlowReading fr 
            ON fr.pipeline = p
           AND fr.readingDate = :readingDate
           AND fr.readingSlot.id = :slotId
        LEFT JOIN ValidationStatus vs ON fr.validationStatus = vs
        LEFT JOIN Employee rec ON fr.recordedBy = rec
        LEFT JOIN Employee val ON fr.validatedBy = val
        WHERE p.manager.id = :structureId
        ORDER BY p.code ASC
    """)
    List<SlotCoverageProjection> findSlotCoverage(
        @Param("readingDate") LocalDate readingDate,
        @Param("slotId") Long slotId,
        @Param("structureId") Long structureId
    );
    
    /**
     * Check if slot is complete
     */
    @Query("""
        SELECT COUNT(DISTINCT p.id) = 
               COUNT(DISTINCT CASE 
                   WHEN vs.code IN ('SUBMITTED', 'APPROVED') 
                   THEN fr.id 
               END)
        FROM Pipeline p
        LEFT JOIN FlowReading fr 
               ON fr.pipeline = p
              AND fr.readingDate = :readingDate
              AND fr.readingSlot.id = :slotId
        LEFT JOIN ValidationStatus vs ON fr.validationStatus = vs
        WHERE p.manager.id = :structureId
    """)
    Boolean isSlotComplete(
        @Param("readingDate") LocalDate readingDate,
        @Param("slotId") Long slotId,
        @Param("structureId") Long structureId
    );
    
    /**
     * Find readings pending validation
     */
    @Query("""
        SELECT fr FROM FlowReading fr
        WHERE fr.pipeline.manager.id = :structureId
            AND fr.readingDate = :readingDate
            AND fr.readingSlot.id = :slotId
            AND fr.validationStatus.code = 'SUBMITTED'
        ORDER BY fr.recordedAt ASC
    """)
    List<FlowReading> findPendingValidations(
        @Param("readingDate") LocalDate readingDate,
        @Param("slotId") Long slotId,
        @Param("structureId") Long structureId
    );
    
    /**
     * Find reading with pessimistic lock for workflow transitions
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT fr FROM FlowReading fr WHERE fr.id = :id")
    Optional<FlowReading> findByIdForUpdate(@Param("id") Long id);
    
    /**
     * Find existing reading by pipeline, date, slot
     */
    @Query("""
        SELECT fr FROM FlowReading fr
        WHERE fr.pipeline.id = :pipelineId
            AND fr.readingDate = :readingDate
            AND fr.readingSlot.id = :slotId
    """)
    Optional<FlowReading> findByPipelineAndDateAndSlot(
        @Param("pipelineId") Long pipelineId,
        @Param("readingDate") LocalDate readingDate,
        @Param("slotId") Long slotId
    );
    
    /**
     * Slot statistics aggregation
     */
    @Query("""
        SELECT 
            COALESCE(vs.code, 'NOT_RECORDED') as statusCode,
            COUNT(p.id) as count
        FROM Pipeline p
        LEFT JOIN FlowReading fr 
               ON fr.pipeline = p
              AND fr.readingDate = :readingDate
              AND fr.readingSlot.id = :slotId
        LEFT JOIN ValidationStatus vs ON fr.validationStatus = vs
        WHERE p.manager.id = :structureId
        GROUP BY vs.code
    """)
    List<Object[]> getSlotStatistics(
        @Param("readingDate") LocalDate readingDate,
        @Param("slotId") Long slotId,
        @Param("structureId") Long structureId
    );
}
