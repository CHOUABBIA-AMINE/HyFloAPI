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
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 * 	@Enhancement: Phase 6 - Replace Map<String, BigDecimal> with typed KeyMetricsDTO.
 * 	@BugFix: Phase 7 - Add null safety for optional pipeline fields
 * 	@Refactor: H2 - All flow/core repository accesses replaced by facade interfaces
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
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowAlertFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowEventFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowReadingFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowThresholdFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.IPipelineFacade;
import dz.sh.trc.hyflo.flow.intelligence.util.SlotStatisticsCalculator;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service providing operational intelligence and analytics for pipelines.
 *
 * H2: All flow/core repository dependencies have been replaced by facade interfaces.
 * - FlowThresholdRepository → IFlowThresholdFacade
 * - FlowAlertRepository    → IFlowAlertFacade
 * - FlowEventRepository    → IFlowEventFacade
 *
 * Module boundary: this service now depends ONLY on facade interfaces from flow/intelligence.
 * No direct flow/core repository imports remain.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PipelineIntelligenceService {
    
    // ========== DEPENDENCIES ==========
    
    private final IPipelineFacade pipelineFacade;
    private final IFlowReadingFacade flowReadingFacade;
    private final IFlowThresholdFacade thresholdFacade;   // H2: was FlowThresholdRepository
    private final IFlowAlertFacade alertFacade;           // H2: was FlowAlertRepository
    private final IFlowEventFacade eventFacade;           // H2: was FlowEventRepository
    
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
            
            .pipelineDetails(pipelineDTO);
        
        return builder.build();
    }
    
    /**
     * Get pipeline health metrics.
     * Uses IFlowAlertFacade for unresolved alert count (H2).
     */
    public PipelineHealthDTO getPipelineHealth(Long pipelineId) {
        log.debug("Getting pipeline health for ID {}", pipelineId);
        
        pipelineFacade.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        LocalDateTime rangeStart = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime rangeEnd   = LocalDateTime.now();
        
        List<FlowAlert> recentAlerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, rangeStart, rangeEnd);
        
        List<FlowThreshold> thresholds = thresholdFacade.findByPipeline(pipelineId);
        
        int alertCount   = recentAlerts.size();
        int criticalCount = (int) recentAlerts.stream()
                .filter(a -> a.getSeverity() != null &&
                             "CRITICAL".equalsIgnoreCase(a.getSeverity().getCode()))
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
     * Uses IFlowReadingFacade, IFlowAlertFacade, IFlowThresholdFacade, IFlowEventFacade (H2).
     */
    public PipelineDynamicDashboardDTO getDashboard(Long pipelineId) {
        log.debug("Building dashboard for pipeline ID {}", pipelineId);
        
        PipelineDTO pipeline = pipelineFacade.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        LocalDateTime rangeStart = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime rangeEnd   = LocalDateTime.now();
        LocalDate today          = LocalDate.now();
        
        // Readings
        List<FlowReadingDTO> readings =
                flowReadingFacade.findByPipelineAndDateRange(pipelineId, today.minusDays(30), today);
        
        // Alerts (H2: via IFlowAlertFacade)
        List<FlowAlert> alerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, rangeStart, rangeEnd);
        
        // Thresholds (H2: via IFlowThresholdFacade)
        List<FlowThreshold> thresholds = thresholdFacade.findByPipeline(pipelineId);
        
        // Events (H2: via IFlowEventFacade)
        List<FlowEvent> events =
                eventFacade.findByPipelineAndTimeRange(pipelineId, rangeStart, rangeEnd);
        
        // Key metrics
        OptionalDouble avgFlow = readings.stream()
                .filter(r -> r.getActualFlow() != null)
                .mapToDouble(r -> r.getActualFlow().doubleValue())
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
     * Uses IFlowAlertFacade and IFlowEventFacade (H2).
     */
    public PipelineTimelineDTO getTimeline(Long pipelineId, LocalDate from, LocalDate to) {
        log.debug("Building timeline for pipeline ID {} from {} to {}", pipelineId, from, to);
        
        pipelineFacade.findById(pipelineId)
            .orElseThrow(() -> new IllegalArgumentException("Pipeline not found: " + pipelineId));
        
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end   = to.plusDays(1).atStartOfDay();
        
        List<FlowAlert> alerts =
                alertFacade.findByPipelineAndTimeRange(pipelineId, start, end);
        List<FlowEvent> events =
                eventFacade.findByPipelineAndTimeRange(pipelineId, start, end);
        
        List<TimelineItemDTO> items = new ArrayList<>();
        
        alerts.forEach(a -> items.add(TimelineItemDTO.builder()
                .type("ALERT")
                .itemId(a.getId())
                .occurredAt(a.getAlertTimestamp())
                .severityCode(a.getSeverity() != null ? a.getSeverity().getCode() : null)
                .build()));
        
        events.forEach(e -> items.add(TimelineItemDTO.builder()
                .type("EVENT")
                .itemId(e.getId())
                .occurredAt(e.getEventTimestamp())
                .severityCode(e.getSeverity() != null ? e.getSeverity().getCode() : null)
                .build()));
        
        items.sort(Comparator.comparing(TimelineItemDTO::getOccurredAt,
                Comparator.nullsLast(Comparator.reverseOrder())));
        
        return PipelineTimelineDTO.builder()
                .pipelineId(pipelineId)
                .from(from)
                .to(to)
                .items(items)
                .build();
    }
    
    /**
     * Get pipeline overview list (all pipelines summary).
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
