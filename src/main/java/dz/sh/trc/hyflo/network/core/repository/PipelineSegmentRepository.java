/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSegmentRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-14-2026 13:31 - Added explicit JPQL query for findByPipelineId
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
    
    /**
     * Find all pipeline segments belonging to a specific pipeline.
     * 
     * IMPORTANT: This method uses an explicit JPQL query to ensure correct filtering.
     * 
     * The Spring Data JPA derived query method name 'findByPipelineId' might not work
     * correctly because:
     * 1. The entity has a @ManyToOne relationship field named 'pipeline' (not 'pipelineId')
     * 2. Spring would look for a field named 'pipelineId' which doesn't exist
     * 3. The database column is F_17, but JPA works with entity fields, not columns
     * 
     * The explicit query ensures we navigate the relationship correctly:
     * - s.pipeline.id references the Pipeline entity's id through the relationship
     * - This generates the correct SQL: WHERE F_17 = ? (F_17 is the FK to Pipeline)
     * 
     * @param pipelineId The ID of the parent pipeline
     * @return List of all segments belonging to the specified pipeline
     */
    @Query("SELECT s FROM PipelineSegment s WHERE s.pipeline.id = :pipelineId")
    List<PipelineSegment> findByPipelineId(@Param("pipelineId") Long pipelineId);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    /**
     * Global search across pipeline segment fields.
     * Currently searches only by code, can be extended to include other fields.
     * 
     * @param search Search term to match against segment fields
     * @param pageable Pagination information
     * @return Page of matching pipeline segments
     */
    @Query("SELECT s FROM PipelineSegment s WHERE "
         + "LOWER(s.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<PipelineSegment> searchByAnyField(@Param("search") String search, Pageable pageable);
}
