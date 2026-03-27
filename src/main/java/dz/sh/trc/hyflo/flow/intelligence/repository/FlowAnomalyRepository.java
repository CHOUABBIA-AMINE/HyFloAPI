/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowAnomalyRepository
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-28-2026 — refactor: moved from flow.core.repository → flow.intelligence.repository
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.intelligence.model.FlowAnomaly;

@Repository
public interface FlowAnomalyRepository extends JpaRepository<FlowAnomaly, Long> {

    List<FlowAnomaly> findByReadingId(Long readingId);

    List<FlowAnomaly> findByDerivedReadingId(Long derivedReadingId);

    List<FlowAnomaly> findByPipelineSegmentId(Long pipelineSegmentId);

    List<FlowAnomaly> findByAnomalyType(String anomalyType);

    @Query("SELECT a FROM FlowAnomaly a WHERE "
            + "LOWER(a.anomalyType) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(a.modelName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(a.explanation) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<FlowAnomaly> searchByAnyField(@Param("search") String search, Pageable pageable);
}
