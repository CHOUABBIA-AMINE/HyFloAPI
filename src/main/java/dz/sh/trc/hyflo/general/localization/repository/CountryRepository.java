/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CountryRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.general.localization.model.Country;

/**
 * Country Repository
 * Basic CRUD operations provided by JpaRepository
 * Custom queries only when needed
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT l FROM Country l WHERE "
            + "LOWER(l.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Country> searchByAnyField(@Param("search") String search, Pageable pageable);
    
}
