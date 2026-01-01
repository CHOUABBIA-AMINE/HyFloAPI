/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ApprovalStatusRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Core
 *	@Package	: Business / Core / Repository
 *
 **/

package dz.mdn.iaas.business.core.repository;

import dz.mdn.iaas.business.core.model.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * ApprovalStatus Repository
 * Methods aligned with ApprovalStatusService requirements
 */
@Repository
public interface ApprovalStatusRepository extends JpaRepository<ApprovalStatus, Long> {
    
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
    @Query("SELECT a FROM ApprovalStatus a WHERE " +
           "LOWER(a.designationFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.designationAr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<ApprovalStatus> searchByDesignation(@Param("search") String search, Pageable pageable);
}
