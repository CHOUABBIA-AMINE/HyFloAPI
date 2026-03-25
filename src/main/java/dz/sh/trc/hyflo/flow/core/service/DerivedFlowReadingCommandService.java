/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DerivedFlowReadingCommandService
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service (Command — System Internal)
 *  @Package    : Flow / Core
 *
 *  @Description: SYSTEM-INTERNAL write contract for DerivedFlowReading.
 *                NOT user-facing. Invoked by SegmentDistributionService and
 *                workflow orchestrator only.
 *                Operators do not directly create derived readings.
 *
 *  Phase 3 — Commit 18
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.util.List;

import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.dto.command.DerivedFlowReadingCommandDto;

/**
 * SYSTEM-INTERNAL write contract for DerivedFlowReading.
 *
 * This service is NOT user-facing. It is invoked by SegmentDistributionService
 * (Commit 19) and the workflow orchestrator (Commit 22) only.
 *
 * Operators do not directly create derived readings.
 *
 * Phase 3 — Commit 18
 */
public interface DerivedFlowReadingCommandService {

    /** Create one system-generated derived reading for a segment. */
    DerivedFlowReadingReadDto createDerivedReading(DerivedFlowReadingCommandDto command);

    /**
     * Rebuild all derived readings for a given source reading.
     * Deletes existing derived readings for that sourceReadingId,
     * then persists the new batch.
     * Called by SegmentDistributionService on re-approval (idempotent).
     */
    List<DerivedFlowReadingReadDto> rebuildForSourceReading(
            Long sourceReadingId, List<DerivedFlowReadingCommandDto> newDerived);

    /** Delete all derived readings generated from a given source reading. */
    void deleteBySourceReading(Long sourceReadingId);
}
