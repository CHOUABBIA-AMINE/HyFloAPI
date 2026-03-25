/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DerivedFlowReadingQueryServiceImpl
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : Service (Query Implementation)
 *  @Package    : Flow / Core
 *
 *  @Description: Query implementation for DerivedFlowReading.
 *                Uses Phase 2 DerivedFlowReadingMapper exclusively.
 *
 *  Phase 3 — Commit 18
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.mapper.DerivedFlowReadingMapper;
import dz.sh.trc.hyflo.flow.core.repository.DerivedFlowReadingRepository;
import dz.sh.trc.hyflo.flow.core.service.DerivedFlowReadingQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DerivedFlowReadingQueryServiceImpl implements DerivedFlowReadingQueryService {

    private final DerivedFlowReadingRepository derivedFlowReadingRepository;

    @Override
    public DerivedFlowReadingReadDto getById(Long id) {
        return derivedFlowReadingRepository.findById(id)
                .map(DerivedFlowReadingMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "DerivedFlowReading not found: " + id));
    }

    @Override
    public List<DerivedFlowReadingReadDto> getBySourceReading(Long sourceReadingId) {
        return derivedFlowReadingRepository.findBySourceReadingId(sourceReadingId)
                .stream().map(DerivedFlowReadingMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<DerivedFlowReadingReadDto> getBySegment(Long pipelineSegmentId) {
        return derivedFlowReadingRepository.findByPipelineSegmentId(pipelineSegmentId)
                .stream().map(DerivedFlowReadingMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<DerivedFlowReadingReadDto> getBySegmentAndDateRange(
            Long pipelineSegmentId, LocalDate from, LocalDate to) {
        return derivedFlowReadingRepository
                .findByPipelineSegmentIdAndReadingDateBetween(pipelineSegmentId, from, to)
                .stream().map(DerivedFlowReadingMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<DerivedFlowReadingReadDto> getBySegmentAndSlot(
            Long pipelineSegmentId, Long readingSlotId) {
        return derivedFlowReadingRepository
                .findByPipelineSegmentIdAndReadingSlotId(pipelineSegmentId, readingSlotId)
                .stream().map(DerivedFlowReadingMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<DerivedFlowReadingReadDto> getByDateRange(LocalDate from, LocalDate to) {
        return derivedFlowReadingRepository.findByReadingDateBetween(from, to)
                .stream().map(DerivedFlowReadingMapper::toReadDto).collect(Collectors.toList());
    }
}
