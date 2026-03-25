/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowAnomalyRepository
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowAnomaly;

/**
 * Repository for FlowAnomaly entities.
 */
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
