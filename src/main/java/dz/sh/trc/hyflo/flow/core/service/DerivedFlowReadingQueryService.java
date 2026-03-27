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
 *                Returns only DerivedFlowReadingReadDTO — never raw entities.
 *
 *  Phase 3 — Commit 18
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.util.List;

import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDTO;

/**
 * Query contract for DerivedFlowReading read operations.
 *
 * Phase 3 — Commit 18
 */
public interface DerivedFlowReadingQueryService {

    DerivedFlowReadingReadDTO getById(Long id);

    List<DerivedFlowReadingReadDTO> getBySourceReading(Long sourceReadingId);

    List<DerivedFlowReadingReadDTO> getBySegment(Long pipelineSegmentId);

    List<DerivedFlowReadingReadDTO> getBySegmentAndDateRange(
            Long pipelineSegmentId, LocalDate from, LocalDate to);

    List<DerivedFlowReadingReadDTO> getBySegmentAndSlot(
            Long pipelineSegmentId, Long readingSlotId);

    List<DerivedFlowReadingReadDTO> getByDateRange(LocalDate from, LocalDate to);
}
