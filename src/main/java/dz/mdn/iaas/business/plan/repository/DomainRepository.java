/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DomainRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Repository
 *
 **/

package dz.mdn.iaas.business.plan.repository;

import dz.mdn.iaas.business.plan.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Domain Repository
 * Basic CRUD operations provided by JpaRepository
 * Domain entity has one-to-many relationship with Rubrics
 */
@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {
    // All basic CRUD operations inherited from JpaRepository
    // Services use GenericService which handles standard operations
}
