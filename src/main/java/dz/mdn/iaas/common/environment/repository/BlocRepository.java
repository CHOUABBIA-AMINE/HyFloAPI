/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: BlocRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Repository
 *
 **/

package dz.mdn.iaas.common.environment.repository;

import dz.mdn.iaas.common.environment.model.Bloc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Bloc Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface BlocRepository extends JpaRepository<Bloc, Long> {
    // All basic CRUD operations inherited from JpaRepository
}
