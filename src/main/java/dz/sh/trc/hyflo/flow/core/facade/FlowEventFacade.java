/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventFacade
 *  @CreatedOn  : 03-26-2026 — H2: implements IFlowEventFacade, bridges flow/intelligence to flow/core
 *
 *  @Type       : Class
 *  @Layer      : Facade
 *  @Package    : Flow / Core
 *
 *  @Description: Concrete implementation of IFlowEventFacade.
 *                Delegates to FlowEventRepository. Lives in flow/core.
 *                Injected into flow/intelligence only through IFlowEventFacade interface.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.model.FlowEvent;
import dz.sh.trc.hyflo.flow.core.repository.FlowEventRepository;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowEventFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowEventFacade implements IFlowEventFacade {

    private final FlowEventRepository flowEventRepository;

    @Override
    public List<FlowEvent> findByPipelineAndTimeRange(Long pipelineId, LocalDateTime start, LocalDateTime end) {
        log.debug("FlowEventFacade: findByPipelineAndTimeRange pipelineId={} {} to {}", pipelineId, start, end);
        return flowEventRepository.findByPipelineAndTimeRange(pipelineId, start, end);
    }

    @Override
    public List<FlowEvent> findByFlowReading(Long flowReadingId) {
        log.debug("FlowEventFacade: findByFlowReading flowReadingId={}", flowReadingId);
        return flowEventRepository.findByFlowReadingId(flowReadingId);
    }
}
