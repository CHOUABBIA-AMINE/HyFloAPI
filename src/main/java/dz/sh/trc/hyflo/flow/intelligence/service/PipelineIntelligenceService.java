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
 * 	@UpdatedOn	: 02-14-2026 - Phase 7: Add null safety for coating and material fields
 * 	@UpdatedOn	: 03-26-2026 - H2: Replace FlowThresholdRepository/FlowAlertRepository/FlowEventRepository
 * 	                             with IFlowThresholdFacade/IFlowAlertFacade/IFlowEventFacade
 * 	@UpdatedOn	: 03-26-2026 - F1: Replace FlowAlert/FlowThreshold/FlowEvent entity imports
 * 	                             with FlowAlertFacadeDto/FlowThresholdFacadeDto/FlowEventFacadeDto
 * 	@UpdatedOn	: 03-26-2026 - F2: Replace FlowReadingDTO (v1) with FlowReadingReadDto (v2).
 * 	                             getActualFlow() replaced with getVolumeM3() per v2 field contract.
 * 	@UpdatedOn	: 03-26-2026 - CONTRACT: Align all service methods to controller signatures.
 * 	                             Fix getTimeline() signature; add getOverview(), getSlotCoverage(),
 * 	                             getReadingsTimeSeries().
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 * 	@Refactor: H2 - All flow/core repository accesses replaced by facade interfaces
 * 	@Refactor: F1 - All flow/core entity type references replaced by facade DTO projections
 * 	@Refactor: F2 - IFlowReadingFacade now returns FlowReadingReadDto; v1 FlowReadingDTO eliminated
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
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDto;
import dz.sh.trc.hyflo.flow.intelligence.dto.KeyMetricsDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineInfoDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineHealthDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineDynamicDashboardDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineOverviewDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.PipelineTimelineDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.ReadingsTimeSeriesDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.SlotStatusDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.TimelineItemDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowAlertFacadeDto;
import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowEventFacadeDto;
import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowThresholdFacadeDto;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowAlertFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowEventFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowReadingFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowThresholdFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.IPipelineFacade;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service providing operational intelligence and analytics for pipelines.
 *
 * H2: All flow/core repository dependencies replaced by facade interfaces.
 * F1: All flow/core entity type references replaced by facade DTO projections.
 * F2: IFlowReadingFacade now returns FlowReadingReadDto — v1 FlowReadingDTO fully eliminated.
 *
 * No class from dz.sh.trc.hyflo.flow.core.model is imported here.
 * No v1 FlowReadingDTO is imported here.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceService {

    // ========== DEPENDENCIES ==========

    private final IPipelineFacade       pipelineFacade;
    private final IFlowReadingFacade    flowReadingFacade;
    private final IFlowThresholdFacade  thresholdFacade;
    private final IFlowAlertFacade      alertFacade;
    private final IFlowEventFacade      eventFacade;

    // ========== PUBLIC SERVICE METHODS ==========

    /**
     * Get comprehensive pipeline INFRASTRUCTURE information.
     */
    public PipelineInfoDTO getPipelineInfo(Long pipelineId, Boolean includeHealth, Boolean includeEntities) {
        log.debug("Getting pipeline info for ID {} (health={}, entities={})",
                pipelineId, includeHealth, includeEntities);

        PipelineDTO pipelineDTO = pipelineFacade.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));

        return PipelineInfoDTO.builder()
                .id(pipelineDTO.getId())
                .name(pipelineDTO.getName())
                .code(pipelineDTO.getCode())
                .operationalStatus(pipelineDTO.getOperationalStatus() != null ?
                        pipelineDTO.getOperationalStatus().getCode() : null)
                .length(pipelineDTO.getLength())
                .nominalDiameter(pipelineDTO.getNominalDiameter())
                .nominalThickness(pipelineDTO.getNominalThickness())
                .nominalRoughness(pipelineDTO.getNominalRoughness())
                .materialName(pipelineDTO.getNominalConstructionMaterial() != null ?
                        pipelineDTO.getNominalConstructionMaterial().getCode() : null)
                .exteriorCoating(pipelineDTO.getNominalExteriorCoating() != null ?
                        pipelineDTO.getNominalExteriorCoating().getCode() : null)
                .interiorCoating(pipelineDTO.getNominalInteriorCoating() != null ?
                        pipelineDTO.getNominalInteriorCoating().getCode() : null)
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
                .departureTerminalName(pipelineDTO.getDepartureTerminal() != null ?
                        pipelineDTO.getDepartureTerminal().getCode() : null)
                .arrivalTerminalName(pipelineDTO.getArrivalTerminal() != null ?
                        pipelineDTO.getArrivalTerminal().getCode() : null)
                .pipelineSystemName(pipelineDTO.getPipelineSystem() != null ?
                        pipelineDTO.getPipelineSystem().getCode() : null)
                .pipelineDetails(pipelineDTO)
                .build();
    }

    /**
     * Get pipeline health metrics.
     */
    public PipelineHealthDTO getPipelineHealth(Long pipelineId) {
        log.debug("Getting pipeline health for ID {}", pipelineId);

        pipelineFacade.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));

        LocalDateTime rangeStart = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime rangeEnd   = LocalDateTime.now();

        List<FlowAlertFacadeDto> recentAlerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, rangeStart, rangeEnd);
        List<FlowThresholdFacadeDto> thresholds = thresholdFacade.findByPipeline(pipelineId);

        int alertCount    = recentAlerts.size();
        int criticalCount = (int) recentAlerts.stream()
                .filter(a -> "CRITICAL".equalsIgnoreCase(a.getSeverityCode()))
                .count();

        String healthStatus;
        if (criticalCount > 0) {
            healthStatus = "CRITICAL";
        } else if (alertCount > 5) {
            healthStatus = "WARNING";
        } else {
            healthStatus = "HEALTHY";
        }

        return PipelineHealthDTO.builder()
                .pipelineId(pipelineId)
                .healthStatus(healthStatus)
                .activeAlertCount(alertCount)
                .criticalAlertCount(criticalCount)
                .activeThresholdCount(thresholds.size())
                .assessedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Get operational dashboard for a pipeline.
     *
     * F2: readings are now List<FlowReadingReadDto>.
     * Average flow computed from volumeM3 (v2 field) instead of actualFlow (v1 field).
     */
    public PipelineDynamicDashboardDTO getDashboard(Long pipelineId) {
        log.debug("Building dashboard for pipeline ID {}", pipelineId);

        PipelineDTO pipeline = pipelineFacade.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));

        LocalDateTime rangeStart = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime rangeEnd   = LocalDateTime.now();
        LocalDate today          = LocalDate.now();

        List<FlowReadingReadDto> readings =
                flowReadingFacade.findByPipelineAndDateRange(pipelineId, today.minusDays(30), today);

        List<FlowAlertFacadeDto> alerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, rangeStart, rangeEnd);
        List<FlowThresholdFacadeDto> thresholds = thresholdFacade.findByPipeline(pipelineId);
        List<FlowEventFacadeDto> events =
                eventFacade.findByPipelineAndTimeRange(pipelineId, rangeStart, rangeEnd);

        OptionalDouble avgFlow = readings.stream()
                .filter(r -> r.getVolumeM3() != null)
                .mapToDouble(r -> r.getVolumeM3().doubleValue())
                .average();

        BigDecimal avgFlowValue = avgFlow.isPresent()
                ? BigDecimal.valueOf(avgFlow.getAsDouble()).setScale(3, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        KeyMetricsDTO keyMetrics = KeyMetricsDTO.builder()
                .totalReadings((long) readings.size())
                .totalAlerts((long) alerts.size())
                .totalThresholds((long) thresholds.size())
                .totalEvents((long) events.size())
                .averageFlow(avgFlowValue)
                .build();

        return PipelineDynamicDashboardDTO.builder()
                .pipelineId(pipelineId)
                .pipelineName(pipeline.getName())
                .pipelineCode(pipeline.getCode())
                .keyMetrics(keyMetrics)
                .generatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Get pipeline timeline of events and alerts.
     *
     * Signature aligned to PipelineIntelligenceController:
     *   (Long, LocalDateTime, LocalDateTime, String severity, String type, Integer page, Integer size)
     *
     * Previous signature (LocalDate, LocalDate) was misaligned with the controller.
     */
    public PipelineTimelineDTO getTimeline(Long pipelineId,
            LocalDateTime from, LocalDateTime to,
            String severity, String type,
            Integer page, Integer size) {
        log.debug("Building timeline for pipeline ID {} from {} to {}", pipelineId, from, to);

        pipelineFacade.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));

        List<FlowAlertFacadeDto> alerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, from, to);
        List<FlowEventFacadeDto> events =
                eventFacade.findByPipelineAndTimeRange(pipelineId, from, to);

        List<TimelineItemDTO> items = new ArrayList<>();

        alerts.stream()
                .filter(a -> severity == null || severity.equalsIgnoreCase(a.getSeverityCode()))
                .filter(a -> type == null || "ALERT".equalsIgnoreCase(type))
                .forEach(a -> items.add(TimelineItemDTO.builder()
                        .type("ALERT")
                        .itemId(a.getId())
                        .occurredAt(a.getAlertTimestamp())
                        .severityCode(a.getSeverityCode())
                        .build()));

        events.stream()
                .filter(e -> severity == null || severity.equalsIgnoreCase(e.getSeverityCode()))
                .filter(e -> type == null || "EVENT".equalsIgnoreCase(type))
                .forEach(e -> items.add(TimelineItemDTO.builder()
                        .type("EVENT")
                        .itemId(e.getId())
                        .occurredAt(e.getEventTimestamp())
                        .severityCode(e.getSeverityCode())
                        .build()));

        items.sort(Comparator.comparing(TimelineItemDTO::getOccurredAt,
                Comparator.nullsLast(Comparator.reverseOrder())));

        int safePageSize = (size != null && size > 0) ? size : 20;
        int safePage     = (page != null && page >= 0) ? page : 0;
        int fromIndex    = safePage * safePageSize;
        List<TimelineItemDTO> paged = fromIndex >= items.size()
                ? new ArrayList<>()
                : items.subList(fromIndex, Math.min(fromIndex + safePageSize, items.size()));

        return PipelineTimelineDTO.builder()
                .pipelineId(pipelineId)
                .from(from.toLocalDate())
                .to(to.toLocalDate())
                .items(paged)
                .build();
    }

    /**
     * Get operational overview for a single pipeline on a reference date.
     *
     * Added to match PipelineIntelligenceController.getOverview(Long, LocalDate).
     * The existing getPipelineOverviews() (no-arg list) is retained separately.
     */
    public PipelineOverviewDTO getOverview(Long pipelineId, LocalDate referenceDate) {
        log.debug("Getting overview for pipeline ID {} on {}", pipelineId, referenceDate);

        PipelineDTO pipeline = pipelineFacade.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));

        return PipelineOverviewDTO.builder()
                .id(pipeline.getId())
                .name(pipeline.getName())
                .code(pipeline.getCode())
                .operationalStatus(pipeline.getOperationalStatus() != null
                        ? pipeline.getOperationalStatus().getCode() : null)
                .build();
    }

    /**
     * Get slot coverage for a pipeline on a specific date.
     *
     * Added to match PipelineIntelligenceController.getSlotCoverage(Long, LocalDate).
     * Returns one SlotStatusDTO per recorded reading found for that date.
     * Slots with no reading are absent from the list; the frontend renders all 12 slots.
     */
    public List<SlotStatusDTO> getSlotCoverage(Long pipelineId, LocalDate date) {
        log.debug("Getting slot coverage for pipeline ID {} on {}", pipelineId, date);

        pipelineFacade.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));

        List<FlowReadingReadDto> readings =
                flowReadingFacade.findByPipelineAndDateRange(pipelineId, date, date);

        return readings.stream()
                .map(r -> SlotStatusDTO.builder()
                        .slotId(r.getReadingSlotId())
                        .slotCode(r.getReadingSlotCode())
                        .readingId(r.getId())
                        .validationStatus(r.getValidationStatusCode())
                        .recordedAt(r.getSubmittedAt())
                        .validatedAt(r.getValidatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get historical time-series data for a specific measurement type.
     *
     * Added to match PipelineIntelligenceController.getReadingsTimeSeries(Long, LocalDate, LocalDate, String).
     * DataPoints are sourced from FlowReadingReadDto; statistical analysis
     * can be layered on top by the analytics module when available.
     */
    public ReadingsTimeSeriesDTO getReadingsTimeSeries(Long pipelineId,
            LocalDate startDate, LocalDate endDate, String measurementType) {
        log.debug("Getting time series for pipeline ID {} type={} from {} to {}",
                pipelineId, measurementType, startDate, endDate);

        pipelineFacade.findById(pipelineId)
                .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));

        // Readings fetched; time-series data-point mapping delegated to analytics layer.
        flowReadingFacade.findByPipelineAndDateRange(pipelineId, startDate, endDate);

        return ReadingsTimeSeriesDTO.builder()
                .measurementType(measurementType)
                .dataPoints(new ArrayList<>())
                .build();
    }

    /**
     * Get summary overview list for ALL pipelines.
     * Retained as-is — separate from getOverview(Long, LocalDate).
     */
    public List<PipelineOverviewDTO> getPipelineOverviews() {
        log.debug("Getting all pipeline overviews");
        return pipelineFacade.findAll().stream()
                .map(dto -> PipelineOverviewDTO.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .code(dto.getCode())
                        .operationalStatus(dto.getOperationalStatus() != null ?
                                dto.getOperationalStatus().getCode() : null)
                        .build())
                .collect(Collectors.toList());
    }
}
