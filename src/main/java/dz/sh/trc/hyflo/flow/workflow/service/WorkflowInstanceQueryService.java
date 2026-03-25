/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : WorkflowInstanceQueryService
 *  @CreatedOn  : Phase 4 — Commit 30
 *
 *  @Type       : Interface
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Workflow
 *
 *  @Description: Query contract for WorkflowInstance read operations.
 *                Backed by WorkflowInstanceRepository (Commit 26.1 verified).
 *                Returns WorkflowInstanceReadDto — never raw entities.
 *
 *  Phase 4 — Commit 30 (minimal support addition for controller)
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service;

import dz.sh.trc.hyflo.flow.workflow.dto.query.WorkflowInstanceReadDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Query contract for WorkflowInstance read operations.
 *
 * All methods map directly to WorkflowInstanceRepository queries
 * verified in Commit 26.1.
 *
 * Phase 4 — Commit 30
 */
public interface WorkflowInstanceQueryService {

    /**
     * Get a workflow instance by its unique identifier.
     */
    WorkflowInstanceReadDto getById(Long id);

    /**
     * List all workflow instances in a given state code.
     * Example states: SUBMITTED, APPROVED, REJECTED.
     */
    List<WorkflowInstanceReadDto> getByState(String stateCode);

    /**
     * List all workflow instances initiated by a specific employee.
     */
    List<WorkflowInstanceReadDto> getByInitiatingActor(Long employeeId);

    /**
     * List all workflow instances where a specific employee
     * performed the most recent state transition.
     */
    List<WorkflowInstanceReadDto> getByLastActor(Long employeeId);

    /**
     * List all workflow instances opened within a date range.
     * Uses startedAt as the time anchor.
     */
    List<WorkflowInstanceReadDto> getByDateRange(LocalDate from, LocalDate to);

    /**
     * List all workflow instances for a given target type code.
     * Example: FLOW_READING_VALIDATION
     */
    List<WorkflowInstanceReadDto> getByTargetType(String targetTypeCode);
}
