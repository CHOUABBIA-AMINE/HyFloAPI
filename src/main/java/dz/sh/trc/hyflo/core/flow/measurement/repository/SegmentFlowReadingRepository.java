/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : SegmentFlowReadingRepository
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 18/25: added Phase 3 service-support query methods
 *  @UpdatedOn  : 03-26-2026 — fix JPQL d.readingSlotId → d.readingSlot.id
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Flow / Measurement
 *
 **/

package dz.sh.trc.hyflo.core.flow.measurement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.core.flow.measurement.model.SegmentFlowReading;

/**
 * Repository for SegmentFlowReading entities.
 *
 * Methods added in Phase 3 (Commit 18/25) support:
 * - SegmentFlowReadingCommandServiceImpl (rebuildForSourceReading)
 * - SegmentFlowReadingQueryServiceImpl (all query methods)
 * - SegmentDistributionServiceImpl (idempotency check)
 */
@Repository
public interface SegmentFlowReadingRepository extends JpaRepository<SegmentFlowReading, Long> {

    // --- Base queries (pre-Phase 3) ---

    List<SegmentFlowReading> findByPipelineSegmentId(Long pipelineSegmentId);

    List<SegmentFlowReading> findBySourceReadingId(Long sourceReadingId);

    List<SegmentFlowReading> findByPipelineSegmentIdAndReadingDateBetween(
            Long pipelineSegmentId, LocalDate from, LocalDate to);

    // --- Phase 3 additions ---

    /** Delete-and-rebuild support for SegmentDistributionService idempotency */
    @Transactional
    void deleteBySourceReadingId(Long sourceReadingId);

    /** Slot-based query for SegmentFlowReadingQueryService */
    List<SegmentFlowReading> findByPipelineSegmentIdAndReadingSlotId(
            Long pipelineSegmentId, Long readingSlotId);

    /** Date range query across all segments */
    List<SegmentFlowReading> findByReadingDateBetween(LocalDate from, LocalDate to);

    /** Idempotency check — used before rebuild to detect existing derived readings */
    boolean existsBySourceReadingIdAndPipelineSegmentId(
            Long sourceReadingId, Long pipelineSegmentId);

    /**
     * Exact source+segment+slot lookup for uniqueness enforcement.
     * readingSlot is a @ManyToOne — navigate via d.readingSlot.id (not d.readingSlotId).
     */
    @Query("SELECT d FROM SegmentFlowReading d "
            + "WHERE d.sourceReading.id = :sourceReadingId "
            + "AND d.pipelineSegment.id = :segmentId "
            + "AND d.readingSlot.id = :slotId")
    Optional<SegmentFlowReading> findBySourceAndSegmentAndSlot(
            @Param("sourceReadingId") Long sourceReadingId,
            @Param("segmentId") Long segmentId,
            @Param("slotId") Long slotId);
}
