/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AlloyRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.network.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.network.common.model.Alloy;

@Repository
public interface AlloyRepository extends JpaRepository<Alloy, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    boolean existsByDesignationFr(String designationFr);
    
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT a FROM Alloy a WHERE LOWER(a.code) LIKE LOWER(CONCAT('%', :search, '%'))"
    		+ " OR LOWER(a.designationAr) LIKE LOWER(CONCAT('%', :search, '%'))"
    		+ " OR LOWER(a.designationEn) LIKE LOWER(CONCAT('%', :search, '%'))"
    		+ " OR LOWER(a.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Alloy> searchByAnyField(@Param("search") String search, Pageable pageable);
}
