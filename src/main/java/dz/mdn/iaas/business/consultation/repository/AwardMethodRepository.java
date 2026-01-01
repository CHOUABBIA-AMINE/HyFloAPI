/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AwardMethodRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Consultation
 *	@Package	: Business / Consultation / Repository
 *
 **/

package dz.mdn.iaas.business.consultation.repository;

import dz.mdn.iaas.business.consultation.model.AwardMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * AwardMethod Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface AwardMethodRepository extends JpaRepository<AwardMethod, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
