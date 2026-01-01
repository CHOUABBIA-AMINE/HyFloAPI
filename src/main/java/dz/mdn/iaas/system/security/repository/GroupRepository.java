/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: GroupRepository
 *	@CreatedOn	: 11-18-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Repository
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.repository;

import dz.mdn.iaas.system.security.model.Group;
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
