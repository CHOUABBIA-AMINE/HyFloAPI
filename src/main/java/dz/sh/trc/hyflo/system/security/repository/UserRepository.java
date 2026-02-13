/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: UserRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-12-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.system.security.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    //@PreAuthorize("hasRole('ADMIN')")
    Page<User> findAll(Pageable page);
    
    Optional<User> findById(@Param("id") Long id);
    
    @Query("SELECT u FROM User u WHERE "
            + "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<User> searchByAnyField(@Param("search") String search, Pageable pageable);
    
    @EntityGraph(attributePaths = {"roles", "roles.permissions", "groups", "groups.roles"})
    Optional<User> findByIdForAuthentication(Long id);
    
    /**
     * Find all enabled users who have a specific role (by role name)
     * This query joins User -> roles -> name to find users with the given role
     * Only returns enabled users
     * 
     * @param roleName Role name (e.g., "ROLE_VALIDATOR", "ROLE_READER")
     * @return List of users with the specified role
     */
    @Query("SELECT DISTINCT u FROM User u "
            + "JOIN u.roles r "
            + "WHERE r.name = :roleName "
            + "AND u.enabled = true")
    List<User> findEnabledUsersByRoleName(@Param("roleName") String roleName);
    
    /**
     * Find all enabled users who have a specific role through groups
     * This query joins User -> groups -> roles -> name
     * Only returns enabled users
     * 
     * @param roleName Role name
     * @return List of users with the specified role (via groups)
     */
    @Query("SELECT DISTINCT u FROM User u "
            + "JOIN u.groups g "
            + "JOIN g.roles r "
            + "WHERE r.name = :roleName "
            + "AND u.enabled = true")
    List<User> findEnabledUsersByRoleNameViaGroups(@Param("roleName") String roleName);
    
    /**
     * Find all enabled users who have a specific role either directly or through groups
     * Combines both direct role assignments and group-based role assignments
     * 
     * @param roleName Role name (e.g., "ROLE_VALIDATOR")
     * @return List of users with the specified role (direct or via groups)
     */
    @Query("SELECT DISTINCT u FROM User u "
            + "LEFT JOIN u.roles r "
            + "LEFT JOIN u.groups g "
            + "LEFT JOIN g.roles gr "
            + "WHERE (r.name = :roleName OR gr.name = :roleName) "
            + "AND u.enabled = true")
    List<User> findAllEnabledUsersByRoleName(@Param("roleName") String roleName);
}