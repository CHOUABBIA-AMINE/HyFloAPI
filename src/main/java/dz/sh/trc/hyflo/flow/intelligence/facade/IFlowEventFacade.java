/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IFlowEventFacade
 *  @CreatedOn  : 03-26-2026 — H2: facade interface to isolate intelligence from flow/core repository
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Cross-module facade interface used by flow/intelligence to access
 *                FlowEvent data without importing flow/core repositories directly.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.time.LocalDateTime;
import java.util.List;

import dz.sh.trc.hyflo.flow.core.model.FlowEvent;

/**
 * Facade interface giving flow/intelligence read-only access to FlowEvent data.
 * Implemented by FlowEventFacade in flow/core.
 */
public interface IFlowEventFacade {

    List<FlowEvent> findByPipelineAndTimeRange(Long pipelineId, LocalDateTime start, LocalDateTime end);

    List<FlowEvent> findByFlowReading(Long flowReadingId);
}
