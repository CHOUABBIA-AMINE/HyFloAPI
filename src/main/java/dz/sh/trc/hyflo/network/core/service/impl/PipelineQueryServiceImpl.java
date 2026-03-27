/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineQueryServiceImpl
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 — Task 3: searchByAnyField(query) → searchByAnyField(query, pageable)
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl
 *  @Package    : Network / Core
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
import dz.sh.trc.hyflo.network.core.dto.query.PipelineReadDTO;
import dz.sh.trc.hyflo.network.core.mapper.PipelineMapper;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import dz.sh.trc.hyflo.network.core.service.PipelineQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PipelineQueryServiceImpl implements PipelineQueryService {

    private final PipelineRepository pipelineRepository;

    @Override
    public PipelineReadDTO getById(Long id) {
        return pipelineRepository.findById(id)
                .map(PipelineMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline not found: " + id));
    }

    @Override
    public Page<PipelineReadDTO> getAll(Pageable pageable) {
        return pipelineRepository.findAll(pageable).map(PipelineMapper::toReadDTO);
    }

    @Override
    public List<PipelineReadDTO> getByPipelineSystem(Long systemId) {
        return pipelineRepository.findByPipelineSystemId(systemId)
                .stream()
                .map(PipelineMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PipelineReadDTO> search(String query, Pageable pageable) {
        return pipelineRepository.searchByAnyField(query, pageable)
                .map(PipelineMapper::toReadDTO);
    }
}
