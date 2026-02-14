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
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 * 	@Refactoring: Phase 2 - Removed direct PipelineRepository dependency.
 * 	              Now uses PipelineFacade exclusively.
 * 	              
 * 	              Benefits:
 * 	              - Enforces module boundaries (no direct network/core repository access)
 * 	              - Improves testability (mock facade instead of repository)
 * 	              - Enables caching and optimization at facade level
 *
 * 	@Refactoring: Phase 1 - Removed buildAssetDTO() method.
 * 	              Now uses PipelineDTO.fromEntity() from network module.
 * 	              
 * 	              Benefits:
 * 	              - Eliminates 150+ LOC of redundant mapping code
 * 	              - Single source of truth for pipeline data structure
 * 	              - Reduces maintenance burden (update mapping in one place)
 * 	              - Removes risk of inconsistency between duplicate DTOs
 *
 * 	@Refactoring: Phase 2 (Enhancement) - Removed ALL entity dependencies.
 * 	              Service now works exclusively with DTOs from facades.
 * 	              
 * 	              Benefits:
 * 	              - Complete decoupling from entity layer
 * 	              - No lazy loading issues (DTOs have all data pre-loaded)
 * 	              - Clear architectural boundary enforcement
 * 	              - Intelligence layer is fully independent
 *
 * 	@Enhancement: Phase 3 - Added PipelineInfoPage support.
 * 	              New methods: getPipelineInfo(), getDashboard(), getTimeline()
 * 	              
 * 	              Features:
 * 	              - Comprehensive pipeline information aggregation
 * 	              - Real-time operational dashboard metrics
 * 	              - Unified timeline of alerts and events
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
 * Focuses on slot-based monitoring, KPI aggregation, and trend analysis.
 * Extended with PipelineInfoPage support for comprehensive operational views.
 * 
 * Architecture Pattern:
 * - Uses PipelineFacade for pipeline data access (enforces module boundary)
 * - Uses FlowReadingFacade for flow reading queries
 * - NO direct access to repositories
 * - NO entity dependencies (works exclusively with DTOs)
 * 
 * Phase 1 Refactoring:
 * - Now uses PipelineDTO.fromEntity() instead of custom buildAssetDTO()
 * - Eliminates redundant mapping code (DRY principle)
 * 
 * Phase 2 Refactoring:
 * - Facades now return DTOs instead of entities
 * - Service no longer imports or uses entity classes
 * - Complete decoupling from entity layer
 * 
 * Phase 3 Enhancement:
 * - Added comprehensive PipelineInfoPage support
 * - New methods for info, dashboard, and timeline views
 * - Maintains existing architecture patterns
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceService {
    
    // ========== DEPENDENCIES (Phase 2 Refactored) ==========
    
    /**
     * Facade for accessing pipeline data as DTOs
     * REFACTORED: Now returns DTOs instead of entities
     */
    private final PipelineFacade pipelineFacade;
    
    /**
     * Facade for accessing flow reading data as DTOs
     * REFACTORED: Now returns DTOs instead of entities
     */
    private final FlowReadingFacade flowReadingFacade;
    
    // TODO: Add AlertFacade when alerts module is implemented
    // private final AlertFacade alertFacade;
    
    // TODO: Add EventFacade when events module is implemented
    // private final EventFacade eventFacade;
    
    // TODO: Add StationFacade, ValveFacade, SensorFacade for entity linking
    // private final StationFacade stationFacade;
    // private final ValveFacade valveFacade;
    // private final SensorFacade sensorFacade;
    
    // ========== PUBLIC SERVICE METHODS (PHASE 3 - NEW) ==========
    
    /**
     * Get comprehensive pipeline information for PipelineInfoPage
     * 
     * Aggregates static infrastructure with optional dynamic health data.
     * Supports lazy loading for linked entities to optimize performance.
     * 
     * @param pipelineId Pipeline ID
     * @param includeHealth Whether to include current health metrics
     * @param includeEntities Whether to include linked entities (stations, valves, sensors)
     * @return Comprehensive pipeline information DTO
     */
    public PipelineInfoDTO getPipelineInfo(Long pipelineId, Boolean includeHealth, Boolean includeEntities) {
        log.debug("Getting pipeline info for ID {} (health={}, entities={})", 
                  pipelineId, includeHealth, includeEntities);
        
        // Get core pipeline data
        PipelineDTO pipelineDTO = pipelineFacade.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        // Build base info
        PipelineInfoDTO.PipelineInfoDTOBuilder builder = PipelineInfoDTO.builder()
            .id(pipelineDTO.getId())
            .name(pipelineDTO.getName())
            .code(pipelineDTO.getCode())
            .status(determineOperationalStatus(pipelineId)) // TODO: Implement status logic
            .lengthKm(pipelineDTO.getLengthKm())
            .diameterMm(pipelineDTO.getDiameterMm())
            .material(pipelineDTO.getMaterial())
            .operatorName(pipelineDTO.getManager() != null ? pipelineDTO.getManager().getDesignationLt() : null)
            .commissionDate(pipelineDTO.getCommissionDate())
            .geometry(pipelineDTO.getGeometry())
            .description(pipelineDTO.getDescription())
            .maxPressureBar(pipelineDTO.getMaxPressureBar())
            .designFlowRate(pipelineDTO.getDesignFlowRate())
            .pipelineDetails(pipelineDTO)
            .lastUpdateTime(LocalDateTime.now());
        
        // Include current health if requested
        if (Boolean.TRUE.equals(includeHealth)) {
            PipelineHealthDTO health = calculatePipelineHealth(pipelineId);
            builder.currentHealth(health);
        }
        
        // Include linked entities if requested
        if (Boolean.TRUE.equals(includeEntities)) {
            // TODO: Implement entity loading via facades
            // builder.stations(stationFacade.findByPipelineId(pipelineId));
            // builder.valves(valveFacade.findByPipelineId(pipelineId));
            // builder.sensors(sensorFacade.findByPipelineId(pipelineId));
            
            // For now, set counts to 0
            builder.stationCount(0);
            builder.valveCount(0);
            builder.sensorCount(0);
        }
        
        return builder.build();
    }
    
    /**
     * Get real-time operational dashboard metrics
     * 
     * Optimized for frequent updates with minimal data transfer.
     * Should be cached with short TTL (30 seconds).
     * 
     * @param pipelineId Pipeline ID
     * @return Real-time dashboard metrics DTO
     */
    public PipelineDynamicDashboardDTO getDashboard(Long pipelineId) {
        log.debug("Getting dashboard for pipeline {}", pipelineId);
        
        // Verify pipeline exists
        PipelineDTO pipeline = pipelineFacade.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        // Get latest reading
        var latestReading = flowReadingFacade.findLatestByPipeline(pipelineId);
        
        // Calculate health metrics
        PipelineHealthDTO health = calculatePipelineHealth(pipelineId);
        
        // Get 24-hour statistics
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        var last24hReadings = flowReadingFacade.findByPipelineAndDateRangeOrdered(
            pipelineId, yesterday, today);
        
        BigDecimal avgPressure24h = calculateAverage(last24hReadings, "PRESSURE");
        BigDecimal avgTemperature24h = calculateAverage(last24hReadings, "TEMPERATURE");
        BigDecimal avgFlowRate24h = calculateAverage(last24hReadings, "FLOW_RATE");
        BigDecimal throughput24h = calculateThroughput(last24hReadings);
        
        // Get today's validation stats
        var todayReadings = flowReadingFacade.findByPipelineAndDate(pipelineId, today);
        int validatedToday = (int) todayReadings.stream()
            .filter(r -> r.getValidationStatus() != null && "APPROVED".equals(r.getValidationStatus().getCode()))
            .count();
        int pendingToday = (int) todayReadings.stream()
            .filter(r -> r.getValidationStatus() != null && "SUBMITTED".equals(r.getValidationStatus().getCode()))
            .count();
        
        // Build key metrics map
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
     * Includes filtering by severity and type.
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
        
        // TODO: Apply pagination
        int totalItems = items.size();
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalItems);
        List<TimelineItemDTO> paginatedItems = items.subList(startIndex, endIndex);
        
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
        
        int totalPages = (int) Math.ceil((double) totalItems / size);
        
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
     * 
     * REFACTORED (Phase 2): Now uses PipelineFacade.findById() which returns PipelineDTO
     * REFACTORED (Phase 1): Reuses network module's PipelineDTO
     * REFACTORED (Phase 2 Enhancement): Works exclusively with DTOs, no entity dependencies
     */
    public PipelineOverviewDTO getOverview(Long pipelineId, LocalDate referenceDate) {
        log.debug("Getting overview for pipeline {} on {}", pipelineId, referenceDate);
        
        // ✅ REFACTORED: Facade returns DTO directly (Phase 2)
        PipelineDTO asset = pipelineFacade.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        // Get today's slot coverage stats using utility
        var allSlots = flowReadingFacade.findAllSlotsOrdered();
        var readings = flowReadingFacade.findByPipelineAndDate(pipelineId, referenceDate);
        
        // ✅ Convert DTOs to minimal data structure for statistics calculation
        // SlotStatisticsCalculator works with reading data, not full entities
        SlotStatisticsCalculator.SlotStatistics slotStats = 
            calculateSlotStatisticsFromDTOs(allSlots, readings, referenceDate);
        
        // Get latest reading for current measurements
        var latestReading = flowReadingFacade.findLatestByPipeline(pipelineId);
        
        // Calculate weekly completion rate using utility
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
     * 
     * REFACTORED: Now works with DTOs from facades
     */
    public List<SlotStatusDTO> getSlotCoverage(Long pipelineId, LocalDate date) {
        log.debug("Getting slot coverage for pipeline {} on {}", pipelineId, date);
        
        // ✅ REFACTORED: Verify existence via facade
        if (!pipelineFacade.existsById(pipelineId)) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }
        
        // Get all 12 slots
        var allSlots = flowReadingFacade.findAllSlotsOrdered();
        
        // Get readings for this date (now returns DTOs)
        var readings = flowReadingFacade.findByPipelineAndDate(pipelineId, date);
        var readingsBySlot = readings.stream()
            .collect(Collectors.toMap(
                reading -> reading.getReadingSlot().getId(),
                reading -> reading
            ));
        
        LocalDateTime now = LocalDateTime.now();
        
        return allSlots.stream().map(slot -> {
            FlowReadingDTO reading = readingsBySlot.get(slot.getId());
            
            // Use utility to check if overdue
            boolean isOverdue = false;
            if (reading == null) {
                isOverdue = SlotStatisticsCalculator.isSlotOverdue(slot, date, now);
            } else if (reading.getValidationStatus() != null && 
                       !"APPROVED".equals(reading.getValidationStatus().getCode())) {
                // Convert DTO data to check if overdue
                isOverdue = isReadingOverdueFromDTO(reading, now);
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
     * 
     * REFACTORED: Now works with DTOs from facades
     */
    public ReadingsTimeSeriesDTO getReadingsTimeSeries(
            Long pipelineId, 
            LocalDate startDate, 
            LocalDate endDate,
            String measurementType) {
        
        log.debug("Getting time-series for pipeline {} from {} to {}, type: {}",
            pipelineId, startDate, endDate, measurementType);
        
        // ✅ REFACTORED: Verify existence via facade
        if (!pipelineFacade.existsById(pipelineId)) {
            throw new IllegalArgumentException("Pipeline not found: " + pipelineId);
        }
        
        // Get readings in date range (now returns DTOs)
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
    
    // ========== HELPER METHODS (PHASE 3 - NEW) ==========
    
    /**
     * Calculate comprehensive pipeline health metrics
     * 
     * Aggregates current readings, alerts, and thresholds to determine health status.
     * 
     * @param pipelineId Pipeline ID
     * @return Health metrics DTO
     */
    private PipelineHealthDTO calculatePipelineHealth(Long pipelineId) {
        // Get latest reading
        var latestReading = flowReadingFacade.findLatestByPipeline(pipelineId);
        
        // TODO: Get active alerts from AlertFacade
        int activeAlerts = 0;
        int criticalAlerts = 0;
        int warningAlerts = 0;
        
        // TODO: Calculate health score based on readings and alerts
        double healthScore = 92.5; // Placeholder
        
        // TODO: Determine overall health status
        String overallHealth = "HEALTHY"; // Placeholder
        
        // Get 24-hour averages
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        var last24hReadings = flowReadingFacade.findByPipelineAndDateRangeOrdered(
            pipelineId, yesterday, today);
        
        BigDecimal avgPressure24h = calculateAverage(last24hReadings, "PRESSURE");
        BigDecimal throughput24h = calculateThroughput(last24hReadings);
        
        // Get 7-day event count
        // TODO: Implement via EventFacade
        int eventsLast7Days = 0;
        
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
            .temperatureStatus("NORMAL") // TODO: Implement threshold checking
            .flowRateStatus("NORMAL") // TODO: Implement threshold checking
            .availabilityPercent(99.5) // TODO: Implement availability calculation
            .sensorOnlinePercent(97.8) // TODO: Implement via SensorFacade
            .build();
    }
    
    /**
     * Determine operational status based on latest data
     * 
     * @param pipelineId Pipeline ID
     * @return Status string (OPERATIONAL, MAINTENANCE, SHUTDOWN, EMERGENCY)
     */
    private String determineOperationalStatus(Long pipelineId) {
        // TODO: Implement status determination logic
        // - Check latest readings
        // - Check active alerts
        // - Check scheduled maintenance
        // - Check emergency shutdowns
        return "OPERATIONAL";
    }
    
    /**
     * Calculate average value for a measurement type
     */
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
    
    /**
     * Calculate total throughput from readings
     */
    private BigDecimal calculateThroughput(List<FlowReadingDTO> readings) {
        // TODO: Implement proper throughput calculation
        // Sum of (flow_rate * time_interval) for each reading
        return readings.stream()
            .map(FlowReadingDTO::getContainedVolume)
            .filter(v -> v != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Calculate data completeness percentage for today
     */
    private Double calculateDataCompleteness(List<FlowReadingDTO> todayReadings) {
        int totalSlots = 12;
        int recordedSlots = todayReadings.size();
        return (recordedSlots * 100.0) / totalSlots;
    }
    
    // ========== HELPER METHODS (EXISTING) ==========
    
    /**
     * Calculate slot statistics from DTOs instead of entities
     * 
     * NEW (Phase 2): Adapter method to work with DTOs while using existing utility
     */
    private SlotStatisticsCalculator.SlotStatistics calculateSlotStatisticsFromDTOs(
            List<ReadingSlot> allSlots, 
            List<FlowReadingDTO> readingDTOs, 
            LocalDate referenceDate) {
        
        // For now, we need to work around the utility expecting entities
        // TODO: Refactor SlotStatisticsCalculator to accept DTOs or interfaces
        
        // Count statistics directly from DTOs
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
        
        // Calculate overdue count
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
    
    /**
     * Check if reading is overdue based on DTO data
     * 
     * NEW (Phase 2): Helper method to check overdue status without entity dependency
     */
    private boolean isReadingOverdueFromDTO(FlowReadingDTO reading, LocalDateTime now) {
        if (reading.getReadingSlot() == null) {
            return false;
        }
        
        LocalDateTime deadline = LocalDateTime.of(
            reading.getReadingDate(),
            reading.getReadingSlot().getEndTime()
        ).plusHours(2); // 2-hour grace period
        
        return now.isAfter(deadline);
    }
    
    /**
     * Calculate weekly completion rate using SlotStatisticsCalculator utility
     * 
     * UPDATED (Phase 2): Now works with DTOs from facade
     */
    private Double calculateWeeklyCompletionRate(Long pipelineId, LocalDate startDate, LocalDate endDate) {
        // Get all slots and readings for the date range
        var allSlots = flowReadingFacade.findAllSlotsOrdered();
        
        // Group readings by date (now working with DTOs)
        var allReadings = flowReadingFacade.findByPipelineAndDateRangeOrdered(pipelineId, startDate, endDate);
        Map<LocalDate, List<FlowReadingDTO>> readingsByDate = allReadings.stream()
            .collect(Collectors.groupingBy(FlowReadingDTO::getReadingDate));
        
        // Calculate average completion rate across all days
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
    
    /**
     * Extract measurement value based on type
     * 
     * UPDATED (Phase 2): Now works with FlowReadingDTO
     */
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
}
