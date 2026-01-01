/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EconomicNatureRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Repository
 *
 **/

package dz.sh.trc.hyflo.business.provider.repository;

import dz.sh.trc.hyflo.business.provider.model.EconomicNature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EconomicNature Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface EconomicNatureRepository extends JpaRepository<EconomicNature, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
