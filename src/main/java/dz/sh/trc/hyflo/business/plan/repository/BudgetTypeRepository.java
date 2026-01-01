/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: BudgetTypeRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-13-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Repository
 *
 **/

package dz.sh.trc.hyflo.business.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.business.plan.model.BudgetType;

/**
 * BudgetType Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface BudgetTypeRepository extends JpaRepository<BudgetType, Long> {
    

}
