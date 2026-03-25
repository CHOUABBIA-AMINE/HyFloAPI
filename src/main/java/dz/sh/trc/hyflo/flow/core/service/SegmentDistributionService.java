/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : SegmentDistributionService
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service (Internal Orchestration)
 *  @Package    : Flow / Core
 *
 *  @Description: Internal orchestration service for distributing an approved FlowReading
 *                into per-segment DerivedFlowReading records.
 *
 *                NOT exposed via any controller.
 *                Called exclusively by ReadingWorkflowService after APPROVED state.
 *
 *                Generation strategy: delete-and-rebuild for the same sourceReadingId.
 *                Idempotent on repeat approval calls.
 *
 *  Phase 3 — Commit 19
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.util.List;

import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;

/**
 * Internal orchestration service for distributing an approved FlowReading
 * into per-segment DerivedFlowReading records.
 *
 * NOT exposed via any controller.
 * Called exclusively by ReadingWorkflowService (Commit 22) after APPROVED terminal state.
 *
 * Generation strategy: delete-and-rebuild for the same sourceReadingId.
 * Idempotent on repeat approval calls.
 *
 * Phase 3 — Commit 19
 */
public interface SegmentDistributionService {

    /**
     * Generate DerivedFlowReading for every active PipelineSegment
     * belonging to the reading's pipeline.
     *
     * @param sourceReading the approved FlowReading entity
     * @return list of generated derived reading DTOs
     */
    List<DerivedFlowReadingReadDto> generateDerivedReadings(FlowReading sourceReading);
}
