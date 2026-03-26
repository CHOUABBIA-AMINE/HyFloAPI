/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventFacade
 *  @CreatedOn  : 03-26-2026 — H2
 *
 *  @Type       : Class
 *  @Layer      : Facade
 *  @Package    : Flow / Core
 *
 *  @Description: Implements IFlowEventFacade to provide flow/intelligence with
 *                read-only access to FlowEvent data without exposing FlowEventRepository.
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

/**
 * Facade implementation providing cross-module read access to FlowEvent data.
 * Registered as a Spring bean; injected into flow/intelligence services.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowEventFacade implements IFlowEventFacade {

    private final FlowEventRepository flowEventRepository;

    @Override
    public List<FlowEvent> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end) {
        log.debug("FlowEventFacade.findByPipelineAndTimeRange pipelineId={} from={} to={}",
                pipelineId, start, end);
        return flowEventRepository.findByPipelineIdAndEventTimestampBetween(pipelineId, start, end);
    }

    @Override
    public List<FlowEvent> findBySeverity(Long pipelineId, String severityCode) {
        log.debug("FlowEventFacade.findBySeverity pipelineId={} severity={}",
                pipelineId, severityCode);
        return flowEventRepository.findByPipelineIdAndSeverityCode(pipelineId, severityCode);
    }
}
