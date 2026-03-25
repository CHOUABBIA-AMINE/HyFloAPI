/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineSegmentCommandServiceImpl
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
import dz.sh.trc.hyflo.network.core.dto.command.PipelineSegmentCommandDto;
import dz.sh.trc.hyflo.network.core.dto.query.PipelineSegmentReadDto;
import dz.sh.trc.hyflo.network.core.mapper.PipelineSegmentMapper;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
import dz.sh.trc.hyflo.network.core.repository.PipelineSegmentRepository;
import dz.sh.trc.hyflo.network.core.service.PipelineSegmentCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Command implementation for PipelineSegment write operations.
 *
 * Uses PipelineSegmentMapper from Phase 2.
 * Returns PipelineSegmentReadDto — no raw entity exposure.
 *
 * Phase 3 — Commit 23
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PipelineSegmentCommandServiceImpl implements PipelineSegmentCommandService {

    private final PipelineSegmentRepository pipelineSegmentRepository;

    @Override
    public PipelineSegmentReadDto createSegment(PipelineSegmentCommandDto command) {
        log.info("Creating pipeline segment: {}", command.getCode());
        PipelineSegment entity = PipelineSegmentMapper.toEntity(command);
        return PipelineSegmentMapper.toReadDto(pipelineSegmentRepository.save(entity));
    }

    @Override
    public PipelineSegmentReadDto updateSegment(Long id, PipelineSegmentCommandDto command) {
        log.info("Updating pipeline segment ID: {}", id);
        PipelineSegment existing = pipelineSegmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PipelineSegment not found: " + id));
        PipelineSegmentMapper.updateEntity(command, existing);
        return PipelineSegmentMapper.toReadDto(pipelineSegmentRepository.save(existing));
    }

    @Override
    public void deleteSegment(Long id) {
        log.info("Deleting pipeline segment ID: {}", id);
        if (!pipelineSegmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("PipelineSegment not found: " + id);
        }
        pipelineSegmentRepository.deleteById(id);
    }
}
