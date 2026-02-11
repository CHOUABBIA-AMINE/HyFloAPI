/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-07-2026 - Added 6 operational monitoring queries
 * 	@UpdatedOn	: 02-07-2026 - Added intelligence service queries
 * 	@UpdatedOn	: 02-10-2026 - Phase 1: Removed analytics queries (moved to IntelligenceQueryRepository)
 * 	@UpdatedOn	: 02-10-2026 - Phase 4: Added JOIN FETCH to prevent N+1 queries
 * 	@UpdatedOn	: 02-11-2026 - Added findByReadingDateAndReadingSlotIdAndStructureId for slot coverage
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Core
 *
 * 	@Refactoring: Phase 1 - Extracted 4 complex analytics queries to IntelligenceQueryRepository:
 * 	              - getDailyCompletionStatistics()
 * 	              - getValidatorWorkloadDistribution()
 * 	              - getSubmissionTrends()
 * 	              - getPipelineCoverageByDateRange()
 * 	              
 * 	              This repository now focuses on core CRUD operations and simple queries.
 * 	
 * 	@Refactoring: Phase 4 - Performance Optimization:
 * 	              - Added JOIN FETCH for lazy associations (pipeline, validationStatus, etc.)
 * 	              - Prevents N+1 query problem when accessing entity relationships
 * 	              - Improves performance in intelligence services using facades
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
    
    /**
     * Find all readings for a specific date, slot, and structure.
     * Used by SlotCoverageService to get all readings for coverage calculation.
     * 
     * @param readingDate Business date
     * @param readingSlotId Slot ID (Long, not Integer)
     * @param structureId Structure ID
     * @return List of readings matching criteria
     */
    @Query("""
        SELECT fr FROM FlowReading fr
        LEFT JOIN FETCH fr.pipeline p
        LEFT JOIN FETCH fr.validationStatus
        LEFT JOIN FETCH fr.readingSlot
        LEFT JOIN FETCH fr.recordedBy
        LEFT JOIN FETCH fr.validatedBy
        WHERE fr.readingDate = :readingDate
            AND fr.readingSlot.id = :readingSlotId
            AND p.manager.id = :structureId
        ORDER BY p.code ASC
    """)
    List<FlowReading> findByReadingDateAndReadingSlotIdAndStructureId(
        @Param("readingDate") LocalDate readingDate,
        @Param("readingSlotId") Long readingSlotId,
        @Param("structureId") Long structureId
    );

    // ========== INTELLIGENCE SERVICE QUERIES (WITH JOIN FETCH) ==========
    
    /**
     * Find latest reading for a specific pipeline
     * Used for current measurement display in overview
     * 
     * PERFORMANCE: Added JOIN FETCH to prevent N+1 queries
     */
    @Query("""
        SELECT fr FROM FlowReading fr
        LEFT JOIN FETCH fr.pipeline
        LEFT JOIN FETCH fr.validationStatus
        LEFT JOIN FETCH fr.recordedBy
        LEFT JOIN FETCH fr.validatedBy
        WHERE fr.pipeline.id = :pipelineId
        ORDER BY fr.recordedAt DESC
        LIMIT 1
    """)
    Optional<FlowReading> findTopByPipelineIdOrderByRecordedAtDesc(@Param("pipelineId") Long pipelineId);
    
    /**
     * Find all readings for a specific pipeline and date
     * Used for slot coverage calculation
     * 
     * PERFORMANCE: Added JOIN FETCH to prevent N+1 queries
     */
    @Query("""
        SELECT fr FROM FlowReading fr
        LEFT JOIN FETCH fr.pipeline
        LEFT JOIN FETCH fr.validationStatus
        LEFT JOIN FETCH fr.readingSlot
        LEFT JOIN FETCH fr.recordedBy
        LEFT JOIN FETCH fr.validatedBy
        WHERE fr.pipeline.id = :pipelineId
            AND fr.readingDate = :readingDate
        ORDER BY fr.readingSlot.displayOrder ASC
    """)
    List<FlowReading> findByPipelineIdAndReadingDate(
        @Param("pipelineId") Long pipelineId, 
        @Param("readingDate") LocalDate readingDate
    );
    
    /**
     * Find readings within date range ordered chronologically
     * Used for time-series analysis
     * 
     * PERFORMANCE: Added JOIN FETCH to prevent N+1 queries
     */
    @Query("""
        SELECT fr FROM FlowReading fr
        LEFT JOIN FETCH fr.pipeline
        LEFT JOIN FETCH fr.validationStatus
        LEFT JOIN FETCH fr.readingSlot
        WHERE fr.pipeline.id = :pipelineId
            AND fr.readingDate BETWEEN :startDate AND :endDate
        ORDER BY fr.readingDate ASC, fr.recordedAt ASC
    """)
    List<FlowReading> findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc(
        @Param("pipelineId") Long pipelineId, 
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );

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
     * FIX #4: Added slot timing for deadline calculation
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
            CONCAT(rec.firstNameLt, ' ', rec.lastNameLt) as recordedByName,
            CONCAT(val.firstNameLt, ' ', val.lastNameLt) as validatedByName,
            CASE WHEN fr.id IS NOT NULL THEN true ELSE false END as hasReading,
            :readingDate as readingDate,
            slot.startTime as slotStartTime,
            slot.endTime as slotEndTime
        FROM Pipeline p
        LEFT JOIN FlowReading fr 
            ON fr.pipeline = p
           AND fr.readingDate = :readingDate
           AND fr.readingSlot.id = :slotId
        LEFT JOIN ReadingSlot slot ON slot.id = :slotId
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
     * Check if slot is complete (all pipelines have APPROVED readings)
     */
    @Query("""
        SELECT 
            COUNT(DISTINCT p.id) = 
            COUNT(DISTINCT CASE 
                WHEN vs.code = 'APPROVED' 
                THEN fr.id 
                ELSE NULL 
            END)
            AND COUNT(DISTINCT p.id) > 0
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
     * 
     * PERFORMANCE: Added JOIN FETCH to prevent N+1 queries
     */
    @Query("""
        SELECT fr FROM FlowReading fr
        LEFT JOIN FETCH fr.pipeline
        LEFT JOIN FETCH fr.validationStatus
        LEFT JOIN FETCH fr.readingSlot
        LEFT JOIN FETCH fr.recordedBy
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
     * Used to prevent concurrent modifications during state changes
     * 
     * PERFORMANCE: Added JOIN FETCH to prevent N+1 queries during validation
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT fr FROM FlowReading fr
        LEFT JOIN FETCH fr.pipeline
        LEFT JOIN FETCH fr.validationStatus
        LEFT JOIN FETCH fr.recordedBy
        LEFT JOIN FETCH fr.validatedBy
        WHERE fr.id = :id
    """)
    Optional<FlowReading> findByIdForUpdate(@Param("id") Long id);
    
    /**
     * Find existing reading by pipeline, date, slot (NO LOCK)
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
     * FIX #1: Find existing reading WITH PESSIMISTIC LOCK
     * Prevents race condition during concurrent submissions
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT fr FROM FlowReading fr
        WHERE fr.pipeline.id = :pipelineId
            AND fr.readingDate = :readingDate
            AND fr.readingSlot.id = :slotId
    """)
    Optional<FlowReading> findByPipelineAndDateAndSlotForUpdate(
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

    // ========== OPERATIONAL MONITORING QUERIES ==========

    /**
     * Find readings by structure and validation status (paginated)
     * Used for pending validations and other status-based queries
     */
    @Query("""
        SELECT fr FROM FlowReading fr
        WHERE fr.pipeline.manager.id = :structureId
            AND fr.validationStatus.id = :statusId
        ORDER BY fr.recordedAt ASC
    """)
    Page<FlowReading> findByStructureAndValidationStatus(
        @Param("structureId") Long structureId,
        @Param("statusId") Long statusId,
        Pageable pageable
    );

    /**
     * Find overdue readings by structure
     * Returns readings past their slot deadline that are not yet validated
     */
    @Query("""
        SELECT fr FROM FlowReading fr
        JOIN fr.readingSlot slot
        WHERE fr.pipeline.manager.id = :structureId
            AND fr.readingDate <= :asOfDate
            AND fr.validationStatus.code NOT IN ('APPROVED', 'VALIDATED')
            AND FUNCTION('TIMESTAMP', fr.readingDate, slot.endTime) < :currentDateTime
        ORDER BY fr.readingDate DESC, slot.endTime DESC
    """)
    Page<FlowReading> findOverdueReadingsByStructure(
        @Param("structureId") Long structureId,
        @Param("asOfDate") LocalDate asOfDate,
        @Param("currentDateTime") LocalDateTime currentDateTime,
        Pageable pageable
    );

    // ========== NOTE: ANALYTICS QUERIES MOVED ==========
    
    /**
     * The following complex analytics queries have been moved to IntelligenceQueryRepository
     * as part of Phase 1 refactoring (intelligence module cleanup):
     * 
     * - getDailyCompletionStatistics()         → IntelligenceQueryRepository
     * - getValidatorWorkloadDistribution()     → IntelligenceQueryRepository
     * - getSubmissionTrends()                  → IntelligenceQueryRepository
     * - getPipelineCoverageByDateRange()       → IntelligenceQueryRepository
     * 
     * Rationale: These queries are ONLY used by intelligence module (FlowMonitoringService)
     * and contain complex native SQL aggregations better suited for a dedicated repository.
     * 
     * This separation:
     * - Reduces this repository from 350 LOC to ~180 LOC
     * - Clarifies module boundaries (core CRUD vs intelligence analytics)
     * - Allows independent optimization of analytics queries
     * - Improves maintainability
     */
}
