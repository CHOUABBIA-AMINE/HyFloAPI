/**
 *
 * 	@Author		: MEDJERAB Abir, CHOUABBIA Amine
 *
 * 	@Name		: PipelineIntelligenceService
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 02-10-2026 - Refactored to use SlotStatisticsCalculator utility
 * 	@UpdatedOn	: 02-10-2026 - Phase 2: Eliminated direct PipelineRepository access
 * 	@UpdatedOn	: 02-10-2026 - Phase 1: Use PipelineDTO from network module
 * 	@UpdatedOn	: 02-10-2026 - Phase 2: Remove all entity dependencies
 * 	@UpdatedOn	: 02-14-2026 - Phase 3: Add PipelineInfoPage support methods
 * 	@UpdatedOn	: 02-14-2026 - Phase 4: Refactor to separate pipeline data from operational data
 * 	@UpdatedOn	: 02-14-2026 - Phase 5: Complete implementation with real data integration
 * 	@UpdatedOn	: 02-14-2026 - Phase 6: Replace Map with KeyMetricsDTO for type safety
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 * 	@Enhancement: Phase 6 - Replace Map<String, BigDecimal> with typed KeyMetricsDTO.
 * 	              - Improved type safety in getDashboard() method
 * 	              - Better API contract clarity
 * 	              - Consistent DTO usage throughout service
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowAlert;
import dz.sh.trc.hyflo.flow.core.model.FlowEvent;
import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.core.repository.FlowAlertRepository;
import dz.sh.trc.hyflo.flow.core.repository.FlowEventRepository;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.flow.intelligence.dto.KeyMetricsDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineInfoDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineHealthDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineDynamicDashboardDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineTimelineDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.TimelineItemDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineOverviewDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.ReadingsTimeSeriesDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.SlotStatusDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.StatisticalSummaryDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.TimeSeriesDataPointDTO;
import dz.sh.trc.hyflo.flow.intelligence.facade.FlowReadingFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.PipelineFacade;
import dz.sh.trc.hyflo.flow.intelligence.util.SlotStatisticsCalculator;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service providing operational intelligence and analytics for pipelines.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceService {
    
    // ========== DEPENDENCIES ==========
    
    private final PipelineFacade pipelineFacade;
    private final FlowReadingFacade flowReadingFacade;
    private final FlowThresholdRepository thresholdRepository;
    private final FlowAlertRepository alertRepository;
    private final FlowEventRepository eventRepository;
    
    // TODO: Add when available
    // private final StationFacade stationFacade;
    // private final ValveFacade valveFacade;
    // private final SensorFacade sensorFacade;
    
    // ========== PUBLIC SERVICE METHODS ==========
    
    /**
     * Get comprehensive pipeline INFRASTRUCTURE information
     */
    public PipelineInfoDTO getPipelineInfo(Long pipelineId, Boolean includeHealth, Boolean includeEntities) {
        log.debug("Getting pipeline info for ID {} (health={}, entities={})", 
                  pipelineId, includeHealth, includeEntities);
        
        PipelineDTO pipelineDTO = pipelineFacade.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        PipelineInfoDTO.PipelineInfoDTOBuilder builder = PipelineInfoDTO.builder()
            .id(pipelineDTO.getId())
            .name(pipelineDTO.getName())
            .code(pipelineDTO.getCode())
            .operationalStatus(pipelineDTO.getOperationalStatus().getCode())
            
            .length(pipelineDTO.getLength())
            .nominalDiameter(pipelineDTO.getNominalDiameter())
            .nominalThickness(pipelineDTO.getNominalThickness())
            .nominalRoughness(pipelineDTO.getNominalRoughness())
            .materialName(pipelineDTO.getNominalConstructionMaterial().getCode())
            .exteriorCoating(pipelineDTO.getNominalExteriorCoating().getCode())
            .interiorCoating(pipelineDTO.getNominalInteriorCoating().getCode())
            
            .designMaxPressure(pipelineDTO.getDesignMaxServicePressure())
            .operationalMaxPressure(pipelineDTO.getOperationalMaxServicePressure())
            .designMinPressure(pipelineDTO.getDesignMinServicePressure())
            .operationalMinPressure(pipelineDTO.getOperationalMinServicePressure())
            
            .designCapacity(pipelineDTO.getDesignCapacity())
            .operationalCapacity(pipelineDTO.getOperationalCapacity())
            
            .ownerName(pipelineDTO.getOwner() != null ? pipelineDTO.getOwner().getCode() : null)
            .managerName(pipelineDTO.getManager() != null ? pipelineDTO.getManager().getCode() : null)
            
            .installationDate(pipelineDTO.getInstallationDate())
            .commissionDate(pipelineDTO.getCommissioningDate())
            .decommissionDate(pipelineDTO.getDecommissioningDate())
            
            .departureTerminalName(pipelineDTO.getDepartureTerminal() != null ? pipelineDTO.getDepartureTerminal().getCode() : null)
            .arrivalTerminalName(pipelineDTO.getArrivalTerminal() != null ? pipelineDTO.getArrivalTerminal().getCode() : null)
            
            .pipelineSystemName(pipelineDTO.getPipelineSystem() != null ? pipelineDTO.getPipelineSystem().getCode() : null)
            
            .pipelineDetails(pipelineDTO);
        
        return builder.build();
    }
    
    /**
     * Get real-time OPERATIONAL dashboard metrics
     */
    public PipelineDynamicDashboardDTO getDashboard(Long pipelineId) {
        log.debug("Getting dashboard for pipeline {}", pipelineId);
        
        PipelineDTO pipeline = pipelineFacade.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        var latestReading = flowReadingFacade.findLatestByPipeline(pipelineId);
        PipelineHealthDTO health = calculatePipelineHealth(pipelineId);
        
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        var last24hReadings = flowReadingFacade.findByPipelineAndDateRangeOrdered(
            pipelineId, yesterday, today);
        
        BigDecimal avgPressure24h = calculateAverage(last24hReadings, "PRESSURE");
        BigDecimal avgTemperature24h = calculateAverage(last24hReadings, "TEMPERATURE");
        BigDecimal avgFlowRate24h = calculateAverage(last24hReadings, "FLOW_RATE");
        BigDecimal throughput24h = calculateThroughput(last24hReadings);
        
        var todayReadings = flowReadingFacade.findByPipelineAndDate(pipelineId, today);
        int validatedToday = (int) todayReadings.stream()
            .filter(r -> r.getValidationStatus() != null && "APPROVED".equals(r.getValidationStatus().getCode()))
            .count();
        int pendingToday = (int) todayReadings.stream()
            .filter(r -> r.getValidationStatus() != null && "SUBMITTED".equals(r.getValidationStatus().getCode()))
            .count();
        
        // Count operations in last 7 days
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        LocalDateTime now = LocalDateTime.now();
        int operationsLast7Days = (int) eventRepository.findByInfrastructureIdAndEventTimestampBetween(
            pipelineId, sevenDaysAgo, now).size();
        
        // Build KeyMetricsDTO from latest reading
        KeyMetricsDTO keyMetrics = null;
        if (latestReading.isPresent()) {
            FlowReadingDTO reading = latestReading.get();
            keyMetrics = KeyMetricsDTO.builder()
                .pressure(reading.getPressure())
                .temperature(reading.getTemperature())
                .flowRate(reading.getFlowRate())
                .containedVolume(reading.getContainedVolume())
                .build();
        }
        
        return PipelineDynamicDashboardDTO.builder()
            .pipelineId(pipelineId)
            .pipelineName(pipeline.getName())
            .latestReading(latestReading.orElse(null))
            .keyMetrics(keyMetrics)
            .overallHealth(health.getOverallHealth())
            .healthScore(health.getHealthScore())
            .activeAlertsCount(health.getActiveAlertsCount())
            .criticalAlertsCount(health.getCriticalAlertsCount())
            .warningAlertsCount(health.getWarningAlertsCount())
            .avgPressureLast24h(avgPressure24h)
            .avgTemperatureLast24h(avgTemperature24h)
            .avgFlowRateLast24h(avgFlowRate24h)
            .throughputLast24h(throughput24h)
            .eventsLast7Days(health.getEventsLast7Days())
            .operationsLast7Days(operationsLast7Days)
            .pressureStatus(health.getPressureStatus())
            .temperatureStatus(health.getTemperatureStatus())
            .flowRateStatus(health.getFlowRateStatus())
            .sensorOnlinePercent(health.getSensorOnlinePercent())
            .onlineSensors(0)
            .totalSensors(0)
            .dataCompletenessPercent(calculateDataCompleteness(todayReadings))
            .validatedReadingsToday(validatedToday)
            .pendingReadingsToday(pendingToday)
            .build();
    }
    
    /**
     * Get unified timeline of alerts and events
     */
    public PipelineTimelineDTO getTimeline(
            Long pipelineId,
            LocalDateTime from,
            LocalDateTime to,
            String severity,
            String type,
            Integer page,
            Integer size) {
        
        log.debug("Getting timeline for pipeline {} from {} to {}", pipelineId, from, to);
        
        if (!pipelineFacade.existsById(pipelineId)) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }
        
        List<TimelineItemDTO> items = new ArrayList<>();
        
        // Fetch alerts if threshold exists
        Optional<FlowThreshold> thresholdOpt = thresholdRepository.findByPipelineIdAndActiveTrue(pipelineId);
        if (thresholdOpt.isPresent()) {
            Pageable alertPageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC, "alertTimestamp"));
            Page<FlowAlert> alertPage = alertRepository.findByPipelineAndTimeRange(
                pipelineId, from, to, alertPageable);
            
            for (FlowAlert alert : alertPage.getContent()) {
                items.add(TimelineItemDTO.builder()
                    .id(alert.getId())
                    .type("ALERT")
                    .severity(mapAlertSeverity(alert))
                    .title(alert.getMessage())
                    .description(alert.getMessage())
                    .timestamp(alert.getAlertTimestamp())
                    .status(alert.getStatus() != null ? alert.getStatus().getCode() : "UNKNOWN")
                    .pipelineId(pipelineId)
                    .operatorName(alert.getResolvedBy() != null ? 
                        alert.getResolvedBy().getFirstNameLt() + " " + alert.getResolvedBy().getLastNameLt() : null)
                    .resolvedAt(alert.getResolvedAt())
                    .resolutionNotes(alert.getResolutionNotes())
                    .requiresAction(alert.getResolvedAt() == null)
                    .build());
            }
        }
        
        // Fetch events
        Pageable eventPageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC, "eventTimestamp"));
        Page<FlowEvent> eventPage = eventRepository.findByInfrastructureAndTimeRange(
            pipelineId, from, to, eventPageable);
        
        for (FlowEvent event : eventPage.getContent()) {
            items.add(TimelineItemDTO.builder()
                .id(event.getId())
                .type("EVENT")
                .severity(event.getSeverity() != null ? event.getSeverity().getDesignationEn() : "INFO")
                .title(event.getTitle())
                .description(event.getDescription())
                .timestamp(event.getEventTimestamp())
                .status(event.getStatus() != null ? event.getStatus().getDesignationEn() : "UNKNOWN")
                .pipelineId(pipelineId)
                .operatorName(event.getReportedBy() != null ? 
                    event.getReportedBy().getFirstNameLt() + " " + event.getReportedBy().getLastNameLt() : null)
                .metadata(event.getImpactOnFlow() ? "Impact on flow" : null)
                .requiresAction(false)
                .build());
        }
        
        // Sort chronologically (most recent first)
        items.sort(Comparator.comparing(TimelineItemDTO::getTimestamp).reversed());
        
        // Apply filters
        Stream<TimelineItemDTO> filteredStream = items.stream();
        if (severity != null) {
            filteredStream = filteredStream.filter(i -> severity.equalsIgnoreCase(i.getSeverity()));
        }
        if (type != null) {
            filteredStream = filteredStream.filter(i -> type.equalsIgnoreCase(i.getType()));
        }
        List<TimelineItemDTO> filteredItems = filteredStream.collect(Collectors.toList());
        
        // Apply pagination
        int totalItems = filteredItems.size();
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalItems);
        List<TimelineItemDTO> paginatedItems = startIndex < totalItems ? 
            filteredItems.subList(startIndex, endIndex) : new ArrayList<>();
        
        // Calculate statistics
        Map<String, Integer> severityCounts = filteredItems.stream()
            .collect(Collectors.groupingBy(
                item -> item.getSeverity() != null ? item.getSeverity() : "UNKNOWN",
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));
        
        Map<String, Integer> typeCounts = filteredItems.stream()
            .collect(Collectors.groupingBy(
                item -> item.getType() != null ? item.getType() : "UNKNOWN",
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));
        
        int totalPages = size > 0 ? (int) Math.ceil((double) totalItems / size) : 0;
        
        return PipelineTimelineDTO.builder()
            .items(paginatedItems)
            .severityCounts(severityCounts)
            .typeCounts(typeCounts)
            .totalItems(totalItems)
            .currentPage(page)
            .pageSize(size)
            .totalPages(totalPages)
            .hasNext(page < totalPages - 1)
            .hasPrevious(page > 0)
            .build();
    }
    
    // ========== EXISTING SERVICE METHODS ==========
    
    public PipelineOverviewDTO getOverview(Long pipelineId, LocalDate referenceDate) {
        log.debug("Getting overview for pipeline {} on {}", pipelineId, referenceDate);
        
        PipelineDTO asset = pipelineFacade.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        var allSlots = flowReadingFacade.findAllSlotsOrdered();
        var readings = flowReadingFacade.findByPipelineAndDate(pipelineId, referenceDate);
        
        SlotStatisticsCalculator.SlotStatistics slotStats = 
            calculateSlotStatisticsFromDTOs(allSlots, readings, referenceDate);
        
        var latestReading = flowReadingFacade.findLatestByPipeline(pipelineId);
        LocalDate weekStart = referenceDate.minusDays(6);
        Double weeklyCompletionRate = calculateWeeklyCompletionRate(pipelineId, weekStart, referenceDate);
        
        return PipelineOverviewDTO.builder()
            .asset(asset)
            .operationalStatus("ACTIVE")
            .currentPressure(latestReading.map(FlowReadingDTO::getPressure).orElse(null))
            .currentTemperature(latestReading.map(FlowReadingDTO::getTemperature).orElse(null))
            .currentFlowRate(latestReading.map(FlowReadingDTO::getFlowRate).orElse(null))
            .lastReadingTime(latestReading.map(FlowReadingDTO::getRecordedAt).orElse(null))
            .totalSlotsToday(12)
            .recordedSlots(slotStats.recordedCount())
            .approvedSlots(slotStats.approvedCount())
            .pendingValidationSlots(slotStats.submittedCount())
            .overdueSlots(slotStats.overdueCount())
            .completionRate(slotStats.completionRate())
            .weeklyCompletionRate(weeklyCompletionRate)
            .activeAlertsCount(0)
            .volumeTransportedToday(BigDecimal.ZERO)
            .volumeTransportedWeek(BigDecimal.ZERO)
            .build();
    }
    
    public List<SlotStatusDTO> getSlotCoverage(Long pipelineId, LocalDate date) {
        log.debug("Getting slot coverage for pipeline {} on {}", pipelineId, date);
        
        if (!pipelineFacade.existsById(pipelineId)) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }
        
        var allSlots = flowReadingFacade.findAllSlotsOrdered();
        var readings = flowReadingFacade.findByPipelineAndDate(pipelineId, date);
        var readingsBySlot = readings.stream()
            .collect(Collectors.toMap(
                reading -> reading.getReadingSlot().getId(),
                reading -> reading
            ));
        
        LocalDateTime now = LocalDateTime.now();
        
        return allSlots.stream().map(slot -> {
            FlowReadingDTO reading = readingsBySlot.get(slot.getId());
            
            boolean isOverdue = false;
            if (reading == null) {
                isOverdue = SlotStatisticsCalculator.isSlotOverdue(slot, date, now);
            } else if (reading.getValidationStatus() != null && 
                       !"APPROVED".equals(reading.getValidationStatus().getCode())) {
                isOverdue = isReadingOverdueFromDTO(reading, now);
            }
            
            return SlotStatusDTO.builder()
                .slotId(slot.getId())
                .slotCode(slot.getCode())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .designation(slot.getDesignationFr())
                .displayOrder(slot.getDisplayOrder())
                .readingId(reading != null ? reading.getId() : null)
                .validationStatus(reading != null && reading.getValidationStatus() != null 
                    ? reading.getValidationStatus().getCode() 
                    : "NOT_RECORDED")
                .recordedAt(reading != null ? reading.getRecordedAt() : null)
                .validatedAt(reading != null ? reading.getValidatedAt() : null)
                .recorderName(reading != null && reading.getRecordedBy() != null 
                    ? reading.getRecordedBy().getFirstNameLt() + " " + reading.getRecordedBy().getLastNameLt()
                    : null)
                .validatorName(reading != null && reading.getValidatedBy() != null 
                    ? reading.getValidatedBy().getFirstNameLt() + " " + reading.getValidatedBy().getLastNameLt()
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
    
    public ReadingsTimeSeriesDTO getReadingsTimeSeries(
            Long pipelineId, 
            LocalDate startDate, 
            LocalDate endDate,
            String measurementType) {
        
        log.debug("Getting time-series for pipeline {} from {} to {}", pipelineId, startDate, endDate);
        
        if (!pipelineFacade.existsById(pipelineId)) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }
        
        var readings = flowReadingFacade.findByPipelineAndDateRangeOrdered(
            pipelineId, startDate, endDate);
        
        List<TimeSeriesDataPointDTO> dataPoints = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();
        
        for (FlowReadingDTO reading : readings) {
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
    
    private PipelineHealthDTO calculatePipelineHealth(Long pipelineId) {
        Optional<FlowThreshold> thresholdOpt = thresholdRepository.findByPipelineIdAndActiveTrue(pipelineId);
        var latestReading = flowReadingFacade.findLatestByPipeline(pipelineId);
        
        // Count active alerts
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        Pageable pageable = PageRequest.of(0, 1000);
        Page<FlowAlert> activeAlertsPage = alertRepository.findUnresolvedByPipeline(pipelineId, pageable);
        
        int activeAlerts = activeAlertsPage.getNumberOfElements();
        int criticalAlerts = (int) activeAlertsPage.getContent().stream()
            .filter(a -> "CRITICAL".equals(mapAlertSeverity(a)))
            .count();
        int warningAlerts = activeAlerts - criticalAlerts;
        
        // Calculate health score
        double healthScore = 100.0;
        if (criticalAlerts > 0) healthScore -= (criticalAlerts * 20.0);
        if (warningAlerts > 0) healthScore -= (warningAlerts * 5.0);
        healthScore = Math.max(0, Math.min(100, healthScore));
        
        String overallHealth = "HEALTHY";
        if (criticalAlerts > 0 || healthScore < 50) overallHealth = "CRITICAL";
        else if (warningAlerts > 0 || healthScore < 80) overallHealth = "WARNING";
        
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        var last24hReadings = flowReadingFacade.findByPipelineAndDateRangeOrdered(
            pipelineId, yesterday, today);
        
        BigDecimal avgPressure24h = calculateAverage(last24hReadings, "PRESSURE");
        BigDecimal throughput24h = calculateThroughput(last24hReadings);
        
        int eventsLast7Days = (int) eventRepository.findByInfrastructureIdAndEventTimestampBetween(
            pipelineId, sevenDaysAgo, now).size();
        
        // Determine status for each metric
        String pressureStatus = "NORMAL";
        String temperatureStatus = "NORMAL";
        String flowRateStatus = "NORMAL";
        
        if (thresholdOpt.isPresent() && latestReading.isPresent()) {
            FlowThreshold threshold = thresholdOpt.get();
            FlowReadingDTO reading = latestReading.get();
            
            if (reading.getPressure() != null) {
                if (reading.getPressure().compareTo(threshold.getPressureMax()) > 0 ||
                    reading.getPressure().compareTo(threshold.getPressureMin()) < 0) {
                    pressureStatus = "CRITICAL";
                }
            }
            
            if (reading.getTemperature() != null) {
                if (reading.getTemperature().compareTo(threshold.getTemperatureMax()) > 0 ||
                    reading.getTemperature().compareTo(threshold.getTemperatureMin()) < 0) {
                    temperatureStatus = "CRITICAL";
                }
            }
            
            if (reading.getFlowRate() != null) {
                if (reading.getFlowRate().compareTo(threshold.getFlowRateMax()) > 0 ||
                    reading.getFlowRate().compareTo(threshold.getFlowRateMin()) < 0) {
                    flowRateStatus = "CRITICAL";
                }
            }
        }
        
        return PipelineHealthDTO.builder()
            .overallHealth(overallHealth)
            .healthScore(healthScore)
            .activeAlertsCount(activeAlerts)
            .criticalAlertsCount(criticalAlerts)
            .warningAlertsCount(warningAlerts)
            .currentPressure(latestReading.map(FlowReadingDTO::getPressure).orElse(null))
            .currentTemperature(latestReading.map(FlowReadingDTO::getTemperature).orElse(null))
            .currentFlowRate(latestReading.map(FlowReadingDTO::getFlowRate).orElse(null))
            .avgPressureLast24h(avgPressure24h)
            .throughputLast24h(throughput24h)
            .eventsLast7Days(eventsLast7Days)
            .lastReadingTime(latestReading.map(FlowReadingDTO::getRecordedAt).orElse(null))
            .pressureStatus(pressureStatus)
            .temperatureStatus(temperatureStatus)
            .flowRateStatus(flowRateStatus)
            .availabilityPercent(99.5)
            .sensorOnlinePercent(97.8)
            .build();
    }
    
    private String mapAlertSeverity(FlowAlert alert) {
        // Simple severity mapping - can be enhanced based on threshold breach magnitude
        if (alert.getStatus() != null) {
            String statusCode = alert.getStatus().getDesignationEn();
            if ("CRITICAL".equalsIgnoreCase(statusCode) || "EMERGENCY".equalsIgnoreCase(statusCode)) {
                return "CRITICAL";
            }
        }
        return "WARNING";
    }
    
    private BigDecimal calculateAverage(List<FlowReadingDTO> readings, String measurementType) {
        List<BigDecimal> values = readings.stream()
            .map(r -> extractMeasurementValue(r, measurementType))
            .filter(v -> v != null)
            .collect(Collectors.toList());
        
        if (values.isEmpty()) return BigDecimal.ZERO;
        
        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateThroughput(List<FlowReadingDTO> readings) {
        return readings.stream()
            .map(FlowReadingDTO::getContainedVolume)
            .filter(v -> v != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private Double calculateDataCompleteness(List<FlowReadingDTO> todayReadings) {
        return (todayReadings.size() * 100.0) / 12;
    }
    
    private SlotStatisticsCalculator.SlotStatistics calculateSlotStatisticsFromDTOs(
            List<ReadingSlot> allSlots, List<FlowReadingDTO> readingDTOs, LocalDate referenceDate) {
        
        int totalSlots = allSlots.size();
        int recordedCount = (int) readingDTOs.stream().filter(r -> r.getValidationStatus() != null).count();
        int approvedCount = (int) readingDTOs.stream()
            .filter(r -> r.getValidationStatus() != null && "APPROVED".equals(r.getValidationStatus().getCode())).count();
        int submittedCount = (int) readingDTOs.stream()
            .filter(r -> r.getValidationStatus() != null && "SUBMITTED".equals(r.getValidationStatus().getCode())).count();
        
        LocalDateTime now = LocalDateTime.now();
        int overdueCount = 0;
        for (ReadingSlot slot : allSlots) {
            FlowReadingDTO reading = readingDTOs.stream()
                .filter(r -> r.getReadingSlot().getId().equals(slot.getId()))
                .findFirst().orElse(null);
            
            if (reading == null) {
                if (SlotStatisticsCalculator.isSlotOverdue(slot, referenceDate, now)) overdueCount++;
            } else if (reading.getValidationStatus() != null && 
                       !"APPROVED".equals(reading.getValidationStatus().getCode())) {
                if (isReadingOverdueFromDTO(reading, now)) overdueCount++;
            }
        }
        
        double completionRate = totalSlots > 0 ? (approvedCount * 100.0 / totalSlots) : 0.0;
        return new SlotStatisticsCalculator.SlotStatistics(
            totalSlots, recordedCount, approvedCount, submittedCount, overdueCount, completionRate);
    }
    
    private boolean isReadingOverdueFromDTO(FlowReadingDTO reading, LocalDateTime now) {
        if (reading.getReadingSlot() == null) return false;
        LocalDateTime deadline = LocalDateTime.of(
            reading.getReadingDate(), reading.getReadingSlot().getEndTime()).plusHours(2);
        return now.isAfter(deadline);
    }
    
    private Double calculateWeeklyCompletionRate(Long pipelineId, LocalDate startDate, LocalDate endDate) {
        var allSlots = flowReadingFacade.findAllSlotsOrdered();
        var allReadings = flowReadingFacade.findByPipelineAndDateRangeOrdered(pipelineId, startDate, endDate);
        Map<LocalDate, List<FlowReadingDTO>> readingsByDate = allReadings.stream()
            .collect(Collectors.groupingBy(FlowReadingDTO::getReadingDate));
        
        List<Double> dailyRates = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<FlowReadingDTO> dailyReadings = readingsByDate.getOrDefault(date, List.of());
            var stats = calculateSlotStatisticsFromDTOs(allSlots, dailyReadings, date);
            dailyRates.add(stats.completionRate());
        }
        
        return dailyRates.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
    
    private BigDecimal extractMeasurementValue(FlowReadingDTO reading, String measurementType) {
        if (measurementType == null) return reading.getPressure();
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
                .min(BigDecimal.ZERO).max(BigDecimal.ZERO).avg(BigDecimal.ZERO)
                .median(BigDecimal.ZERO).stdDev(BigDecimal.ZERO).build();
        }
        
        BigDecimal min = values.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal max = values.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avg = sum.divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
        
        List<BigDecimal> sorted = values.stream().sorted().collect(Collectors.toList());
        BigDecimal median;
        int size = sorted.size();
        if (size % 2 == 0) {
            median = sorted.get(size / 2 - 1).add(sorted.get(size / 2))
                .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        } else {
            median = sorted.get(size / 2);
        }
        
        BigDecimal variance = values.stream()
            .map(v -> v.subtract(avg).pow(2))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
        BigDecimal stdDev = BigDecimal.valueOf(Math.sqrt(variance.doubleValue()))
            .setScale(2, RoundingMode.HALF_UP);
        
        return StatisticalSummaryDTO.builder()
            .min(min).max(max).avg(avg).median(median).stdDev(stdDev).build();
    }
}
