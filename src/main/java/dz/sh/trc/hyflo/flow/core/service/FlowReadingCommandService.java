/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingCommandService
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service (Command)
 *  @Package    : Flow / Core
 *
 *  @Description: Command contract for FlowReading write operations.
 *                Handles create / update / delete / workflow attachment.
 *                All methods return FlowReadingReadDTO — never raw entities.
 *                Invariant enforcement lives here, not in entities or controllers.
 *
 *  Phase 3 — Commit 16
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.FlowReadingCommandDTO;

/**
 * Command contract for FlowReading write operations.
 *
 * Handles create / update / delete / workflow attachment.
 * All methods return FlowReadingReadDTO — never raw entities.
 * Invariant enforcement lives here, not in entities or controllers.
 *
 * Phase 3 — Commit 16
 */
public interface FlowReadingCommandService {

    /**
     * Record a new direct operational reading for a pipeline.
     * Attaches DataSource and optional WorkflowInstance reference.
     * Validates pipeline exists and readable target type is allowed.
     */
    FlowReadingReadDTO createReading(FlowReadingCommandDTO command);

    /**
     * Update a direct reading that has not yet been submitted to workflow.
     * Rejected or draft readings may be corrected here.
     */
    FlowReadingReadDTO updateReading(Long id, FlowReadingCommandDTO command);

    /**
     * Soft-delete or hard-delete a reading.
     * Deletion is only allowed if reading has not been approved.
     * Throws IllegalStateException if reading is in a terminal approved state.
     */
    void deleteReading(Long id);

    /**
     * Submit reading into the workflow lifecycle.
     * Attaches or creates WorkflowInstance.
     * Transitions status to SUBMITTED.
     */
    FlowReadingReadDTO submitForWorkflow(Long id, Long submittedById);
}
