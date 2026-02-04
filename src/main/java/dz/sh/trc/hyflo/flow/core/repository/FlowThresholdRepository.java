/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowThresholdRepository
 * 	@CreatedOn	: 01-21-2026
 * 	@UpdatedOn	: 02-03-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;

@Repository
public interface FlowThresholdRepository extends JpaRepository<FlowThreshold, Long> {

    // ========== SPRING DERIVED QUERIES ==========
    
    /**
     * Find threshold by pipeline ID
     * Note: Should return 0 or 1 record due to unique constraint on (pipeline, active)
     * 
     * @param pipelineId Pipeline identifier
     * @return List of thresholds (typically 0 or 1 element)
     */
    List<FlowThreshold> findByPipelineId(Long pipelineId);
    
    /**
     * Find active threshold for a pipeline
     * FIX #6: Used by ReadingBusinessValidator for validation
     * 
     * @param pipelineId Pipeline identifier
     * @return Optional containing active threshold if exists
     */
    Optional<FlowThreshold> findByPipelineIdAndActiveTrue(Long pipelineId);
    
    /**
     * Find inactive threshold for a pipeline
     * 
     * @param pipelineId Pipeline identifier
     * @return Optional containing inactive threshold if exists
     */
    Optional<FlowThreshold> findByPipelineIdAndActiveFalse(Long pipelineId);
    
    /**
     * Check if pipeline has an active threshold
     * 
     * @param pipelineId Pipeline identifier
     * @return true if active threshold exists
     */
    boolean existsByPipelineIdAndActiveTrue(Long pipelineId);
    
    /**
     * Find all active thresholds
     * 
     * @return List of all active thresholds across all pipelines
     */
    List<FlowThreshold> findByActiveTrue();
    
    /**
     * Find all inactive thresholds
     * 
     * @return List of all inactive thresholds
     */
    List<FlowThreshold> findByActiveFalse();
    
    /**
     * Count active thresholds
     * 
     * @return Number of active thresholds
     */
    long countByActiveTrue();
    
    /**
     * Count thresholds for a specific pipeline
     * 
     * @param pipelineId Pipeline identifier
     * @return Number of thresholds (should be 0-2: one active, one inactive)
     */
    long countByPipelineId(Long pipelineId);
    
    // ========== CUSTOM QUERIES ==========
    
