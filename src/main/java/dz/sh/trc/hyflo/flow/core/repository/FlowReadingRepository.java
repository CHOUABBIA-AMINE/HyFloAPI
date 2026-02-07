/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-07-2026 - Added 6 operational monitoring queries
 * 	@UpdatedOn	: 02-07-2026 - Added intelligence service queries
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
    
    // ========== INTELLIGENCE SERVICE QUERIES ==========
    
    /**
     * Find latest reading for a specific pipeline
     * Used for current measurement display in overview
     */
    Optional<FlowReading> findTopByPipelineIdOrderByRecordedAtDesc(Long pipelineId);
    
    /**
     * Find all readings for a specific pipeline and date
     * Used for slot coverage calculation
     */
    List<FlowReading> findByPipelineIdAndReadingDate(Long pipelineId, LocalDate readingDate);
    
    /**
     * Find readings within date range ordered chronologically
     * Used for time-series analysis
     */
    List<FlowReading> findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc(
        Long pipelineId, LocalDate startDate, LocalDate endDate);

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
     * Used to prevent concurrent modifications during state changes
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT fr FROM FlowReading fr WHERE fr.id = :id")
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

    /**
     * Get daily completion statistics
     * Aggregates completion metrics by date for a structure
     */
    @Query(value = """
        SELECT 
            fr.reading_date as date,
            COUNT(DISTINCT p.id) as totalPipelines,
            COUNT(DISTINCT CASE WHEN fr.id IS NOT NULL THEN fr.id END) as recordedCount,
            COUNT(DISTINCT CASE WHEN vs.code = 'SUBMITTED' THEN fr.id END) as submittedCount,
            COUNT(DISTINCT CASE WHEN vs.code IN ('APPROVED', 'VALIDATED') THEN fr.id END) as approvedCount,
            COUNT(DISTINCT CASE WHEN vs.code = 'REJECTED' THEN fr.id END) as rejectedCount,
            ROUND(COUNT(DISTINCT CASE WHEN fr.id IS NOT NULL THEN fr.id END) * 100.0 / 
                  NULLIF(COUNT(DISTINCT p.id), 0), 2) as recordingCompletionPercentage,
            ROUND(COUNT(DISTINCT CASE WHEN vs.code IN ('APPROVED', 'VALIDATED', 'REJECTED') THEN fr.id END) * 100.0 / 
                  NULLIF(COUNT(DISTINCT p.id), 0), 2) as validationCompletionPercentage
        FROM pipeline p
        LEFT JOIN flow_reading fr ON fr.pipeline_id = p.id
            AND fr.reading_date BETWEEN :startDate AND :endDate
        LEFT JOIN validation_status vs ON fr.validation_status_id = vs.id
        WHERE p.manager_id = :structureId
        GROUP BY fr.reading_date
        ORDER BY fr.reading_date DESC
    """, nativeQuery = true)
    List<Object[]> getDailyCompletionStatistics(
        @Param("structureId") Long structureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    /**
     * Get validator workload distribution
     * Shows validation counts and approval rates by validator
     */
    @Query(value = """
        SELECT 
            e.id as validatorId,
            CONCAT(e.first_name_lt, ' ', e.last_name_lt) as validatorName,
            COUNT(CASE WHEN vs.code IN ('APPROVED', 'VALIDATED') THEN 1 END) as approvedCount,
            COUNT(CASE WHEN vs.code = 'REJECTED' THEN 1 END) as rejectedCount,
            COUNT(*) as totalValidations,
            ROUND(COUNT(CASE WHEN vs.code IN ('APPROVED', 'VALIDATED') THEN 1 END) * 100.0 / 
                  NULLIF(COUNT(*), 0), 2) as approvalRate
        FROM flow_reading fr
        JOIN validation_status vs ON fr.validation_status_id = vs.id
        JOIN employee e ON fr.validated_by_id = e.id
        JOIN pipeline p ON fr.pipeline_id = p.id
        WHERE p.manager_id = :structureId
            AND fr.reading_date BETWEEN :startDate AND :endDate
            AND vs.code IN ('APPROVED', 'VALIDATED', 'REJECTED')
        GROUP BY e.id, e.first_name_lt, e.last_name_lt
        ORDER BY totalValidations DESC
    """, nativeQuery = true)
    List<Object[]> getValidatorWorkloadDistribution(
        @Param("structureId") Long structureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    /**
     * Get submission trends
     * Time-series data showing submission patterns grouped by time interval
     */
    @Query(value = """
        SELECT 
            CASE 
                WHEN :groupBy = 'HOUR' THEN DATE_FORMAT(fr.recorded_at, '%Y-%m-%d %H:00:00')
                WHEN :groupBy = 'DAY' THEN DATE_FORMAT(fr.recorded_at, '%Y-%m-%d')
                WHEN :groupBy = 'WEEK' THEN DATE_FORMAT(fr.recorded_at, '%Y-%u')
                WHEN :groupBy = 'MONTH' THEN DATE_FORMAT(fr.recorded_at, '%Y-%m')
                ELSE DATE_FORMAT(fr.recorded_at, '%Y-%m-%d')
            END as period,
            COUNT(*) as submissionCount,
            COUNT(DISTINCT fr.pipeline_id) as uniquePipelines,
            ROUND(COUNT(*) * 1.0 / NULLIF(COUNT(DISTINCT fr.pipeline_id), 0), 2) as averageSubmissionsPerPipeline
        FROM flow_reading fr
        JOIN pipeline p ON fr.pipeline_id = p.id
        WHERE p.manager_id = :structureId
            AND fr.reading_date BETWEEN :startDate AND :endDate
        GROUP BY period
        ORDER BY period ASC
    """, nativeQuery = true)
    List<Object[]> getSubmissionTrends(
        @Param("structureId") Long structureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("groupBy") String groupBy
    );

    /**
     * Get pipeline coverage by date range
     * Shows which pipelines consistently submit readings vs those with gaps
     */
    @Query(value = """
        WITH date_range AS (
            SELECT DISTINCT reading_date
            FROM flow_reading
            WHERE reading_date BETWEEN :startDate AND :endDate
        ),
        pipeline_list AS (
            SELECT id, code, name
            FROM pipeline
            WHERE manager_id = :structureId
        )
        SELECT 
            pl.id as pipelineId,
            pl.code as pipelineCode,
            pl.name as pipelineName,
            COUNT(DISTINCT dr.reading_date) as expectedReadings,
            COUNT(DISTINCT fr.reading_date) as actualReadings,
            ROUND(COUNT(DISTINCT fr.reading_date) * 100.0 / 
                  NULLIF(COUNT(DISTINCT dr.reading_date), 0), 2) as coveragePercentage,
            GROUP_CONCAT(DISTINCT 
                CASE WHEN fr.id IS NULL THEN dr.reading_date END 
                ORDER BY dr.reading_date SEPARATOR ', ') as missingDates
        FROM pipeline_list pl
        CROSS JOIN date_range dr
        LEFT JOIN flow_reading fr 
            ON fr.pipeline_id = pl.id 
           AND fr.reading_date = dr.reading_date
        GROUP BY pl.id, pl.code, pl.name
        ORDER BY coveragePercentage ASC, pl.code ASC
    """, nativeQuery = true)
    List<Object[]> getPipelineCoverageByDateRange(
        @Param("structureId") Long structureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
