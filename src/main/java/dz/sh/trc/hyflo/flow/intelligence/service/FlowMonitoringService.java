/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: FlowMonitoringService
 * 	@CreatedOn	: 02-10-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Service for operational monitoring and analytics.
 * 	             Extracted from FlowReadingService to separate concerns.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.entity.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.DailyCompletionStatisticsDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.PipelineCoverageDetailDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.SubmissionTrendDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.ValidatorWorkloadDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service providing operational monitoring and analytics for flow readings.
 * 
 * This service handles:
 * - Pending validations tracking
 * - Overdue readings detection
 * - Daily completion statistics
 * - Validator workload distribution
 * - Submission trend analysis
 * - Pipeline coverage reporting
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowMonitoringService {

    private final FlowReadingRepository flowReadingRepository;
    private final ValidationStatusRepository validationStatusRepository;

    /**
     * Find pending validations by structure
     * Returns readings in SUBMITTED status awaiting validation
     * 
     * @param structureId Structure ID to filter by
     * @param pageable Pagination parameters
     * @return Paginated list of readings awaiting validation
     */
    public Page<FlowReadingDTO> findPendingValidationsByStructure(
            Long structureId, Pageable pageable) {
        log.debug("Finding pending validations for structure: {}", structureId);
        
        ValidationStatus submittedStatus = validationStatusRepository.findByCode("SUBMITTED")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SUBMITTED status not found in database"));
        
        return flowReadingRepository.findByStructureAndValidationStatus(
                structureId, submittedStatus.getId(), pageable)
                .map(FlowReadingDTO::fromEntity);
    }

    /**
     * Find overdue readings by structure
     * Returns readings past their slot deadline and not yet validated
     * 
     * @param structureId Structure ID to filter by
     * @param asOfDate Date to check for overdue readings
     * @param pageable Pagination parameters
     * @return Paginated list of overdue readings
     */
    public Page<FlowReadingDTO> findOverdueReadingsByStructure(
            Long structureId, LocalDate asOfDate, Pageable pageable) {
        log.debug("Finding overdue readings for structure: {} as of date: {}", 
                  structureId, asOfDate);
        
        return flowReadingRepository.findOverdueReadingsByStructure(
                structureId, asOfDate, java.time.LocalDateTime.now(), pageable)
                .map(FlowReadingDTO::fromEntity);
    }

    /**
     * Get daily completion statistics
     * Returns aggregated statistics for reading completion by date
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
        
        List<Object[]> results = flowReadingRepository.getDailyCompletionStatistics(
                structureId, startDate, endDate);
        
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
     * Returns workload showing number of validations by validator
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of validators with their validation counts
     */
    public List<ValidatorWorkloadDTO> getValidatorWorkloadDistribution(
            Long structureId, LocalDate startDate, LocalDate endDate) {
        log.debug("Calculating validator workload for structure: {} from {} to {}", 
                  structureId, startDate, endDate);
        
        List<Object[]> results = flowReadingRepository.getValidatorWorkloadDistribution(
                structureId, startDate, endDate);
        
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
     * Returns time-series data showing submission patterns over time
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @param groupBy Grouping interval: HOUR, DAY, WEEK, MONTH
     * @return List of submission counts grouped by time interval
     */
    public List<SubmissionTrendDTO> getSubmissionTrends(
            Long structureId, LocalDate startDate, LocalDate endDate, String groupBy) {
        log.debug("Calculating submission trends for structure: {} from {} to {} grouped by {}", 
                  structureId, startDate, endDate, groupBy);
        
        List<Object[]> results = flowReadingRepository.getSubmissionTrends(
                structureId, startDate, endDate, groupBy);
        
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
     * Returns coverage percentage for each pipeline over a date range
     * 
     * @param structureId Structure ID to filter by
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of pipelines with their coverage percentages
     */
    public List<PipelineCoverageDetailDTO> getPipelineCoverageByDateRange(
            Long structureId, LocalDate startDate, LocalDate endDate) {
        log.debug("Calculating pipeline coverage for structure: {} from {} to {}", 
                  structureId, startDate, endDate);
        
        List<Object[]> results = flowReadingRepository.getPipelineCoverageByDateRange(
                structureId, startDate, endDate);
        
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
