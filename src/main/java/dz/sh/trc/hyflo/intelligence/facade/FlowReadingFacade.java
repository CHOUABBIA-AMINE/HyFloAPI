/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingFacade
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-28-2026 — CRITICAL refactor: was impl class, now interface.
 *                             Impl moved to facade.impl.FlowReadingFacadeImpl
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Intelligence / Facade
 *
 **/

package dz.sh.trc.hyflo.intelligence.facade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;

/**
 * Abstraction for flow reading queries exposed to the intelligence layer.
 * Implemented by FlowReadingFacadeImpl in facade.impl.
 */
public interface FlowReadingFacade {

    Optional<FlowReadingReadDTO> findLatestByPipeline(Long pipelineId);

    List<FlowReadingReadDTO> findByPipelineAndDate(Long pipelineId, LocalDate readingDate);

    List<FlowReadingReadDTO> findByPipelineAndDateRangeOrdered(
            Long pipelineId, LocalDate startDate, LocalDate endDate);

    List<FlowReadingReadDTO> findByPipelineAndDateRange(
            Long pipelineId, LocalDate startDate, LocalDate endDate);

    List<ReadingSlot> findAllSlotsOrdered();

    Page<FlowReadingReadDTO> findPendingValidationsByStructure(Long structureId, Pageable pageable);

    Page<FlowReadingReadDTO> findOverdueReadingsByStructure(
            Long structureId, LocalDate asOfDate, LocalDateTime currentDateTime, Pageable pageable);
}
