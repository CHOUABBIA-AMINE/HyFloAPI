/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: QualityFlagRepository
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

import dz.sh.trc.hyflo.flow.common.model.QualityFlag;

/**
 * Repository interface for QualityFlag entity.
 * Provides CRUD operations and custom queries for quality flag classification.
 */
@Repository
public interface QualityFlagRepository extends JpaRepository<QualityFlag, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    boolean existsByDesignationFr(String designationFr);
    
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    Optional<QualityFlag> findByCode(String code);
    
    Optional<QualityFlag> findByDesignationFr(String designationFr);
    
    List<QualityFlag> findAllByOrderByCodeAsc();

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT qf FROM QualityFlag qf WHERE " +
           "LOWER(qf.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(qf.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(qf.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(qf.designationFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(qf.descriptionAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(qf.descriptionEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(qf.descriptionFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<QualityFlag> searchByAnyField(@Param("search") String search, Pageable pageable);
}
