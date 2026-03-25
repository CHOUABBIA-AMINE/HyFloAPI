/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: DerivedFlowReadingRepository
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.DerivedFlowReading;

/**
 * Repository for DerivedFlowReading entities.
 */
@Repository
public interface DerivedFlowReadingRepository extends JpaRepository<DerivedFlowReading, Long> {

    List<DerivedFlowReading> findByPipelineSegmentId(Long pipelineSegmentId);

    List<DerivedFlowReading> findBySourceReadingId(Long sourceReadingId);

    List<DerivedFlowReading> findByPipelineSegmentIdAndReadingDateBetween(
            Long pipelineSegmentId, LocalDate from, LocalDate to);
}
