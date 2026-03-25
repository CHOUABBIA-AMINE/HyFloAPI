/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: PipelineRiskScoreRepository
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.network.core.model.PipelineRiskScore;

/**
 * Repository for PipelineRiskScore entities.
 */
@Repository
public interface PipelineRiskScoreRepository extends JpaRepository<PipelineRiskScore, Long> {

    List<PipelineRiskScore> findByPipelineSegmentId(Long pipelineSegmentId);

    Optional<PipelineRiskScore> findTopByPipelineSegmentIdOrderByCalculatedAtDesc(Long pipelineSegmentId);

    @Query("SELECT r FROM PipelineRiskScore r WHERE "
            + "LOWER(r.modelName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(r.details) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<PipelineRiskScore> searchByAnyField(@Param("search") String search, Pageable pageable);
}
