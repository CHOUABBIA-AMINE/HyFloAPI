package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.util.List;
import java.util.Optional;

import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;

/**
 * Abstraction for pipeline queries exposed to the intelligence layer.
 *
 * Implemented by {@link PipelineFacade}.
 */
public interface IPipelineFacade {

    Optional<PipelineDTO> findById(Long pipelineId);

    boolean existsById(Long pipelineId);

    List<PipelineDTO> findByManagerId(Long managerId);
}
