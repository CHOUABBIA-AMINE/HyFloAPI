/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: StateRepository
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

import dz.sh.trc.hyflo.system.localization.model.State;

/**
 * State Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
}
