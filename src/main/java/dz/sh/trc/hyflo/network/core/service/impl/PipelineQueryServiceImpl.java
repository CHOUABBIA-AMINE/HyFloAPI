/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineQueryServiceImpl
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

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.network.core.dto.query.PipelineReadDto;
import dz.sh.trc.hyflo.network.core.mapper.PipelineMapper;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import dz.sh.trc.hyflo.network.core.service.PipelineQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Query implementation for Pipeline read operations.
 *
 * Uses PipelineMapper from Phase 2.
 * Returns PipelineReadDto — no raw entity exposure.
 *
 * Phase 3 — Commit 23
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PipelineQueryServiceImpl implements PipelineQueryService {

    private final PipelineRepository pipelineRepository;

    @Override
    public PipelineReadDto getById(Long id) {
        return pipelineRepository.findById(id)
                .map(PipelineMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline not found: " + id));
    }

    @Override
    public Page<PipelineReadDto> getAll(Pageable pageable) {
        return pipelineRepository.findAll(pageable).map(PipelineMapper::toReadDto);
    }

    @Override
    public List<PipelineReadDto> getByPipelineSystem(Long systemId) {
        return pipelineRepository.findByPipelineSystemId(systemId)
                .stream()
                .map(PipelineMapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PipelineReadDto> search(String query) {
        return pipelineRepository.searchByAnyField(query)
                .stream()
                .map(PipelineMapper::toReadDto)
                .collect(Collectors.toList());
    }
}
