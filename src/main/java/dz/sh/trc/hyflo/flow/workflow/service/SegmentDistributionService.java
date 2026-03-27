/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : SegmentDistributionService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: moved from flow.core.service → flow.workflow.service
 *
 *  @Type       : Interface
 *  @Layer      : Service (Internal Orchestration)
 *  @Package    : Flow / Workflow
 *
 *  @Description: Internal orchestration service for distributing an approved FlowReading
 *                into per-segment DerivedFlowReading records.
 *                NOT exposed via any controller.
 *                Called exclusively by ReadingWorkflowService after APPROVED state.
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;

public interface SegmentDistributionService {

    /**
     * Generate DerivedFlowReading for every active PipelineSegment synchronously.
     */
    List<DerivedFlowReadingReadDTO> generateDerivedReadings(FlowReading sourceReading);

    /**
     * Asynchronous entry point for derived reading generation.
     * Executes on the configured taskExecutor thread pool.
     */
    CompletableFuture<List<DerivedFlowReadingReadDTO>> asyncGenerateDerivedReadings(
            FlowReading sourceReading);
}
