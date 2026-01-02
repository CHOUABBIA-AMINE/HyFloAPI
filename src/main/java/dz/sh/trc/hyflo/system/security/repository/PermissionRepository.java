/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PermissionRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-12-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.repository;

import dz.sh.trc.hyflo.system.security.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    /**
     * Find permission by name
     */
    Optional<Permission> findByName(String name);
    
    /**
     * Find permissions by resource
     */
    List<Permission> findByResource(String resource);
    
    /**
     * Find permissions by action
     */
    List<Permission> findByAction(String action);
    
    /**
     * Find permissions by resource and action
     */
    List<Permission> findByResourceAndAction(String resource, String action);
    
    /**
     * Check if permission exists by name
     */
    boolean existsByName(String name);
}
