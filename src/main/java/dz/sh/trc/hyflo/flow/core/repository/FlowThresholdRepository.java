/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowThresholdRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
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

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    List<FlowThreshold> findByPipelineId(Long pipelineId);
    
    List<FlowThreshold> findByProductId(Long productId);
    
    List<FlowThreshold> findByActive(Boolean active);
    
    List<FlowThreshold> findByPipelineIdAndActive(Long pipelineId, Boolean active);
    
    Optional<FlowThreshold> findByPipelineIdAndProductId(Long pipelineId, Long productId);
    
    boolean existsByPipelineIdAndProductId(Long pipelineId, Long productId);
    
    boolean existsByPipelineIdAndProductIdAndIdNot(Long pipelineId, Long productId, Long id);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT ft FROM FlowThreshold ft WHERE "
         + "ft.pipeline.id = :pipelineId AND "
         + "ft.product.id = :productId AND "
         + "ft.active = true")
    Optional<FlowThreshold> findActiveByPipelineAndProduct(
        @Param("pipelineId") Long pipelineId,
        @Param("productId") Long productId);
    
    @Query("SELECT ft FROM FlowThreshold ft WHERE "
         + "ft.active = true")
    Page<FlowThreshold> findAllActive(Pageable pageable);
    
    @Query("SELECT ft FROM FlowThreshold ft WHERE "
         + "ft.pipeline.id = :pipelineId AND "
         + "ft.active = true")
    Page<FlowThreshold> findActiveByPipeline(
        @Param("pipelineId") Long pipelineId,
        Pageable pageable);
}
