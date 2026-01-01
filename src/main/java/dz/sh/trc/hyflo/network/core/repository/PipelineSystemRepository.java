/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PipelineSystemRepository
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Network / Repository
 *	@Package	: Network / Repository
 *
 **/

package dz.sh.trc.hyflo.network.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.network.core.model.PipelineSystem;

@Repository
public interface PipelineSystemRepository extends JpaRepository<PipelineSystem, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    List<PipelineSystem> findByProductId(Long productId);

    List<PipelineSystem> findByOperationalStatusId(Long operationalStatusId);

    List<PipelineSystem> findByRegionId(Long regionId);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT s FROM PipelineSystem s WHERE "
         + "LOWER(s.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<PipelineSystem> searchByAnyField(@Param("search") String search, Pageable pageable);
}
