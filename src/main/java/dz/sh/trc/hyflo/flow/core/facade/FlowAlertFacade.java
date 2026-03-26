/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertFacade
 *  @CreatedOn  : 03-26-2026 — H2
 *
 *  @Type       : Class
 *  @Layer      : Facade
 *  @Package    : Flow / Core
 *
 *  @Description: Implements IFlowAlertFacade to provide flow/intelligence with
 *                read-only access to FlowAlert data without exposing FlowAlertRepository.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.model.FlowAlert;
import dz.sh.trc.hyflo.flow.core.repository.FlowAlertRepository;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowAlertFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Facade implementation providing cross-module read access to FlowAlert data.
 * Registered as a Spring bean; injected into flow/intelligence services.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowAlertFacade implements IFlowAlertFacade {

    private final FlowAlertRepository flowAlertRepository;

    @Override
    public List<FlowAlert> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end) {
        log.debug("FlowAlertFacade.findByPipelineAndTimeRange pipelineId={} from={} to={}",
                pipelineId, start, end);
        return flowAlertRepository.findByPipelineIdAndAlertTimestampBetween(pipelineId, start, end);
    }

    @Override
    public Page<FlowAlert> findUnresolvedByPipeline(Long pipelineId, Pageable pageable) {
        log.debug("FlowAlertFacade.findUnresolvedByPipeline pipelineId={}", pipelineId);
        return flowAlertRepository.findByPipelineIdAndResolvedFalse(pipelineId,
                pageable != null ? pageable : PageRequest.of(0, 50));
    }

    @Override
    public List<FlowAlert> findByThreshold(Long thresholdId) {
        log.debug("FlowAlertFacade.findByThreshold thresholdId={}", thresholdId);
        return flowAlertRepository.findByThresholdId(thresholdId);
    }

    @Override
    public List<FlowAlert> findByFlowReading(Long flowReadingId) {
        log.debug("FlowAlertFacade.findByFlowReading flowReadingId={}", flowReadingId);
        return flowAlertRepository.findByFlowReadingId(flowReadingId);
    }
}
