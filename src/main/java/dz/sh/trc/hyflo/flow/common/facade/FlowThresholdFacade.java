/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdFacade
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Facade (Service)
 *  @Package    : Flow / Common
 *
 *  @Description: Implements IFlowThresholdFacade from flow/intelligence.
 *                Provides cross-module read access to FlowThreshold data
 *                without exposing flow/core entities outside their module.
 *
 *                FlowThreshold is a composite range entity (pressureMin/Max,
 *                temperatureMin/Max, flowRateMin/Max, containedVolumeMin/Max).
 *                One FlowThresholdFacadeDTO is produced per FlowThreshold record,
 *                with metricCode = "MULTI" to represent the composite nature.
 *                The consuming service (PipelineIntelligenceService) only inspects
 *                list emptiness — no individual field access is required at this stage.
 *
 *  @Fix        : Resolves Spring boot startup failure:
 *                "required a bean of type IFlowThresholdFacade that could not be found"
 *
 **/

package dz.sh.trc.hyflo.flow.common.facade;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.common.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.common.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowThresholdFacadeDTO;
import dz.sh.trc.hyflo.flow.intelligence.facade.impl.IFlowThresholdFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Facade bean exposing FlowThreshold data to flow/intelligence module.
 *
 * Registered as a Spring {@code @Service} so it can be injected as
 * {@code IFlowThresholdFacade} into PipelineIntelligenceService.
 *
 * Placed in flow/core/facade because it accesses the FlowThresholdRepository
 * (now in flow/common/repository) and the FlowThreshold entity
 * (now in flow/common/model).
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
        return thresholdRepository.findById(id)
                .map(this::toDTO);
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("FlowThresholdFacade.existsById id={}", id);
        return thresholdRepository.existsById(id);
    }

    // ================================================================
    //  PRIVATE MAPPING
    // ================================================================

    /**
     * Map FlowThreshold entity to FlowThresholdFacadeDTO.
     *
     * FlowThreshold is a composite range entity: it holds min/max bounds
     * for pressure, temperature, flow rate and contained volume.
     * There is no single metric/operator/thresholdValue triple — the
     * entity represents a full operating envelope.
     *
     * Mapping strategy:
     *   - metricCode      = "MULTI"  (composite; consuming service checks emptiness only)
     *   - operator        = "RANGE"  (descriptive; no enum assumed)
     *   - thresholdValue  = pressureMax (primary operational bound for health scoring)
     *   - active          = entity.active (null-safe defaulting to false)
     *   - pipelineId      = entity.pipeline.id
     */
    private FlowThresholdFacadeDTO toDTO(FlowThreshold threshold) {
        return FlowThresholdFacadeDTO.builder()
                .id(threshold.getId())
                .pipelineId(threshold.getPipeline() != null
                        ? threshold.getPipeline().getId() : null)
                .metricCode("MULTI")
                .operator("RANGE")
                .thresholdValue(threshold.getPressureMax())
                .active(Boolean.TRUE.equals(threshold.getActive()))
                .build();
    }
}
