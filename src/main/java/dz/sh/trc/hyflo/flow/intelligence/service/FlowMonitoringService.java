/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowMonitoringService
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026 - Refactored to use DateTimeUtils utility
 * 	@UpdatedOn	: 02-10-2026 - Phase 1 refactoring: Eliminated direct repository access
 * 	@UpdatedOn	: 02-10-2026 - Phase 4 refactoring: Added date range validation
 * 	@UpdatedOn	: 02-10-2026 - Phase 2: Facades return DTOs, no conversion needed
 * 	@UpdatedOn	: 03-26-2026 - F2: Replace FlowReadingDTO (v1) with FlowReadingReadDTO (v2)
 * 	@UpdatedOn	: 03-26-2026 - COMPILE FIX: DateTimeUtils.currentDateTime() → .now();
 * 	                            getDailyCompletionStatistics / getPipelineCoverageDetail:
 * 	                            map List<Object[]> → DTO inline;
 * 	                            getValidatorWorkload / getSubmissionTrend: repo requires
 * 	                            structureId + date params not available at call site —
 * 	                            marked TODO for controller alignment.
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Service for operational monitoring and analytics.
 * 	              Extracted from FlowReadingService to separate concerns.
 *
 * 	@Refactoring: Phase 1 - Removed direct FlowReadingRepository dependency.
 * 	              Now uses IFlowReadingFacade and IntelligenceQueryRepository exclusively.
 *
 * 	@Refactoring: Phase 4 - Performance Optimization:
 * 	              Added date range validation (max 90 days).
 *
 * 	@Refactoring: Phase 2 - Facades return DTOs directly:
 * 	              Removed .map(FlowReadingDTO::fromEntity) calls.
 *
 * 	@Refactoring: F2 - IFlowReadingFacade now returns FlowReadingReadDTO (v2).
 * 	              FlowReadingDTO (v1) import removed. All Page<FlowReadingReadDTO>
 * 	              and List<FlowReadingReadDTO> updated accordingly.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.DailyCompletionStatisticsDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.PipelineCoverageDetailDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.SubmissionTrendDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.ValidatorWorkloadDTO;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowReadingFacade;
