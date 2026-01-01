/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: LocalityRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Repository
 *
 **/

package dz.sh.trc.hyflo.system.localization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.system.localization.model.Locality;

import java.util.List;

/**
 * Locality Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface LocalityRepository extends JpaRepository<Locality, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find localities by state ID
     * Used by LocalityService.getByStateId()
     */
    List<Locality> findByStateId(Long stateId);
}
