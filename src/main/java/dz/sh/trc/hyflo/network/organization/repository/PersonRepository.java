/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PersonRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Organization
 *
 **/

package dz.sh.trc.hyflo.network.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.network.organization.model.Person;

/**
 * Person Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
