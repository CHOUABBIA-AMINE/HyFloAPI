/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationWorkflowService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service (Workflow)
 *  @Package    : Flow / Workflow
 *
 *  @Description: Workflow lifecycle contract for FlowOperation.
 *                Owns approve and reject transitions, extracted from flow.core
 *                per HyFlo v2 architecture.
 *
 *                approve: PENDING → VALIDATED
 *                reject:  PENDING → REJECTED
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service;

import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDTO;

public interface FlowOperationWorkflowService {

    /**
     * Approve a PENDING flow operation.
     * Transitions status: PENDING → VALIDATED.
     * Updates linked WorkflowInstance if present.
     *
     * @param id          FlowOperation ID
     * @param validatorId Employee ID performing the approval
     * @return updated FlowOperationReadDTO
     */
    FlowOperationReadDTO approve(Long id, Long validatorId);

    /**
     * Reject a PENDING flow operation.
     * Transitions status: PENDING → REJECTED.
     * Appends rejection reason to audit notes.
     * Updates linked WorkflowInstance if present.
     *
     * @param id              FlowOperation ID
     * @param validatorId     Employee ID performing the rejection
     * @param rejectionReason Mandatory human-readable rejection justification
     * @return updated FlowOperationReadDTO
     */
    FlowOperationReadDTO reject(Long id, Long validatorId, String rejectionReason);
}
