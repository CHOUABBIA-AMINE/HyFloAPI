/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: FlowMonitoringService
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026 - Refactored to use DateTimeUtils utility
 * 	@UpdatedOn	: 02-10-2026 - Phase 1 refactoring: Eliminated direct repository access
 * 	@UpdatedOn	: 02-10-2026 - Phase 4 refactoring: Added date range validation
 * 	@UpdatedOn	: 02-10-2026 - Phase 2: Facades return DTOs, no conversion needed
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
 * 	@Refactoring: Phase 4 - Performance Optimization:
 * 	              - Added date range validation (max 90 days)
 * 	              - Prevents performance issues with large result sets
 * 	              - Validates input parameters before executing expensive queries
 *
 * 	@Refactoring: Phase 2 - Facades now return DTOs directly:
 * 	              - Removed .map(FlowReadingDTO::fromEntity) calls
 * 	              - Facades handle entity→DTO conversion
 * 	              - Service works exclusively with DTOs
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
 * - NO entity dependencies (works exclusively with DTOs)
 * 
 * Performance:
 * - Date range validation enforced (max 90 days for analytics)
 * - JOIN FETCH used in repository queries to prevent N+1 issues
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowMonitoringService {

    // ========== DEPENDENCIES (Phase 1 Refactored) ==========
    
    /**
     * Facade for accessing core flow reading queries
     * REFACTORED (Phase 1): Now using facade instead of direct FlowReadingRepository access
     * REFACTORED (Phase 2): Facade now returns DTOs instead of entities
     */
    private final FlowReadingFacade flowReadingFacade;
    
    /**
     * Repository for complex analytics queries
     * NEW: Dedicated repository for intelligence-specific queries
     */
    private final IntelligenceQueryRepository intelligenceQueryRepository;

    // ========== CONSTANTS ==========
    
    /**
     * Whitelist of valid groupBy parameters for submission trends
     * Prevents SQL injection via CASE statement in native query
     */
    private static final Set<String> VALID_GROUP_BY = Set.of("HOUR", "DAY", "WEEK", "MONTH");
    
    /**
     * Maximum allowed date range for analytics queries (days)
     * Prevents performance issues with large result sets
     */
    private static final long MAX_DATE_RANGE_DAYS = 90;

    // ========== MONITORING QUERY METHODS ==========

    /**
     * Find pending validations by structure
     * 
     * Returns readings in SUBMITTED status awaiting validation.
     * Used for "Pending Validations" dashboard view.
     * 
     * REFACTORED (Phase 1): Now uses facade instead of direct repository access
     * REFACTORED (Phase 2): Facade returns DTOs directly, no conversion needed
     * 
     * @param structureId Structure ID to filter by
     * @param pageable Pagination parameters
     * @return Paginated list of reading DTOs awaiting validation
     */
    public Page<FlowReadingDTO> findPendingValidationsByStructure(
            Long structureId, Pageable pageable) {
        log.debug("Finding pending validations for structure: {}", structureId);
        
        // ✅ REFACTORED (Phase 2): Facade returns Page<FlowReadingDTO> directly
        // No .map() needed - already DTOs
        return flowReadingFacade.findPendingValidationsByStructure(structureId, pageable);
    }

    /**
     * Find overdue readings by structure
     * 
     * Returns readings past their slot deadline and not yet validated.
     * A reading is overdue if: current_time > (reading_date + slot_end_time)
     * 
     * REFACTORED (Phase 1): Now uses facade instead of direct repository access
     * REFACTORED (Phase 2): Facade returns DTOs directly, no conversion needed
     * 
     * @param structureId Structure ID to filter by
     * @param asOfDate Date to check for overdue readings (usually today)
     * @param pageable Pagination parameters
     * @return Paginated list of overdue reading DTOs
     */
    public Page<FlowReadingDTO> findOverdueReadingsByStructure(
            Long structureId, LocalDate asOfDate, Pageable pageable) {
        log.debug("Finding overdue readings for structure: {} as of date: {}", 
                  structureId, asOfDate);
        
        // ✅ REFACTORED (Phase 2): Facade returns Page<FlowReadingDTO> directly
        // No .map() needed - already DTOs
        return flowReadingFacade.findOverdueReadingsByStructure(
                structureId, asOfDate, DateTimeUtils.now(), pageable);
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
     * PERFORMANCE: Added date range validation (max 90 days)
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of daily statistics with completion percentages
     * @throws IllegalArgumentException if date range exceeds 90 days
     */
    public List<DailyCompletionStatisticsDTO> getDailyCompletionStatistics(
            Long structureId, LocalDate startDate, LocalDate endDate) {
        log.debug("Calculating daily completion statistics for structure: {} from {} to {}", 
                  structureId, startDate, endDate);
        
        // ✅ PERFORMANCE: Validate date range
        validateDateRange(startDate, endDate, "getDailyCompletionStatistics");
        
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
     * PERFORMANCE: Added date range validation (max 90 days)
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of validators with their validation metrics
     * @throws IllegalArgumentException if date range exceeds 90 days
     */
    public List<ValidatorWorkloadDTO> getValidatorWorkloadDistribution(
            Long structureId, LocalDate startDate, LocalDate endDate) {
        log.debug("Calculating validator workload for structure: {} from {} to {}", 
                  structureId, startDate, endDate);
        
        // ✅ PERFORMANCE: Validate date range
        validateDateRange(startDate, endDate, "getValidatorWorkloadDistribution");
        
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
     * PERFORMANCE: Added date range validation (max 90 days)
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @param groupBy Grouping interval: HOUR, DAY, WEEK, MONTH (validated)
     * @return List of submission counts grouped by time interval
     * @throws IllegalArgumentException if groupBy is not in whitelist or date range exceeds 90 days
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
        
        // ✅ PERFORMANCE: Validate date range
        validateDateRange(startDate, endDate, "getSubmissionTrends");
        
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
     * PERFORMANCE: Added date range validation (max 90 days)
     * 
     * Performance Note: Uses CTE with CROSS JOIN - date range limit is essential
     *                   for optimal performance.
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of pipelines with coverage metrics and missing dates
     * @throws IllegalArgumentException if date range exceeds 90 days
     */
    public List<PipelineCoverageDetailDTO> getPipelineCoverageByDateRange(
            Long structureId, LocalDate startDate, LocalDate endDate) {
        log.debug("Calculating pipeline coverage for structure: {} from {} to {}", 
                  structureId, startDate, endDate);
        
        // ✅ PERFORMANCE: Validate date range (CRITICAL for CTE query performance)
        validateDateRange(startDate, endDate, "getPipelineCoverageByDateRange");
        
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
    
    // ========== VALIDATION HELPER METHODS ==========
    
    /**
     * Validate date range for analytics queries
     * 
     * Ensures date range does not exceed MAX_DATE_RANGE_DAYS to prevent:
     * - Large result sets that slow down queries
     * - Memory consumption issues
     * - Database performance degradation
     * 
     * @param startDate Start of date range
     * @param endDate End of date range
     * @param methodName Name of calling method (for logging)
     * @throws IllegalArgumentException if date range exceeds limit or is invalid
     */
    private void validateDateRange(LocalDate startDate, LocalDate endDate, String methodName) {
        // Validate dates are not null
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                String.format("%s: Start date and end date are required", methodName)
            );
        }
        
        // Validate start date is before or equal to end date
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                String.format("%s: Start date (%s) must be before or equal to end date (%s)",
                             methodName, startDate, endDate)
            );
        }
        
        // Calculate date range in days
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        
        // Validate against maximum allowed range
        if (daysBetween > MAX_DATE_RANGE_DAYS) {
            log.warn("{}: Date range validation failed - requested {} days, max allowed {}",
                    methodName, daysBetween, MAX_DATE_RANGE_DAYS);
            throw new IllegalArgumentException(
                String.format("%s: Date range cannot exceed %d days (requested: %d days from %s to %s)",
                             methodName, MAX_DATE_RANGE_DAYS, daysBetween, startDate, endDate)
            );
        }
        
        log.debug("{}: Date range validated - {} days (from {} to {})",
                 methodName, daysBetween, startDate, endDate);
    }
}
