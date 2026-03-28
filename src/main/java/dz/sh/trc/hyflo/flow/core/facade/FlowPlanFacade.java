/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanFacade
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Core
 *
 *  @Description: Anti-corruption boundary exposing FlowPlan data to flow.intelligence
 *                without leaking FlowPlan entities or flow.core repository types.
 *                Returns FlowPlanFacadeDTO only.
 *                Mirrors FlowReadingFacade pattern.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowPlanFacadeDTO;

/**
 * Facade interface giving flow.intelligence read-only access to FlowPlan data.
 * All return types are FlowPlanFacadeDTO — no entity types cross the boundary.
 */
public interface FlowPlanFacade {

    List<FlowPlanFacadeDTO> findByPipelineAndDate(Long pipelineId, LocalDate date);

    List<FlowPlanFacadeDTO> findByPipelineAndDateRange(Long pipelineId, LocalDate from, LocalDate to);

    Optional<FlowPlanFacadeDTO> findLatestApprovedByPipelineAndDate(Long pipelineId, LocalDate date);

    List<FlowPlanFacadeDTO> findApprovedByPipeline(Long pipelineId);
}
