/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IFlowThresholdFacade
 *  @CreatedOn  : 03-26-2026 — H2: facade interface to isolate intelligence from flow/core repository
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Cross-module facade interface used by flow/intelligence to access
 *                FlowThreshold data without importing flow/core repositories directly.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.util.List;
import java.util.Optional;

import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;

/**
 * Facade interface giving flow/intelligence read-only access to FlowThreshold data.
 * Implemented by FlowThresholdFacade in flow/core.
 */
public interface IFlowThresholdFacade {

    List<FlowThreshold> findByPipeline(Long pipelineId);

    Optional<FlowThreshold> findById(Long id);

    boolean existsById(Long id);
}
