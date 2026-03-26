/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertFacade
 *  @CreatedOn  : 03-26-2026 — H2: implements IFlowAlertFacade, bridges flow/intelligence to flow/core
 *
 *  @Type       : Class
 *  @Layer      : Facade
 *  @Package    : Flow / Core
 *
 *  @Description: Concrete implementation of IFlowAlertFacade.
 *                Delegates to FlowAlertRepository. Lives in flow/core.
 *                Injected into flow/intelligence only through IFlowAlertFacade interface.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.model.FlowAlert;
import dz.sh.trc.hyflo.flow.core.repository.FlowAlertRepository;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowAlertFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowAlertFacade implements IFlowAlertFacade {

    private final FlowAlertRepository flowAlertRepository;

    @Override
    public List<FlowAlert> findByPipelineAndTimeRange(Long pipelineId, LocalDateTime start, LocalDateTime end) {
        log.debug("FlowAlertFacade: findByPipelineAndTimeRange pipelineId={} {} to {}", pipelineId, start, end);
        return flowAlertRepository.findByPipelineAndTimeRange(
                pipelineId, start, end, Pageable.unpaged()).getContent();
    }

    @Override
    public Page<FlowAlert> findUnresolvedByPipeline(Long pipelineId, Pageable pageable) {
        log.debug("FlowAlertFacade: findUnresolvedByPipeline pipelineId={}", pipelineId);
        return flowAlertRepository.findUnresolvedByPipeline(pipelineId, pageable);
    }

    @Override
    public List<FlowAlert> findByThreshold(Long thresholdId) {
        log.debug("FlowAlertFacade: findByThreshold thresholdId={}", thresholdId);
        return flowAlertRepository.findByThresholdId(thresholdId);
    }

    @Override
    public List<FlowAlert> findByFlowReading(Long flowReadingId) {
        log.debug("FlowAlertFacade: findByFlowReading flowReadingId={}", flowReadingId);
        return flowAlertRepository.findByFlowReadingId(flowReadingId);
    }
}
