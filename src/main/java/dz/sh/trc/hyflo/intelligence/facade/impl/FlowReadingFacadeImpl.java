/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingFacadeImpl
 *  @CreatedOn  : 03-28-2026
 *  @UpdatedOn  : 03-28-2026 — fix: align all method calls with actual
 *                             FlowReadingRepository and ReadingSlotRepository signatures.
 *
 *  @Type       : Class
 *  @Layer      : Facade / Impl
 *  @Package    : Flow / Intelligence / Facade / Impl
 *
 *  Fixes applied:
 *  1. findTopByPipelineIdOrderByReadingDateDesc  → findTopByPipelineIdOrderByRecordedAtDesc
 *  2. findByPipelineIdAndReadingDateBetweenOrderByReadingDateAsc
 *                                               → findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc
 *  3. findAllByOrderByIdAsc                     → findAllByOrderByDisplayOrder
 *  4. findPendingValidationsByStructure         → findByStructureAndValidationStatus
 *                                                  (validationStatusId must be resolved via service)
 *
 **/

package dz.sh.trc.hyflo.intelligence.facade.impl;

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
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowReadingMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.intelligence.facade.FlowReadingFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowReadingFacadeImpl implements FlowReadingFacade {

    private static final String SUBMITTED_STATUS_CODE = "SUBMITTED";

    private final FlowReadingRepository flowReadingRepository;
    private final ReadingSlotRepository readingSlotRepository;
    private final ValidationStatusRepository validationStatusRepository;

    // -------------------------------------------------------------------------
    // FIX 1: was findTopByPipelineIdOrderByReadingDateDesc (does not exist)
    //         actual method: findTopByPipelineIdOrderByRecordedAtDesc
    // -------------------------------------------------------------------------
    @Override
    public Optional<FlowReadingReadDTO> findLatestByPipeline(Long pipelineId) {
        log.debug("FlowReadingFacadeImpl.findLatestByPipeline pipelineId={}", pipelineId);
        return flowReadingRepository.findTopByPipelineIdOrderByRecordedAtDesc(pipelineId)
                .map(FlowReadingMapper::toReadDTO);
    }

    @Override
    public List<FlowReadingReadDTO> findByPipelineAndDate(Long pipelineId, LocalDate readingDate) {
        log.debug("FlowReadingFacadeImpl.findByPipelineAndDate pipelineId={} date={}", pipelineId, readingDate);
        return flowReadingRepository.findByPipelineIdAndReadingDate(pipelineId, readingDate)
                .stream().map(FlowReadingMapper::toReadDTO).toList();
    }

    // -------------------------------------------------------------------------
    // FIX 2: was findByPipelineIdAndReadingDateBetweenOrderByReadingDateAsc (does not exist)
    //         actual method: findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc
    // -------------------------------------------------------------------------
    @Override
    public List<FlowReadingReadDTO> findByPipelineAndDateRangeOrdered(
            Long pipelineId, LocalDate startDate, LocalDate endDate) {
        log.debug("FlowReadingFacadeImpl.findByPipelineAndDateRangeOrdered pipelineId={}", pipelineId);
        return flowReadingRepository
                .findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc(
                        pipelineId, startDate, endDate)
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

    // -------------------------------------------------------------------------
    // FIX 3: was findAllByOrderByIdAsc (does not exist)
    //         actual method: findAllByOrderByDisplayOrder
    // -------------------------------------------------------------------------
    @Override
    public List<ReadingSlot> findAllSlotsOrdered() {
        return readingSlotRepository.findAllByOrderByDisplayOrder();
    }

    // -------------------------------------------------------------------------
    // FIX 4: was findPendingValidationsByStructure(Long, Pageable) (does not exist)
    //         actual method: findByStructureAndValidationStatus(Long, Long, Pageable)
    //         validationStatusId resolved by looking up the SUBMITTED code.
    //         structureId is passed through as documented (not yet applied at DB level).
    // -------------------------------------------------------------------------
    @Override
    public Page<FlowReadingReadDTO> findPendingValidationsByStructure(Long structureId, Pageable pageable) {
        log.debug("FlowReadingFacadeImpl.findPendingValidationsByStructure structureId={}", structureId);
        Long submittedStatusId = validationStatusRepository
                .findByCode(SUBMITTED_STATUS_CODE)
                .map(vs -> vs.getId())
                .orElse(-1L); // -1L yields empty page if SUBMITTED status not found
        return flowReadingRepository
                .findByStructureAndValidationStatus(structureId, submittedStatusId, pageable)
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
