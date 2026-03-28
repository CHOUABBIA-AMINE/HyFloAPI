/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IFlowReadingFacade
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 — F2: replace FlowReadingDTO (v1) with FlowReadingReadDTO (v2)
 *                             in all method return types.
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Abstraction for flow reading queries exposed to the intelligence layer.
 *
 *                F2: all return types migrated from v1 FlowReadingDTO to v2 FlowReadingReadDTO.
 *                Implemented by FlowReadingFacade in flow/core.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade.impl;

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
 *
 * Implemented by {@link FlowReadingFacade} in flow/core.
 *
 * F2: all method signatures migrated from FlowReadingDTO (v1) to FlowReadingReadDTO (v2).
 * No v1 DTO types remain in this interface contract.
 */
public interface IFlowReadingFacade {

    /**
     * Find the latest approved reading for a pipeline.
     *
     * @param pipelineId the pipeline ID
     * @return optional v2 read DTO
     */
    Optional<FlowReadingReadDTO> findLatestByPipeline(Long pipelineId);

    /**
     * Find all readings for a pipeline on a specific date.
     *
     * @param pipelineId  the pipeline ID
     * @param readingDate the target date
     * @return list of v2 read DTOs
     */
    List<FlowReadingReadDTO> findByPipelineAndDate(Long pipelineId, LocalDate readingDate);

    /**
     * Find all readings for a pipeline within a date range, ordered by date ascending.
     *
     * @param pipelineId the pipeline ID
     * @param startDate  range start (inclusive)
     * @param endDate    range end (inclusive)
     * @return ordered list of v2 read DTOs
     */
    List<FlowReadingReadDTO> findByPipelineAndDateRangeOrdered(
            Long pipelineId, LocalDate startDate, LocalDate endDate);

    /**
     * Convenience alias for findByPipelineAndDateRangeOrdered.
     * Used by PipelineIntelligenceService.getDashboard().
     *
     * @param pipelineId the pipeline ID
     * @param startDate  range start (inclusive)
     * @param endDate    range end (inclusive)
     * @return ordered list of v2 read DTOs
     */
    List<FlowReadingReadDTO> findByPipelineAndDateRange(
            Long pipelineId, LocalDate startDate, LocalDate endDate);

    /**
     * Find all reading slots ordered by sequence.
     *
     * @return list of ReadingSlot (flow/common — not a flow/core entity reference)
     */
    List<ReadingSlot> findAllSlotsOrdered();

    /**
     * Find readings pending validation for a structure, paginated.
     *
     * @param structureId the structure ID
     * @param pageable    pagination parameters
     * @return page of v2 read DTOs
     */
    Page<FlowReadingReadDTO> findPendingValidationsByStructure(Long structureId, Pageable pageable);

    /**
     * Find overdue readings for a structure, paginated.
     *
     * @param structureId     the structure ID
     * @param asOfDate        the reference date
     * @param currentDateTime current datetime for overdue calculation
     * @param pageable        pagination parameters
     * @return page of v2 read DTOs
     */
    Page<FlowReadingReadDTO> findOverdueReadingsByStructure(
            Long structureId, LocalDate asOfDate, LocalDateTime currentDateTime, Pageable pageable);
}
