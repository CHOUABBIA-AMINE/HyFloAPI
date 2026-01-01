/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: CountryRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Repository
 *
 **/

package dz.sh.trc.hyflo.system.localization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.system.localization.model.Country;

/**
 * Country Repository
 * Basic CRUD operations provided by JpaRepository
 * Custom queries only when needed
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    // All basic CRUD operations (findById, findAll, save, delete, existsById, count) 
    // are inherited from JpaRepository
    
    // Add custom query methods here only if needed by service
}
