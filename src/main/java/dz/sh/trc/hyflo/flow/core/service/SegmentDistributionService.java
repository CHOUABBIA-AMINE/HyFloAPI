/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : SegmentDistributionService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 — H4: add async trigger method for fire-and-forget generation
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
 *  Phase 4 — H4: asyncGenerateDerivedReadings() added for non-blocking approval flow
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
 * H4: asyncGenerateDerivedReadings() executes generation on the taskExecutor thread pool,
 * freeing the approval HTTP thread immediately after state persistence.
 *
 * Phase 3 — Commit 19
 * Phase 4 — H4
 */
public interface SegmentDistributionService {

    /**
     * Generate DerivedFlowReading for every active PipelineSegment synchronously.
     *
     * @param sourceReading the approved FlowReading entity
     * @return list of generated derived reading DTOs
     */
    List<DerivedFlowReadingReadDto> generateDerivedReadings(FlowReading sourceReading);

    /**
     * H4: Asynchronous entry point for derived reading generation.
     * Executes generateDerivedReadings() on the configured taskExecutor thread pool.
     * Returns a CompletableFuture for optional caller-side monitoring.
     * Failure does NOT propagate to the caller thread.
     *
     * @param sourceReading the approved FlowReading entity
     * @return CompletableFuture that completes when generation is done or fails
     */
    CompletableFuture<List<DerivedFlowReadingReadDto>> asyncGenerateDerivedReadings(
            FlowReading sourceReading);
}
