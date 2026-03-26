/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingRepository
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 17/25: added Phase 3 service-support query methods
 *  @UpdatedOn  : 03-26-2026 — fix: remove readingSlotId derived queries;
 *                              FlowReading has no readingSlot field.
 *                              Add @Query equivalents for facade + monitoring.
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
 * IMPORTANT — FlowReading fields available for derived queries:
 *   readingDate, volumeM3, volumeMscf, inletPressureBar, outletPressureBar,
 *   temperatureCelsius, notes, submittedAt, validatedAt,
 *   pipeline (FK), validationStatus (FK), workflowInstance (FK),
 *   dataSource (FK), version
 *
 * There is NO readingSlot field on FlowReading.
 * Slot-based filtering must use @Query with explicit joins or
 * be deferred until a readingSlot FK is added to the entity.
 */
@Repository
public interface FlowReadingRepository extends JpaRepository<FlowReading, Long> {

    // =========================================================
    // BASE QUERIES
    // =========================================================

    List<FlowReading> findByPipelineId(Long pipelineId);

    List<FlowReading> findByReadingDateBetween(LocalDate from, LocalDate to);

    List<FlowReading> findByPipelineIdAndReadingDateBetween(
            Long pipelineId, LocalDate from, LocalDate to);

    List<FlowReading> findByValidationStatusId(Long validationStatusId);

    @Query("SELECT r FROM FlowReading r WHERE "
            + "CAST(r.id AS string) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(r.notes) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<FlowReading> searchByAnyField(@Param("search") String search, Pageable pageable);

    // =========================================================
    // PHASE 3 — SERVICE SUPPORT
    // =========================================================

    /**
     * Paginated list ordered by date desc — used by
     * FlowReadingQueryServiceImpl.getLatestByPipeline().
     */
    Page<FlowReading> findByPipelineId(Long pipelineId, Pageable pageable);

    /**
     * Exact date lookup per pipeline — uniqueness support.
     * Replaces the removed findByPipelineIdAndReadingSlotId variant.
     */
    List<FlowReading> findByPipelineIdAndReadingDate(Long pipelineId, LocalDate date);

    /**
     * Workflow linkage — used by ReadingWorkflowService.
     */
    List<FlowReading> findByWorkflowInstanceId(Long workflowInstanceId);

    /**
     * Latest reading per pipeline — used by dashboard endpoints.
     */
    @Query("SELECT r FROM FlowReading r WHERE r.pipeline.id = :pipelineId "
            + "ORDER BY r.readingDate DESC")
    List<FlowReading> findLatestByPipelineId(
            @Param("pipelineId") Long pipelineId, Pageable pageable);

    // =========================================================
    // FACADE SUPPORT — used by FlowReadingFacade
    // =========================================================

    /**
     * Single latest reading for a pipeline — used by
     * FlowReadingFacade.findLatestByPipeline().
     */
    Optional<FlowReading> findTopByPipelineIdOrderByRecordedAtDesc(Long pipelineId);

    /**
     * Ordered date-range query for time-series dashboard.
     * Used by FlowReadingFacade.findByPipelineAndDateRangeOrdered().
     */
    @Query("SELECT r FROM FlowReading r "
            + "WHERE r.pipeline.id = :pipelineId "
            + "AND r.readingDate BETWEEN :startDate AND :endDate "
            + "ORDER BY r.readingDate ASC, r.submittedAt ASC")
    List<FlowReading> findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc(
            @Param("pipelineId") Long pipelineId,
            @Param("startDate")  LocalDate startDate,
            @Param("endDate")    LocalDate endDate);

    // =========================================================
    // MONITORING SUPPORT — used by FlowReadingFacade
    // =========================================================

    /**
     * Readings in SUBMITTED status for a structure (pending validation).
     * Used by FlowReadingFacade.findPendingValidationsByStructure().
     *
     * Note: FlowReading does not carry a direct structure FK.
     * Pending validations are queried by validationStatus ID only;
     * the structureId parameter is accepted for API compatibility
     * but not yet applied at DB level until a structure FK is added
     * to FlowReading.
     *
     * TODO: add structure FK to FlowReading when operator-structure
     *       assignment is modelled at the reading level.
     */
    @Query("SELECT r FROM FlowReading r "
            + "WHERE r.validationStatus.id = :validationStatusId")
    Page<FlowReading> findByStructureAndValidationStatus(
            @Param("structureId")       Long structureId,
            @Param("validationStatusId") Long validationStatusId,
            Pageable pageable);

    /**
     * Overdue readings for a structure.
     * A reading is overdue when its readingDate <= asOfDate AND
     * validatedAt IS NULL AND submittedAt < currentDateTime (past deadline).
     *
     * Same note as above: structureId not yet applied at DB level.
     */
    @Query("SELECT r FROM FlowReading r "
            + "WHERE r.readingDate <= :asOfDate "
            + "AND r.validatedAt IS NULL "
            + "AND r.submittedAt < :currentDateTime")
    Page<FlowReading> findOverdueReadingsByStructure(
            @Param("structureId")      Long structureId,
            @Param("asOfDate")         LocalDate asOfDate,
            @Param("currentDateTime")  LocalDateTime currentDateTime,
            Pageable pageable);
}
