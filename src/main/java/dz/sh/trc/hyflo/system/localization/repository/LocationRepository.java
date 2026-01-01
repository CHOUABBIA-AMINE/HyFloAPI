/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: LocationRepository
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Network / Repository
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.system.localization.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.system.localization.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    List<Location> findByLocalityId(Long localityId);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT l FROM Location l WHERE "
            + "LOWER(l.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(l.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Location> searchByAnyField(@Param("search") String search, Pageable pageable);
}
