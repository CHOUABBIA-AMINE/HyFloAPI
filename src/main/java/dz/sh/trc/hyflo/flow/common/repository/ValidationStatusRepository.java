/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ValidationStatusRepository
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

import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;

/**
 * Repository interface for ValidationStatus entity.
 * Provides CRUD operations and custom queries for validation status classification.
 */
@Repository
public interface ValidationStatusRepository extends JpaRepository<ValidationStatus, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    boolean existsByDesignationFr(String designationFr);
    
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    Optional<ValidationStatus> findByCode(String code);
    
    Optional<ValidationStatus> findByDesignationFr(String designationFr);
    
    List<ValidationStatus> findAllByOrderByCodeAsc();

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT vs FROM ValidationStatus vs WHERE " +
           "LOWER(vs.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(vs.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(vs.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(vs.designationFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(vs.descriptionAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(vs.descriptionEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(vs.descriptionFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<ValidationStatus> searchByAnyField(@Param("search") String search, Pageable pageable);
}
