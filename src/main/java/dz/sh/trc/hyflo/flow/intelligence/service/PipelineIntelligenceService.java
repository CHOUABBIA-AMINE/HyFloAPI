/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineIntelligenceService
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 02-10-2026 - Refactored to use SlotStatisticsCalculator utility
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.common.util.SlotStatisticsCalculator;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineAssetDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineOverviewDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.ReadingsTimeSeriesDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.SlotStatusDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.StatisticalSummaryDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.TimeSeriesDataPointDTO;
import dz.sh.trc.hyflo.flow.intelligence.facade.FlowReadingFacade;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service providing operational intelligence and analytics for pipelines
 * Focuses on slot-based monitoring, KPI aggregation, and trend analysis
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceService {
    
    private final PipelineRepository pipelineRepository;
    private final FlowReadingFacade flowReadingFacade;
    
    /**
     * Get comprehensive overview with asset specs and operational KPIs
     */
    public PipelineOverviewDTO getOverview(Long pipelineId, LocalDate referenceDate) {
        log.debug("Getting overview for pipeline {} on {}", pipelineId, referenceDate);
        
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        // Build asset DTO
        PipelineAssetDTO asset = buildAssetDTO(pipeline);
        
        // Get today's slot coverage stats using utility
        var allSlots = flowReadingFacade.findAllSlotsOrdered();
        var readings = flowReadingFacade.findByPipelineAndDate(pipelineId, referenceDate);
        SlotStatisticsCalculator.SlotStatistics slotStats = 
            SlotStatisticsCalculator.calculateForDate(allSlots, readings, referenceDate, LocalDateTime.now());
        
        // Get latest reading for current measurements
        var latestReading = flowReadingFacade.findLatestByPipeline(pipelineId);
        
        // Calculate weekly completion rate using utility
        LocalDate weekStart = referenceDate.minusDays(6);
        Double weeklyCompletionRate = calculateWeeklyCompletionRate(pipelineId, weekStart, referenceDate);
        
        return PipelineOverviewDTO.builder()
            .asset(asset)
            .operationalStatus("ACTIVE") // TODO: Implement actual status tracking
            .currentPressure(latestReading.map(FlowReading::getPressure).orElse(null))
            .currentTemperature(latestReading.map(FlowReading::getTemperature).orElse(null))
            .currentFlowRate(latestReading.map(FlowReading::getFlowRate).orElse(null))
            .lastReadingTime(latestReading.map(FlowReading::getRecordedAt).orElse(null))
            .totalSlotsToday(12)
            .recordedSlots(slotStats.recordedCount())
            .approvedSlots(slotStats.approvedCount())
            .pendingValidationSlots(slotStats.submittedCount())
            .overdueSlots(slotStats.overdueCount())
            .completionRate(slotStats.completionRate())
            .weeklyCompletionRate(weeklyCompletionRate)
            .activeAlertsCount(0) // TODO: Implement alert counting
            .volumeTransportedToday(BigDecimal.ZERO) // TODO: Implement volume calculation
            .volumeTransportedWeek(BigDecimal.ZERO)
            .build();
    }
    
    /**
     * Get slot coverage for specific date (12 slots)
     */
    public List<SlotStatusDTO> getSlotCoverage(Long pipelineId, LocalDate date) {
        log.debug("Getting slot coverage for pipeline {} on {}", pipelineId, date);
        
        // Verify pipeline exists
        if (!pipelineRepository.existsById(pipelineId)) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }
        
        // Get all 12 slots
        var allSlots = flowReadingFacade.findAllSlotsOrdered();
        
        // Get readings for this date
        var readings = flowReadingFacade.findByPipelineAndDate(pipelineId, date);
        var readingsBySlot = readings.stream()
            .collect(Collectors.toMap(
                reading -> reading.getReadingSlot().getId(),
                reading -> reading
            ));
        
        LocalDateTime now = LocalDateTime.now();
        
        return allSlots.stream().map(slot -> {
            FlowReading reading = readingsBySlot.get(slot.getId());
            
            // Use utility to check if overdue
            boolean isOverdue = false;
            if (reading == null) {
                isOverdue = SlotStatisticsCalculator.isSlotOverdue(slot, date, now);
            } else if (reading.getValidationStatus() != null && 
                       !"APPROVED".equals(reading.getValidationStatus().getCode())) {
                isOverdue = SlotStatisticsCalculator.isReadingOverdue(reading, now);
            }
            
            return SlotStatusDTO.builder()
                .slotId(slot.getId())
                .slotCode(slot.getCode())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .designation(slot.getDesignationFr()) // TODO: Handle i18n
                .displayOrder(slot.getDisplayOrder())
                .readingId(reading != null ? reading.getId() : null)
                .validationStatus(reading != null && reading.getValidationStatus() != null 
                    ? reading.getValidationStatus().getCode() 
                    : "NOT_RECORDED")
                .recordedAt(reading != null ? reading.getRecordedAt() : null)
                .validatedAt(reading != null ? reading.getValidatedAt() : null)
                .recorderName(reading != null && reading.getRecordedBy() != null 
                    ? getEmployeeFullName(reading.getRecordedBy())
                    : null)
                .validatorName(reading != null && reading.getValidatedBy() != null 
                    ? getEmployeeFullName(reading.getValidatedBy())
                    : null)
                .pressure(reading != null ? reading.getPressure() : null)
                .temperature(reading != null ? reading.getTemperature() : null)
                .flowRate(reading != null ? reading.getFlowRate() : null)
                .containedVolume(reading != null ? reading.getContainedVolume() : null)
                .isOverdue(isOverdue)
                .hasWarnings(reading != null && reading.getNotes() != null && !reading.getNotes().isEmpty())
                .notes(reading != null ? reading.getNotes() : null)
                .build();
        }).collect(Collectors.toList());
    }
    
    /**
     * Get time-series data for readings
     */
    public ReadingsTimeSeriesDTO getReadingsTimeSeries(
            Long pipelineId, 
            LocalDate startDate, 
            LocalDate endDate,
            String measurementType) {
        
        log.debug("Getting time-series for pipeline {} from {} to {}, type: {}",
            pipelineId, startDate, endDate, measurementType);
        
        // Verify pipeline exists
        if (!pipelineRepository.existsById(pipelineId)) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }
        
        // Get readings in date range
        var readings = flowReadingFacade.findByPipelineAndDateRangeOrdered(
            pipelineId, startDate, endDate);
        
        List<TimeSeriesDataPointDTO> dataPoints = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();
        
        for (FlowReading reading : readings) {
            BigDecimal value = extractMeasurementValue(reading, measurementType);
            if (value != null) {
                dataPoints.add(TimeSeriesDataPointDTO.builder()
                    .timestamp(reading.getRecordedAt())
                    .value(value)
                    .slotCode(reading.getReadingSlot().getCode())
                    .validationStatus(reading.getValidationStatus() != null 
                        ? reading.getValidationStatus().getCode() 
                        : "DRAFT")
                    .hasWarning(reading.getNotes() != null && !reading.getNotes().isEmpty())
                    .build());
                values.add(value);
            }
        }
        
        StatisticalSummaryDTO statistics = calculateStatistics(values);
        
        return ReadingsTimeSeriesDTO.builder()
            .measurementType(measurementType != null ? measurementType : "PRESSURE")
            .dataPoints(dataPoints)
            .statistics(statistics)
            .build();
    }
    
    // ========== HELPER METHODS ==========
    
    /**
     * Build asset DTO from Pipeline entity
     */
    private PipelineAssetDTO buildAssetDTO(Pipeline pipeline) {
        return PipelineAssetDTO.builder()
            .id(pipeline.getId())
            .code(pipeline.getCode())
            .name(pipeline.getName()) // TODO: Handle i18n
            .length(pipeline.getLength())
            .nominalDiameter(pipeline.getNominalDiameter())
            .nominalThickness(pipeline.getNominalThickness())
            .designMaxServicePressure(pipeline.getDesignMaxServicePressure())
            .operationalMaxServicePressure(pipeline.getOperationalMaxServicePressure())
            .designMinServicePressure(pipeline.getDesignMinServicePressure())
            .operationalMinServicePressure(pipeline.getOperationalMinServicePressure())
            .designCapacity(pipeline.getDesignCapacity())
            .operationalCapacity(pipeline.getOperationalCapacity())
            .departureTerminal(PipelineAssetDTO.TerminalInfoDTO.builder()
                .id(pipeline.getDepartureTerminal().getId())
                .code(pipeline.getDepartureTerminal().getCode())
                .name(pipeline.getDepartureTerminal().getName())
                .type(pipeline.getDepartureTerminal().getTerminalType() != null 
                    ? pipeline.getDepartureTerminal().getTerminalType().getDesignationFr() 
                    : null)
                .build())
            .arrivalTerminal(PipelineAssetDTO.TerminalInfoDTO.builder()
                .id(pipeline.getArrivalTerminal().getId())
                .code(pipeline.getArrivalTerminal().getCode())
                .name(pipeline.getArrivalTerminal().getName())
                .type(pipeline.getArrivalTerminal().getTerminalType() != null 
                    ? pipeline.getArrivalTerminal().getTerminalType().getDesignationFr() 
                    : null)
                .build())
            .constructionMaterial(pipeline.getNominalConstructionMaterial() != null
                ? PipelineAssetDTO.AlloyInfoDTO.builder()
                    .id(pipeline.getNominalConstructionMaterial().getId())
                    .code(pipeline.getNominalConstructionMaterial().getCode())
                    .designation(pipeline.getNominalConstructionMaterial().getDesignationFr())
                    .build()
                : null)
            .pipelineSystem(PipelineAssetDTO.PipelineSystemInfoDTO.builder()
                .id(pipeline.getPipelineSystem().getId())
                .code(pipeline.getPipelineSystem().getCode())
                .designation(pipeline.getPipelineSystem().getName())
                .build())
            .manager(PipelineAssetDTO.StructureInfoDTO.builder()
                .id(pipeline.getManager().getId())
                .code(pipeline.getManager().getCode())
                .designation(pipeline.getManager().getDesignationFr())
                .build())
            .build();
    }
    
    /**
     * Calculate weekly completion rate using SlotStatisticsCalculator utility
     */
    private Double calculateWeeklyCompletionRate(Long pipelineId, LocalDate startDate, LocalDate endDate) {
        // Get all slots and readings for the date range
        var allSlots = flowReadingFacade.findAllSlotsOrdered();
        
        // Group readings by date
        var allReadings = flowReadingFacade.findByPipelineAndDateRangeOrdered(pipelineId, startDate, endDate);
        Map<LocalDate, List<FlowReading>> readingsByDate = allReadings.stream()
            .collect(Collectors.groupingBy(FlowReading::getReadingDate));
        
        // Use utility to calculate average completion rate
        return SlotStatisticsCalculator.calculateAverageCompletionRate(
            allSlots,
            readingsByDate,
            startDate,
            endDate,
            LocalDateTime.now()
        );
    }
    
    /**
     * Extract measurement value based on type
     */
    private BigDecimal extractMeasurementValue(FlowReading reading, String measurementType) {
        if (measurementType == null) {
            return reading.getPressure();
        }
        
        return switch (measurementType.toUpperCase()) {
            case "PRESSURE" -> reading.getPressure();
            case "TEMPERATURE" -> reading.getTemperature();
            case "FLOW_RATE" -> reading.getFlowRate();
            case "CONTAINED_VOLUME" -> reading.getContainedVolume();
            default -> reading.getPressure();
        };
    }
    
    /**
     * Calculate statistical summary from values
     */
    private StatisticalSummaryDTO calculateStatistics(List<BigDecimal> values) {
        if (values.isEmpty()) {
            return StatisticalSummaryDTO.builder()
                .min(BigDecimal.ZERO)
                .max(BigDecimal.ZERO)
                .avg(BigDecimal.ZERO)
                .median(BigDecimal.ZERO)
                .stdDev(BigDecimal.ZERO)
                .build();
        }
        
        BigDecimal min = values.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal max = values.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        
        BigDecimal sum = values.stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avg = sum.divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
        
        // Calculate median
        List<BigDecimal> sorted = values.stream().sorted().collect(Collectors.toList());
        BigDecimal median;
        int size = sorted.size();
        if (size % 2 == 0) {
            median = sorted.get(size / 2 - 1)
                .add(sorted.get(size / 2))
                .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        } else {
            median = sorted.get(size / 2);
        }
        
        // Calculate standard deviation
        BigDecimal variance = values.stream()
            .map(v -> v.subtract(avg).pow(2))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
        
        BigDecimal stdDev = BigDecimal.valueOf(Math.sqrt(variance.doubleValue()))
            .setScale(2, RoundingMode.HALF_UP);
        
        return StatisticalSummaryDTO.builder()
            .min(min)
            .max(max)
            .avg(avg)
            .median(median)
            .stdDev(stdDev)
            .build();
    }
    
    /**
     * Get employee full name (handles potential missing getFullName() method)
     */
    private String getEmployeeFullName(Employee employee) {
        if (employee == null) {
            return null;
        }
        
        // Try to use getFullName() if it exists, otherwise construct manually
        try {
            // If Employee has getFullName() method
            return (String) employee.getClass().getMethod("getFullName").invoke(employee);
        } catch (Exception e) {
            // Fallback: construct from first and last name
            try {
                String firstName = (String) employee.getClass().getMethod("getFirstNameLt").invoke(employee);
                String lastName = (String) employee.getClass().getMethod("getLastNameLt").invoke(employee);
                
                if (firstName != null && lastName != null) {
                    return firstName + " " + lastName;
                } else if (firstName != null) {
                    return firstName;
                } else if (lastName != null) {
                    return lastName;
                }
            } catch (Exception ex) {
                log.warn("Unable to extract employee name", ex);
            }
        }
        
        return "Unknown";
    }
}
