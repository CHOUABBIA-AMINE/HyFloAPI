/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ActivityRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Organization
 *
 **/

package dz.sh.trc.hyflo.system.organization.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.system.organization.model.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    boolean existsByDesignationFr(String designationFr);
    
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT z FROM Activity z WHERE "
         + "LOWER(z.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(z.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(z.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(z.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Activity> searchByAnyField(@Param("search") String search, Pageable pageable);
}
