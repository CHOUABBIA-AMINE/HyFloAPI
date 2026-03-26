/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdFacade
 *  @CreatedOn  : 03-26-2026 — H2: implements IFlowThresholdFacade, bridges flow/intelligence to flow/core
 *
 *  @Type       : Class
 *  @Layer      : Facade
 *  @Package    : Flow / Core
 *
 *  @Description: Concrete implementation of IFlowThresholdFacade.
 *                Delegates to FlowThresholdRepository. Lives in flow/core.
 *                Injected into flow/intelligence only through IFlowThresholdFacade interface.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowThresholdFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowThresholdFacade implements IFlowThresholdFacade {

    private final FlowThresholdRepository flowThresholdRepository;

    @Override
    public List<FlowThreshold> findByPipeline(Long pipelineId) {
        log.debug("FlowThresholdFacade: findByPipeline pipelineId={}", pipelineId);
        return flowThresholdRepository.findByPipelineId(pipelineId);
    }

    @Override
    public Optional<FlowThreshold> findById(Long id) {
        log.debug("FlowThresholdFacade: findById id={}", id);
        return flowThresholdRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return flowThresholdRepository.existsById(id);
    }
}
