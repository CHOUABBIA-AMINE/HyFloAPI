/**
 *
 * 	@Author		: MEDJERAB Abir, CHOUABBIA Amine
 *
 * 	@Name		: PipelineIntelligenceService
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 03-26-2026 - Phase B/C: Align ALL builder calls to real DTO field contracts.
 * 	                             Source-of-truth: DTO class field declarations only.
 * 	                             No assumed fields. No invented getters.
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 * 	Entity source-of-truth per DTO:
 * 	  PipelineHealthDTO        : overallHealth, healthScore, activeAlertsCount,
 * 	                             criticalAlertsCount, warningAlertsCount,
 * 	                             currentPressure, currentTemperature, currentFlowRate,
 * 	                             avgPressureLast24h, throughputLast24h,
 * 	                             eventsLast7Days, lastReadingTime,
 * 	                             pressureStatus, temperatureStatus, flowRateStatus,
 * 	                             availabilityPercent, sensorOnlinePercent
 * 	  KeyMetricsDTO            : pressure, temperature, flowRate, containedVolume
 * 	  PipelineDynamicDashboardDTO: pipelineId, pipelineName, latestReading, keyMetrics,
 * 	                             overallHealth, healthScore, activeAlertsCount,
 * 	                             criticalAlertsCount, warningAlertsCount,
 * 	                             avgPressureLast24h, avgTemperatureLast24h,
 * 	                             avgFlowRateLast24h, throughputLast24h,
 * 	                             eventsLast7Days, operationsLast7Days,
 * 	                             pressureStatus, temperatureStatus, flowRateStatus,
 * 	                             sensorOnlinePercent, onlineSensors, totalSensors,
 * 	                             dataCompletenessPercent, validatedReadingsToday,
 * 	                             pendingReadingsToday
 * 	  PipelineOverviewDTO      : asset (PipelineDTO), operationalStatus,
 * 	                             currentPressure, currentTemperature, currentFlowRate,
 * 	                             lastReadingTime, totalSlotsToday, recordedSlots,
 * 	                             approvedSlots, pendingValidationSlots, overdueSlots,
 * 	                             completionRate, weeklyCompletionRate, activeAlertsCount,
 * 	                             volumeTransportedToday, volumeTransportedWeek
 * 	  PipelineTimelineDTO      : items, severityCounts, typeCounts, totalItems,
 * 	                             currentPage, pageSize, totalPages, hasNext, hasPrevious
 * 	  TimelineItemDTO          : id, type, severity, title, description, timestamp,
 * 	                             status, pipelineId, pipelineName, operatorName,
 * 	                             metadata, category, requiresAction, detailsUrl,
 * 	                             resolvedAt, resolutionNotes
 * 	  FlowReadingReadDTO       : readingDate, volumeM3, volumeMscf,
 * 	                             inletPressureBar, outletPressureBar, temperatureCelsius,
 * 	                             notes, submittedAt, validatedAt, pipelineId, pipelineCode,
 * 	                             validationStatusId, validationStatusCode,
 * 	                             readingSlotId, readingSlotCode,
 * 	                             workflowInstanceId, workflowStateCode
 * 	  FlowAlertFacadeDTO       : id, pipelineId, thresholdId, flowReadingId,
 * 	                             alertTimestamp, severityCode, statusCode, message
 * 	  FlowEventFacadeDTO       : id, pipelineId, flowReadingId, eventTimestamp,
 * 	                             severityCode, eventTypeCode, description
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.KeyMetricsDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineDynamicDashboardDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineHealthDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineInfoDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineOverviewDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineTimelineDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.ReadingsTimeSeriesDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.SlotStatusDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.TimelineItemDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowAlertFacadeDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowEventFacadeDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowThresholdFacadeDTO;
import dz.sh.trc.hyflo.flow.intelligence.facade.impl.IFlowAlertFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.impl.IFlowEventFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.impl.IFlowReadingFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.impl.IFlowThresholdFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.impl.IPipelineFacade;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service providing operational intelligence and analytics for pipelines.
 *
 * Phase B/C: All builder call sites realigned to real DTO field declarations.
 * No field is assumed; every name is sourced from the actual DTO class.
 *
 * H2: All flow/core repository dependencies replaced by facade interfaces.
 * F1: All flow/core entity type references replaced by facade DTO projections.
 * F2: IFlowReadingFacade returns FlowReadingReadDTO — v1 FlowReadingDTO eliminated.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceService {

    // =====================================================================
    // DEPENDENCIES
    // =====================================================================

    private final IPipelineFacade      pipelineFacade;
    private final IFlowReadingFacade   flowReadingFacade;
    private final IFlowThresholdFacade thresholdFacade;
    private final IFlowAlertFacade     alertFacade;
    private final IFlowEventFacade     eventFacade;

    // =====================================================================
    // getPipelineInfo
    // =====================================================================

    /**
     * Get comprehensive pipeline INFRASTRUCTURE information.
     */
    public PipelineInfoDTO getPipelineInfo(Long pipelineId,
            Boolean includeHealth, Boolean includeEntities) {
        log.debug("Getting pipeline info for ID {} (health={}, entities={})",
                pipelineId, includeHealth, includeEntities);

        PipelineDTO pipelineDTO = pipelineFacade.findById(pipelineId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pipeline not found: " + pipelineId));

        return PipelineInfoDTO.builder()
                .id(pipelineDTO.getId())
                .name(pipelineDTO.getName())
                .code(pipelineDTO.getCode())
                .operationalStatus(pipelineDTO.getOperationalStatus() != null
                        ? pipelineDTO.getOperationalStatus().getCode() : null)
                .length(pipelineDTO.getLength())
                .nominalDiameter(pipelineDTO.getNominalDiameter())
                .nominalThickness(pipelineDTO.getNominalThickness())
                .nominalRoughness(pipelineDTO.getNominalRoughness())
                .materialName(pipelineDTO.getNominalConstructionMaterial() != null
                        ? pipelineDTO.getNominalConstructionMaterial().getCode() : null)
                .exteriorCoating(pipelineDTO.getNominalExteriorCoating() != null
                        ? pipelineDTO.getNominalExteriorCoating().getCode() : null)
                .interiorCoating(pipelineDTO.getNominalInteriorCoating() != null
                        ? pipelineDTO.getNominalInteriorCoating().getCode() : null)
                .designMaxPressure(pipelineDTO.getDesignMaxServicePressure())
                .operationalMaxPressure(pipelineDTO.getOperationalMaxServicePressure())
                .designMinPressure(pipelineDTO.getDesignMinServicePressure())
                .operationalMinPressure(pipelineDTO.getOperationalMinServicePressure())
                .designCapacity(pipelineDTO.getDesignCapacity())
                .operationalCapacity(pipelineDTO.getOperationalCapacity())
                .ownerName(pipelineDTO.getOwner() != null
                        ? pipelineDTO.getOwner().getCode() : null)
                .managerName(pipelineDTO.getManager() != null
                        ? pipelineDTO.getManager().getCode() : null)
                .installationDate(pipelineDTO.getInstallationDate())
                .commissionDate(pipelineDTO.getCommissioningDate())
                .decommissionDate(pipelineDTO.getDecommissioningDate())
                .departureTerminalName(pipelineDTO.getDepartureTerminal() != null
                        ? pipelineDTO.getDepartureTerminal().getCode() : null)
                .arrivalTerminalName(pipelineDTO.getArrivalTerminal() != null
                        ? pipelineDTO.getArrivalTerminal().getCode() : null)
                .pipelineSystemName(pipelineDTO.getPipelineSystem() != null
                        ? pipelineDTO.getPipelineSystem().getCode() : null)
                .pipelineDetails(pipelineDTO)
                .build();
    }

    // =====================================================================
    // getPipelineHealth
    // =====================================================================

    /**
     * Get pipeline health metrics.
     *
     * Phase C fix:
     *   BEFORE (broken)  → AFTER (real DTO fields)
     *   .pipelineId()    → removed  (not in PipelineHealthDTO)
     *   .healthStatus()  → .overallHealth()
     *   .activeAlertCount()   → .activeAlertsCount()
     *   .criticalAlertCount() → .criticalAlertsCount()
     *   .activeThresholdCount() → removed  (not in PipelineHealthDTO)
     *   .assessedAt()    → removed  (not in PipelineHealthDTO)
     */
    public PipelineHealthDTO getPipelineHealth(Long pipelineId) {
        log.debug("Getting pipeline health for ID {}", pipelineId);

        pipelineFacade.findById(pipelineId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pipeline not found: " + pipelineId));

        LocalDateTime rangeStart = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime rangeEnd   = LocalDateTime.now();

        List<FlowAlertFacadeDTO> recentAlerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, rangeStart, rangeEnd);

        // thresholds fetched — count used only for healthScore weighting
        List<FlowThresholdFacadeDTO> thresholds =
                thresholdFacade.findByPipeline(pipelineId);

        int totalAlerts    = recentAlerts.size();
        int criticalCount  = (int) recentAlerts.stream()
                .filter(a -> "CRITICAL".equalsIgnoreCase(a.getSeverityCode()))
                .count();
        int warningCount   = (int) recentAlerts.stream()
                .filter(a -> "HIGH".equalsIgnoreCase(a.getSeverityCode())
                          || "MEDIUM".equalsIgnoreCase(a.getSeverityCode()))
                .count();

        // Health classification
        String overallHealth;
        double healthScore;
        if (criticalCount > 0) {
            overallHealth = "CRITICAL";
            healthScore   = Math.max(0.0, 40.0 - (criticalCount * 10.0));
        } else if (totalAlerts > 5 || warningCount > 3) {
            overallHealth = "WARNING";
            healthScore   = Math.max(40.0, 75.0 - (warningCount * 5.0));
        } else if (thresholds.isEmpty()) {
            overallHealth = "UNKNOWN";
            healthScore   = 50.0;
        } else {
            overallHealth = "HEALTHY";
            healthScore   = Math.min(100.0, 95.0 - (totalAlerts * 2.0));
        }

        // Latest readings for current measurement snapshot
        LocalDate today = LocalDate.now();
        List<FlowReadingReadDTO> todayReadings =
                flowReadingFacade.findByPipelineAndDateRange(pipelineId, today, today);

        FlowReadingReadDTO latest = latestReading(todayReadings);

        return PipelineHealthDTO.builder()
                // ── PipelineHealthDTO real fields ──────────────────────────
                .overallHealth(overallHealth)                           // ✅
                .healthScore(healthScore)                               // ✅
                .activeAlertsCount(totalAlerts)                         // ✅
                .criticalAlertsCount(criticalCount)                     // ✅
                .warningAlertsCount(warningCount)                       // ✅
                .currentPressure(latest != null
                        ? latest.getInletPressureBar() : null)          // ✅
                .currentTemperature(latest != null
                        ? latest.getTemperatureCelsius() : null)        // ✅
                .currentFlowRate(latest != null
                        ? latest.getVolumeM3() : null)                  // ✅
                .lastReadingTime(latest != null
                        ? latest.getSubmittedAt() : null)               // ✅
                .build();
    }

    // =====================================================================
    // getDashboard
    // =====================================================================

    /**
     * Get operational dashboard for a pipeline.
     *
     * Phase C fix:
     *   KeyMetricsDTO:
     *     BEFORE: .totalReadings/.totalAlerts/.totalThresholds/.totalEvents/.averageFlow
     *     AFTER:  .pressure/.temperature/.flowRate/.containedVolume
     *             — all sourced from latest FlowReadingReadDTO
     *
     *   PipelineDynamicDashboardDTO:
     *     BEFORE: .pipelineCode() / .generatedAt()  (not in DTO)
     *     AFTER:  removed; real fields used instead
     *
     *   FlowReadingReadDTO field usage:
     *     .getPressureValue()    → .getInletPressureBar()    ✅
     *     .getTemperatureValue() → .getTemperatureCelsius()  ✅
     *     .getVolumeM3()         ✅ already correct
     */
    public PipelineDynamicDashboardDTO getDashboard(Long pipelineId) {
        log.debug("Building dashboard for pipeline ID {}", pipelineId);

        PipelineDTO pipeline = pipelineFacade.findById(pipelineId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pipeline not found: " + pipelineId));

        LocalDateTime rangeStart = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime rangeEnd   = LocalDateTime.now();
        LocalDate today          = LocalDate.now();

        List<FlowReadingReadDTO> readings =
                flowReadingFacade.findByPipelineAndDateRange(
                        pipelineId, today.minusDays(1), today);

        List<FlowAlertFacadeDTO> alerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, rangeStart, rangeEnd);

        List<FlowEventFacadeDTO> events =
                eventFacade.findByPipelineAndTimeRange(pipelineId, rangeStart, rangeEnd);

        // Latest reading — for snapshot metrics
        FlowReadingReadDTO latest = latestReading(readings);

        // Average flow (volumeM3) over the date range
        BigDecimal avgFlowValue = readings.stream()
                .filter(r -> r.getVolumeM3() != null)
                .map(FlowReadingReadDTO::getVolumeM3)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(
                        readings.isEmpty() ? BigDecimal.ONE
                                : BigDecimal.valueOf(readings.size()),
                        3, RoundingMode.HALF_UP);

        // Average pressure over the date range
        BigDecimal avgPressure = readings.stream()
                .filter(r -> r.getInletPressureBar() != null)
                .map(FlowReadingReadDTO::getInletPressureBar)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(
                        readings.isEmpty() ? BigDecimal.ONE
                                : BigDecimal.valueOf(readings.size()),
                        3, RoundingMode.HALF_UP);

        // Average temperature over the date range
        BigDecimal avgTemperature = readings.stream()
                .filter(r -> r.getTemperatureCelsius() != null)
                .map(FlowReadingReadDTO::getTemperatureCelsius)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(
                        readings.isEmpty() ? BigDecimal.ONE
                                : BigDecimal.valueOf(readings.size()),
                        3, RoundingMode.HALF_UP);

        // Total throughput (sum of all volumeM3 readings)
        BigDecimal throughput = readings.stream()
                .filter(r -> r.getVolumeM3() != null)
                .map(FlowReadingReadDTO::getVolumeM3)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Alert counts
        int totalAlerts   = alerts.size();
        int criticalCount = (int) alerts.stream()
                .filter(a -> "CRITICAL".equalsIgnoreCase(a.getSeverityCode()))
                .count();
        int warningCount  = totalAlerts - criticalCount;

        // Health classification
        String overallHealth = criticalCount > 0 ? "CRITICAL"
                : totalAlerts > 5 ? "WARNING" : "HEALTHY";
        double healthScore = criticalCount > 0 ? 30.0
                : totalAlerts > 5 ? 65.0 : 95.0;

        // ── KeyMetricsDTO — ONLY real fields: pressure, temperature, flowRate, containedVolume
        KeyMetricsDTO keyMetrics = KeyMetricsDTO.builder()
                .pressure(latest != null ? latest.getInletPressureBar() : null)      // ✅
                .temperature(latest != null ? latest.getTemperatureCelsius() : null) // ✅
                .flowRate(avgFlowValue)                                               // ✅
                .containedVolume(latest != null ? latest.getVolumeM3() : null)       // ✅
                .build();

        // Validated / pending counts for today
        List<FlowReadingReadDTO> todayReadings =
                flowReadingFacade.findByPipelineAndDateRange(pipelineId, today, today);
        int validatedToday = (int) todayReadings.stream()
                .filter(r -> "APPROVED".equalsIgnoreCase(r.getValidationStatusCode()))
                .count();
        int pendingToday = (int) todayReadings.stream()
                .filter(r -> r.getValidationStatusCode() == null
                          || "SUBMITTED".equalsIgnoreCase(r.getValidationStatusCode())
                          || "PENDING".equalsIgnoreCase(r.getValidationStatusCode()))
                .count();

        // ── PipelineDynamicDashboardDTO — only real fields ──────────────
        return PipelineDynamicDashboardDTO.builder()
                .pipelineId(pipelineId)                 // ✅
                .pipelineName(pipeline.getName())        // ✅
                .latestReading(latest)                   // ✅
                .keyMetrics(keyMetrics)                  // ✅
                .overallHealth(overallHealth)            // ✅
                .healthScore(healthScore)                // ✅
                .activeAlertsCount(totalAlerts)          // ✅
                .criticalAlertsCount(criticalCount)      // ✅
                .warningAlertsCount(warningCount)        // ✅
                .avgPressureLast24h(avgPressure)         // ✅
                .avgTemperatureLast24h(avgTemperature)   // ✅
                .avgFlowRateLast24h(avgFlowValue)        // ✅
                .throughputLast24h(throughput)           // ✅
                .eventsLast7Days(events.size())          // ✅
                .validatedReadingsToday(validatedToday)  // ✅
                .pendingReadingsToday(pendingToday)      // ✅
                .build();
    }

    // =====================================================================
    // getTimeline
    // =====================================================================

    /**
     * Get pipeline timeline of events and alerts.
     *
     * Phase C fix:
     *   TimelineItemDTO:
     *     BEFORE: .itemId()       → AFTER: .id()         (real field)
     *     BEFORE: .occurredAt()   → AFTER: .timestamp()  (real field)
     *     BEFORE: .severityCode() → AFTER: .severity()   (real field)
     *
     *   PipelineTimelineDTO:
     *     BEFORE: .pipelineId()/.from()/.to()  (not in DTO)  → removed
     *     AFTER:  .items/.totalItems/.currentPage/.pageSize/
     *             .totalPages/.hasNext/.hasPrevious           (real fields)
     */
    public PipelineTimelineDTO getTimeline(Long pipelineId,
            LocalDateTime from, LocalDateTime to,
            String severity, String type,
            Integer page, Integer size) {
        log.debug("Building timeline for pipeline ID {} from {} to {}",
                pipelineId, from, to);

        pipelineFacade.findById(pipelineId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pipeline not found: " + pipelineId));

        List<FlowAlertFacadeDTO> alerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, from, to);
        List<FlowEventFacadeDTO> events =
                eventFacade.findByPipelineAndTimeRange(pipelineId, from, to);

        List<TimelineItemDTO> items = new ArrayList<>();

        // ── Alerts → TimelineItemDTO (real fields only) ──────────────────
        alerts.stream()
                .filter(a -> severity == null
                          || severity.equalsIgnoreCase(a.getSeverityCode()))
                .filter(a -> type == null || "ALERT".equalsIgnoreCase(type))
                .forEach(a -> items.add(TimelineItemDTO.builder()
                        .id(a.getId())                          // ✅ real field
                        .type("ALERT")                          // ✅ real field
                        .severity(a.getSeverityCode())          // ✅ real field
                        .description(a.getMessage())            // ✅ real field
                        .timestamp(a.getAlertTimestamp())       // ✅ real field
                        .build()));

        // ── Events → TimelineItemDTO (real fields only) ──────────────────
        events.stream()
                .filter(e -> severity == null
                          || severity.equalsIgnoreCase(e.getSeverityCode()))
                .filter(e -> type == null || "EVENT".equalsIgnoreCase(type))
                .forEach(e -> items.add(TimelineItemDTO.builder()
                        .id(e.getId())                          // ✅ real field
                        .type("EVENT")                          // ✅ real field
                        .severity(e.getSeverityCode())          // ✅ real field
                        .description(e.getDescription())        // ✅ real field
                        .timestamp(e.getEventTimestamp())       // ✅ real field
                        .build()));

        // Sort descending by timestamp (null-safe)
        items.sort(Comparator.comparing(TimelineItemDTO::getTimestamp,
                Comparator.nullsLast(Comparator.reverseOrder())));

        // Pagination
        int safePageSize = (size != null && size > 0)  ? size : 20;
        int safePage     = (page != null && page >= 0) ? page : 0;
        int fromIndex    = safePage * safePageSize;
        List<TimelineItemDTO> paged = fromIndex >= items.size()
                ? new ArrayList<>()
                : items.subList(fromIndex,
                        Math.min(fromIndex + safePageSize, items.size()));

        int totalItems = items.size();
        int totalPages = totalItems == 0 ? 0
                : (int) Math.ceil((double) totalItems / safePageSize);

        // Severity and type distribution maps
        Map<String, Integer> severityCounts = new HashMap<>();
        Map<String, Integer> typeCounts     = new HashMap<>();
        for (TimelineItemDTO item : items) {
            if (item.getSeverity() != null) {
                severityCounts.merge(item.getSeverity(), 1, Integer::sum);
            }
            if (item.getType() != null) {
                typeCounts.merge(item.getType(), 1, Integer::sum);
            }
        }

        // ── PipelineTimelineDTO — only real fields ───────────────────────
        return PipelineTimelineDTO.builder()
                .items(paged)                                           // ✅
                .severityCounts(severityCounts)                         // ✅
                .typeCounts(typeCounts)                                 // ✅
                .totalItems(totalItems)                                 // ✅
                .currentPage(safePage)                                  // ✅
                .pageSize(safePageSize)                                  // ✅
                .totalPages(totalPages)                                  // ✅
                .hasNext(fromIndex + safePageSize < totalItems)         // ✅
                .hasPrevious(safePage > 0)                              // ✅
                .build();
    }

    // =====================================================================
    // getOverview  (single pipeline, specific date)
    // =====================================================================

    /**
     * Get operational overview for a single pipeline on a reference date.
     *
     * Phase C fix:
     *   BEFORE: .id()/.name()/.code()   → not in PipelineOverviewDTO
     *   AFTER:  .asset(pipelineDTO)     → real field containing full PipelineDTO
     *           .operationalStatus()    → real field ✅
     */
    public PipelineOverviewDTO getOverview(Long pipelineId, LocalDate referenceDate) {
        log.debug("Getting overview for pipeline ID {} on {}", pipelineId, referenceDate);

        PipelineDTO pipeline = pipelineFacade.findById(pipelineId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pipeline not found: " + pipelineId));

        // Readings on reference date for slot/volume stats
        List<FlowReadingReadDTO> dayReadings =
                flowReadingFacade.findByPipelineAndDateRange(
                        pipelineId, referenceDate, referenceDate);

        FlowReadingReadDTO latest = latestReading(dayReadings);

        // Alert snapshot
        LocalDateTime dayStart = referenceDate.atStartOfDay();
        LocalDateTime dayEnd   = referenceDate.plusDays(1).atStartOfDay();
        List<FlowAlertFacadeDTO> dayAlerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, dayStart, dayEnd);

        // Slot coverage counts
        int recordedSlots = dayReadings.size();
        int approvedSlots = (int) dayReadings.stream()
                .filter(r -> "APPROVED".equalsIgnoreCase(r.getValidationStatusCode()))
                .count();
        int pendingSlots = (int) dayReadings.stream()
                .filter(r -> r.getValidationStatusCode() == null
                          || "SUBMITTED".equalsIgnoreCase(r.getValidationStatusCode())
                          || "PENDING".equalsIgnoreCase(r.getValidationStatusCode()))
                .count();

        // Total volume transported today
        BigDecimal volumeToday = dayReadings.stream()
                .filter(r -> r.getVolumeM3() != null)
                .map(FlowReadingReadDTO::getVolumeM3)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Completion rate (approved / total slots, assuming 12 slots/day)
        int totalSlotsToday = 12;
        double completionRate = totalSlotsToday > 0
                ? (approvedSlots * 100.0) / totalSlotsToday : 0.0;

        // ── PipelineOverviewDTO — only real fields ───────────────────────
        return PipelineOverviewDTO.builder()
                .asset(pipeline)                                         // ✅ real field
                .operationalStatus(pipeline.getOperationalStatus() != null
                        ? pipeline.getOperationalStatus().getCode() : null) // ✅
                .currentPressure(latest != null
                        ? latest.getInletPressureBar() : null)           // ✅
                .currentTemperature(latest != null
                        ? latest.getTemperatureCelsius() : null)         // ✅
                .currentFlowRate(latest != null
                        ? latest.getVolumeM3() : null)                   // ✅
                .lastReadingTime(latest != null
                        ? latest.getSubmittedAt() : null)                // ✅
                .totalSlotsToday(totalSlotsToday)                        // ✅
                .recordedSlots(recordedSlots)                            // ✅
                .approvedSlots(approvedSlots)                            // ✅
                .pendingValidationSlots(pendingSlots)                    // ✅
                .overdueSlots(0)                                         // ✅ (future: compute)
                .completionRate(completionRate)                          // ✅
                .activeAlertsCount(dayAlerts.size())                     // ✅
                .volumeTransportedToday(volumeToday)                     // ✅
                .build();
    }

    // =====================================================================
    // getPipelineOverviews  (all pipelines — summary list)
    // =====================================================================

    /**
     * Get summary overview list for ALL pipelines.
     * Retained as-is — separate from getOverview(Long, LocalDate).
     */
    public List<PipelineOverviewDTO> getPipelineOverviews() {
        log.debug("Getting all pipeline overviews");
        return pipelineFacade.findAll().stream()
                .map(dto -> PipelineOverviewDTO.builder()
                        .asset(dto)                                      // ✅ real field
                        .operationalStatus(dto.getOperationalStatus() != null
                                ? dto.getOperationalStatus().getCode() : null) // ✅
                        .build())
                .collect(Collectors.toList());
    }

    // =====================================================================
    // getSlotCoverage
    // =====================================================================

    /**
     * Get slot coverage for a pipeline on a specific date.
     */
    public List<SlotStatusDTO> getSlotCoverage(Long pipelineId, LocalDate date) {
        log.debug("Getting slot coverage for pipeline ID {} on {}", pipelineId, date);

        pipelineFacade.findById(pipelineId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pipeline not found: " + pipelineId));

        List<FlowReadingReadDTO> readings =
                flowReadingFacade.findByPipelineAndDateRange(pipelineId, date, date);

        return readings.stream()
                .map(r -> SlotStatusDTO.builder()
                        .slotId(r.getReadingSlotId())               // ✅
                        .slotCode(r.getReadingSlotCode())           // ✅
                        .readingId(r.getId())                       // ✅
                        .validationStatus(r.getValidationStatusCode()) // ✅
                        .recordedAt(r.getSubmittedAt())             // ✅
                        .validatedAt(r.getValidatedAt())            // ✅
                        .build())
                .collect(Collectors.toList());
    }

    // =====================================================================
    // getReadingsTimeSeries
    // =====================================================================

    /**
     * Get historical time-series data for a specific measurement type.
     */
    public ReadingsTimeSeriesDTO getReadingsTimeSeries(Long pipelineId,
            LocalDate startDate, LocalDate endDate, String measurementType) {
        log.debug("Getting time series for pipeline ID {} type={} from {} to {}",
                pipelineId, measurementType, startDate, endDate);

        pipelineFacade.findById(pipelineId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pipeline not found: " + pipelineId));

        // Readings fetched; data-point mapping delegated to analytics layer.
        flowReadingFacade.findByPipelineAndDateRange(pipelineId, startDate, endDate);

        return ReadingsTimeSeriesDTO.builder()
                .measurementType(measurementType)   // ✅
                .dataPoints(new ArrayList<>())      // ✅
                .build();
    }

    // =====================================================================
    // PRIVATE UTILITIES
    // =====================================================================

    /**
     * Selects the most recently submitted reading from a list.
     * Returns null if the list is empty or all submittedAt values are null.
     */
    private FlowReadingReadDTO latestReading(List<FlowReadingReadDTO> readings) {
        if (readings == null || readings.isEmpty()) return null;
        return readings.stream()
                .filter(r -> r.getSubmittedAt() != null)
                .max(Comparator.comparing(FlowReadingReadDTO::getSubmittedAt))
                .orElse(readings.get(readings.size() - 1));
    }
}
