/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: AuthorityRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.repository;

import dz.sh.trc.hyflo.system.security.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    
    /**
     * Find authority by name
     */
    Optional<Authority> findByName(String name);
    
    /**
     * Find authorities by type
     */
    List<Authority> findByType(String type);
    
    /**
     * Check if authority exists by name
     */
    boolean existsByName(String name);
}
