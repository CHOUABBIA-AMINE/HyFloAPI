/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowDeviationRepository
 * 	@CreatedOn	: 04-19-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Monitoring
 *
 **/

package dz.sh.trc.hyflo.core.flow.monitoring.repository;

import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowDeviation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlowDeviationRepository extends JpaRepository<FlowDeviation, Long> {

    List<FlowDeviation> findByPipelineIdAndReadingDateBetween(
        Long pipelineId,
        LocalDate from,
        LocalDate to
    );

    List<FlowDeviation> findByReadingId(Long readingId);

    @Query("""
        SELECT d FROM FlowDeviation d
        WHERE d.pipelineId = :pipelineId
        AND ABS(d.deviationPercent) >= :threshold
        ORDER BY d.readingDate DESC
    """)
    List<FlowDeviation> findSignificantDeviations(
        @Param("pipelineId") Long pipelineId,
        @Param("threshold") Double threshold
    );
}