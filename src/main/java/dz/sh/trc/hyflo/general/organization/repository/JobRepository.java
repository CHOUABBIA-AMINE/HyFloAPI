/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: JobRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.general.organization.model.Job;

/**
 * Job Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    // All basic CRUD operations inherited from JpaRepository
	
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find Jobs by structure ID
     * Used by JobService.getByStructureId()
     */
    List<Job> findByStructureId(Long structureId);
    
    @Query("SELECT p FROM Job p WHERE "
            + "LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(p.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(p.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(p.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Job> searchByAnyField(@Param("search") String search, Pageable pageable);
}
