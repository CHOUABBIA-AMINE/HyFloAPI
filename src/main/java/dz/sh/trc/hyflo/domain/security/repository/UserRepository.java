/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: UserRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-12-2025
 *	@UpdatedOn	: 03-26-2026 - Phase 7: add findByRoles_Id + findByRoleId alias
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.domain.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.domain.security.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@EntityGraph(attributePaths = {"roles", "roles.permissions", "groups", "groups.roles"})
    Optional<User> findByUsername(String username);
	
    Optional<User> findByEmail(String email);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
    
    Page<User> findAll(Pageable page);
    
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findById(@Param("id") Long id);
    
    @Query("SELECT u FROM User u WHERE "
            + "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<User> searchByAnyField(@Param("search") String search, Pageable pageable);

    /**
     * Find users by role ID — Spring Data traversal via @ManyToMany roles collection.
     * Primary method: correct Spring Data property path syntax.
     */
    List<User> findByRoles_Id(Long roleId);

    /**
     * Alias: findByRoleId — delegates to findByRoles_Id.
     * Satisfies call sites that reference findByRoleId(Long roleId).
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.id = :roleId")
    List<User> findByRoleId(@Param("roleId") Long roleId);
    
    @Query("SELECT DISTINCT u FROM User u "
            + "JOIN u.roles r "
            + "WHERE r.name = :roleName "
            + "AND u.enabled = true")
    List<User> findEnabledUsersByRoleName(@Param("roleName") String roleName);
    
    @Query("SELECT DISTINCT u FROM User u "
            + "JOIN u.groups g "
            + "JOIN g.roles r "
            + "WHERE r.name = :roleName "
            + "AND u.enabled = true")
    List<User> findEnabledUsersByRoleNameViaGroups(@Param("roleName") String roleName);
    
    @Query("SELECT DISTINCT u FROM User u "
            + "LEFT JOIN u.roles r "
            + "LEFT JOIN u.groups g "
            + "LEFT JOIN g.roles gr "
            + "WHERE (r.name = :roleName OR gr.name = :roleName) "
            + "AND u.enabled = true")
    List<User> findAllEnabledUsersByRoleName(@Param("roleName") String roleName);
}
