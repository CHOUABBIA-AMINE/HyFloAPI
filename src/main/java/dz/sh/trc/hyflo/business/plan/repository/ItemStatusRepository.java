/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ItemStatusRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Repository
 *
 **/

package dz.sh.trc.hyflo.business.plan.repository;

import dz.sh.trc.hyflo.business.plan.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ItemStatus Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ItemStatusRepository extends JpaRepository<ItemStatus, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
