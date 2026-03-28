/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventFacade
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Core / Facade
 *
 *  @Description: Cross-module facade interface for FlowEvent data access.
 *                Owned by flow.core because FlowEvent entity lives in flow.core.
 *                Implemented by flow.core.facade.impl.FlowEventFacadeImpl.
 *                Consumed by flow.intelligence services.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade;

import java.time.LocalDateTime;
import java.util.List;

import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowEventFacadeDTO;

/**
 * Facade interface giving flow/intelligence read-only access to FlowEvent data.
 * All return types are FlowEventFacadeDTO — no flow/core entity types cross this boundary.
 */
public interface FlowEventFacade {

    List<FlowEventFacadeDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end);

    List<FlowEventFacadeDTO> findByFlowReading(Long flowReadingId);
}
