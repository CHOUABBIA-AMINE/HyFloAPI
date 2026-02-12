/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: IntelligenceQueryRepository
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026 - Created during Phase 1 refactoring
 * 	@UpdatedOn	: 02-10-2026 - Phase 4: Converted native SQL to JPQL where possible
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Repository for complex analytics and monitoring queries.
 * 	              Extracted from FlowReadingRepository to separate intelligence
 * 	              concerns from core CRUD operations.
 *
 * 	@Rationale: These queries are ONLY used by intelligence module services
 * 	            (FlowMonitoringService). Moving them here:
 * 	            - Reduces core repository bloat (300 LOC → 150 LOC)
 * 	            - Improves module boundary clarity
 * 	            - Allows independent query optimization
 * 	            - Separates analytics concerns from core persistence
 * 	
 * 	@Refactoring: Phase 4 - Query Optimization:
 * 	              - Converted daily completion statistics to JPQL (database-portable)
 * 	              - Converted validator workload to JPQL (database-portable)
 * 	              - Kept submission trends as native SQL (requires DATE_FORMAT)
 * 	              - Kept pipeline coverage as native SQL (requires CTE + CROSS JOIN)
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowReading;

/**
 * Repository for intelligence-specific analytics queries.
 * 
 * Contains complex aggregations for analytics/monitoring use cases.
 * Queries are mixed: some use JPQL (database-portable), some use native SQL
 * (for advanced features like CTEs, DATE_FORMAT).
 * 
 * All queries are read-only and focused on analytics/monitoring use cases.
 */
@Repository
public interface IntelligenceQueryRepository extends JpaRepository<FlowReading, Long> {

    // ========== DAILY COMPLETION STATISTICS (JPQL) ==========

