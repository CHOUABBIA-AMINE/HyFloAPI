/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PermissionRepository
 *	@CreatedOn	: 11-18-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Repository
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.repository;

import dz.mdn.iaas.system.security.model.Permission;
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
