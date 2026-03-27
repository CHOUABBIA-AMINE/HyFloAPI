/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowThresholdRepository
 * 	@CreatedOn	: 01-21-2026
 * 	@UpdatedOn	: 03-28-2026 — refactor: moved from flow.core.repository → flow.common.repository
 *                             Entity reference updated to flow.common.model.FlowThreshold
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.common.model.FlowThreshold;

@Repository
public interface FlowThresholdRepository extends JpaRepository<FlowThreshold, Long> {

    List<FlowThreshold> findByPipelineId(Long pipelineId);
    Optional<FlowThreshold> findByPipelineIdAndActiveTrue(Long pipelineId);
    Optional<FlowThreshold> findByPipelineIdAndActiveFalse(Long pipelineId);
    boolean existsByPipelineIdAndActiveTrue(Long pipelineId);
    List<FlowThreshold> findByActiveTrue();
    List<FlowThreshold> findByActiveFalse();
    long countByActiveTrue();
    long countByPipelineId(Long pipelineId);

    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.pipeline.manager.id = :structureId
        ORDER BY ft.pipeline.code ASC
    """)
    List<FlowThreshold> findByStructureId(@Param("structureId") Long structureId);

    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.pipeline.manager.id = :structureId
            AND ft.active = true
        ORDER BY ft.pipeline.code ASC
    """)
    List<FlowThreshold> findActiveByStructureId(@Param("structureId") Long structureId);

    @Query("""
        SELECT ft FROM FlowThreshold ft
        ORDER BY ft.pipeline.code ASC
    """)
    Page<FlowThreshold> findAllWithPagination(Pageable pageable);

    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.active = true
        ORDER BY ft.pipeline.code ASC
    """)
    Page<FlowThreshold> findActiveWithPagination(Pageable pageable);

    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.pressureMax >= :minPressure
            AND ft.active = true
        ORDER BY ft.pressureMax DESC
    """)
    List<FlowThreshold> findHighPressurePipelines(@Param("minPressure") BigDecimal minPressure);

    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.temperatureMax >= :minTemperature
            AND ft.active = true
        ORDER BY ft.temperatureMax DESC
    """)
    List<FlowThreshold> findHighTemperaturePipelines(@Param("minTemperature") BigDecimal minTemperature);

    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.flowRateMax >= :minFlowRate
            AND ft.active = true
        ORDER BY ft.flowRateMax DESC
    """)
    List<FlowThreshold> findHighCapacityPipelines(@Param("minFlowRate") BigDecimal minFlowRate);

    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.alertTolerance >= :minTolerance
            AND ft.active = true
        ORDER BY ft.alertTolerance DESC
    """)
    List<FlowThreshold> findHighTolerancePipelines(@Param("minTolerance") BigDecimal minTolerance);

    @Query("""
        SELECT p.id FROM Pipeline p
        WHERE p.id NOT IN (
            SELECT ft.pipeline.id FROM FlowThreshold ft
        )
        ORDER BY p.code ASC
    """)
    List<Long> findPipelinesWithoutThresholds();

    @Query("""
        SELECT p.id FROM Pipeline p
        WHERE p.id NOT IN (
            SELECT ft.pipeline.id FROM FlowThreshold ft
            WHERE ft.active = true
        )
        ORDER BY p.code ASC
    """)
    List<Long> findPipelinesWithoutActiveThresholds();

    @Query("""
        SELECT COUNT(p) FROM Pipeline p
        WHERE p.id NOT IN (
            SELECT ft.pipeline.id FROM FlowThreshold ft
        )
    """)
    long countPipelinesWithoutThresholds();

    @Query("""
        SELECT COUNT(CASE WHEN ft.active = true THEN 1 END),
               COUNT(CASE WHEN ft.active = false THEN 1 END),
               COUNT(*),
               AVG(CASE WHEN ft.active = true THEN ft.alertTolerance ELSE NULL END)
        FROM FlowThreshold ft
    """)
    Object[] getThresholdStatistics();

    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.pipeline.code = :pipelineCode
    """)
    List<FlowThreshold> findByPipelineCode(@Param("pipelineCode") String pipelineCode);

    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.pipeline.code LIKE :pipelineCodePattern
        ORDER BY ft.pipeline.code ASC
    """)
    List<FlowThreshold> findByPipelineCodeLike(@Param("pipelineCodePattern") String pipelineCodePattern);

    @Query("""
        UPDATE FlowThreshold ft
        SET ft.active = false
        WHERE ft.id = :thresholdId
    """)
    int deactivateThreshold(@Param("thresholdId") Long thresholdId);

    @Query("""
        UPDATE FlowThreshold ft
        SET ft.active = true
        WHERE ft.id = :thresholdId
    """)
    int activateThreshold(@Param("thresholdId") Long thresholdId);
}
