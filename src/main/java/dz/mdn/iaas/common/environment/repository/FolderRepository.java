/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FolderRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Repository
 *
 **/

package dz.mdn.iaas.common.environment.repository;

import dz.mdn.iaas.common.environment.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Folder Repository
 */
@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find folders by archive box ID
     * Used by FolderService.getByArchiveBoxId()
     */
    List<Folder> findByArchiveBoxId(Long archiveBoxId);
}