    /**
     * Get daily completion statistics
     * 
     * Aggregates completion metrics by date for a structure, showing:
     * - Total pipelines managed by structure
     * - Recording completion (how many pipelines have readings)
     * - Validation completion (how many readings are validated)
     * 
     * REFACTORED: Converted from native SQL to JPQL for database portability.
     * 
     * Used by: FlowMonitoringService.getDailyCompletionStatistics()
     * 
     * @param structureId Structure (organization unit) ID
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return Array of objects: [date, totalPipelines, recordedCount, submittedCount, 
     *                            approvedCount, rejectedCount, recordingCompletionPct, 
     *                            validationCompletionPct]
     */
    @Query("""
        SELECT 
            fr.readingDate as date,
            COUNT(DISTINCT p.id) as totalPipelines,
            COUNT(DISTINCT CASE WHEN fr.id IS NOT NULL THEN fr.id ELSE NULL END) as recordedCount,
            COUNT(DISTINCT CASE WHEN vs.code = 'SUBMITTED' THEN fr.id ELSE NULL END) as submittedCount,
            COUNT(DISTINCT CASE WHEN vs.code IN ('APPROVED', 'VALIDATED') THEN fr.id ELSE NULL END) as approvedCount,
            COUNT(DISTINCT CASE WHEN vs.code = 'REJECTED' THEN fr.id ELSE NULL END) as rejectedCount,
            ROUND(COUNT(DISTINCT CASE WHEN fr.id IS NOT NULL THEN fr.id ELSE NULL END) * 100.0 / 
                  NULLIF(COUNT(DISTINCT p.id), 0), 2) as recordingCompletionPercentage,
            ROUND(COUNT(DISTINCT CASE WHEN vs.code IN ('APPROVED', 'VALIDATED', 'REJECTED') THEN fr.id ELSE NULL END) * 100.0 / 
                  NULLIF(COUNT(DISTINCT p.id), 0), 2) as validationCompletionPercentage
        FROM Pipeline p
        LEFT JOIN FlowReading fr 
            ON fr.pipeline = p
           AND fr.readingDate BETWEEN :startDate AND :endDate
        LEFT JOIN ValidationStatus vs ON fr.validationStatus = vs
        WHERE p.manager.id = :structureId
        GROUP BY fr.readingDate
        ORDER BY fr.readingDate DESC
    """)
    List<Object[]> getDailyCompletionStatistics(
        @Param("structureId") Long structureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    // ========== VALIDATOR WORKLOAD DISTRIBUTION (JPQL) ==========

    /**
     * Get validator workload distribution
     * 
     * Shows validation activity by validator employee, including:
     * - Total validations performed
     * - Approved vs rejected counts
     * - Approval rate percentage
     * 
     * REFACTORED: Converted from native SQL to JPQL for database portability.
     * Now uses Employee.getFullNameLt() helper method instead of CONCAT.
     * 
     * Used by: FlowMonitoringService.getValidatorWorkloadDistribution()
     * 
     * Performance Note: Joins employee, validation_status, pipeline entities.
     *                   Consider adding index on (validated_by_id, reading_date)
     *                   if this query becomes slow.
     * 
     * @param structureId Structure ID to filter pipelines
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return Array of objects: [validatorId, validatorName, approvedCount, 
     *                            rejectedCount, totalValidations, approvalRate]
     */
    @Query("""
        SELECT 
            e.id as validatorId,
            CONCAT(e.firstNameLt, ' ', e.lastNameLt) as validatorName,
            COUNT(CASE WHEN vs.code IN ('APPROVED', 'VALIDATED') THEN 1 ELSE NULL END) as approvedCount,
            COUNT(CASE WHEN vs.code = 'REJECTED' THEN 1 ELSE NULL END) as rejectedCount,
            COUNT(fr) as totalValidations,
            ROUND(COUNT(CASE WHEN vs.code IN ('APPROVED', 'VALIDATED') THEN 1 ELSE NULL END) * 100.0 / 
                  NULLIF(COUNT(fr), 0), 2) as approvalRate
        FROM FlowReading fr
        JOIN fr.validationStatus vs
        JOIN fr.validatedBy e
        JOIN fr.pipeline p
        WHERE p.manager.id = :structureId
            AND fr.readingDate BETWEEN :startDate AND :endDate
            AND vs.code IN ('APPROVED', 'VALIDATED', 'REJECTED')
        GROUP BY e.id, e.firstNameLt, e.lastNameLt
        ORDER BY totalValidations DESC
    """)
    List<Object[]> getValidatorWorkloadDistribution(
        @Param("structureId") Long structureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    // ========== SUBMISSION TRENDS (Native SQL - Database-Specific) ==========

    /**
     * Get submission trends
     * 
     * Time-series data showing reading submission patterns grouped by time interval.
     * Useful for identifying peak submission times and usage patterns.
     * 
     * KEPT AS NATIVE SQL: Uses DATE_FORMAT (MySQL-specific) for date grouping.
     * Cannot be easily converted to JPQL without using database-specific FUNCTION().
     * 
     * Used by: FlowMonitoringService.getSubmissionTrends()
     * 
     * Security Note: groupBy parameter MUST be validated in service layer
     *                to prevent SQL injection (whitelist: HOUR, DAY, WEEK, MONTH)
     * 
     * @param structureId Structure ID to filter pipelines
     * @param startDate Start of date range
     * @param endDate End of date range
     * @param groupBy Time grouping: 'HOUR', 'DAY', 'WEEK', or 'MONTH'
     * @return Array of objects: [period, submissionCount, uniquePipelines, 
     *                            averageSubmissionsPerPipeline]
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

    // ========== PIPELINE COVERAGE (Native SQL - Advanced Features) ==========

    /**
     * Get pipeline coverage by date range
     * 
     * Shows which pipelines consistently submit readings vs those with gaps.
     * Uses CTE (Common Table Expression) to generate expected readings count
     * and compares with actual readings.
     * 
     * KEPT AS NATIVE SQL: Uses CTE (WITH clause) and CROSS JOIN which are not
     * well-supported in JPQL. Also uses GROUP_CONCAT (MySQL-specific).
     * 
     * Used by: FlowMonitoringService.getPipelineCoverageByDateRange()
     * 
     * Performance Note: CTE with CROSS JOIN can be expensive for large date ranges.
     *                   Date range validation (max 90 days) is enforced in service layer.
     * 
     * @param structureId Structure ID to filter pipelines
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return Array of objects: [pipelineId, pipelineCode, pipelineName, 
     *                            expectedReadings, actualReadings, 
     *                            coveragePercentage, missingDates]
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
