/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventStatusRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.common.model.EventStatus;

/**
 * Repository interface for EventStatus entity.
 * Provides CRUD operations and custom queries for event status classification.
 */
@Repository
public interface EventStatusRepository extends JpaRepository<EventStatus, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    boolean existsByDesignationFr(String designationFr);
    
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    Optional<EventStatus> findByCode(String code);
    
    Optional<EventStatus> findByDesignationFr(String designationFr);
    
    List<EventStatus> findAllByOrderByCodeAsc();

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT evs FROM EventStatus evs WHERE " +
           "LOWER(evs.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(evs.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(evs.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(evs.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<EventStatus> searchByAnyField(@Param("search") String search, Pageable pageable);
}
