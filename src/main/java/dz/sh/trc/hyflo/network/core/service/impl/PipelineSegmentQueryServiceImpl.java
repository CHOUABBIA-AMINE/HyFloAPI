/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineSegmentQueryServiceImpl
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
import dz.sh.trc.hyflo.network.core.dto.query.PipelineSegmentReadDTO;
import dz.sh.trc.hyflo.network.core.mapper.PipelineSegmentMapper;
import dz.sh.trc.hyflo.network.core.repository.PipelineSegmentRepository;
import dz.sh.trc.hyflo.network.core.service.PipelineSegmentQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PipelineSegmentQueryServiceImpl implements PipelineSegmentQueryService {

    private final PipelineSegmentRepository pipelineSegmentRepository;

    @Override
    public PipelineSegmentReadDTO getById(Long id) {
        return pipelineSegmentRepository.findById(id)
                .map(PipelineSegmentMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PipelineSegment not found: " + id));
    }

    @Override
    public Page<PipelineSegmentReadDTO> getAll(Pageable pageable) {
        return pipelineSegmentRepository.findAll(pageable).map(PipelineSegmentMapper::toReadDTO);
    }

    @Override
    public List<PipelineSegmentReadDTO> getByPipeline(Long pipelineId) {
        return pipelineSegmentRepository.findByPipelineId(pipelineId)
                .stream()
                .map(PipelineSegmentMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PipelineSegmentReadDTO> search(String query, Pageable pageable) {
        return pipelineSegmentRepository.searchByAnyField(query, pageable)
                .map(PipelineSegmentMapper::toReadDTO);
    }
}
