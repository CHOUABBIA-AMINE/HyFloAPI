/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationWorkflowService
 *  @CreatedOn  : 03-28-2026 — extracted from FlowOperationCommandService (workflow refactor)
 *
 *  @Type       : Interface
 *  @Layer      : Service (Workflow)
 *  @Package    : Flow / Workflow
 *
 *  @Description: Workflow lifecycle contract for FlowOperation.
 *                Handles approve and reject transitions.
 *                These were extracted from FlowOperationCommandService
 *                per HyFlo v2 architecture (core must not contain workflow logic).
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service;

import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDTO;

/**
 * Workflow lifecycle service for FlowOperation.
 * Covers approval and rejection transitions.
 * Extracted from FlowOperationCommandService per HyFlo v2 core/workflow separation.
 */
public interface FlowOperationWorkflowService {

    /**
     * Approve a pending FlowOperation.
     * Transitions status from PENDING to VALIDATED.
     * Updates the linked WorkflowInstance.
     *
     * @param id          FlowOperation ID
     * @param validatorId Employee ID performing the approval
     * @return Updated FlowOperationReadDTO
     * @throws dz.sh.trc.hyflo.exception.ResourceNotFoundException if operation or employee not found
     * @throws dz.sh.trc.hyflo.exception.BusinessValidationException if not in PENDING state
     */
    FlowOperationReadDTO approve(Long id, Long validatorId);

    /**
     * Reject a pending FlowOperation.
     * Transitions status from PENDING to REJECTED.
     * Appends rejection reason to audit notes.
     *
     * @param id          FlowOperation ID
     * @param validatorId Employee ID performing the rejection
     * @param reason      Mandatory rejection reason
     * @return Updated FlowOperationReadDTO
     * @throws dz.sh.trc.hyflo.exception.ResourceNotFoundException if operation or employee not found
     * @throws dz.sh.trc.hyflo.exception.BusinessValidationException if not in a rejectable state
     */
    FlowOperationReadDTO reject(Long id, Long validatorId, String reason);
}
