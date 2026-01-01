/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RoomRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Repository
 *
 **/

package dz.sh.trc.hyflo.common.environment.repository;

import dz.sh.trc.hyflo.common.environment.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Room Repository
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find rooms by bloc ID
     * Used by RoomService.getByBlocId()
     */
    List<Room> findByBlocId(Long blocId);
    
    /**
     * Find rooms by floor ID
     * Used by RoomService.getByFloorId()
     */
    List<Room> findByFloorId(Long floorId);
}