    /**
     * Find thresholds for pipelines managed by a specific structure
     * Useful for getting all thresholds for a station, terminal, or manifold
     * 
     * @param structureId Structure identifier
     * @return List of thresholds for all pipelines under this structure
     */
    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.pipeline.manager.id = :structureId
        ORDER BY ft.pipeline.code ASC
    """)
    List<FlowThreshold> findByStructureId(@Param("structureId") Long structureId);
    
    /**
     * Find active thresholds for pipelines managed by a specific structure
     * 
     * @param structureId Structure identifier
     * @return List of active thresholds for all pipelines under this structure
     */
    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.pipeline.manager.id = :structureId
            AND ft.active = true
        ORDER BY ft.pipeline.code ASC
    """)
    List<FlowThreshold> findActiveByStructureId(@Param("structureId") Long structureId);
    
    /**
     * Find thresholds with pagination
     * 
     * @param pageable Pagination parameters
     * @return Page of thresholds
     */
    @Query("""
        SELECT ft FROM FlowThreshold ft
        ORDER BY ft.pipeline.code ASC
    """)
    Page<FlowThreshold> findAllWithPagination(Pageable pageable);
    
    /**
     * Find active thresholds with pagination
     * 
     * @param pageable Pagination parameters
     * @return Page of active thresholds
     */
    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.active = true
        ORDER BY ft.pipeline.code ASC
    """)
    Page<FlowThreshold> findActiveWithPagination(Pageable pageable);
    
    /**
     * Find thresholds where pressure exceeds a certain value
     * Useful for identifying high-pressure pipelines
     * 
     * @param minPressure Minimum pressure threshold
     * @return List of thresholds with pressureMax >= minPressure
     */
    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.pressureMax >= :minPressure
            AND ft.active = true
        ORDER BY ft.pressureMax DESC
    """)
    List<FlowThreshold> findHighPressurePipelines(@Param("minPressure") java.math.BigDecimal minPressure);
    
    /**
     * Find thresholds where temperature exceeds a certain value
     * Useful for identifying high-temperature pipelines
     * 
     * @param minTemperature Minimum temperature threshold
     * @return List of thresholds with temperatureMax >= minTemperature
     */
    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.temperatureMax >= :minTemperature
            AND ft.active = true
        ORDER BY ft.temperatureMax DESC
    """)
    List<FlowThreshold> findHighTemperaturePipelines(@Param("minTemperature") java.math.BigDecimal minTemperature);
    
    /**
     * Find thresholds where flow rate exceeds a certain value
     * Useful for identifying high-capacity pipelines
     * 
     * @param minFlowRate Minimum flow rate threshold
     * @return List of thresholds with flowRateMax >= minFlowRate
     */
    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.flowRateMax >= :minFlowRate
            AND ft.active = true
        ORDER BY ft.flowRateMax DESC
    """)
    List<FlowThreshold> findHighCapacityPipelines(@Param("minFlowRate") java.math.BigDecimal minFlowRate);
    
    /**
     * Find thresholds with alert tolerance above a certain percentage
     * Useful for identifying pipelines with loose monitoring
     * 
     * @param minTolerance Minimum alert tolerance percentage
     * @return List of thresholds with alertTolerance >= minTolerance
     */
    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.alertTolerance >= :minTolerance
            AND ft.active = true
        ORDER BY ft.alertTolerance DESC
    """)
    List<FlowThreshold> findHighTolerancePipelines(@Param("minTolerance") java.math.BigDecimal minTolerance);
    
    /**
     * Find pipelines without any threshold configuration
     * Critical for ensuring all operational pipelines have monitoring limits
     * 
     * @return List of pipeline IDs without thresholds
     */
    @Query("""
        SELECT p.id FROM Pipeline p
        WHERE p.id NOT IN (
            SELECT ft.pipeline.id FROM FlowThreshold ft
        )
        ORDER BY p.code ASC
    """)
    List<Long> findPipelinesWithoutThresholds();
    
    /**
     * Find pipelines without active threshold configuration
     * Important for operational monitoring - all active pipelines should have active thresholds
     * 
     * @return List of pipeline IDs without active thresholds
     */
    @Query("""
        SELECT p.id FROM Pipeline p
        WHERE p.id NOT IN (
            SELECT ft.pipeline.id FROM FlowThreshold ft
            WHERE ft.active = true
        )
        ORDER BY p.code ASC
    """)
    List<Long> findPipelinesWithoutActiveThresholds();
    
    /**
     * Count pipelines without thresholds
     * 
     * @return Number of active pipelines lacking threshold configuration
     */
    @Query("""
        SELECT COUNT(p) FROM Pipeline p
        WHERE p.id NOT IN (
            SELECT ft.pipeline.id FROM FlowThreshold ft
        )
    """)
    long countPipelinesWithoutThresholds();
    
    /**
     * Get threshold statistics summary
     * Returns: [activeCount, inactiveCount, totalCount, avgAlertTolerance]
     * 
     * @return Array of statistics
     */
    @Query("""
        SELECT 
            COUNT(CASE WHEN ft.active = true THEN 1 END) as activeCount,
            COUNT(CASE WHEN ft.active = false THEN 1 END) as inactiveCount,
            COUNT(*) as totalCount,
            AVG(CASE WHEN ft.active = true THEN ft.alertTolerance ELSE NULL END) as avgAlertTolerance
        FROM FlowThreshold ft
    """)
    Object[] getThresholdStatistics();
    
    /**
     * Find thresholds by pipeline code (for user-friendly search)
     * 
     * @param pipelineCode Pipeline code (exact match)
     * @return List of thresholds for matching pipeline
     */
    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.pipeline.code = :pipelineCode
    """)
    List<FlowThreshold> findByPipelineCode(@Param("pipelineCode") String pipelineCode);
    
    /**
     * Find thresholds by pipeline code pattern (for autocomplete/search)
     * 
     * @param pipelineCodePattern Pipeline code pattern (e.g., "OZ%")
     * @return List of thresholds for matching pipelines
     */
    @Query("""
        SELECT ft FROM FlowThreshold ft
        WHERE ft.pipeline.code LIKE :pipelineCodePattern
        ORDER BY ft.pipeline.code ASC
    """)
    List<FlowThreshold> findByPipelineCodeLike(@Param("pipelineCodePattern") String pipelineCodePattern);
    
    /**
     * Deactivate threshold (soft disable)
     * Used when temporarily disabling threshold enforcement without deleting configuration
     * 
     * @param thresholdId Threshold identifier
     * @return Number of records updated (should be 1)
     */
    @Query("""
        UPDATE FlowThreshold ft
        SET ft.active = false
        WHERE ft.id = :thresholdId
    """)
    int deactivateThreshold(@Param("thresholdId") Long thresholdId);
    
    /**
     * Activate threshold
     * Note: Ensure only one threshold per pipeline is active (handled by unique constraint)
     * 
     * @param thresholdId Threshold identifier
     * @return Number of records updated (should be 1)
     */
    @Query("""
        UPDATE FlowThreshold ft
        SET ft.active = true
        WHERE ft.id = :thresholdId
    """)
    int activateThreshold(@Param("thresholdId") Long thresholdId);
}
