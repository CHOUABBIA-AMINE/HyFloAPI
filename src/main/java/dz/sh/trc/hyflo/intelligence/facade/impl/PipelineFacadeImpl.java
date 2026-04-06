/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineFacadeImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Facade / Impl
 *  @Package    : Flow / Intelligence / Facade / Impl
 *
 **/

package dz.sh.trc.hyflo.intelligence.facade.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.intelligence.facade.PipelineFacade;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PipelineFacadeImpl implements PipelineFacade {

    private final PipelineRepository pipelineRepository;

    @Override
    public Optional<PipelineDTO> findById(Long pipelineId) {
        return pipelineRepository.findById(pipelineId).map(this::toDTO);
    }

    @Override
    public boolean existsById(Long pipelineId) {
        return pipelineRepository.existsById(pipelineId);
    }

    @Override
    public List<PipelineDTO> findByManagerId(Long managerId) {
        return pipelineRepository.findByManagerId(managerId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<PipelineDTO> findAll() {
        return pipelineRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PipelineDTO toDTO(Pipeline p) {
        return PipelineDTO.builder()
                .id(p.getId())
                .code(p.getCode())
                .name(p.getName())
                .build();
    }
}
