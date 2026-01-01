/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ArchiveBoxRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Repository
 *
 **/

package dz.sh.trc.hyflo.common.environment.repository;

import dz.sh.trc.hyflo.common.environment.model.ArchiveBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ArchiveBox Repository
 */
@Repository
public interface ArchiveBoxRepository extends JpaRepository<ArchiveBox, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find archive boxes by shelf floor ID
     * Used by ArchiveBoxService.getByShelfId()
     */
    List<ArchiveBox> findByShelfId(Long shelfId);
    
    /**
     * Find archive boxes by shelf floor ID
     * Used by ArchiveBoxService.getByShelfFloorId()
     */
    List<ArchiveBox> findByShelfFloorId(Long shelfFloorId);
}
