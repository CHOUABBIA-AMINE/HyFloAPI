/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: AlertStatusRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.common.model.AlertStatus;

/**
 * Repository interface for AlertStatus entity.
 * Provides CRUD operations and custom queries for alert status classification.
 */
@Repository
public interface AlertStatusRepository extends JpaRepository<AlertStatus, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByDesignationFr(String designationFr);
    
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    Optional<AlertStatus> findByDesignationFr(String designationFr);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT als FROM AlertStatus als WHERE " +
           "LOWER(als.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(als.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(als.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<AlertStatus> searchByAnyField(@Param("search") String search, Pageable pageable);
}
