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
 * 	@BugFix: Phase 7 - Add null safety for optional pipeline fields
 * 	          - Fix NullPointerException when coating/material fields are null
 * 	          - Graceful handling of incomplete pipeline data
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
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowReadingFacade;
import dz.sh.trc.hyflo.flow.intelligence.facade.IPipelineFacade;
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
    
    private final IPipelineFacade pipelineFacade;
    private final IFlowReadingFacade flowReadingFacade;
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
            .operationalStatus(pipelineDTO.getOperationalStatus() != null ? 
                pipelineDTO.getOperationalStatus().getCode() : null)
            
            .length(pipelineDTO.getLength())
            .nominalDiameter(pipelineDTO.getNominalDiameter())
            .nominalThickness(pipelineDTO.getNominalThickness())
            .nominalRoughness(pipelineDTO.getNominalRoughness())
            
            // NULL-SAFE: Check for null before calling getCode()
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
    
    // ... rest of class unchanged ...
}
