/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdFacade
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Common / Facade
 *
 *  @Description: Cross-module facade interface for FlowThreshold data access.
 *                Owned by flow.common because FlowThreshold entity lives in flow.common.
 *                Implemented by flow.common.facade.impl.FlowThresholdFacadeImpl.
 *                Consumed by flow.intelligence services.
 *
 **/

package dz.sh.trc.hyflo.flow.common.facade;

import java.util.List;
import java.util.Optional;

import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowThresholdFacadeDTO;

/**
 * Facade interface giving flow/intelligence read-only access to FlowThreshold data.
 * All return types are FlowThresholdFacadeDTO — no entity types cross the boundary.
 */
public interface FlowThresholdFacade {

    List<FlowThresholdFacadeDTO> findByPipeline(Long pipelineId);

    Optional<FlowThresholdFacadeDTO> findById(Long id);

    boolean existsById(Long id);
}
