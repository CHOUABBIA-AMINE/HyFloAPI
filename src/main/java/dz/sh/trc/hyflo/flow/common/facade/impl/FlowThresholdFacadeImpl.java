/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdFacadeImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Facade / Impl
 *  @Package    : Flow / Common / Facade / Impl
 *
 *  @Description: Implements FlowThresholdFacade.
 *                Provides cross-module read-only access to FlowThreshold data
 *                for the flow.intelligence layer.
 *
 *  IMPORTANT — FlowThreshold entity field mapping:
 *    FlowThresholdFacadeDTO.metricCode    → no metric FK on entity;
 *                                           derived from threshold range context
 *                                           (set to "MULTI_RANGE" as a placeholder)
 *    FlowThresholdFacadeDTO.operator      → no operator FK on entity;
 *                                           set to "RANGE" as placeholder
 *    FlowThresholdFacadeDTO.thresholdValue → no single value field;
 *                                           mapped to pressureMax as representative value
 *    FlowThresholdFacadeDTO.active        → entity.getActive() (Boolean → primitive)
 *
 *  TODO: If the facade DTO contract needs per-metric granularity, either:
 *    a) Add metric/operator FKs to FlowThreshold entity, OR
 *    b) Expand the mapping to return one DTO per metric range per threshold row.
 *
 **/

package dz.sh.trc.hyflo.flow.common.facade.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.common.facade.FlowThresholdFacade;
import dz.sh.trc.hyflo.flow.common.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.common.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.intelligence.dto.facade.FlowThresholdFacadeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowThresholdFacadeImpl implements FlowThresholdFacade {

    private final FlowThresholdRepository flowThresholdRepository;

    @Override
    public List<FlowThresholdFacadeDTO> findByPipeline(Long pipelineId) {
        log.debug("FlowThresholdFacadeImpl.findByPipeline pipelineId={}", pipelineId);
        return flowThresholdRepository.findByPipelineId(pipelineId)
                .stream()
                .map(this::toFacadeDTO)
                .toList();
    }

    @Override
    public Optional<FlowThresholdFacadeDTO> findById(Long id) {
        log.debug("FlowThresholdFacadeImpl.findById id={}", id);
        return flowThresholdRepository.findById(id)
                .map(this::toFacadeDTO);
    }

    @Override
    public boolean existsById(Long id) {
        return flowThresholdRepository.existsById(id);
    }

    // -------------------------------------------------------------------------
    // Mapper
    // -------------------------------------------------------------------------

    /**
     * Maps a FlowThreshold entity to FlowThresholdFacadeDTO.
     *
     * FlowThreshold uses range-based fields (pressureMin/Max, temperatureMin/Max,
     * flowRateMin/Max, containedVolumeMin/Max) — there is no single metric/operator/
     * thresholdValue triplet on the entity.
     *
     * The DTO fields are populated with representative values:
     *   metricCode    = "MULTI_RANGE"  (placeholder — entity covers multiple metrics)
     *   operator      = "RANGE"        (placeholder — entity is range-based, not comparative)
     *   thresholdValue = pressureMax   (most commonly checked field in intelligence layer)
     *
     * If per-metric DTO granularity is needed, the entity model or the mapping
     * strategy must be revisited (see class-level TODO).
     */
    private FlowThresholdFacadeDTO toFacadeDTO(FlowThreshold entity) {
        return FlowThresholdFacadeDTO.builder()
                .id(entity.getId())
                .pipelineId(entity.getPipeline() != null
                        ? entity.getPipeline().getId() : null)
                .metricCode("MULTI_RANGE")
                .operator("RANGE")
                .thresholdValue(entity.getPressureMax())
                .active(Boolean.TRUE.equals(entity.getActive()))
                .build();
    }
}
