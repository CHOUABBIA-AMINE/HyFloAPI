/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: StructureRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Repository
 *
 **/

package dz.sh.trc.hyflo.common.administration.repository;

import dz.sh.trc.hyflo.common.administration.model.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Structure Repository
 */
@Repository
public interface StructureRepository extends JpaRepository<Structure, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find structures by structure type ID
     * Used by StructureService.getByStructureTypeId()
     */
    List<Structure> findByStructureTypeId(Long structureTypeId);
    
    /**
     * Find structures by parent structure ID
     * Used by StructureService.getByParentStructureId()
     */
    List<Structure> findByParentStructureId(Long parentStructureId);
    
    /**
     * Find root structures (no parent)
     * Used by StructureService.getRootStructures()
     */
    List<Structure> findByParentStructureIsNull();
}
