/**
 *	
 *	@Author		: CHOUABBIA Amine
 *
 *	@Name		: ResourceRepository
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

import dz.sh.trc.hyflo.core.system.security.model.Resource;
import dz.sh.trc.hyflo.core.system.security.model.ResourceType;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    /**
     * Find resource by its unique code.
     * Code is the canonical identifier used by permission assignment logic.
     */
    Optional<Resource> findByCode(String code);

    /**
     * Check if a resource with the given code already exists.
     * Used during seeding and uniqueness validation.
     */
    boolean existsByCode(String code);

    /**
     * Find all resources belonging to a given ResourceType.
     * Used to list all protectable objects within a domain (e.g., all SECURITY resources).
     */
    List<Resource> findByResourceType(ResourceType resourceType);

    /**
     * Find all resources by ResourceType id — avoids loading the full ResourceType entity.
     */
    @Query("SELECT r FROM Resource r WHERE r.resourceType.id = :resourceTypeId")
    List<Resource> findByResourceTypeId(@Param("resourceTypeId") Long resourceTypeId);

    /**
     * Full-text search across code and description for admin management screens.
     */
    @Query("SELECT r FROM Resource r WHERE LOWER(r.code) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(r.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Resource> searchByAnyField(@Param("search") String search, Pageable pageable);
}
