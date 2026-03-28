/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingFacadeImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Facade / Impl
 *  @Package    : Flow / Intelligence / Facade / Impl
 *
 *  @Description: Implements FlowReadingFacade interface.
 *                Provides cross-module read access to FlowReading data
 *                for the intelligence layer.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.repository.ReadingSlotRepository;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowReadingMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.intelligence.facade.FlowReadingFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowReadingFacadeImpl implements FlowReadingFacade {

    private final FlowReadingRepository flowReadingRepository;
    private final ReadingSlotRepository readingSlotRepository;

    @Override
    public Optional<FlowReadingReadDTO> findLatestByPipeline(Long pipelineId) {
        log.debug("FlowReadingFacadeImpl.findLatestByPipeline pipelineId={}", pipelineId);
        return flowReadingRepository.findTopByPipelineIdOrderByReadingDateDesc(pipelineId)
                .map(FlowReadingMapper::toReadDTO);
    }

    @Override
    public List<FlowReadingReadDTO> findByPipelineAndDate(Long pipelineId, LocalDate readingDate) {
        log.debug("FlowReadingFacadeImpl.findByPipelineAndDate pipelineId={} date={}", pipelineId, readingDate);
        return flowReadingRepository.findByPipelineIdAndReadingDate(pipelineId, readingDate)
                .stream().map(FlowReadingMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowReadingReadDTO> findByPipelineAndDateRangeOrdered(
            Long pipelineId, LocalDate startDate, LocalDate endDate) {
        log.debug("FlowReadingFacadeImpl.findByPipelineAndDateRangeOrdered pipelineId={}", pipelineId);
        return flowReadingRepository
                .findByPipelineIdAndReadingDateBetweenOrderByReadingDateAsc(pipelineId, startDate, endDate)
                .stream().map(FlowReadingMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowReadingReadDTO> findByPipelineAndDateRange(
            Long pipelineId, LocalDate startDate, LocalDate endDate) {
        log.debug("FlowReadingFacadeImpl.findByPipelineAndDateRange pipelineId={}", pipelineId);
        return flowReadingRepository
                .findByPipelineIdAndReadingDateBetween(pipelineId, startDate, endDate)
                .stream().map(FlowReadingMapper::toReadDTO).toList();
    }

    @Override
    public List<ReadingSlot> findAllSlotsOrdered() {
        return readingSlotRepository.findAllByOrderByIdAsc();
    }

    @Override
    public Page<FlowReadingReadDTO> findPendingValidationsByStructure(Long structureId, Pageable pageable) {
        log.debug("FlowReadingFacadeImpl.findPendingValidationsByStructure structureId={}", structureId);
        return flowReadingRepository
                .findPendingValidationsByStructure(structureId, pageable)
                .map(FlowReadingMapper::toReadDTO);
    }

    @Override
    public Page<FlowReadingReadDTO> findOverdueReadingsByStructure(
            Long structureId, LocalDate asOfDate, LocalDateTime currentDateTime, Pageable pageable) {
        log.debug("FlowReadingFacadeImpl.findOverdueReadingsByStructure structureId={}", structureId);
        return flowReadingRepository
                .findOverdueReadingsByStructure(structureId, asOfDate, currentDateTime, pageable)
                .map(FlowReadingMapper::toReadDTO);
    }
}
