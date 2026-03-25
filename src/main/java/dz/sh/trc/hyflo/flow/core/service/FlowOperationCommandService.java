/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationCommandService
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service (Command)
 *  @Package    : Flow / Core
 *
 *  Write contract for FlowOperation domain.
 *  Separates write operations from read operations per CQRS pattern.
 *
 *  Commit 26.2 — post-Phase 3 corrective
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import dz.sh.trc.hyflo.flow.core.dto.FlowOperationDTO;
import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDto;

/**
 * Command service contract for {@link dz.sh.trc.hyflo.flow.core.model.FlowOperation}.
 *
 * <h3>Responsibilities</h3>
 * <ul>
 *   <li>Create, update, delete flow operations</li>
 *   <li>Submit operation for workflow validation</li>
 *   <li>Approve (validate) a submitted operation</li>
 *   <li>Reject a submitted operation with mandatory reason</li>
 * </ul>
 *
 * <h3>Return contract</h3>
 * All methods return {@link FlowOperationReadDto} — never raw entities.
 * Input uses {@link FlowOperationDTO} for backward compatibility
 * with existing controller layer (Phase 4 will migrate to command DTOs).
 */
public interface FlowOperationCommandService {

    /**
     * Create a new flow operation.
     * Enforces date+infrastructure+product+type uniqueness invariant.
     *
     * @param dto command DTO from controller (legacy DTO for Phase 3 compat)
     * @return created operation as read DTO
     */
    FlowOperationReadDto create(FlowOperationDTO dto);

    /**
     * Update an existing flow operation.
     * Only PENDING operations may be updated.
     *
     * @param id  operation ID
     * @param dto patch DTO
     * @return updated operation as read DTO
     */
    FlowOperationReadDto update(Long id, FlowOperationDTO dto);

    /**
     * Delete a flow operation.
     * Only PENDING operations may be deleted.
     *
     * @param id operation ID
     */
    void delete(Long id);

    /**
     * Approve (validate) a PENDING flow operation.
     * Transitions status from PENDING → VALIDATED.
     * Updates linked WorkflowInstance if present.
     *
     * @param id          operation ID
     * @param validatorId employee performing the approval
     * @return updated operation as read DTO
     */
    FlowOperationReadDto approve(Long id, Long validatorId);

    /**
     * Reject a PENDING flow operation.
     * Transitions status from PENDING → REJECTED.
     * Appends rejection reason to notes.
     * Updates linked WorkflowInstance if present.
     *
     * @param id              operation ID
     * @param validatorId     employee performing the rejection
     * @param rejectionReason mandatory rejection reason
     * @return updated operation as read DTO
     */
    FlowOperationReadDto reject(Long id, Long validatorId, String rejectionReason);
}
