/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PlannedItemRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-13-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Repository
 *
 **/

package dz.mdn.iaas.business.plan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.iaas.business.plan.model.PlannedItem;

/**
 * PlannedItem Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface PlannedItemRepository extends JpaRepository<PlannedItem, Long> {
    
    /**
     * Find all planned items by year
     * @param year the year
     * @return list of planned items
     */
    List<PlannedItem> findByFinancialOperation_BudgetYear(String year);
    
    /**
     * Find all planned items by Financial Operation ID
     * @param financialOperationId the Financial Operation ID
     * @return list of planned items
     */
    List<PlannedItem> findByFinancialOperationId(Long financialOperationId);
}
