/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventTypeRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.type.model.EventType;

/**
 * Repository interface for EventType entity.
 * Provides CRUD operations and custom queries for operational event type classification.
 */
@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Check if an event type with the given code exists.
     *
     * @param code the event type code
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);
    
    /**
     * Check if an event type with the given code exists, excluding a specific ID.
     * Useful for update operations to avoid duplicate code validation.
     *
     * @param code the event type code
     * @param id the ID to exclude from the check
     * @return true if exists, false otherwise
     */
    boolean existsByCodeAndIdNot(String code, Long id);
    
    /**
     * Check if an event type with the given French designation exists.
     *
     * @param designationFr the French designation
     * @return true if exists, false otherwise
     */
    boolean existsByDesignationFr(String designationFr);
    
    /**
     * Check if an event type with the given French designation exists, excluding a specific ID.
     *
     * @param designationFr the French designation
     * @param id the ID to exclude from the check
     * @return true if exists, false otherwise
     */
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    /**
     * Find event type by code.
     *
     * @param code the event type code
     * @return Optional containing the event type if found
     */
    Optional<EventType> findByCode(String code);
    
    /**
     * Find event type by French designation.
     *
     * @param designationFr the French designation
     * @return Optional containing the event type if found
     */
    Optional<EventType> findByDesignationFr(String designationFr);
    
    /**
     * Find all event types ordered by code.
     *
     * @return list of event types
     */
    List<EventType> findAllByOrderByCodeAsc();

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    /**
     * Search event types by any field (code or designations in any language).
     *
     * @param search the search term
     * @param pageable pagination parameters
     * @return page of matching event types
     */
    @Query("SELECT et FROM EventType et WHERE " +
           "LOWER(et.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(et.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(et.designationFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(et.designationEn) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<EventType> searchByAnyField(@Param("search") String search, Pageable pageable);
}
