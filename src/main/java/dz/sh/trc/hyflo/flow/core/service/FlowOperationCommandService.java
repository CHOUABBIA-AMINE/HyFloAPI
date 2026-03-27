/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationCommandService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : Phase 4/5 bridge — Commit 37
 *
 *  @Type       : Interface
 *  @Layer      : Service (Command)
 *  @Package    : Flow / Core
 *
 *  Write contract for FlowOperation domain.
 *  Separates write operations from read operations per CQRS pattern.
 *
 *  Commit 26.2 — post-Phase 3 corrective
 *  Commit 37   — create/update signatures migrated from FlowOperationDTO
 *                to FlowOperationCommandDTO (write-only command DTO).
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.FlowOperationCommandDTO;

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
 * All methods return {@link FlowOperationReadDTO} — never raw entities.
 * Input uses {@link FlowOperationCommandDTO} for write-only command operations
 * (Commit 37 migration from fat FlowOperationDTO in v2 path).
 *
 * <h3>DTO separation</h3>
 * • {@link FlowOperationCommandDTO}  — write path (v2 controller create/update)
 * • {@link FlowOperationReadDTO}     — read path (all responses)
 * • Legacy {@code FlowOperationDTO}  — retained ONLY for legacy FlowOperationController
 */
public interface FlowOperationCommandService {

    /**
     * Create a new flow operation.
     * Enforces date+infrastructure+product+type uniqueness invariant.
     * PENDING validation status is resolved internally.
     *
     * @param dto write-only command DTO from v2 controller
     * @return created operation as read DTO
     */
    FlowOperationReadDTO create(FlowOperationCommandDTO dto);

    /**
     * Update an existing flow operation.
     * Only PENDING operations may be updated.
     *
     * @param id  operation ID
     * @param dto write-only command DTO with updated fields
     * @return updated operation as read DTO
     */
    FlowOperationReadDTO update(Long id, FlowOperationCommandDTO dto);

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
    FlowOperationReadDTO approve(Long id, Long validatorId);

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
    FlowOperationReadDTO reject(Long id, Long validatorId, String rejectionReason);
}
