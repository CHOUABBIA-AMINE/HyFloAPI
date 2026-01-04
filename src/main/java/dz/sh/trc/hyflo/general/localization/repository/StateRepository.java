/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StateRepository
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

import dz.sh.trc.hyflo.general.localization.model.State;

/**
 * State Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT l FROM State l WHERE "
            + "LOWER(l.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<State> searchByAnyField(@Param("search") String search, Pageable pageable);
    
}
