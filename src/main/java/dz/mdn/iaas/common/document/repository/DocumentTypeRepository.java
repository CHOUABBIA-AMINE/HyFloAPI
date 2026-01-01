/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DocumentTypeRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Document
 *	@Package	: Common / Document / Repository
 *
 **/

package dz.mdn.iaas.common.document.repository;

import dz.mdn.iaas.common.document.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DocumentType Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
