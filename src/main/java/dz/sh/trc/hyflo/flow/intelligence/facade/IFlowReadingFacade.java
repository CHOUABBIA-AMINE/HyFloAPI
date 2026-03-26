package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;

/**
 * Abstraction for flow reading queries exposed to the intelligence layer.
 *
 * Implemented by {@link FlowReadingFacade}.
 */
public interface IFlowReadingFacade {

    Optional<FlowReadingDTO> findLatestByPipeline(Long pipelineId);

    List<FlowReadingDTO> findByPipelineAndDate(Long pipelineId, LocalDate readingDate);

    List<FlowReadingDTO> findByPipelineAndDateRangeOrdered(
            Long pipelineId, LocalDate startDate, LocalDate endDate);

    List<ReadingSlot> findAllSlotsOrdered();

    Page<FlowReadingDTO> findPendingValidationsByStructure(Long structureId, Pageable pageable);

    Page<FlowReadingDTO> findOverdueReadingsByStructure(
            Long structureId, LocalDate asOfDate, LocalDateTime currentDateTime, Pageable pageable);
}
