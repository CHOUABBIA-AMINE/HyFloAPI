/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: CurrencyRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Core
 *	@Package	: Business / Core / Repository
 *
 **/

package dz.mdn.iaas.business.core.repository;

import dz.mdn.iaas.business.core.model.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Currency Repository
 * Methods aligned with CurrencyService requirements
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
    
    boolean existsByDesignationAr(String designationAr);
    boolean existsByDesignationArAndIdNot(String designationAr, Long id);
    boolean existsByDesignationEn(String designationEn);
    boolean existsByDesignationEnAndIdNot(String designationEn, Long id);
    boolean existsByDesignationFr(String designationFr);
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    boolean existsByAcronymAr(String acronymAr);
    boolean existsByAcronymArAndIdNot(String acronymAr, Long id);
    boolean existsByAcronymEn(String acronymEn);
    boolean existsByAcronymEnAndIdNot(String acronymEn, Long id);
    boolean existsByAcronymFr(String acronymFr);
    boolean existsByAcronymFrAndIdNot(String acronymFr, Long id);
    
    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    /**
     * Search across all fields (code, designations, acronyms)
     */
    @Query("SELECT c FROM Currency c WHERE " +
           "LOWER(c.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.designationFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.acronymFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.acronymEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.acronymAr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Currency> searchByAnyField(@Param("search") String search, Pageable pageable);
    
    /**
     * Search by designation (all languages)
     */
    @Query("SELECT c FROM Currency c WHERE " +
           "LOWER(c.designationFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.designationAr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Currency> searchByDesignation(@Param("search") String search, Pageable pageable);
    
    /**
     * Search by acronym (all languages)
     */
    @Query("SELECT c FROM Currency c WHERE " +
           "LOWER(c.acronymFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.acronymEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.acronymAr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Currency> searchByAcronym(@Param("search") String search, Pageable pageable);
}
