/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingQueryServiceImpl
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : Service (Query Implementation)
 *  @Package    : Flow / Core
 *
 *  @Description: Query implementation for FlowReading read operations.
 *                All methods use FlowReadingMapper.toReadDto() from Phase 2.
 *                No entity returned to caller.
 *
 *  Phase 3 — Commit 17
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.mapper.FlowReadingMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Query implementation for FlowReading read operations.
 *
 * All methods use FlowReadingMapper.toReadDto() from Phase 2.
 * No entity returned to caller.
 *
 * Phase 3 — Commit 17
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowReadingQueryServiceImpl implements FlowReadingQueryService {

    private final FlowReadingRepository flowReadingRepository;

    @Override
    public FlowReadingReadDto getById(Long id) {
        return flowReadingRepository.findById(id)
                .map(FlowReadingMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException("FlowReading not found: " + id));
    }

    @Override
    public Page<FlowReadingReadDto> getAll(Pageable pageable) {
        return flowReadingRepository.findAll(pageable).map(FlowReadingMapper::toReadDto);
    }

    @Override
    public Page<FlowReadingReadDto> search(String query, Pageable pageable) {
        if (query == null || query.isBlank()) return getAll(pageable);
        return flowReadingRepository.searchByAnyField(query, pageable).map(FlowReadingMapper::toReadDto);
    }

    @Override
    public List<FlowReadingReadDto> getByPipeline(Long pipelineId) {
        return flowReadingRepository.findByPipelineId(pipelineId)
                .stream().map(FlowReadingMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<FlowReadingReadDto> getByDateRange(LocalDate from, LocalDate to) {
        return flowReadingRepository.findByReadingDateBetween(from, to)
                .stream().map(FlowReadingMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<FlowReadingReadDto> getByPipelineAndDateRange(Long pipelineId, LocalDate from, LocalDate to) {
        return flowReadingRepository.findByPipelineIdAndReadingDateBetween(pipelineId, from, to)
                .stream().map(FlowReadingMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<FlowReadingReadDto> getByPipelineAndSlot(Long pipelineId, Long slotId) {
        return flowReadingRepository.findByPipelineIdAndReadingSlotId(pipelineId, slotId)
                .stream().map(FlowReadingMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<FlowReadingReadDto> getByValidationStatus(Long validationStatusId) {
        return flowReadingRepository.findByValidationStatusId(validationStatusId)
                .stream().map(FlowReadingMapper::toReadDto).collect(Collectors.toList());
    }

    @Override
    public List<FlowReadingReadDto> getLatestByPipeline(Long pipelineId, int limit) {
        Pageable top = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "readingDate"));
        return flowReadingRepository.findByPipelineId(pipelineId, top)
                .stream().map(FlowReadingMapper::toReadDto).collect(Collectors.toList());
    }
}
