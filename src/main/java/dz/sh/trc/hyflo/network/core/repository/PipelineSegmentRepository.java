/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSegmentRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Network / Core
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

import dz.sh.trc.hyflo.network.core.model.PipelineSegment;

@Repository
public interface PipelineSegmentRepository extends JpaRepository<PipelineSegment, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    List<PipelineSegment> findByPipelineId(Long pipelineId);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT s FROM PipelineSegment s WHERE "
         + "LOWER(s.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<PipelineSegment> searchByAnyField(@Param("search") String search, Pageable pageable);
}
