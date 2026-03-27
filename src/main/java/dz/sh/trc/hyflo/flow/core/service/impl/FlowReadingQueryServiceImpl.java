/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingQueryServiceImpl
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 — fix: getByPipelineAndSlot() — remove call to
 *                              findByPipelineIdAndReadingSlotId() which no longer
 *                              exists in FlowReadingRepository (FlowReading has no
 *                              readingSlot FK). Delegates to findByPipelineId() until
 *                              a readingSlot FK is added to the FlowReading entity.
 *
 *  @Type       : Class
 *  @Layer      : Service (Query Implementation)
 *  @Package    : Flow / Core
 *
 *  @Description: Query implementation for FlowReading read operations.
 *                All methods use FlowReadingMapper.toReadDTO() from Phase 2.
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
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowReadingMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Query implementation for FlowReading read operations.
 *
 * All methods use FlowReadingMapper.toReadDTO() from Phase 2.
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
    public FlowReadingReadDTO getById(Long id) {
        return flowReadingRepository.findById(id)
                .map(FlowReadingMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("FlowReading not found: " + id));
    }

    @Override
    public Page<FlowReadingReadDTO> getAll(Pageable pageable) {
        return flowReadingRepository.findAll(pageable).map(FlowReadingMapper::toReadDTO);
    }

    @Override
    public Page<FlowReadingReadDTO> search(String query, Pageable pageable) {
        if (query == null || query.isBlank()) return getAll(pageable);
        return flowReadingRepository.searchByAnyField(query, pageable).map(FlowReadingMapper::toReadDTO);
    }

    @Override
    public List<FlowReadingReadDTO> getByPipeline(Long pipelineId) {
        return flowReadingRepository.findByPipelineId(pipelineId)
                .stream().map(FlowReadingMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public List<FlowReadingReadDTO> getByDateRange(LocalDate from, LocalDate to) {
        return flowReadingRepository.findByReadingDateBetween(from, to)
                .stream().map(FlowReadingMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public List<FlowReadingReadDTO> getByPipelineAndDateRange(Long pipelineId, LocalDate from, LocalDate to) {
        return flowReadingRepository.findByPipelineIdAndReadingDateBetween(pipelineId, from, to)
                .stream().map(FlowReadingMapper::toReadDTO).collect(Collectors.toList());
    }

    /**
     * Returns all readings for the given pipeline.
     *
     * NOTE: FlowReading currently has no readingSlot FK — the slotId parameter
     * is accepted for API compatibility but not applied at DB level.
     * Slot-based filtering will be activated once a readingSlot FK is added
     * to the FlowReading entity (tracked as a TODO in FlowReadingRepository).
     *
     * @param pipelineId the pipeline to query
     * @param slotId     reserved — not yet applied to the query
     */
    @Override
    public List<FlowReadingReadDTO> getByPipelineAndSlot(Long pipelineId, Long slotId) {
        log.debug("getByPipelineAndSlot(pipelineId={}, slotId={}) — slot filter not yet active "
                + "(FlowReading has no readingSlot FK); returning all readings for pipeline.",
                pipelineId, slotId);
        // TODO: replace with findByPipelineIdAndReadingSlotId() once readingSlot FK
        //       is added to FlowReading entity.
        return flowReadingRepository.findByPipelineId(pipelineId)
                .stream().map(FlowReadingMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public List<FlowReadingReadDTO> getByValidationStatus(Long validationStatusId) {
        return flowReadingRepository.findByValidationStatusId(validationStatusId)
                .stream().map(FlowReadingMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public List<FlowReadingReadDTO> getLatestByPipeline(Long pipelineId, int limit) {
        Pageable top = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "readingDate"));
        return flowReadingRepository.findByPipelineId(pipelineId, top)
                .stream().map(FlowReadingMapper::toReadDTO).collect(Collectors.toList());
    }
}
