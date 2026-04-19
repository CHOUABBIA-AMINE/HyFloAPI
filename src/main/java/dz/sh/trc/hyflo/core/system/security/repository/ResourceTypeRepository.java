/**
 *	
 *	@Author		: CHOUABBIA Amine
 *
 *	@Name		: ResourceTypeRepository
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

import dz.sh.trc.hyflo.core.system.security.model.ResourceType;

@Repository
public interface ResourceTypeRepository extends JpaRepository<ResourceType, Long> {

    /**
     * Find resource type by its unique name (domain classification code).
     * Name is the canonical identifier: e.g., SECURITY, BUSINESS, AI.
     */
    Optional<ResourceType> findByName(String name);

    /**
     * Check if a resource type with the given name already exists.
     * Used during seeding and uniqueness validation.
     */
    boolean existsByName(String name);

    /**
     * Find all active resource types.
     * Used to populate selection lists — only active types are available for resource assignment.
     */
    List<ResourceType> findByActiveTrue();

    /**
     * Find all non-deletable (system-protected) resource types.
     * protectedType = true means the type was seeded by the system and must not be deleted.
     */
    List<ResourceType> findByProtectedTypeTrue();

    /**
     * Full-text search across name and description for admin management screens.
     */
    @Query("SELECT rt FROM ResourceType rt WHERE LOWER(rt.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(rt.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<ResourceType> searchByAnyField(@Param("search") String search, Pageable pageable);
}
