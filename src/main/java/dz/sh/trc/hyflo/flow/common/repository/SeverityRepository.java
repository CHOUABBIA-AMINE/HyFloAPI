/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: SeverityRepository
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

import dz.sh.trc.hyflo.flow.common.model.Severity;

/**
 * Repository interface for Severity entity.
 * Provides CRUD operations and custom queries for severity classification.
 */
@Repository
public interface SeverityRepository extends JpaRepository<Severity, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByDesignationFr(String designationFr);
    
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    Optional<Severity> findByDesignationFr(String designationFr);
    
    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT sev FROM Severity sev WHERE " +
           "LOWER(sev.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(sev.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(sev.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Severity> searchByAnyField(@Param("search") String search, Pageable pageable);
}
