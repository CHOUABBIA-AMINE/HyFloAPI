/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProviderRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Repository
 *
 **/

package dz.mdn.iaas.business.provider.repository;

import dz.mdn.iaas.business.provider.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provider Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
