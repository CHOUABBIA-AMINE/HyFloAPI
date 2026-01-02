/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: LocalityRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.general.localization.model.Locality;

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
