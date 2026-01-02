/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: GroupRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.repository;

import dz.sh.trc.hyflo.system.security.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    
    /**
     * Find group by name
     */
    Optional<Group> findByName(String name);
    
    /**
     * Check if group exists by name
     */
    boolean existsByName(String name);
}
