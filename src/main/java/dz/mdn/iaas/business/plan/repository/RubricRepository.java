/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RubricRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Repository
 *
 **/

package dz.mdn.iaas.business.plan.repository;

import dz.mdn.iaas.business.plan.model.Rubric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Rubric Repository
 * Rubric belongs to Domain (many-to-one)
 * Rubric has one-to-many relationship with Items
 */
@Repository
public interface RubricRepository extends JpaRepository<Rubric, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find rubrics by domain ID
     * Used by RubricService.getByDomainId()
     */
    List<Rubric> findByDomainId(Long domainId);
}
