/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FinancialOperationRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-13-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Repository
 *
 **/

package dz.mdn.iaas.business.plan.repository;

import dz.mdn.iaas.business.plan.model.FinancialOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FinancialOperation Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface FinancialOperationRepository extends JpaRepository<FinancialOperation, Long> {
    
    /**
     * Find all financial operations by year
     * @param year the year
     * @return list of financial operations
     */
    List<FinancialOperation> findByBudgetYear(Integer year);
}
