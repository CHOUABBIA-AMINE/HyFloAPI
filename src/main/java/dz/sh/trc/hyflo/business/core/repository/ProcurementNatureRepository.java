/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProcurementNatureRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Core
 *	@Package	: Business / Core / Repository
 *
 **/

package dz.sh.trc.hyflo.business.core.repository;

import dz.sh.trc.hyflo.business.core.model.ProcurementNature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * ProcurementNature Repository
 * Methods aligned with ProcurementNatureService requirements
 */
@Repository
public interface ProcurementNatureRepository extends JpaRepository<ProcurementNature, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Check if French designation exists
     */
    boolean existsByDesignationFr(String designationFr);
    
    /**
     * Check if French designation exists excluding current ID (for updates)
     */
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    /**
     * Search by designation (all languages)
     */
    @Query("SELECT p FROM ProcurementNature p WHERE " +
           "LOWER(p.designationFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.designationAr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<ProcurementNature> searchByDesignation(@Param("search") String search, Pageable pageable);
}
