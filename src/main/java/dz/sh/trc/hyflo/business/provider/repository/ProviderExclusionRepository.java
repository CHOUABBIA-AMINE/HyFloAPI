/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProviderExclusionRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-13-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Repository
 *
 **/

package dz.sh.trc.hyflo.business.provider.repository;

import dz.sh.trc.hyflo.business.provider.model.ProviderExclusion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProviderExclusion Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ProviderExclusionRepository extends JpaRepository<ProviderExclusion, Long> {
    
    /**
     * Find all provider exclusions by provider ID
     * @param providerId the provider ID
     * @return list of provider exclusions
     */
    List<ProviderExclusion> findByProviderId(Long providerId);
}
