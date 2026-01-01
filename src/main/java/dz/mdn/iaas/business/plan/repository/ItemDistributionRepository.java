/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ItemDistributionRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-13-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Repository
 *
 **/

package dz.mdn.iaas.business.plan.repository;

import dz.mdn.iaas.business.plan.model.ItemDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ItemDistribution Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ItemDistributionRepository extends JpaRepository<ItemDistribution, Long> {
    
    /**
     * Find all item distributions by item ID
     * @param itemId the item ID
     * @return list of item distributions
     */
    List<ItemDistribution> findByPlannedItemId(Long itemId);
}
