/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IFlowThresholdFacade
 *  @CreatedOn  : 03-26-2026 — H2: facade interface to isolate intelligence from flow/core repository
 *  @UpdatedOn  : 03-26-2026 — F1: replace FlowThreshold entity returns with FlowThresholdFacadeDTO projections
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Cross-module facade interface used by flow/intelligence to access
 *                FlowThreshold data without importing flow/core repositories or entity classes.
 *
 *                F1: All methods now return FlowThresholdFacadeDTO — no flow/core entity
 *                types cross the module boundary.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade.impl;

import java.util.List;
import java.util.Optional;

import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowThresholdFacadeDTO;

/**
 * Facade interface giving flow/intelligence read-only access to FlowThreshold data.
 *
 * Implemented by FlowThresholdFacade in flow/core.
 * All return types are FlowThresholdFacadeDTO — no flow/core entity types cross this boundary.
 *
 * F1: upgraded from entity-returning contract to DTO-projection contract.
 */
public interface IFlowThresholdFacade {

    /**
     * Find all thresholds configured for a pipeline.
     *
     * @param pipelineId the owning pipeline ID
     * @return list of threshold projections
     */
    List<FlowThresholdFacadeDTO> findByPipeline(Long pipelineId);

    /**
     * Find a threshold by its primary key.
     *
     * @param id the threshold ID
     * @return optional threshold projection
     */
    Optional<FlowThresholdFacadeDTO> findById(Long id);

    /**
     * Check if a threshold with the given ID exists.
     *
     * @param id the threshold ID
     * @return true if the threshold exists
     */
    boolean existsById(Long id);
}
