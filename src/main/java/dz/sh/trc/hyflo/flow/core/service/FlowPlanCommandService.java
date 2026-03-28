/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanCommandService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service (Command)
 *  @Package    : Flow / Core
 *
 *  @Description: Command contract for FlowPlan write operations.
 *                Status transition rules:
 *                  DRAFT → APPROVED  (approve)
 *                  APPROVED → SUPERSEDED  (supersede)
 *                  delete: only allowed on DRAFT.
 *                All methods return FlowPlanReadDTO — never raw entities.
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import dz.sh.trc.hyflo.flow.core.dto.FlowPlanReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.FlowPlanCommandDTO;

/**
 * Command contract for FlowPlan write operations.
 * All methods return FlowPlanReadDTO — never raw entities.
 */
public interface FlowPlanCommandService {

    /**
     * Create a new management plan for a pipeline.
     * Pipeline and PlanStatus must exist.
     * Initial status is set to the statusId provided (must be DRAFT).
     */
    FlowPlanReadDTO create(FlowPlanCommandDTO command);

    /**
     * Update a plan that is still in DRAFT status.
     * Throws IllegalStateException if plan is APPROVED or SUPERSEDED.
     */
    FlowPlanReadDTO update(Long id, FlowPlanCommandDTO command);

    /**
     * Approve a DRAFT plan.
     * Transitions status: DRAFT → APPROVED.
     * Sets approvedAt and approvedBy.
     * Throws IllegalStateException if plan is not in DRAFT.
     */
    FlowPlanReadDTO approve(Long id, Long approverEmployeeId);

    /**
     * Supersede an APPROVED plan.
     * Transitions status: APPROVED → SUPERSEDED.
     * Throws IllegalStateException if plan is not APPROVED.
     */
    FlowPlanReadDTO supersede(Long id);

    /**
     * Delete a plan.
     * Only allowed when status is DRAFT.
     * Throws IllegalStateException if plan is APPROVED or SUPERSEDED.
     */
    void delete(Long id);
}
