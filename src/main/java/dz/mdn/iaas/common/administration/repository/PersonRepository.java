/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PersonRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Repository
 *
 **/

package dz.mdn.iaas.common.administration.repository;

import dz.mdn.iaas.common.administration.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Person Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
