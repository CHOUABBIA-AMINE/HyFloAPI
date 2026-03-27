/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : SegmentDistributionService
 *  @CreatedOn  : 03-25-2026
 *  @MovedOn    : 03-28-2026 — refactor: flow.core.service → flow.workflow.service
 *
 *  @Type       : Interface
 *  @Layer      : Service (Workflow Orchestration)
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

/**
 * Internal orchestration service for distributing an approved FlowReading
 * into per-segment DerivedFlowReading records.
 * Moved to flow.workflow per HyFlo v2 architecture.
 */
public interface SegmentDistributionService {

    List<DerivedFlowReadingReadDTO> generateDerivedReadings(FlowReading sourceReading);

    CompletableFuture<List<DerivedFlowReadingReadDTO>> asyncGenerateDerivedReadings(
            FlowReading sourceReading);
}
