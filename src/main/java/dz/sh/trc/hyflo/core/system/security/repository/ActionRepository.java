/**
 *	
 *	@Author		: CHOUABBIA Amine
 *
 *	@Name		: ActionRepository
 *	@CreatedOn	: 04-19-2026
 *	@UpdatedOn	: 04-19-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.core.system.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.core.system.security.model.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    /**
     * Find action by its unique name (authority code).
     * Used for Spring Security authority resolution and RBAC seed checks.
     */
    Optional<Action> findByName(String name);

    /**
     * Check if an action with the given name already exists.
     * Used during seeding and uniqueness validation.
     */
    boolean existsByName(String name);

    /**
     * Find all actions whose name starts with the given category prefix.
     * Convention: authority names are prefixed by category (e.g. "FLOW_", "USER_", "REPORT_").
     * Example: findByCategory("FLOW_") returns FLOW_READ, FLOW_WRITE, FLOW_VALIDATE.
     */
    @Query("SELECT a FROM Action a WHERE a.name LIKE CONCAT(:category, '%')")
    List<Action> findByCategory(@Param("category") String category);

    /**
     * Full-text search across name and description for admin management screens.
     */
    @Query("SELECT a FROM Action a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Action> searchByAnyField(@Param("search") String search, Pageable pageable);
}
