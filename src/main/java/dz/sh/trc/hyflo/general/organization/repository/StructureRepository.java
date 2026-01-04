/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StructureRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.general.organization.model.Structure;

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
    
    @Query("SELECT p FROM Structure p WHERE "
            + "LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(p.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(p.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(p.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Structure> searchByAnyField(@Param("search") String search, Pageable pageable);
}
