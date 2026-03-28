/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: PipelineFacade
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026 - Created during Phase 2 refactoring
 * 	@UpdatedOn	: 02-10-2026 - Phase 2: Return DTOs instead of entities
 * 	@UpdatedOn	: 03-26-2026 - Phase 3: Add findAll() implementation
 *
 * 	@Type		: Class
 * 	@Layer		: Facade
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.intelligence.facade.impl.IPipelineFacade;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PipelineFacade implements IPipelineFacade {

    private final PipelineRepository pipelineRepository;

    @Override
    public Optional<PipelineDTO> findById(Long pipelineId) {
        log.debug("Finding pipeline by ID: {}", pipelineId);
        return pipelineRepository.findById(pipelineId)
                .map(PipelineDTO::fromEntity);
    }

    @Override
    public boolean existsById(Long pipelineId) {
        log.debug("Checking if pipeline exists: {}", pipelineId);
        return pipelineRepository.existsById(pipelineId);
    }

    @Override
    public List<PipelineDTO> findByManagerId(Long managerId) {
        log.debug("Finding pipelines by manager ID: {}", managerId);
        return pipelineRepository.findByManagerId(managerId)
                .stream()
                .map(PipelineDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<PipelineDTO> findAll() {
        log.debug("Finding all pipelines");
        return pipelineRepository.findAll()
                .stream()
                .map(PipelineDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