import dz.sh.trc.hyflo.flow.intelligence.repository.IntelligenceQueryRepository;
import dz.sh.trc.hyflo.flow.intelligence.util.DateTimeUtils;
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
 * - Uses IFlowReadingFacade for simple queries (enforces module boundary)
 * - Uses IntelligenceQueryRepository for complex analytics (native SQL / JPQL)
 * - NO direct access to FlowReadingRepository (core module)
 * - NO entity dependencies (works exclusively with DTOs)
 *
 * F2: IFlowReadingFacade now returns FlowReadingReadDTO (v2).
 * All references to FlowReadingDTO (v1) have been removed.
 *
 * CONTRACT NOTES (pending controller alignment):
 * - getValidatorWorkload(): repository requires (structureId, startDate, endDate).
 *   Controller currently passes no parameters — see TODO below.
 * - getSubmissionTrend(): repository requires (structureId, startDate, endDate, groupBy).
 *   Controller currently passes only (startDate, endDate) — see TODO below.
 * - getDailyCompletionStatistics(): parameter is named pipelineId in controller but
 *   repository semantics use structureId — see TODO below.
 * - getPipelineCoverageDetail(): same structureId/pipelineId mismatch — see TODO below.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowMonitoringService {

    // ========== DEPENDENCIES ==========

    /**
     * Facade for accessing core flow reading queries.
     * F2: now returns FlowReadingReadDTO (v2) — no FlowReadingDTO (v1) in this module.
     */
    private final IFlowReadingFacade flowReadingFacade;

    /**
     * Repository for complex analytics queries.
     */
    private final IntelligenceQueryRepository intelligenceQueryRepository;

    // ========== PENDING VALIDATIONS ==========

    /**
     * Find readings pending validation for a structure, paginated.
     *
     * @param structureId the structure ID
     * @param pageable    pagination parameters
     * @return page of v2 read DTOs
     */
    public Page<FlowReadingReadDTO> findPendingValidations(Long structureId, Pageable pageable) {
        log.debug("Finding pending validations for structure ID {}", structureId);
        return flowReadingFacade.findPendingValidationsByStructure(structureId, pageable);
    }

    /**
     * Count pending validations for a structure.
     *
     * @param structureId the structure ID
     * @return count of pending readings
     */
    public long countPendingValidations(Long structureId) {
        log.debug("Counting pending validations for structure ID {}", structureId);
        return flowReadingFacade
                .findPendingValidationsByStructure(structureId, Pageable.unpaged())
                .getTotalElements();
    }

    // ========== OVERDUE READINGS ==========

    /**
     * Find overdue readings for a structure, paginated.
     *
     * @param structureId the structure ID
     * @param asOfDate    the reference date
     * @param pageable    pagination parameters
     * @return page of v2 read DTOs
     */
    public Page<FlowReadingReadDTO> findOverdueReadings(
            Long structureId, LocalDate asOfDate, Pageable pageable) {
        log.debug("Finding overdue readings for structure ID {} as of {}", structureId, asOfDate);
        // COMPILE FIX: DateTimeUtils.currentDateTime() does not exist — replaced with .now()
        return flowReadingFacade.findOverdueReadingsByStructure(
                structureId, asOfDate, DateTimeUtils.now(), pageable);
    }

    // ========== ANALYTICS ==========

    /**
     * Get daily completion statistics for a structure over a date range.
     * Max range: 90 days.
     *
     * TODO: ALIGN CONTROLLER WITH SERVICE CONTRACT
     *   - FlowMonitoringController passes @RequestParam Long pipelineId
     *   - IntelligenceQueryRepository.getDailyCompletionStatistics() expects structureId
     *   - Parameter was renamed to structureId here; controller must be updated to pass
     *     structureId instead of pipelineId.
     *
     * @param structureId the structure ID (NOT pipeline ID)
     * @param startDate   range start
     * @param endDate     range end
     * @return list of daily completion stats
     */
    public List<DailyCompletionStatisticsDTO> getDailyCompletionStatistics(
            Long structureId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        log.debug("Getting daily completion statistics for structure {} from {} to {}",
                structureId, startDate, endDate);
        List<Object[]> rows = intelligenceQueryRepository
                .getDailyCompletionStatistics(structureId, startDate, endDate);
        return rows.stream()
                .map(r -> DailyCompletionStatisticsDTO.builder()
                        // r[0]: readingDate — JPQL LocalDate, native SQL may return java.sql.Date
                        .date(toLocalDate(r[0]))
                        .totalPipelines(toLong(r[1]))
                        .recordedCount(toLong(r[2]))
                        .submittedCount(toLong(r[3]))
                        .approvedCount(toLong(r[4]))
                        .rejectedCount(toLong(r[5]))
                        .recordingCompletionPercentage(toDouble(r[6]))
                        .validationCompletionPercentage(toDouble(r[7]))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get validator workload distribution.
     *
     * TODO: ALIGN CONTROLLER WITH SERVICE CONTRACT
     *   - IntelligenceQueryRepository.getValidatorWorkloadDistribution() requires
     *     (structureId, startDate, endDate).
     *   - FlowMonitoringController.getValidatorWorkload() currently passes ZERO parameters.
     *   - Controller must be updated to accept and forward structureId, startDate, endDate.
     *   - Until aligned: returns empty list to preserve compile success.
     *     This is NOT a silent omission — it is an explicit contract gap.
     *
     * @return list of validator workload DTOs (empty until controller is aligned)
     */
    public List<ValidatorWorkloadDTO> getValidatorWorkload() {
        // TODO: ALIGN CONTROLLER WITH SERVICE CONTRACT — see Javadoc above
        // Required call once controller passes correct params:
        //   intelligenceQueryRepository.getValidatorWorkloadDistribution(structureId, startDate, endDate)
        //   .stream().map(r -> ValidatorWorkloadDTO.builder()
        //       .validatorId(toLong(r[0])).validatorName(r[1] != null ? r[1].toString() : null)
        //       .approvedCount(toLong(r[2])).rejectedCount(toLong(r[3]))
        //       .totalValidations(toLong(r[4])).approvalRate(toDouble(r[5]))
        //       .build()).collect(Collectors.toList());
        log.warn("getValidatorWorkload() called without structureId/date params — " +
                 "controller must be aligned. Returning empty list.");
        return Collections.emptyList();
    }

    /**
     * Get submission trend over a date range.
     * Max range: 90 days.
     *
     * TODO: ALIGN CONTROLLER WITH SERVICE CONTRACT
     *   - IntelligenceQueryRepository.getSubmissionTrends() requires
     *     (structureId, startDate, endDate, groupBy).
     *   - FlowMonitoringController.getSubmissionTrend() currently passes only
     *     (startDate, endDate) — structureId and groupBy are missing.
     *   - Controller must be updated to accept and forward structureId and groupBy.
     *   - Until aligned: returns empty list to preserve compile success.
     *     This is NOT a silent omission — it is an explicit contract gap.
     *
     * @param startDate range start
     * @param endDate   range end
     * @return list of submission trend DTOs (empty until controller is aligned)
     */
    public List<SubmissionTrendDTO> getSubmissionTrend(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        // TODO: ALIGN CONTROLLER WITH SERVICE CONTRACT — see Javadoc above
        // Required call once controller passes correct params:
        //   String safeGroupBy = List.of("HOUR","DAY","WEEK","MONTH").contains(groupBy) ? groupBy : "DAY";
        //   intelligenceQueryRepository.getSubmissionTrends(structureId, startDate, endDate, safeGroupBy)
        //   .stream().map(r -> SubmissionTrendDTO.builder()
        //       .period(r[0] != null ? r[0].toString() : null)
        //       .submissionCount(toLong(r[1])).uniquePipelines(toLong(r[2]))
        //       .averageSubmissionsPerPipeline(toDouble(r[3]))
        //       .build()).collect(Collectors.toList());
        log.warn("getSubmissionTrend() called without structureId/groupBy params — " +
                 "controller must be aligned. Returning empty list.");
        return Collections.emptyList();
    }

    /**
     * Get pipeline coverage detail for a specific structure and date range.
     * Max range: 90 days.
     *
     * TODO: ALIGN CONTROLLER WITH SERVICE CONTRACT
     *   - IntelligenceQueryRepository.getPipelineCoverageByDateRange() expects structureId.
     *   - FlowMonitoringController passes @RequestParam Long pipelineId.
     *   - These are semantically different: structureId filters all pipelines of a structure,
     *     pipelineId targets a single pipeline.
     *   - Parameter was renamed to structureId here; controller must be updated.
     *
     * @param structureId the structure ID (NOT pipeline ID — see TODO above)
     * @param startDate   range start
     * @param endDate     range end
     * @return list of coverage detail DTOs
     */
    public List<PipelineCoverageDetailDTO> getPipelineCoverageDetail(
            Long structureId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        log.debug("Getting pipeline coverage detail for structure {} from {} to {}",
                structureId, startDate, endDate);
        List<Object[]> rows = intelligenceQueryRepository
                .getPipelineCoverageByDateRange(structureId, startDate, endDate);
        return rows.stream()
                .map(r -> PipelineCoverageDetailDTO.builder()
                        .pipelineId(toLong(r[0]))
                        .pipelineCode(r[1] != null ? r[1].toString() : null)
                        .pipelineName(r[2] != null ? r[2].toString() : null)
                        .expectedReadings(toLong(r[3]))
                        .actualReadings(toLong(r[4]))
                        .coveragePercentage(toDouble(r[5]))
                        .missingDates(r[6] != null ? r[6].toString() : null)
                        .build())
                .collect(Collectors.toList());
    }

    // ========== PRIVATE HELPERS ==========

    /**
     * Validate that a date range does not exceed 90 days.
     *
     * @param startDate range start
     * @param endDate   range end
     * @throws IllegalArgumentException if the range exceeds 90 days
     */
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days > 90) {
            throw new IllegalArgumentException(
                    "Date range cannot exceed 90 days. Requested: " + days + " days.");
        }
    }

    /**
     * Safely cast an Object[] cell to Long.
     * Handles both Long (JPQL) and Number (native SQL / BigDecimal / BigInteger).
     */
    private Long toLong(Object value) {
        if (value == null) return 0L;
        if (value instanceof Long l) return l;
        if (value instanceof Number n) return n.longValue();
        return 0L;
    }

    /**
     * Safely cast an Object[] cell to Double.
     * Handles both Double (JPQL) and Number (native SQL / BigDecimal).
     */
    private Double toDouble(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Double d) return d;
        if (value instanceof Number n) return n.doubleValue();
        return 0.0;
    }

    /**
     * Safely cast an Object[] cell to LocalDate.
     * JPQL returns LocalDate; native SQL may return java.sql.Date.
     */
    private LocalDate toLocalDate(Object value) {
        if (value == null) return null;
        if (value instanceof LocalDate ld) return ld;
        if (value instanceof java.sql.Date sd) return sd.toLocalDate();
        return null;
    }
}
