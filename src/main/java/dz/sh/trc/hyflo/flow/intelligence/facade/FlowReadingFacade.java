/**
 *
 * 	@Author		: MEDJERAB Abir (original)
 * 	@UpdatedBy	: MEDJERAB Abir
 * 	@UpdatedBy	: HyFlo v2 — F2 migration
 *
 * 	@Name		: FlowReadingFacade
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 02-10-2026 - Added monitoring methods (Phase 1 refactoring)
 * 	@UpdatedOn	: 02-10-2026 - Phase 2: Return DTOs instead of entities
 * 	@UpdatedOn	: 03-26-2026 - F2: Migrate fromEntity() to FlowReadingMapper.toReadDto()
 *                              All methods now return FlowReadingReadDto (v2).
 *                              FlowReadingDTO (bridge) import removed.
 *
 * 	@Type		: Class
 * 	@Layer		: Facade
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Facade providing intelligence module with controlled access
 * 	              to flow readings and reading slots.
 *
 * 	@Pattern: Facade Pattern - Wraps FlowReadingRepository and
 * 	          ReadingSlotRepository to provide simplified interface
 * 	          and enforce module boundaries.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ReadingSlotRepository;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.mapper.FlowReadingMapper;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Facade providing intelligence module with controlled access to flow readings
 * and reading slots, wrapping FlowReadingRepository and ReadingSlotRepository.
 *
 * All entity→DTO conversion delegates to FlowReadingMapper.toReadDto().
 * No fromEntity() calls remain in this class.
 *
 * All methods are read-only (@Transactional(readOnly = true)) since
 * the intelligence module only queries data, never modifies it.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FlowReadingFacade implements IFlowReadingFacade {

    private final FlowReadingRepository        flowReadingRepository;
    private final ReadingSlotRepository        readingSlotRepository;
    private final ValidationStatusRepository   validationStatusRepository;

    // ========== BASIC QUERY METHODS ==========

    @Override
    public Optional<FlowReadingReadDto> findLatestByPipeline(Long pipelineId) {
        log.debug("Finding latest flow reading for pipeline: {}", pipelineId);
        return flowReadingRepository.findTopByPipelineIdOrderByRecordedAtDesc(pipelineId)
                .map(FlowReadingMapper::toReadDto);
    }

    @Override
    public List<FlowReadingReadDto> findByPipelineAndDate(Long pipelineId, LocalDate readingDate) {
        log.debug("Finding flow readings for pipeline {} on date {}", pipelineId, readingDate);
        return flowReadingRepository.findByPipelineIdAndReadingDate(pipelineId, readingDate)
                .stream()
                .map(FlowReadingMapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowReadingReadDto> findByPipelineAndDateRangeOrdered(
            Long pipelineId, LocalDate startDate, LocalDate endDate) {
        log.debug("Finding ordered flow readings for pipeline {} between {} and {}",
                  pipelineId, startDate, endDate);
        return flowReadingRepository
                .findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc(
                        pipelineId, startDate, endDate)
                .stream()
                .map(FlowReadingMapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowReadingReadDto> findByPipelineAndDateRange(
            Long pipelineId, LocalDate startDate, LocalDate endDate) {
        return findByPipelineAndDateRangeOrdered(pipelineId, startDate, endDate);
    }

    @Override
    public List<ReadingSlot> findAllSlotsOrdered() {
        log.debug("Finding all reading slots ordered by display order");
        return readingSlotRepository.findAllByOrderByDisplayOrder();
    }

    // ========== MONITORING QUERY METHODS ==========

    @Override
    public Page<FlowReadingReadDto> findPendingValidationsByStructure(
            Long structureId, Pageable pageable) {
        log.debug("Finding pending validations for structure: {}", structureId);

        ValidationStatus submittedStatus = validationStatusRepository.findByCode("SUBMITTED")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SUBMITTED validation status not found in database. "
                        + "Please ensure reference data is properly initialized."));

        Page<FlowReading> entityPage = flowReadingRepository.findByStructureAndValidationStatus(
                structureId, submittedStatus.getId(), pageable);

        List<FlowReadingReadDto> dtoList = entityPage.getContent()
                .stream()
                .map(FlowReadingMapper::toReadDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    @Override
    public Page<FlowReadingReadDto> findOverdueReadingsByStructure(
            Long structureId,
            LocalDate asOfDate,
            LocalDateTime currentDateTime,
            Pageable pageable) {
        log.debug("Finding overdue readings for structure: {} as of date: {} at time: {}",
                  structureId, asOfDate, currentDateTime);

        Page<FlowReading> entityPage = flowReadingRepository.findOverdueReadingsByStructure(
                structureId, asOfDate, currentDateTime, pageable);

        List<FlowReadingReadDto> dtoList = entityPage.getContent()
                .stream()
                .map(FlowReadingMapper::toReadDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }
}
