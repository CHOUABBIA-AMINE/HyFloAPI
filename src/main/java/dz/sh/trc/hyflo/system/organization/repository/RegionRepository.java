/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RegionRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Organization
 *
 **/

package dz.sh.trc.hyflo.system.organization.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.system.organization.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    boolean existsByDesignationFr(String designationFr);
    
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    List<Region> findByZoneId(Long zoneId);
    
    List<Region> findByActivityId(Long activityId);
    
    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT r FROM Region r WHERE "
            + "LOWER(r.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(r.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(r.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(r.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Region> searchByAnyField(@Param("search") String search, Pageable pageable);
}
