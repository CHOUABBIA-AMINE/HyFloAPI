/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MailRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Communication
 *	@Package	: Common / Communication / Repository
 *
 **/

package dz.sh.trc.hyflo.common.communication.repository;

import dz.sh.trc.hyflo.common.communication.model.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Mail Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
