/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineFacade
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-28-2026 — CRITICAL refactor: was impl class, now interface.
 *                             Impl moved to facade.impl.PipelineFacadeImpl
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Intelligence / Facade
 *
 **/

package dz.sh.trc.hyflo.intelligence.facade;

import java.util.List;
import java.util.Optional;

import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;

/**
 * Abstraction for pipeline queries exposed to the intelligence layer.
 * Implemented by PipelineFacadeImpl in facade.impl.
 */
public interface PipelineFacade {

    Optional<PipelineDTO> findById(Long pipelineId);

    boolean existsById(Long pipelineId);

    List<PipelineDTO> findByManagerId(Long managerId);

    List<PipelineDTO> findAll();
}
