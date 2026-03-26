/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DerivedFlowReadingRepository
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 18/25: added Phase 3 service-support query methods
 *  @UpdatedOn  : 03-26-2026 — fix JPQL d.readingSlotId → d.readingSlot.id
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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.model.DerivedFlowReading;

/**
 * Repository for DerivedFlowReading entities.
 *
 * Methods added in Phase 3 (Commit 18/25) support:
 * - DerivedFlowReadingCommandServiceImpl (rebuildForSourceReading)
 * - DerivedFlowReadingQueryServiceImpl (all query methods)
 * - SegmentDistributionServiceImpl (idempotency check)
 */
@Repository
public interface DerivedFlowReadingRepository extends JpaRepository<DerivedFlowReading, Long> {

    // --- Base queries (pre-Phase 3) ---

    List<DerivedFlowReading> findByPipelineSegmentId(Long pipelineSegmentId);

    List<DerivedFlowReading> findBySourceReadingId(Long sourceReadingId);

    List<DerivedFlowReading> findByPipelineSegmentIdAndReadingDateBetween(
            Long pipelineSegmentId, LocalDate from, LocalDate to);

    // --- Phase 3 additions ---

    /** Delete-and-rebuild support for SegmentDistributionService idempotency */
    @Transactional
    void deleteBySourceReadingId(Long sourceReadingId);

    /** Slot-based query for DerivedFlowReadingQueryService */
    List<DerivedFlowReading> findByPipelineSegmentIdAndReadingSlotId(
            Long pipelineSegmentId, Long readingSlotId);

    /** Date range query across all segments */
    List<DerivedFlowReading> findByReadingDateBetween(LocalDate from, LocalDate to);

    /** Idempotency check — used before rebuild to detect existing derived readings */
    boolean existsBySourceReadingIdAndPipelineSegmentId(
            Long sourceReadingId, Long pipelineSegmentId);

    /**
     * Exact source+segment+slot lookup for uniqueness enforcement.
     * readingSlot is a @ManyToOne — navigate via d.readingSlot.id (not d.readingSlotId).
     */
    @Query("SELECT d FROM DerivedFlowReading d "
            + "WHERE d.sourceReading.id = :sourceReadingId "
            + "AND d.pipelineSegment.id = :segmentId "
            + "AND d.readingSlot.id = :slotId")
    Optional<DerivedFlowReading> findBySourceAndSegmentAndSlot(
            @Param("sourceReadingId") Long sourceReadingId,
            @Param("segmentId") Long segmentId,
            @Param("slotId") Long slotId);
}
