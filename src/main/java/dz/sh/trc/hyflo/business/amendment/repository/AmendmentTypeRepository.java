/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AmendmentTypeRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Amendment
 *	@Package	: Business / Amendment / Repository
 *
 **/

package dz.sh.trc.hyflo.business.amendment.repository;

import dz.sh.trc.hyflo.business.amendment.model.AmendmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * AmendmentType Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface AmendmentTypeRepository extends JpaRepository<AmendmentType, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
