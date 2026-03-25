/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DerivedFlowReadingQueryService
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Core
 *
 *  @Description: Query contract for DerivedFlowReading read operations.
 *                Returns only DerivedFlowReadingReadDto — never raw entities.
 *
 *  Phase 3 — Commit 18
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.util.List;

import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDto;

/**
 * Query contract for DerivedFlowReading read operations.
 *
 * Phase 3 — Commit 18
 */
public interface DerivedFlowReadingQueryService {

    DerivedFlowReadingReadDto getById(Long id);

    List<DerivedFlowReadingReadDto> getBySourceReading(Long sourceReadingId);

    List<DerivedFlowReadingReadDto> getBySegment(Long pipelineSegmentId);

    List<DerivedFlowReadingReadDto> getBySegmentAndDateRange(
            Long pipelineSegmentId, LocalDate from, LocalDate to);

    List<DerivedFlowReadingReadDto> getBySegmentAndSlot(
            Long pipelineSegmentId, Long readingSlotId);

    List<DerivedFlowReadingReadDto> getByDateRange(LocalDate from, LocalDate to);
}
