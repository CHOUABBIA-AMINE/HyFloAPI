/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MailNatureRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Communication
 *	@Package	: Common / Communication / Repository
 *
 **/

package dz.mdn.iaas.common.communication.repository;

import dz.mdn.iaas.common.communication.model.MailNature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MailNature Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface MailNatureRepository extends JpaRepository<MailNature, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
