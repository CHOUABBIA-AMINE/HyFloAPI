/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdFacade
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Facade (Service)
 *  @Package    : Flow / Core
 *
 *  @Description: Implements IFlowThresholdFacade from flow/intelligence.
 *                Provides cross-module read access to FlowThreshold data
 *                without exposing flow/core entities outside their module.
 *
 *                Maps FlowThreshold entities to FlowThresholdFacadeDTO.
 *                Owned by flow/core because it accesses flow/core repositories
 *                and entities.
 *
 *                Note on FlowThresholdFacadeDTO fields:
 *                The DTO was designed for a single-metric/operator/value model
 *                (e.g. PRESSURE > 120.0), but the FlowThreshold entity uses
 *                min/max range pairs (pressureMin, pressureMax, flowRateMin...).
 *                metricCode, operator, and thresholdValue are mapped to null
 *                in this implementation. PipelineIntelligenceService only uses
 *                the list size (threshold count) — not individual field values.
 *                A future schema evolution can add these fields to FlowThreshold
 *                if single-metric threshold semantics are needed.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade;

import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowThresholdFacadeDTO;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowThresholdFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Facade bean exposing FlowThreshold data to flow/intelligence module.
 *
 * Registered as a Spring @Service so it can be injected as IFlowThresholdFacade
 * into PipelineIntelligenceService.
 *
 * Placed in flow/core/facade because it owns FlowThreshold repository access.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowThresholdFacade implements IFlowThresholdFacade {

    private final FlowThresholdRepository thresholdRepository;

    // ----------------------------------------------------------------
    //  IFlowThresholdFacade contract
    // ----------------------------------------------------------------

    @Override
    public List<FlowThresholdFacadeDTO> findByPipeline(Long pipelineId) {
        log.debug("FlowThresholdFacade.findByPipeline pipelineId={}", pipelineId);
        return thresholdRepository.findByPipelineId(pipelineId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FlowThresholdFacadeDTO> findById(Long id) {
        log.debug("FlowThresholdFacade.findById id={}", id);
        return thresholdRepository.findById(id).map(this::toDTO);
    }

    @Override
    public boolean existsById(Long id) {
        return thresholdRepository.existsById(id);
    }

    // ================================================================
    //  PRIVATE MAPPING
    // ================================================================

    /**
     * Map FlowThreshold entity to FlowThresholdFacadeDTO.
     *
     * metricCode / operator / thresholdValue: mapped to null.
     * The current FlowThreshold entity uses range pairs (min/max) per
     * physical parameter, not a single metric+operator+value model.
     * PipelineIntelligenceService only uses the list size (threshold count)
     * from this facade, so null fields have no impact on current behavior.
     */
    private FlowThresholdFacadeDTO toDTO(FlowThreshold t) {
        return FlowThresholdFacadeDTO.builder()
                .id(t.getId())
                .pipelineId(t.getPipeline() != null ? t.getPipeline().getId() : null)
                .metricCode(null)    // FlowThreshold uses min/max ranges, not single-metric model
                .operator(null)      // see class Javadoc
                .thresholdValue(null) // see class Javadoc
                .active(Boolean.TRUE.equals(t.getActive()))
                .build();
    }
}
