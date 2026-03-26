/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingRepository
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 17/25: added Phase 3 service-support query methods
 *  @UpdatedOn  : 03-26-2026 — derived method names now resolve: readingSlot added to FlowReading
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowReading;

/**
 * Repository for FlowReading entities.
 *
 * Methods added in Phase 3 (Commit 17/25) support:
 * - Command/query service split (FlowReadingQueryServiceImpl)
 * - Segment distribution and workflow orchestration
 *
 * readingSlot was added to FlowReading (F_15, FK_05) to support
 * slot-scoped query methods below.
 */
@Repository
public interface FlowReadingRepository extends JpaRepository<FlowReading, Long> {

    // --- Base queries (pre-Phase 3) ---

    List<FlowReading> findByPipelineId(Long pipelineId);

    List<FlowReading> findByReadingDateBetween(LocalDate from, LocalDate to);

    List<FlowReading> findByPipelineIdAndReadingDateBetween(
            Long pipelineId, LocalDate from, LocalDate to);

    List<FlowReading> findByValidationStatusId(Long validationStatusId);

    @Query("SELECT r FROM FlowReading r WHERE "
            + "CAST(r.id AS string) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(r.notes) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<FlowReading> searchByAnyField(@Param("search") String search, Pageable pageable);

    // --- Phase 3 additions ---

    /** Used by FlowReadingQueryServiceImpl.getByPipelineAndSlot() */
    List<FlowReading> findByPipelineIdAndReadingSlotId(Long pipelineId, Long readingSlotId);

    /** Used by FlowReadingQueryServiceImpl.getAll(Pageable) with sorted pageable */
    Page<FlowReading> findByPipelineId(Long pipelineId, Pageable pageable);

    /** Exact slot+date lookup per pipeline — uniqueness support */
    List<FlowReading> findByPipelineIdAndReadingSlotIdAndReadingDate(
            Long pipelineId, Long readingSlotId, LocalDate date);

    /** Workflow linkage — used by ReadingWorkflowService */
    List<FlowReading> findByWorkflowInstanceId(Long workflowInstanceId);

    /** Latest reading per pipeline — used by dashboard endpoints */
    @Query("SELECT r FROM FlowReading r WHERE r.pipeline.id = :pipelineId "
            + "ORDER BY r.readingDate DESC")
    List<FlowReading> findLatestByPipelineId(
            @Param("pipelineId") Long pipelineId, Pageable pageable);

    /** Single latest reading for a pipeline — convenience method */
    Optional<FlowReading> findTopByPipelineIdOrderByReadingDateDesc(Long pipelineId);
}
