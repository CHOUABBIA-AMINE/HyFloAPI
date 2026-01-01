/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ExclusionTypeRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Repository
 *
 **/

package dz.sh.trc.hyflo.business.provider.repository;

import dz.sh.trc.hyflo.business.provider.model.ExclusionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ExclusionType Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ExclusionTypeRepository extends JpaRepository<ExclusionType, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
