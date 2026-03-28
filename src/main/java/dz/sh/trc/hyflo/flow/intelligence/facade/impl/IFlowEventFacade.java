/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IFlowEventFacade
 *  @CreatedOn  : 03-26-2026 — H2: facade interface to isolate intelligence from flow/core repository
 *  @UpdatedOn  : 03-26-2026 — F1: replace FlowEvent entity returns with FlowEventFacadeDTO projections
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Cross-module facade interface used by flow/intelligence to access
 *                FlowEvent data without importing flow/core repositories or entity classes.
 *
 *                F1: All methods now return FlowEventFacadeDTO — no flow/core entity
 *                types cross the module boundary.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade.impl;

import java.time.LocalDateTime;
import java.util.List;

import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowEventFacadeDTO;

/**
 * Facade interface giving flow/intelligence read-only access to FlowEvent data.
 *
 * Implemented by FlowEventFacade in flow/core.
 * All return types are FlowEventFacadeDTO — no flow/core entity types cross this boundary.
 *
 * F1: upgraded from entity-returning contract to DTO-projection contract.
 */
public interface IFlowEventFacade {

    /**
     * Find all events for a pipeline within a time range.
     *
     * @param pipelineId the owning pipeline ID
     * @param start      range start (inclusive)
     * @param end        range end (inclusive)
     * @return list of event projections, ordered by eventTimestamp descending
     */
    List<FlowEventFacadeDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end);

    /**
     * Find all events associated with a specific flow reading.
     *
     * @param flowReadingId the source FlowReading ID
     * @return list of event projections
     */
    List<FlowEventFacadeDTO> findByFlowReading(Long flowReadingId);
}
