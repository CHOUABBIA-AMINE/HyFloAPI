/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdFacade
 *  @CreatedOn  : 03-26-2026 — H2
 *
 *  @Type       : Class
 *  @Layer      : Facade
 *  @Package    : Flow / Core
 *
 *  @Description: Implements IFlowThresholdFacade to provide flow/intelligence with
 *                read-only access to FlowThreshold data without exposing FlowThresholdRepository.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowThresholdFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Facade implementation providing cross-module read access to FlowThreshold data.
 * Registered as a Spring bean; injected into flow/intelligence services.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowThresholdFacade implements IFlowThresholdFacade {

    private final FlowThresholdRepository flowThresholdRepository;

    @Override
    public List<FlowThreshold> findByPipeline(Long pipelineId) {
        log.debug("FlowThresholdFacade.findByPipeline pipelineId={}", pipelineId);
        return flowThresholdRepository.findByPipelineId(pipelineId);
    }

    @Override
    public List<FlowThreshold> findActiveByPipeline(Long pipelineId) {
        log.debug("FlowThresholdFacade.findActiveByPipeline pipelineId={}", pipelineId);
        return flowThresholdRepository.findByPipelineIdAndIsActiveTrue(pipelineId);
    }
}
