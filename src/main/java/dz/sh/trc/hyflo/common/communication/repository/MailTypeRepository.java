/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MailTypeRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Communication
 *	@Package	: Common / Communication / Repository
 *
 **/

package dz.sh.trc.hyflo.common.communication.repository;

import dz.sh.trc.hyflo.common.communication.model.MailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MailType Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface MailTypeRepository extends JpaRepository<MailType, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
