/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PersonRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.general.organization.model.Person;

/**
 * Person Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
