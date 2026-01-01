/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ShelfRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Repository
 *
 **/

package dz.mdn.iaas.common.environment.repository;

import dz.mdn.iaas.common.environment.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Shelf Repository
 */
@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find shelves by room ID
     * Used by ShelfService.getByRoomId()
     */
    List<Shelf> findByRoomId(Long roomId);
}
