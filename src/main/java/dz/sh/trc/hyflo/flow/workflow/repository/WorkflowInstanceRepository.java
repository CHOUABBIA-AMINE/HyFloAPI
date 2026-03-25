/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : WorkflowInstanceRepository
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Flow / Workflow
 *
 *  @Description: Authoritative repository for WorkflowInstance state.
 *                WorkflowInstance is the source of truth for all reading lifecycle transitions.
 *                Injected into ReadingWorkflowService (Commit 21) for state mutation.
 *
 *  Phase 3 — Commit 25
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
 * WorkflowInstance is the source of truth for reading lifecycle state.
 * ValidationStatus (on FlowReading) is a compatibility projection maintained
 * in parallel for backward compatibility — not authoritative.
 *
 * Key query patterns:
 * - Single reading → workflow instance resolution
 * - State + time-window filtering for operational alerts
 * - Idempotency guards for transition services
 * - Dashboard KPI aggregation
 *
 * Phase 3 — Commit 25
 */
@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, Long> {

    /**
     * Find the workflow instance governing a specific flow reading.
     * Returns Optional because some readings may predate the workflow module.
     */
    Optional<WorkflowInstance> findByFlowReadingId(Long flowReadingId);

    /**
     * Find instances in a given state modified after a given time.
     * Used by operational alert detection (future Phase 4 analytics).
     */
    @Query("SELECT wi FROM WorkflowInstance wi "
            + "WHERE wi.currentStateCode = :stateCode "
            + "AND wi.updatedAt > :since "
            + "ORDER BY wi.updatedAt DESC")
    List<WorkflowInstance> findByCurrentStateCodeAndUpdatedAtAfter(
            @Param("stateCode") String stateCode,
            @Param("since") LocalDateTime since);

    /**
     * Idempotency guard — check if a reading already has a specific state.
     * Used before triggering transitions to prevent duplicate state writes.
     */
    boolean existsByFlowReadingIdAndCurrentStateCode(
            Long flowReadingId, String currentStateCode);

    /**
     * Latest workflow instance per reading — max by createdAt.
     * Handles rare cases of multiple WF instances per reading (re-open scenarios).
     */
    @Query("SELECT wi FROM WorkflowInstance wi "
            + "WHERE wi.flowReading.id = :readingId "
            + "ORDER BY wi.createdAt DESC")
    List<WorkflowInstance> findLatestByFlowReadingId(
            @Param("readingId") Long readingId,
            org.springframework.data.domain.Pageable pageable);

    /**
     * Count instances by current state — dashboard KPI support.
     * Example: count of SUBMITTED readings awaiting approval.
     */
    long countByCurrentStateCode(String stateCode);

    /**
     * Find all instances created within a date range — used by period-based reporting.
     */
    List<WorkflowInstance> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
