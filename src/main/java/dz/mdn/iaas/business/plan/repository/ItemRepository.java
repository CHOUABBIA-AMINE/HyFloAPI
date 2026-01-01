/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ItemRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Repository
 *
 **/

package dz.mdn.iaas.business.plan.repository;

import dz.mdn.iaas.business.plan.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Item Repository
 * Item belongs to Rubric (many-to-one)
 * Item can have parent Item (hierarchical structure)
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find items by rubric ID
     * Used by ItemService.getByRubricId()
     */
    List<Item> findByRubricId(Long rubricId);
    
}
