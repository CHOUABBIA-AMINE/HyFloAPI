/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: IntelligenceQueryRepository
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026 - Created during Phase 1 refactoring
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
 * Contains complex native SQL aggregations that were previously in
 * FlowReadingRepository but are only used by intelligence module.
 * 
 * All queries are read-only and focused on analytics/monitoring use cases.
 */
@Repository
public interface IntelligenceQueryRepository extends JpaRepository<FlowReading, Long> {

    // ========== DAILY COMPLETION STATISTICS ==========

    /**
     * Get daily completion statistics
     * 
     * Aggregates completion metrics by date for a structure, showing:
     * - Total pipelines managed by structure
     * - Recording completion (how many pipelines have readings)
     * - Validation completion (how many readings are validated)
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

    // ========== VALIDATOR WORKLOAD DISTRIBUTION ==========

    /**
     * Get validator workload distribution
     * 
     * Shows validation activity by validator employee, including:
     * - Total validations performed
     * - Approved vs rejected counts
     * - Approval rate percentage
     * 
     * Used by: FlowMonitoringService.getValidatorWorkloadDistribution()
     * 
     * Performance Note: Joins employee, validation_status, pipeline tables.
     *                   Consider adding index on (validated_by_id, reading_date)
     *                   if this query becomes slow.
     * 
     * @param structureId Structure ID to filter pipelines
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return Array of objects: [validatorId, validatorName, approvedCount, 
     *                            rejectedCount, totalValidations, approvalRate]
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

    // ========== SUBMISSION TRENDS ==========

    /**
     * Get submission trends
     * 
     * Time-series data showing reading submission patterns grouped by time interval.
     * Useful for identifying peak submission times and usage patterns.
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

    // ========== PIPELINE COVERAGE ==========

    /**
     * Get pipeline coverage by date range
     * 
     * Shows which pipelines consistently submit readings vs those with gaps.
     * Uses CTE (Common Table Expression) to generate expected readings count
     * and compares with actual readings.
     * 
     * Used by: FlowMonitoringService.getPipelineCoverageByDateRange()
     * 
     * Performance Note: CTE with CROSS JOIN can be expensive for large date ranges.
     *                   Consider limiting to 30-90 day ranges.
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
