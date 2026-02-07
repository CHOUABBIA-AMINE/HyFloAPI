/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineIntelligenceService
 * 	@CreatedOn	: 02-07-2026
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.core.repository.ReadingSlotRepository;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineAssetDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineOverviewDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.ReadingsTimeSeriesDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.SlotStatusDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.StatisticalSummaryDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.TimeSeriesDataPointDTO;
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
    private final FlowReadingRepository flowReadingRepository;
    private final ReadingSlotRepository readingSlotRepository;
    
    /**
     * Get comprehensive overview with asset specs and operational KPIs
     */
    public PipelineOverviewDTO getOverview(Long pipelineId, LocalDate referenceDate) {
        log.debug("Getting overview for pipeline {} on {}", pipelineId, referenceDate);
        
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        // Build asset DTO
        PipelineAssetDTO asset = buildAssetDTO(pipeline);
        
        // Get today's slot coverage stats
        var slotStats = getSlotStatistics(pipelineId, referenceDate);
        
        // Get latest reading for current measurements
        var latestReading = flowReadingRepository.findTopByPipelineIdOrderByRecordedAtDesc(pipelineId);
        
        // Calculate weekly completion rate
        LocalDate weekStart = referenceDate.minusDays(6);
        Double weeklyCompletionRate = calculateWeeklyCompletionRate(pipelineId, weekStart, referenceDate);
        
        return PipelineOverviewDTO.builder()
            .asset(asset)
            .operationalStatus("ACTIVE") // TODO: Implement actual status tracking
            .currentPressure(latestReading.map(r -> r.getPressure()).orElse(null))
            .currentTemperature(latestReading.map(r -> r.getTemperature()).orElse(null))
            .currentFlowRate(latestReading.map(r -> r.getFlowRate()).orElse(null))
            .lastReadingTime(latestReading.map(r -> r.getRecordedAt()).orElse(null))
            .totalSlotsToday(12)
            .recordedSlots(slotStats.recordedCount)
            .approvedSlots(slotStats.approvedCount)
            .pendingValidationSlots(slotStats.submittedCount)
            .overdueSlots(slotStats.overdueCount)
            .completionRate(slotStats.completionRate)
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
        var allSlots = readingSlotRepository.findAllByOrderByDisplayOrder();
        
        // Get readings for this date
        var readings = flowReadingRepository.findByPipelineIdAndReadingDate(pipelineId, date);
        var readingsBySlot = readings.stream()
            .collect(Collectors.toMap(
                reading -> reading.getReadingSlot().getId(),
                reading -> reading
            ));
        
        LocalDateTime now = LocalDateTime.now();
        
        return allSlots.stream().map(slot -> {
            var reading = readingsBySlot.get(slot.getId());
            
            boolean isOverdue = false;
            if (reading == null || 
                (reading.getValidationStatus() != null && 
                 !"APPROVED".equals(reading.getValidationStatus().getCode()))) {
                LocalDateTime slotDeadline = LocalDateTime.of(date, slot.getEndTime());
                isOverdue = now.isAfter(slotDeadline);
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
                    ? reading.getRecordedBy().getFullName() 
                    : null)
                .validatorName(reading != null && reading.getValidatedBy() != null 
                    ? reading.getValidatedBy().getFullName() 
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
        var readings = flowReadingRepository.findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc(
            pipelineId, startDate, endDate);
        
        List<TimeSeriesDataPointDTO> dataPoints = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();
        
        for (var reading : readings) {
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
    
    private PipelineAssetDTO buildAssetDTO(Pipeline pipeline) {
        return PipelineAssetDTO.builder()
            .id(pipeline.getId())
            .code(pipeline.getCode())
            .name(pipeline.getDesignationFr()) // TODO: Handle i18n
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
                .name(pipeline.getDepartureTerminal().getDesignationFr())
                .type(pipeline.getDepartureTerminal().getTerminalType() != null 
                    ? pipeline.getDepartureTerminal().getTerminalType().getDesignationFr() 
                    : null)
                .build())
            .arrivalTerminal(PipelineAssetDTO.TerminalInfoDTO.builder()
                .id(pipeline.getArrivalTerminal().getId())
                .code(pipeline.getArrivalTerminal().getCode())
                .name(pipeline.getArrivalTerminal().getDesignationFr())
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
                .designation(pipeline.getPipelineSystem().getDesignationFr())
                .build())
            .manager(PipelineAssetDTO.StructureInfoDTO.builder()
                .id(pipeline.getManager().getId())
                .code(pipeline.getManager().getCode())
                .designation(pipeline.getManager().getDesignationFr())
                .build())
            .build();
    }
    
    private SlotStatistics getSlotStatistics(Long pipelineId, LocalDate date) {
        var readings = flowReadingRepository.findByPipelineIdAndReadingDate(pipelineId, date);
        
        int recordedCount = 0;
        int approvedCount = 0;
        int submittedCount = 0;
        int overdueCount = 0;
        LocalDateTime now = LocalDateTime.now();
        
        var allSlots = readingSlotRepository.findAllByOrderByDisplayOrder();
        var readingsBySlot = readings.stream()
            .collect(Collectors.toMap(
                reading -> reading.getReadingSlot().getId(),
                reading -> reading
            ));
        
        for (var slot : allSlots) {
            var reading = readingsBySlot.get(slot.getId());
            if (reading != null) {
                recordedCount++;
                String status = reading.getValidationStatus() != null 
                    ? reading.getValidationStatus().getCode() 
                    : "DRAFT";
                
                if ("APPROVED".equals(status)) {
                    approvedCount++;
                } else if ("SUBMITTED".equals(status)) {
                    submittedCount++;
                }
                
                if (!"APPROVED".equals(status)) {
                    LocalDateTime slotDeadline = LocalDateTime.of(date, slot.getEndTime());
                    if (now.isAfter(slotDeadline)) {
                        overdueCount++;
                    }
                }
            } else {
                LocalDateTime slotDeadline = LocalDateTime.of(date, slot.getEndTime());
                if (now.isAfter(slotDeadline)) {
                    overdueCount++;
                }
            }
        }
        
        double completionRate = (approvedCount * 100.0) / 12.0;
        
        return new SlotStatistics(recordedCount, approvedCount, submittedCount, overdueCount, completionRate);
    }
    
    private Double calculateWeeklyCompletionRate(Long pipelineId, LocalDate startDate, LocalDate endDate) {
        List<Double> dailyRates = new ArrayList<>();
        LocalDate current = startDate;
        
        while (!current.isAfter(endDate)) {
            var stats = getSlotStatistics(pipelineId, current);
            dailyRates.add(stats.completionRate);
            current = current.plusDays(1);
        }
        
        return dailyRates.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }
    
    private BigDecimal extractMeasurementValue(var reading, String measurementType) {
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
     * Inner class for slot statistics
     */
    private record SlotStatistics(
        int recordedCount,
        int approvedCount,
        int submittedCount,
        int overdueCount,
        double completionRate
    ) {}
}
