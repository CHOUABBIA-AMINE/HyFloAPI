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
 * 	@UpdatedOn	: 03-28-2026 - fix: IFlowReadingFacade (wrong package facade.impl) →
 * 	                            FlowReadingFacade (correct package facade).
 * 	                            The interface lives in facade/, the impl in facade/impl/.
 * 	                            Spring injects the impl bean via the interface type.
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
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
import dz.sh.trc.hyflo.flow.intelligence.facade.FlowReadingFacade;
import dz.sh.trc.hyflo.flow.intelligence.repository.IntelligenceQueryRepository;
import dz.sh.trc.hyflo.flow.intelligence.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowMonitoringService {

    // ========== DEPENDENCIES ==========

    /**
     * FIX: was imported as dz.sh.trc.hyflo.flow.intelligence.facade.impl.IFlowReadingFacade
     * (wrong package, non-existent type).
     * Correct type: FlowReadingFacade — interface in facade/, impl in facade/impl/.
     * Spring resolves FlowReadingFacadeImpl via this interface.
     */
    private final FlowReadingFacade flowReadingFacade;

    private final IntelligenceQueryRepository intelligenceQueryRepository;

    // ========== PENDING VALIDATIONS ==========

    public Page<FlowReadingReadDTO> findPendingValidations(Long structureId, Pageable pageable) {
        log.debug("Finding pending validations for structure ID {}", structureId);
        return flowReadingFacade.findPendingValidationsByStructure(structureId, pageable);
    }

    public long countPendingValidations(Long structureId) {
        log.debug("Counting pending validations for structure ID {}", structureId);
        return flowReadingFacade
                .findPendingValidationsByStructure(structureId, Pageable.unpaged())
                .getTotalElements();
    }

    // ========== OVERDUE READINGS ==========

    public Page<FlowReadingReadDTO> findOverdueReadings(
            Long structureId, LocalDate asOfDate, Pageable pageable) {
        log.debug("Finding overdue readings for structure ID {} as of {}", structureId, asOfDate);
        return flowReadingFacade.findOverdueReadingsByStructure(
                structureId, asOfDate, DateTimeUtils.now(), pageable);
    }

    // ========== ANALYTICS ==========

    public List<DailyCompletionStatisticsDTO> getDailyCompletionStatistics(
            Long structureId, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        log.debug("Getting daily completion statistics for structure {} from {} to {}",
                structureId, startDate, endDate);
        List<Object[]> rows = intelligenceQueryRepository
                .getDailyCompletionStatistics(structureId, startDate, endDate);
        return rows.stream()
                .map(r -> DailyCompletionStatisticsDTO.builder()
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

    public List<ValidatorWorkloadDTO> getValidatorWorkload() {
        // TODO: ALIGN CONTROLLER — requires (structureId, startDate, endDate)
        log.warn("getValidatorWorkload() called without structureId/date params — " +
                 "controller must be aligned. Returning empty list.");
        return Collections.emptyList();
    }

    public List<SubmissionTrendDTO> getSubmissionTrend(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        // TODO: ALIGN CONTROLLER — requires (structureId, startDate, endDate, groupBy)
        log.warn("getSubmissionTrend() called without structureId/groupBy params — " +
                 "controller must be aligned. Returning empty list.");
        return Collections.emptyList();
    }

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

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days > 90) {
            throw new IllegalArgumentException(
                    "Date range cannot exceed 90 days. Requested: " + days + " days.");
        }
    }

    private Long toLong(Object value) {
        if (value == null) return 0L;
        if (value instanceof Long l) return l;
        if (value instanceof Number n) return n.longValue();
        return 0L;
    }

    private Double toDouble(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Double d) return d;
        if (value instanceof Number n) return n.doubleValue();
        return 0.0;
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) return null;
        if (value instanceof LocalDate ld) return ld;
        if (value instanceof java.sql.Date sd) return sd.toLocalDate();
        return null;
    }
}
