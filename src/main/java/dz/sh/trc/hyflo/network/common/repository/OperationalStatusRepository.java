/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: OperationalStatusRepository
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Network / Repository
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

import dz.sh.trc.hyflo.network.common.model.OperationalStatus;

@Repository
public interface OperationalStatusRepository extends JpaRepository<OperationalStatus, Long> {

	// ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    boolean existsByDesignationFr(String designationFr);
    
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    // ========== CUSTOM QUERIES (Complex multi-field search) ==========

    @Query("SELECT t FROM OperationalStatus t WHERE LOWER(t.code) LIKE LOWER(CONCAT('%', :search, '%'))"
            + " OR LOWER(t.designationAr) LIKE LOWER(CONCAT('%', :search, '%'))"
            + " OR LOWER(t.designationEn) LIKE LOWER(CONCAT('%', :search, '%'))"
            + " OR LOWER(t.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<OperationalStatus> searchByAnyField(@Param("search") String search, Pageable pageable);

}
