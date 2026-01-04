/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: LocalityRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.general.localization.model.Locality;

/**
 * Locality Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface LocalityRepository extends JpaRepository<Locality, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find localities by state ID
     * Used by LocalityService.getByStateId()
     */
    List<Locality> findByStateId(Long stateId);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT l FROM Locality l WHERE "
            + "LOWER(l.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Locality> searchByAnyField(@Param("search") String search, Pageable pageable);
}
