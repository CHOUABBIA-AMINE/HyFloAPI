/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: RoleRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-12-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.repository;

import dz.sh.trc.hyflo.system.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Find role by name
     */
    Optional<Role> findByName(String name);
    
    /**
     * Check if role exists by name
     */
    boolean existsByName(String name);
}
