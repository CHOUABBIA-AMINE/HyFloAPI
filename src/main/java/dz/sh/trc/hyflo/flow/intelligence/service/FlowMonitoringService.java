/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: FlowMonitoringService
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026 - Refactored to use DateTimeUtils utility
 * 	@UpdatedOn	: 02-10-2026 - Phase 1 refactoring: Eliminated direct repository access
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Service for operational monitoring and analytics.
 * 	              Extracted from FlowReadingService to separate concerns.
 *
 * 	@Refactoring: Phase 1 - Removed direct FlowReadingRepository dependency.
 * 	              Now uses FlowReadingFacade and IntelligenceQueryRepository exclusively.
 * 	              
 * 	              Benefits:
 * 	              - Enforces module boundaries (no direct core repository access)
 * 	              - Improves testability (mock facade instead of repository)
 * 	              - Clear separation: facade for queries, repository for analytics
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.common.util.DateTimeUtils;
import dz.sh.trc.hyflo.flow.core.dto.entity.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.DailyCompletionStatisticsDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.PipelineCoverageDetailDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.SubmissionTrendDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.ValidatorWorkloadDTO;
import dz.sh.trc.hyflo.flow.intelligence.facade.FlowReadingFacade;
import dz.sh.trc.hyflo.flow.intelligence.repository.IntelligenceQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service providing operational monitoring and analytics for flow readings.
 * 
 * This service handles:
 * - Pending validations tracking (via facade)
 * - Overdue readings detection (via facade)
 * - Daily completion statistics (via IntelligenceQueryRepository)
 * - Validator workload distribution (via IntelligenceQueryRepository)
 * - Submission trend analysis (via IntelligenceQueryRepository)
 * - Pipeline coverage reporting (via IntelligenceQueryRepository)
 * 
 * Architecture Pattern:
 * - Uses FlowReadingFacade for simple queries (enforces module boundary)
 * - Uses IntelligenceQueryRepository for complex analytics (native SQL)
 * - NO direct access to FlowReadingRepository (core module)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowMonitoringService {

    // ========== DEPENDENCIES (Phase 1 Refactored) ==========
    
    /**
     * Facade for accessing core flow reading queries
     * REFACTORED: Now using facade instead of direct FlowReadingRepository access
     */
    private final FlowReadingFacade flowReadingFacade;
    
    /**
     * Repository for complex analytics queries
     * NEW: Dedicated repository for intelligence-specific queries
     */
    private final IntelligenceQueryRepository intelligenceQueryRepository;

    // ========== VALID GROUP BY VALUES (Security) ==========
    
    /**
     * Whitelist of valid groupBy parameters for submission trends
     * Prevents SQL injection via CASE statement in native query
     */
    private static final Set<String> VALID_GROUP_BY = Set.of("HOUR", "DAY", "WEEK", "MONTH");

    // ========== MONITORING QUERY METHODS ==========

    /**
     * Find pending validations by structure
     * 
     * Returns readings in SUBMITTED status awaiting validation.
     * Used for "Pending Validations" dashboard view.
     * 
     * REFACTORED: Now uses facade instead of direct repository access
     * 
     * @param structureId Structure ID to filter by
     * @param pageable Pagination parameters
     * @return Paginated list of readings awaiting validation
     */
    public Page<FlowReadingDTO> findPendingValidationsByStructure(
            Long structureId, Pageable pageable) {
        log.debug("Finding pending validations for structure: {}", structureId);
        
        // ✅ REFACTORED: Access via facade (enforces module boundary)
        return flowReadingFacade.findPendingValidationsByStructure(structureId, pageable)
                .map(FlowReadingDTO::fromEntity);
    }

    /**
     * Find overdue readings by structure
     * 
     * Returns readings past their slot deadline and not yet validated.
     * A reading is overdue if: current_time > (reading_date + slot_end_time)
     * 
     * REFACTORED: Now uses facade instead of direct repository access
     * 
     * @param structureId Structure ID to filter by
     * @param asOfDate Date to check for overdue readings (usually today)
     * @param pageable Pagination parameters
     * @return Paginated list of overdue readings
     */
    public Page<FlowReadingDTO> findOverdueReadingsByStructure(
            Long structureId, LocalDate asOfDate, Pageable pageable) {
        log.debug("Finding overdue readings for structure: {} as of date: {}", 
                  structureId, asOfDate);
        
        // ✅ REFACTORED: Access via facade with DateTimeUtils for consistency
        return flowReadingFacade.findOverdueReadingsByStructure(
                structureId, asOfDate, DateTimeUtils.now(), pageable)
                .map(FlowReadingDTO::fromEntity);
    }

    // ========== ANALYTICS METHODS ==========

    /**
     * Get daily completion statistics
     * 
     * Returns aggregated statistics for reading completion by date, including:
     * - Total pipelines in structure
     * - Recording completion percentage
     * - Validation completion percentage
     * 
     * REFACTORED: Now uses IntelligenceQueryRepository instead of FlowReadingRepository
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of daily statistics with completion percentages
     */
    public List<DailyCompletionStatisticsDTO> getDailyCompletionStatistics(
            Long structureId, LocalDate startDate, LocalDate endDate) {
        log.debug("Calculating daily completion statistics for structure: {} from {} to {}", 
                  structureId, startDate, endDate);
        
        // ✅ REFACTORED: Use IntelligenceQueryRepository (dedicated analytics repository)
        List<Object[]> results = intelligenceQueryRepository.getDailyCompletionStatistics(
                structureId, startDate, endDate);
        
        // Map native SQL results to DTO
        return results.stream().map(row -> DailyCompletionStatisticsDTO.builder()
                .date((LocalDate) row[0])
                .totalPipelines((Long) row[1])
                .recordedCount((Long) row[2])
                .submittedCount((Long) row[3])
                .approvedCount((Long) row[4])
                .rejectedCount((Long) row[5])
                .recordingCompletionPercentage((Double) row[6])
                .validationCompletionPercentage((Double) row[7])
                .build()
        ).collect(Collectors.toList());
    }

    /**
     * Get validator workload distribution
     * 
     * Returns workload showing number of validations by validator, including:
     * - Approved vs rejected counts
     * - Approval rate percentage
     * - Total validations performed
     * 
     * REFACTORED: Now uses IntelligenceQueryRepository
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of validators with their validation metrics
     */
    public List<ValidatorWorkloadDTO> getValidatorWorkloadDistribution(
            Long structureId, LocalDate startDate, LocalDate endDate) {
        log.debug("Calculating validator workload for structure: {} from {} to {}", 
                  structureId, startDate, endDate);
        
        // ✅ REFACTORED: Use IntelligenceQueryRepository
        List<Object[]> results = intelligenceQueryRepository.getValidatorWorkloadDistribution(
                structureId, startDate, endDate);
        
        // Map native SQL results to DTO
        return results.stream().map(row -> ValidatorWorkloadDTO.builder()
                .validatorId((Long) row[0])
                .validatorName((String) row[1])
                .approvedCount((Long) row[2])
                .rejectedCount((Long) row[3])
                .totalValidations((Long) row[4])
                .approvalRate((Double) row[5])
                .build()
        ).collect(Collectors.toList());
    }

    /**
     * Get reading submission trends
     * 
     * Returns time-series data showing submission patterns over time.
     * Useful for identifying peak submission times and usage patterns.
     * 
     * REFACTORED: Now uses IntelligenceQueryRepository
     * SECURITY: Validates groupBy parameter against whitelist
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @param groupBy Grouping interval: HOUR, DAY, WEEK, MONTH (validated)
     * @return List of submission counts grouped by time interval
     * @throws IllegalArgumentException if groupBy is not in whitelist
     */
    public List<SubmissionTrendDTO> getSubmissionTrends(
            Long structureId, LocalDate startDate, LocalDate endDate, String groupBy) {
        log.debug("Calculating submission trends for structure: {} from {} to {} grouped by {}", 
                  structureId, startDate, endDate, groupBy);
        
        // ✅ SECURITY: Validate groupBy parameter (prevent SQL injection)
        if (!VALID_GROUP_BY.contains(groupBy.toUpperCase())) {
            throw new IllegalArgumentException(
                String.format("Invalid groupBy parameter: '%s'. Must be one of: %s", 
                             groupBy, VALID_GROUP_BY));
        }
        
        // ✅ REFACTORED: Use IntelligenceQueryRepository
        List<Object[]> results = intelligenceQueryRepository.getSubmissionTrends(
                structureId, startDate, endDate, groupBy.toUpperCase());
        
        // Map native SQL results to DTO
        return results.stream().map(row -> SubmissionTrendDTO.builder()
                .period((String) row[0])
                .submissionCount((Long) row[1])
                .uniquePipelines((Long) row[2])
                .averageSubmissionsPerPipeline((Double) row[3])
                .build()
        ).collect(Collectors.toList());
    }

    /**
     * Get pipeline coverage by date range
     * 
     * Returns coverage percentage for each pipeline over a date range.
     * Shows which pipelines consistently submit readings vs those with gaps.
     * 
     * REFACTORED: Now uses IntelligenceQueryRepository
     * 
     * Performance Note: Uses CTE with CROSS JOIN - consider limiting date range
     *                   to 30-90 days for optimal performance.
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of pipelines with coverage metrics and missing dates
     */
    public List<PipelineCoverageDetailDTO> getPipelineCoverageByDateRange(
            Long structureId, LocalDate startDate, LocalDate endDate) {
        log.debug("Calculating pipeline coverage for structure: {} from {} to {}", 
                  structureId, startDate, endDate);
        
        // ✅ REFACTORED: Use IntelligenceQueryRepository
        List<Object[]> results = intelligenceQueryRepository.getPipelineCoverageByDateRange(
                structureId, startDate, endDate);
        
        // Map native SQL results to DTO
        return results.stream().map(row -> PipelineCoverageDetailDTO.builder()
                .pipelineId((Long) row[0])
                .pipelineCode((String) row[1])
                .pipelineName((String) row[2])
                .expectedReadings((Long) row[3])
                .actualReadings((Long) row[4])
                .coveragePercentage((Double) row[5])
                .missingDates((String) row[6])
                .build()
        ).collect(Collectors.toList());
    }
}
