/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IFlowAlertFacade
 *  @CreatedOn  : 03-26-2026 — H2: facade interface to isolate intelligence from flow/core repository
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Cross-module facade interface used by flow/intelligence to access
 *                FlowAlert data without importing flow/core repositories directly.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.flow.core.model.FlowAlert;

/**
 * Facade interface giving flow/intelligence read-only access to FlowAlert data.
 * Implemented by FlowAlertFacade in flow/core.
 */
public interface IFlowAlertFacade {

    List<FlowAlert> findByPipelineAndTimeRange(Long pipelineId, LocalDateTime start, LocalDateTime end);

    Page<FlowAlert> findUnresolvedByPipeline(Long pipelineId, Pageable pageable);

    List<FlowAlert> findByThreshold(Long thresholdId);

    List<FlowAlert> findByFlowReading(Long flowReadingId);
}
