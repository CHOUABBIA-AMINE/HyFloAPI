/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: PipelineIntelligenceService
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 02-10-2026 - Refactored to use SlotStatisticsCalculator utility
 * 	@UpdatedOn	: 02-10-2026 - Phase 2: Eliminated direct PipelineRepository access
 * 	@UpdatedOn	: 02-10-2026 - Phase 1: Use PipelineDTO from network module
 * 	@UpdatedOn	: 02-10-2026 - Phase 2: Remove all entity dependencies
 * 	@UpdatedOn	: 02-14-2026 - Phase 3: Add PipelineInfoPage support methods
 * 	@UpdatedOn	: 02-14-2026 - Phase 4: Refactor to separate pipeline data from operational data
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 * 	@Refactoring: Phase 4 - Clean separation of concerns.
 * 	              PipelineInfoDTO now contains ONLY pipeline-specific data.
 * 	              Operational metrics moved to PipelineDynamicDashboardDTO.
 * 	              
 * 	              Benefits:
 * 	              - Clear module boundaries (pipeline data vs operational data)
 * 	              - No data mixing from different modules
 * 	              - Each DTO has single responsibility
 * 	              - Frontend can cache static pipeline info separately from dynamic metrics
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
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
 * 
 * Phase 4 Refactoring:
 * - Clean separation: Pipeline static data vs operational dynamic data
 * - PipelineInfoDTO: ONLY pipeline infrastructure (network module domain)
 * - PipelineDynamicDashboardDTO: ONLY operational metrics (flow module domain)
 * - No data mixing between modules
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceService {
    
    // ========== DEPENDENCIES ==========
    
    private final PipelineFacade pipelineFacade;
    private final FlowReadingFacade flowReadingFacade;
    
    // TODO: Add when available
    // private final AlertFacade alertFacade;
    // private final EventFacade eventFacade;
    // private final StationFacade stationFacade;
    // private final ValveFacade valveFacade;
    // private final SensorFacade sensorFacade;
    
    // ========== PUBLIC SERVICE METHODS (PHASE 3 - NEW) ==========
    
    /**
     * Get comprehensive pipeline INFRASTRUCTURE information
     * 
     * REFACTORED (Phase 4): Returns ONLY pipeline-specific static data.
     * Does NOT include operational metrics, thresholds, or forecasts.
     * 
     * For operational data, use getDashboard() instead.
     * 
     * @param pipelineId Pipeline ID
     * @param includeHealth Whether to include current health metrics (from operational data)
     * @param includeEntities Whether to include linked entities (stations, valves, sensors)
     * @return Pipeline infrastructure information DTO
     */
    public PipelineInfoDTO getPipelineInfo(Long pipelineId, Boolean includeHealth, Boolean includeEntities) {
        log.debug("Getting pipeline info for ID {} (health={}, entities={})", 
                  pipelineId, includeHealth, includeEntities);
        
        // Get core pipeline data from Network module
        PipelineDTO pipelineDTO = pipelineFacade.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        // Build base infrastructure info (ONLY pipeline attributes)
        PipelineInfoDTO.PipelineInfoDTOBuilder builder = PipelineInfoDTO.builder()
            // Core identification
            .id(pipelineDTO.getId())
            .name(pipelineDTO.getName())
            .code(pipelineDTO.getCode())
            .description(pipelineDTO.getDescription())
            
            // Physical specifications
            .lengthKm(pipelineDTO.getLengthKm())
            .diameterMm(pipelineDTO.getDiameterMm())
            .material(pipelineDTO.getMaterial())
            .wallThicknessMm(pipelineDTO.getWallThicknessMm())
            .coatingType(pipelineDTO.getCoatingType())
            .burialDepthM(pipelineDTO.getBurialDepthM())
            
            // Design specifications
            .maxDesignPressureBar(pipelineDTO.getMaxPressureBar())
            .minDesignPressureBar(pipelineDTO.getMinPressureBar())
            .maxDesignTemperatureC(pipelineDTO.getMaxTemperatureC())
            .minDesignTemperatureC(pipelineDTO.getMinTemperatureC())
            .designFlowRate(pipelineDTO.getDesignFlowRate())
            .designStandard(pipelineDTO.getDesignStandard())
            
            // Operational info (pipeline-level, not reading-level)
            .operatorName(pipelineDTO.getManager() != null ? 
                pipelineDTO.getManager().getDesignationLt() : null)
            .commissionDate(pipelineDTO.getCommissionDate())
            .lastInspectionDate(pipelineDTO.getLastInspectionDate())
            .nextInspectionDate(pipelineDTO.getNextInspectionDate())
            .installationYear(pipelineDTO.getInstallationYear())
            .status(pipelineDTO.getStatus())
            
            // Geographic data
            .geometry(pipelineDTO.getGeometry())
            .startLocation(pipelineDTO.getStartLocation())
            .endLocation(pipelineDTO.getEndLocation())
            .routeDescription(pipelineDTO.getRouteDescription())
            
            // Additional technical info
            .fluidType(pipelineDTO.getFluidType())
            .cathodicProtectionType(pipelineDTO.getCathodicProtectionType())
            .scadaIntegrated(pipelineDTO.getScadaIntegrated())
            .pipelineClass(pipelineDTO.getPipelineClass())
            .complianceStatus(pipelineDTO.getComplianceStatus())
            .certifications(pipelineDTO.getCertifications())
            
            // Full details
            .pipelineDetails(pipelineDTO)
            
            // Metadata
            .lastUpdateTime(LocalDateTime.now())
            .dataSource("Network Module")
            .active(pipelineDTO.getActive());
        
        // Include linked entities if requested (lazy-loaded from Network module)
        if (Boolean.TRUE.equals(includeEntities)) {
            // TODO: Load via respective facades when available
            // List<StationDTO> stations = stationFacade.findByPipelineId(pipelineId);
            // List<ValveDTO> valves = valveFacade.findByPipelineId(pipelineId);
            // List<SensorDTO> sensors = sensorFacade.findByPipelineId(pipelineId);
            
            // builder.stations(stations)
            //     .valves(valves)
            //     .sensors(sensors)
            //     .stationCount(stations.size())
            //     .valveCount(valves.size())
            //     .sensorCount(sensors.size());
            
            // For now, set counts to 0
            builder.stationCount(0)
                .valveCount(0)
                .sensorCount(0)
                .cpStationCount(0);
        }
        
        // Optionally include current health (from Flow module)
        // NOTE: This is operational data, but included here for convenience
        // Frontend can cache static info separately and refresh health independently
        if (Boolean.TRUE.equals(includeHealth)) {
            PipelineHealthDTO health = calculatePipelineHealth(pipelineId);
            builder.currentHealth(health);
        }
        
        return builder.build();
    }
    
    /**
     * Get real-time OPERATIONAL dashboard metrics
     * 
     * REFACTORED (Phase 4): Contains ONLY operational/dynamic data.
     * Does NOT include static pipeline specifications.
     * 
     * For static pipeline info, use getPipelineInfo() instead.
     * 
     * @param pipelineId Pipeline ID
     * @return Real-time operational dashboard metrics DTO
     */
    public PipelineDynamicDashboardDTO getDashboard(Long pipelineId) {
        log.debug("Getting dashboard for pipeline {}", pipelineId);
        
        // Verify pipeline exists
        PipelineDTO pipeline = pipelineFacade.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        // Get latest reading (operational data)
        var latestReading = flowReadingFacade.findLatestByPipeline(pipelineId);
        
        // Calculate health metrics (operational data)
        PipelineHealthDTO health = calculatePipelineHealth(pipelineId);
        
        // Get 24-hour statistics (operational data)
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        var last24hReadings = flowReadingFacade.findByPipelineAndDateRangeOrdered(
            pipelineId, yesterday, today);
        
        BigDecimal avgPressure24h = calculateAverage(last24hReadings, "PRESSURE");
        BigDecimal avgTemperature24h = calculateAverage(last24hReadings, "TEMPERATURE");
        BigDecimal avgFlowRate24h = calculateAverage(last24hReadings, "FLOW_RATE");
        BigDecimal throughput24h = calculateThroughput(last24hReadings);
        
        // Get today's validation stats (operational data)
        var todayReadings = flowReadingFacade.findByPipelineAndDate(pipelineId, today);
        int validatedToday = (int) todayReadings.stream()
            .filter(r -> r.getValidationStatus() != null && "APPROVED".equals(r.getValidationStatus().getCode()))
            .count();
        int pendingToday = (int) todayReadings.stream()
            .filter(r -> r.getValidationStatus() != null && "SUBMITTED".equals(r.getValidationStatus().getCode()))
            .count();
        
        // Build key metrics map (operational data)
        Map<String, BigDecimal> keyMetrics = new HashMap<>();
        latestReading.ifPresent(reading -> {
            if (reading.getPressure() != null) keyMetrics.put("pressure", reading.getPressure());
            if (reading.getTemperature() != null) keyMetrics.put("temperature", reading.getTemperature());
            if (reading.getFlowRate() != null) keyMetrics.put("flowRate", reading.getFlowRate());
        });
        
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
            .eventsLast7Days(0) // TODO: Implement via EventFacade
            .operationsLast7Days(0) // TODO: Implement via OperationFacade
            .pressureStatus(health.getPressureStatus())
            .temperatureStatus(health.getTemperatureStatus())
            .flowRateStatus(health.getFlowRateStatus())
            .sensorOnlinePercent(health.getSensorOnlinePercent())
            .onlineSensors(0) // TODO: Implement via SensorFacade
            .totalSensors(0) // TODO: Implement via SensorFacade
            .dataCompletenessPercent(calculateDataCompleteness(todayReadings))
            .validatedReadingsToday(validatedToday)
            .pendingReadingsToday(pendingToday)
            .build();
    }
    
    /**
     * Get unified timeline of alerts and events
     * 
     * Merges alerts and events chronologically with pagination support.
     * 
     * @param pipelineId Pipeline ID
     * @param from Start date/time
     * @param to End date/time
     * @param severity Filter by severity (optional)
     * @param type Filter by type (optional)
     * @param page Page number (0-indexed)
     * @param size Items per page
     * @return Unified timeline with pagination
     */
    public PipelineTimelineDTO getTimeline(
            Long pipelineId,
            LocalDateTime from,
            LocalDateTime to,
            String severity,
            String type,
            Integer page,
            Integer size) {
        
        log.debug("Getting timeline for pipeline {} from {} to {} (severity={}, type={}, page={}, size={})",
                  pipelineId, from, to, severity, type, page, size);
        
        // Verify pipeline exists
        if (!pipelineFacade.existsById(pipelineId)) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }
        
        // TODO: Fetch alerts from AlertFacade
        // List<AlertDTO> alerts = alertFacade.findByPipelineAndDateRange(pipelineId, from, to);
        
        // TODO: Fetch events from EventFacade
        // List<EventDTO> events = eventFacade.findByPipelineAndDateRange(pipelineId, from, to);
        
        // For now, return empty timeline
        List<TimelineItemDTO> items = new ArrayList<>();
        
        // TODO: Merge and sort alerts and events
        // items = mergeAndSortTimeline(alerts, events);
        
        // TODO: Apply filters
        // if (severity != null) items = items.stream().filter(i -> severity.equals(i.getSeverity())).collect(Collectors.toList());
        // if (type != null) items = items.stream().filter(i -> type.equals(i.getType())).collect(Collectors.toList());
        
        // Apply pagination
        int totalItems = items.size();
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalItems);
        List<TimelineItemDTO> paginatedItems = items.isEmpty() ? items : items.subList(startIndex, endIndex);
        
        // Calculate distribution statistics
        Map<String, Integer> severityCounts = items.stream()
            .collect(Collectors.groupingBy(
                item -> item.getSeverity() != null ? item.getSeverity() : "UNKNOWN",
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));
        
        Map<String, Integer> typeCounts = items.stream()
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
    
    // ========== PUBLIC SERVICE METHODS (EXISTING) ==========
    
    /**
     * Get comprehensive overview with asset specs and operational KPIs
     */
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
            .operationalStatus("ACTIVE") // TODO: Implement actual status tracking
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
    
    /**
     * Calculate comprehensive pipeline health metrics (operational data)
     */
    private PipelineHealthDTO calculatePipelineHealth(Long pipelineId) {
        var latestReading = flowReadingFacade.findLatestByPipeline(pipelineId);
        
        // TODO: Get active alerts from AlertFacade
        int activeAlerts = 0;
        int criticalAlerts = 0;
        int warningAlerts = 0;
        
        // TODO: Calculate health score based on readings and alerts
        double healthScore = 92.5;
        String overallHealth = "HEALTHY";
        
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        var last24hReadings = flowReadingFacade.findByPipelineAndDateRangeOrdered(
            pipelineId, yesterday, today);
        
        BigDecimal avgPressure24h = calculateAverage(last24hReadings, "PRESSURE");
        BigDecimal throughput24h = calculateThroughput(last24hReadings);
        
        int eventsLast7Days = 0; // TODO: Implement via EventFacade
        
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
            .pressureStatus("NORMAL") // TODO: Implement threshold checking
            .temperatureStatus("NORMAL")
            .flowRateStatus("NORMAL")
            .availabilityPercent(99.5) // TODO: Implement availability calculation
            .sensorOnlinePercent(97.8) // TODO: Implement via SensorFacade
            .build();
    }
    
    private BigDecimal calculateAverage(List<FlowReadingDTO> readings, String measurementType) {
        List<BigDecimal> values = readings.stream()
            .map(r -> extractMeasurementValue(r, measurementType))
            .filter(v -> v != null)
            .collect(Collectors.toList());
        
        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
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
        int totalSlots = 12;
        int recordedSlots = todayReadings.size();
        return (recordedSlots * 100.0) / totalSlots;
    }
    
    private SlotStatisticsCalculator.SlotStatistics calculateSlotStatisticsFromDTOs(
            List<ReadingSlot> allSlots, 
            List<FlowReadingDTO> readingDTOs, 
            LocalDate referenceDate) {
        
        int totalSlots = allSlots.size();
        int recordedCount = (int) readingDTOs.stream()
            .filter(r -> r.getValidationStatus() != null)
            .count();
        int approvedCount = (int) readingDTOs.stream()
            .filter(r -> r.getValidationStatus() != null && "APPROVED".equals(r.getValidationStatus().getCode()))
            .count();
        int submittedCount = (int) readingDTOs.stream()
            .filter(r -> r.getValidationStatus() != null && "SUBMITTED".equals(r.getValidationStatus().getCode()))
            .count();
        
        LocalDateTime now = LocalDateTime.now();
        int overdueCount = 0;
        for (ReadingSlot slot : allSlots) {
            FlowReadingDTO reading = readingDTOs.stream()
                .filter(r -> r.getReadingSlot().getId().equals(slot.getId()))
                .findFirst()
                .orElse(null);
            
            if (reading == null) {
                if (SlotStatisticsCalculator.isSlotOverdue(slot, referenceDate, now)) {
                    overdueCount++;
                }
            } else if (reading.getValidationStatus() != null && 
                       !"APPROVED".equals(reading.getValidationStatus().getCode())) {
                if (isReadingOverdueFromDTO(reading, now)) {
                    overdueCount++;
                }
            }
        }
        
        double completionRate = totalSlots > 0 ? (approvedCount * 100.0 / totalSlots) : 0.0;
        
        return new SlotStatisticsCalculator.SlotStatistics(
            totalSlots, recordedCount, approvedCount, submittedCount, overdueCount, completionRate
        );
    }
    
    private boolean isReadingOverdueFromDTO(FlowReadingDTO reading, LocalDateTime now) {
        if (reading.getReadingSlot() == null) {
            return false;
        }
        
        LocalDateTime deadline = LocalDateTime.of(
            reading.getReadingDate(),
            reading.getReadingSlot().getEndTime()
        ).plusHours(2);
        
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
        
        return dailyRates.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }
    
    private BigDecimal extractMeasurementValue(FlowReadingDTO reading, String measurementType) {
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
}
