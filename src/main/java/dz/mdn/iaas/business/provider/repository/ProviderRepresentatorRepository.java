/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProviderRepresentatorRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-13-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Repository
 *
 **/

package dz.mdn.iaas.business.provider.repository;

import dz.mdn.iaas.business.provider.model.ProviderRepresentator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProviderRepresentator Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ProviderRepresentatorRepository extends JpaRepository<ProviderRepresentator, Long> {
    
    /**
     * Find all provider representators by provider ID
     * @param providerId the provider ID
     * @return list of provider representators
     */
    List<ProviderRepresentator> findByProviderId(Long providerId);
}
