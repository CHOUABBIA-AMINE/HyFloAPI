/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IFlowAlertFacade
 *  @CreatedOn  : 03-26-2026 — H2: facade interface to isolate intelligence from flow/core repository
 *  @UpdatedOn  : 03-26-2026 — F1: replace FlowAlert entity returns with FlowAlertFacadeDto projections
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Cross-module facade interface used by flow/intelligence to access
 *                FlowAlert data without importing flow/core repositories or entity classes.
 *
 *                F1: All methods now return FlowAlertFacadeDto — no flow/core entity
 *                types cross the module boundary.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowAlertFacadeDto;

/**
 * Facade interface giving flow/intelligence read-only access to FlowAlert data.
 *
 * Implemented by FlowAlertFacade in flow/core.
 * All return types are FlowAlertFacadeDto — no flow/core entity types cross this boundary.
 *
 * F1: upgraded from entity-returning contract to DTO-projection contract.
 */
public interface IFlowAlertFacade {

    /**
     * Find all alerts for a pipeline within a time range.
     *
     * @param pipelineId the owning pipeline ID
     * @param start      range start (inclusive)
     * @param end        range end (inclusive)
     * @return list of alert projections, ordered by alertTimestamp descending
     */
    List<FlowAlertFacadeDto> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end);

    /**
     * Find unresolved alerts for a pipeline with pagination.
     *
     * @param pipelineId the owning pipeline ID
     * @param pageable   pagination parameters
     * @return page of unresolved alert projections
     */
    Page<FlowAlertFacadeDto> findUnresolvedByPipeline(Long pipelineId, Pageable pageable);

    /**
     * Find all alerts triggered by a specific threshold.
     *
     * @param thresholdId the threshold ID
     * @return list of alert projections
     */
    List<FlowAlertFacadeDto> findByThreshold(Long thresholdId);

    /**
     * Find all alerts associated with a specific flow reading.
     *
     * @param flowReadingId the source FlowReading ID
     * @return list of alert projections
     */
    List<FlowAlertFacadeDto> findByFlowReading(Long flowReadingId);
}
