/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: JobRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Repository
 *
 **/

package dz.mdn.iaas.common.administration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.iaas.common.administration.model.Job;

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
}
