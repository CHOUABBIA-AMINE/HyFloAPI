/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineCommandServiceImpl
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl
 *  @Package    : Network / Core
 *
 *  Phase 3 — Commit 23
 *
 **/

package dz.sh.trc.hyflo.network.core.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.network.core.dto.command.PipelineCommandDto;
import dz.sh.trc.hyflo.network.core.dto.query.PipelineReadDto;
import dz.sh.trc.hyflo.network.core.mapper.PipelineMapper;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import dz.sh.trc.hyflo.network.core.service.PipelineCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Command implementation for Pipeline write operations.
 *
 * Uses PipelineMapper from Phase 2.
 * Returns PipelineReadDto — no raw entity exposure.
 *
 * Phase 3 — Commit 23
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PipelineCommandServiceImpl implements PipelineCommandService {

    private final PipelineRepository pipelineRepository;

    @Override
    public PipelineReadDto createPipeline(PipelineCommandDto command) {
        log.info("Creating pipeline: {}", command.getCode());
        Pipeline entity = PipelineMapper.toEntity(command);
        return PipelineMapper.toReadDto(pipelineRepository.save(entity));
    }

    @Override
    public PipelineReadDto updatePipeline(Long id, PipelineCommandDto command) {
        log.info("Updating pipeline ID: {}", id);
        Pipeline existing = pipelineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline not found: " + id));
        PipelineMapper.updateEntity(command, existing);
        return PipelineMapper.toReadDto(pipelineRepository.save(existing));
    }

    @Override
    public void deletePipeline(Long id) {
        log.info("Deleting pipeline ID: {}", id);
        if (!pipelineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pipeline not found: " + id);
        }
        pipelineRepository.deleteById(id);
    }
}
