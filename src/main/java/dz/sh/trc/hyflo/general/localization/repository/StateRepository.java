/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StateRepository
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

import dz.sh.trc.hyflo.general.localization.model.State;

/**
 * State Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
}
