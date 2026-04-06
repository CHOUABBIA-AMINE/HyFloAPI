/**
 *
 * 	@Author		: MEDJERAB Abir, CHOUABBIA Amine
 *
 * 	@Name		: PipelineIntelligenceService
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 03-26-2026 - Phase B/C: Align ALL builder calls to real DTO field contracts.
 * 	@UpdatedOn	: 03-28-2026 - fix: Replace all facade.impl.I* imports with correct
 * 	                            facade interface packages:
 * 	                              IPipelineFacade      → facade.PipelineFacade
 * 	                              IFlowReadingFacade   → facade.FlowReadingFacade
 * 	                              IFlowAlertFacade     → facade.FlowAlertFacade
 * 	                              IFlowEventFacade     → flow.core.facade.FlowEventFacade
 * 	                              IFlowThresholdFacade → flow.common.facade.FlowThresholdFacade
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.intelligence.service;

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

import dz.sh.trc.hyflo.flow.common.facade.FlowThresholdFacade;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.facade.FlowEventFacade;
import dz.sh.trc.hyflo.intelligence.dto.KeyMetricsDTO;
import dz.sh.trc.hyflo.intelligence.dto.PipelineDynamicDashboardDTO;
import dz.sh.trc.hyflo.intelligence.dto.PipelineHealthDTO;
import dz.sh.trc.hyflo.intelligence.dto.PipelineInfoDTO;
import dz.sh.trc.hyflo.intelligence.dto.PipelineOverviewDTO;
import dz.sh.trc.hyflo.intelligence.dto.PipelineTimelineDTO;
import dz.sh.trc.hyflo.intelligence.dto.ReadingsTimeSeriesDTO;
import dz.sh.trc.hyflo.intelligence.dto.SlotStatusDTO;
import dz.sh.trc.hyflo.intelligence.dto.TimelineItemDTO;
import dz.sh.trc.hyflo.intelligence.dto.facade.FlowAlertFacadeDTO;
import dz.sh.trc.hyflo.intelligence.dto.facade.FlowEventFacadeDTO;
import dz.sh.trc.hyflo.intelligence.dto.facade.FlowThresholdFacadeDTO;
import dz.sh.trc.hyflo.intelligence.facade.FlowAlertFacade;
import dz.sh.trc.hyflo.intelligence.facade.FlowReadingFacade;
import dz.sh.trc.hyflo.intelligence.facade.PipelineFacade;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceService {

    // =====================================================================
    // DEPENDENCIES — corrected facade interface types
    // =====================================================================

    private final PipelineFacade      pipelineFacade;    // was IPipelineFacade      (facade.impl)
    private final FlowReadingFacade   flowReadingFacade; // was IFlowReadingFacade   (facade.impl)
    private final FlowThresholdFacade thresholdFacade;   // was IFlowThresholdFacade (facade.impl)
    private final FlowAlertFacade     alertFacade;       // was IFlowAlertFacade     (facade.impl)
    private final FlowEventFacade     eventFacade;       // was IFlowEventFacade     (facade.impl)

    // =====================================================================
    // getPipelineInfo
    // =====================================================================

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

    public PipelineHealthDTO getPipelineHealth(Long pipelineId) {
        log.debug("Getting pipeline health for ID {}", pipelineId);

        pipelineFacade.findById(pipelineId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pipeline not found: " + pipelineId));

        LocalDateTime rangeStart = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime rangeEnd   = LocalDateTime.now();

        List<FlowAlertFacadeDTO> recentAlerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, rangeStart, rangeEnd);

        List<FlowThresholdFacadeDTO> thresholds =
                thresholdFacade.findByPipeline(pipelineId);

        int totalAlerts   = recentAlerts.size();
        int criticalCount = (int) recentAlerts.stream()
                .filter(a -> "CRITICAL".equalsIgnoreCase(a.getSeverityCode())).count();
        int warningCount  = (int) recentAlerts.stream()
                .filter(a -> "HIGH".equalsIgnoreCase(a.getSeverityCode())
                          || "MEDIUM".equalsIgnoreCase(a.getSeverityCode())).count();

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

        LocalDate today = LocalDate.now();
        List<FlowReadingReadDTO> todayReadings =
                flowReadingFacade.findByPipelineAndDateRange(pipelineId, today, today);
        FlowReadingReadDTO latest = latestReading(todayReadings);

        return PipelineHealthDTO.builder()
                .overallHealth(overallHealth)
                .healthScore(healthScore)
                .activeAlertsCount(totalAlerts)
                .criticalAlertsCount(criticalCount)
                .warningAlertsCount(warningCount)
                .currentPressure(latest != null ? latest.getInletPressureBar() : null)
                .currentTemperature(latest != null ? latest.getTemperatureCelsius() : null)
                .currentFlowRate(latest != null ? latest.getVolumeM3() : null)
                .lastReadingTime(latest != null ? latest.getSubmittedAt() : null)
                .build();
    }

    // =====================================================================
    // getDashboard
    // =====================================================================

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

        FlowReadingReadDTO latest = latestReading(readings);

        BigDecimal avgFlowValue = readings.stream()
                .filter(r -> r.getVolumeM3() != null).map(FlowReadingReadDTO::getVolumeM3)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(readings.isEmpty() ? BigDecimal.ONE : BigDecimal.valueOf(readings.size()),
                        3, RoundingMode.HALF_UP);

        BigDecimal avgPressure = readings.stream()
                .filter(r -> r.getInletPressureBar() != null).map(FlowReadingReadDTO::getInletPressureBar)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(readings.isEmpty() ? BigDecimal.ONE : BigDecimal.valueOf(readings.size()),
                        3, RoundingMode.HALF_UP);

        BigDecimal avgTemperature = readings.stream()
                .filter(r -> r.getTemperatureCelsius() != null).map(FlowReadingReadDTO::getTemperatureCelsius)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(readings.isEmpty() ? BigDecimal.ONE : BigDecimal.valueOf(readings.size()),
                        3, RoundingMode.HALF_UP);

        BigDecimal throughput = readings.stream()
                .filter(r -> r.getVolumeM3() != null).map(FlowReadingReadDTO::getVolumeM3)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalAlerts   = alerts.size();
        int criticalCount = (int) alerts.stream()
                .filter(a -> "CRITICAL".equalsIgnoreCase(a.getSeverityCode())).count();
        int warningCount  = totalAlerts - criticalCount;
        String overallHealth = criticalCount > 0 ? "CRITICAL" : totalAlerts > 5 ? "WARNING" : "HEALTHY";
        double healthScore   = criticalCount > 0 ? 30.0 : totalAlerts > 5 ? 65.0 : 95.0;

        KeyMetricsDTO keyMetrics = KeyMetricsDTO.builder()
                .pressure(latest != null ? latest.getInletPressureBar() : null)
                .temperature(latest != null ? latest.getTemperatureCelsius() : null)
                .flowRate(avgFlowValue)
                .containedVolume(latest != null ? latest.getVolumeM3() : null)
                .build();

        List<FlowReadingReadDTO> todayReadings =
                flowReadingFacade.findByPipelineAndDateRange(pipelineId, today, today);
        int validatedToday = (int) todayReadings.stream()
                .filter(r -> "APPROVED".equalsIgnoreCase(r.getValidationStatusCode())).count();
        int pendingToday = (int) todayReadings.stream()
                .filter(r -> r.getValidationStatusCode() == null
                          || "SUBMITTED".equalsIgnoreCase(r.getValidationStatusCode())
                          || "PENDING".equalsIgnoreCase(r.getValidationStatusCode())).count();

        return PipelineDynamicDashboardDTO.builder()
                .pipelineId(pipelineId)
                .pipelineName(pipeline.getName())
                .latestReading(latest)
                .keyMetrics(keyMetrics)
                .overallHealth(overallHealth)
                .healthScore(healthScore)
                .activeAlertsCount(totalAlerts)
                .criticalAlertsCount(criticalCount)
                .warningAlertsCount(warningCount)
                .avgPressureLast24h(avgPressure)
                .avgTemperatureLast24h(avgTemperature)
                .avgFlowRateLast24h(avgFlowValue)
                .throughputLast24h(throughput)
                .eventsLast7Days(events.size())
                .validatedReadingsToday(validatedToday)
                .pendingReadingsToday(pendingToday)
                .build();
    }

    // =====================================================================
    // getTimeline
    // =====================================================================

    public PipelineTimelineDTO getTimeline(Long pipelineId,
            LocalDateTime from, LocalDateTime to,
            String severity, String type,
            Integer page, Integer size) {
        log.debug("Building timeline for pipeline ID {} from {} to {}", pipelineId, from, to);

        pipelineFacade.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));

        List<FlowAlertFacadeDTO> alerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, from, to);
        List<FlowEventFacadeDTO> events =
                eventFacade.findByPipelineAndTimeRange(pipelineId, from, to);

        List<TimelineItemDTO> items = new ArrayList<>();

        alerts.stream()
                .filter(a -> severity == null || severity.equalsIgnoreCase(a.getSeverityCode()))
                .filter(a -> type == null || "ALERT".equalsIgnoreCase(type))
                .forEach(a -> items.add(TimelineItemDTO.builder()
                        .id(a.getId()).type("ALERT").severity(a.getSeverityCode())
                        .description(a.getMessage()).timestamp(a.getAlertTimestamp()).build()));

        events.stream()
                .filter(e -> severity == null || severity.equalsIgnoreCase(e.getSeverityCode()))
                .filter(e -> type == null || "EVENT".equalsIgnoreCase(type))
                .forEach(e -> items.add(TimelineItemDTO.builder()
                        .id(e.getId()).type("EVENT").severity(e.getSeverityCode())
                        .description(e.getDescription()).timestamp(e.getEventTimestamp()).build()));

        items.sort(Comparator.comparing(TimelineItemDTO::getTimestamp,
                Comparator.nullsLast(Comparator.reverseOrder())));

        int safePageSize = (size != null && size > 0)  ? size : 20;
        int safePage     = (page != null && page >= 0) ? page : 0;
        int fromIndex    = safePage * safePageSize;
        List<TimelineItemDTO> paged = fromIndex >= items.size() ? new ArrayList<>()
                : items.subList(fromIndex, Math.min(fromIndex + safePageSize, items.size()));

        int totalItems = items.size();
        int totalPages = totalItems == 0 ? 0 : (int) Math.ceil((double) totalItems / safePageSize);

        Map<String, Integer> severityCounts = new HashMap<>();
        Map<String, Integer> typeCounts     = new HashMap<>();
        for (TimelineItemDTO item : items) {
            if (item.getSeverity() != null) severityCounts.merge(item.getSeverity(), 1, Integer::sum);
            if (item.getType()     != null) typeCounts.merge(item.getType(), 1, Integer::sum);
        }

        return PipelineTimelineDTO.builder()
                .items(paged).severityCounts(severityCounts).typeCounts(typeCounts)
                .totalItems(totalItems).currentPage(safePage).pageSize(safePageSize)
                .totalPages(totalPages)
                .hasNext(fromIndex + safePageSize < totalItems)
                .hasPrevious(safePage > 0)
                .build();
    }

    // =====================================================================
    // getOverview
    // =====================================================================

    public PipelineOverviewDTO getOverview(Long pipelineId, LocalDate referenceDate) {
        log.debug("Getting overview for pipeline ID {} on {}", pipelineId, referenceDate);

        PipelineDTO pipeline = pipelineFacade.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));

        List<FlowReadingReadDTO> dayReadings =
                flowReadingFacade.findByPipelineAndDateRange(pipelineId, referenceDate, referenceDate);
        FlowReadingReadDTO latest = latestReading(dayReadings);

        LocalDateTime dayStart = referenceDate.atStartOfDay();
        LocalDateTime dayEnd   = referenceDate.plusDays(1).atStartOfDay();
        List<FlowAlertFacadeDTO> dayAlerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, dayStart, dayEnd);

        int recordedSlots = dayReadings.size();
        int approvedSlots = (int) dayReadings.stream()
                .filter(r -> "APPROVED".equalsIgnoreCase(r.getValidationStatusCode())).count();
        int pendingSlots  = (int) dayReadings.stream()
                .filter(r -> r.getValidationStatusCode() == null
                          || "SUBMITTED".equalsIgnoreCase(r.getValidationStatusCode())
                          || "PENDING".equalsIgnoreCase(r.getValidationStatusCode())).count();

        BigDecimal volumeToday = dayReadings.stream()
                .filter(r -> r.getVolumeM3() != null).map(FlowReadingReadDTO::getVolumeM3)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalSlotsToday = 12;
        double completionRate = (approvedSlots * 100.0) / totalSlotsToday;

        return PipelineOverviewDTO.builder()
                .asset(pipeline)
                .operationalStatus(pipeline.getOperationalStatus() != null
                        ? pipeline.getOperationalStatus().getCode() : null)
                .currentPressure(latest != null ? latest.getInletPressureBar() : null)
                .currentTemperature(latest != null ? latest.getTemperatureCelsius() : null)
                .currentFlowRate(latest != null ? latest.getVolumeM3() : null)
                .lastReadingTime(latest != null ? latest.getSubmittedAt() : null)
                .totalSlotsToday(totalSlotsToday).recordedSlots(recordedSlots)
                .approvedSlots(approvedSlots).pendingValidationSlots(pendingSlots)
                .overdueSlots(0).completionRate(completionRate)
                .activeAlertsCount(dayAlerts.size())
                .volumeTransportedToday(volumeToday)
                .build();
    }

    // =====================================================================
    // getPipelineOverviews
    // =====================================================================

    public List<PipelineOverviewDTO> getPipelineOverviews() {
        log.debug("Getting all pipeline overviews");
        return pipelineFacade.findAll().stream()
                .map(dto -> PipelineOverviewDTO.builder()
                        .asset(dto)
                        .operationalStatus(dto.getOperationalStatus() != null
                                ? dto.getOperationalStatus().getCode() : null)
                        .build())
                .collect(Collectors.toList());
    }

    // =====================================================================
    // getSlotCoverage
    // =====================================================================

    public List<SlotStatusDTO> getSlotCoverage(Long pipelineId, LocalDate date) {
        log.debug("Getting slot coverage for pipeline ID {} on {}", pipelineId, date);
        pipelineFacade.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        return flowReadingFacade.findByPipelineAndDateRange(pipelineId, date, date).stream()
                .map(r -> SlotStatusDTO.builder()
                        .slotId(r.getReadingSlotId()).slotCode(r.getReadingSlotCode())
                        .readingId(r.getId()).validationStatus(r.getValidationStatusCode())
                        .recordedAt(r.getSubmittedAt()).validatedAt(r.getValidatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // =====================================================================
    // getReadingsTimeSeries
    // =====================================================================

    public ReadingsTimeSeriesDTO getReadingsTimeSeries(Long pipelineId,
            LocalDate startDate, LocalDate endDate, String measurementType) {
        log.debug("Getting time series for pipeline ID {} type={} from {} to {}",
                pipelineId, measurementType, startDate, endDate);
        pipelineFacade.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        flowReadingFacade.findByPipelineAndDateRange(pipelineId, startDate, endDate);
        return ReadingsTimeSeriesDTO.builder()
                .measurementType(measurementType)
                .dataPoints(new ArrayList<>())
                .build();
    }

    // =====================================================================
    // PRIVATE UTILITIES
    // =====================================================================

    private FlowReadingReadDTO latestReading(List<FlowReadingReadDTO> readings) {
        if (readings == null || readings.isEmpty()) return null;
        return readings.stream()
                .filter(r -> r.getSubmittedAt() != null)
                .max(Comparator.comparing(FlowReadingReadDTO::getSubmittedAt))
                .orElse(readings.get(readings.size() - 1));
    }
}
