/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : WorkflowInstanceRepository
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 26.1 corrective
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Flow / Workflow
 *
 *  @Description: Authoritative repository for WorkflowInstance state.
 *
 *  OWNERSHIP MODEL:
 *  WorkflowInstance does NOT hold a FK back to domain entities.
 *  Domain entities (FlowReading, FlowOperation, etc.) hold the FK
 *  to their WorkflowInstance.
 *  Therefore this repository does NOT expose findByFlowReadingId.
 *  Callers must query via the owning entity's repository instead.
 *
 *  All query methods in this interface are verified against the
 *  actual WorkflowInstance field set:
 *    targetType     @ManyToOne WorkflowTargetType
 *    currentState   @ManyToOne WorkflowState
 *    startedAt      LocalDateTime (non-null)
 *    completedAt    LocalDateTime (nullable)
 *    initiatedBy    @ManyToOne Employee (nullable)
 *    lastActor      @ManyToOne Employee (nullable)
 *    comment        String (nullable)
 *    history        TEXT (nullable)
 *    id             Long (from GenericModel)
 *
 *  Commit 26.1 — post-Phase 3 corrective
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.workflow.model.WorkflowInstance;

/**
 * Repository for WorkflowInstance entities.
 *
 * <h3>Ownership note</h3>
 * WorkflowInstance is the lifecycle source of truth but does NOT
 * own a FK back to the domain entity it governs. Domain entities
 * ({@code FlowReading}, {@code FlowOperation}, etc.) hold a nullable
 * FK to their associated {@code WorkflowInstance}.
 *
 * To find the WorkflowInstance for a specific FlowReading, use:
 * <pre>
 *   FlowReadingRepository.findByWorkflowInstanceId(workflowInstanceId)
 * </pre>
 * or navigate: {@code flowReading.getWorkflowInstance()}.
 *
 * <h3>Query field safety</h3>
 * All derived method names and JPQL expressions in this interface
 * have been verified against the WorkflowInstance model as of
 * Commit 26.1. No phantom field references remain.
 */
@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, Long> {

    // ------------------------------------------------------------------
    // State-based queries — use Spring Data traversal on currentState FK
    // ------------------------------------------------------------------

    /**
     * Find all workflow instances currently in the given state code.
     * Uses Spring Data join traversal: currentState.code.
     *
     * Example: findByCurrentState_Code("APPROVED")
     */
    List<WorkflowInstance> findByCurrentState_Code(String stateCode);

    /**
     * Count workflow instances by current state code — dashboard KPI.
     * Spring Data traversal: currentState.code.
     *
     * Example: countByCurrentState_Code("SUBMITTED") → pending approvals.
     */
    long countByCurrentState_Code(String stateCode);

    /**
     * Check whether a specific workflow instance is in a given state.
     * Used as idempotency guard before triggering a transition.
     *
     * Note: to check if a FlowReading's workflow is in a given state,
     * resolve via: flowReading.getWorkflowInstance().getId() then call this.
     */
    boolean existsByIdAndCurrentState_Code(Long workflowInstanceId, String stateCode);

    // ------------------------------------------------------------------
    // Time-window queries — operational alert detection
    // ------------------------------------------------------------------

    /**
     * Find workflow instances in a given state that were started after
     * a given time. Used by operational alert detection logic.
     *
     * startedAt is used as the time anchor because it is always non-null
     * and reflects when the workflow process was opened.
     */
    @Query("SELECT wi FROM WorkflowInstance wi "
            + "JOIN wi.currentState ws "
            + "WHERE ws.code = :stateCode "
            + "AND wi.startedAt > :since "
            + "ORDER BY wi.startedAt DESC")
    List<WorkflowInstance> findByStateCodeAndStartedAfter(
            @Param("stateCode") String stateCode,
            @Param("since") LocalDateTime since);

    // ------------------------------------------------------------------
    // Process-type filter — target type code traversal
    // ------------------------------------------------------------------

    /**
     * Find all workflow instances for a specific process type.
     * Uses Spring Data traversal: targetType.code.
     *
     * Example: findByTargetType_Code("FLOW_READING_VALIDATION")
     */
    List<WorkflowInstance> findByTargetType_Code(String targetTypeCode);

    // ------------------------------------------------------------------
    // Period-based reporting
    // ------------------------------------------------------------------

    /**
     * Find all workflow instances opened within a date range.
     * Used by period-based reporting and audit trail queries.
     */
    List<WorkflowInstance> findByStartedAtBetween(LocalDateTime from, LocalDateTime to);

    /**
     * Find workflow instances completed within a date range.
     * completedAt is nullable — only terminal-state instances are returned.
     */
    List<WorkflowInstance> findByCompletedAtBetween(LocalDateTime from, LocalDateTime to);

    // ------------------------------------------------------------------
    // Actor-based queries
    // ------------------------------------------------------------------

    /**
     * Find all workflow instances initiated by a given employee.
     * Spring Data traversal: initiatedBy.id.
     */
    List<WorkflowInstance> findByInitiatedBy_Id(Long employeeId);

    /**
     * Find all workflow instances where the given employee performed
     * the most recent transition.
     * Spring Data traversal: lastActor.id.
     */
    List<WorkflowInstance> findByLastActor_Id(Long employeeId);

    // ------------------------------------------------------------------
    // Direct ID lookup (base JpaRepository covers findById)
    // ------------------------------------------------------------------

    /**
     * Find the most recent workflow instance for a given target type,
     * ordered by startedAt descending. Used to get the current active
     * workflow for a process type when only one is expected.
     */
    @Query("SELECT wi FROM WorkflowInstance wi "
            + "JOIN wi.targetType tt "
            + "WHERE tt.code = :targetTypeCode "
            + "ORDER BY wi.startedAt DESC")
    List<WorkflowInstance> findLatestByTargetTypeCode(
            @Param("targetTypeCode") String targetTypeCode,
            org.springframework.data.domain.Pageable pageable);
}
