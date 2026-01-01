/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DocumentRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Document
 *	@Package	: Common / Document / Repository
 *
 **/

package dz.mdn.iaas.common.document.repository;

import dz.mdn.iaas.common.document.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Document Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
