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
 * 	@UpdatedOn	: 03-26-2026 - F2: Replace FlowReadingDTO (v1) with FlowReadingReadDto (v2)
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
 * 	@Refactoring: F2 - IFlowReadingFacade now returns FlowReadingReadDto (v2).
 * 	              FlowReadingDTO (v1) import removed. All Page<FlowReadingReadDto>
 * 	              and List<FlowReadingReadDto> updated accordingly.
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

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDto;
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
 * - Uses IntelligenceQueryRepository for complex analytics (native SQL)
 * - NO direct access to FlowReadingRepository (core module)
 * - NO entity dependencies (works exclusively with DTOs)
 *
 * F2: IFlowReadingFacade now returns FlowReadingReadDto (v2).
 * All references to FlowReadingDTO (v1) have been removed.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowMonitoringService {

    // ========== DEPENDENCIES ==========

    /**
     * Facade for accessing core flow reading queries.
     * F2: now returns FlowReadingReadDto (v2) — no FlowReadingDTO (v1) in this module.
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
    public Page<FlowReadingReadDto> findPendingValidations(Long structureId, Pageable pageable) {
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
    public Page<FlowReadingReadDto> findOverdueReadings(
            Long structureId, LocalDate asOfDate, Pageable pageable) {
        log.debug("Finding overdue readings for structure ID {} as of {}", structureId, asOfDate);
        return flowReadingFacade.findOverdueReadingsByStructure(
                structureId, asOfDate, DateTimeUtils.currentDateTime(), pageable);
    }

    // ========== ANALYTICS ==========

    /**
     * Get daily completion statistics for a pipeline over a date range.
     * Max range: 90 days.
     *
     * @param pipelineId the pipeline ID
     * @param startDate  range start
     * @param endDate    range end
     * @return list of daily completion stats
     */
    public List<DailyCompletionStatisticsDTO> getDailyCompletionStatistics(
            Long pipelineId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        log.debug("Getting daily completion statistics for pipeline {} from {} to {}",
                pipelineId, startDate, endDate);
        return intelligenceQueryRepository.getDailyCompletionStatistics(
                pipelineId, startDate, endDate);
    }

    /**
     * Get validator workload distribution.
     *
     * @return list of validator workload DTOs
     */
    public List<ValidatorWorkloadDTO> getValidatorWorkload() {
        log.debug("Getting validator workload distribution");
        return intelligenceQueryRepository.getValidatorWorkload();
    }

    /**
     * Get submission trend over a date range.
     * Max range: 90 days.
     *
     * @param startDate range start
     * @param endDate   range end
     * @return list of submission trend DTOs
     */
    public List<SubmissionTrendDTO> getSubmissionTrend(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        log.debug("Getting submission trend from {} to {}", startDate, endDate);
        return intelligenceQueryRepository.getSubmissionTrend(startDate, endDate);
    }

    /**
     * Get pipeline coverage detail for a specific pipeline and date range.
     * Max range: 90 days.
     *
     * @param pipelineId the pipeline ID
     * @param startDate  range start
     * @param endDate    range end
     * @return list of coverage detail DTOs
     */
    public List<PipelineCoverageDetailDTO> getPipelineCoverageDetail(
            Long pipelineId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        log.debug("Getting pipeline coverage detail for pipeline {} from {} to {}",
                pipelineId, startDate, endDate);
        return intelligenceQueryRepository.getPipelineCoverageDetail(
                pipelineId, startDate, endDate);
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
}
