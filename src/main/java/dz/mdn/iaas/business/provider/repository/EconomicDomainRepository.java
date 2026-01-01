/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EconomicDomainRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Repository
 *
 **/

package dz.mdn.iaas.business.provider.repository;

import dz.mdn.iaas.business.provider.model.EconomicDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EconomicDomain Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface EconomicDomainRepository extends JpaRepository<EconomicDomain, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
