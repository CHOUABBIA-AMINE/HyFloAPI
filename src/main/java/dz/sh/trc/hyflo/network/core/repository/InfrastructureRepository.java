/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: InfrastructureRepository
 *	@CreatedOn	: 12-19-2025
 *	@UpdatedOn	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Network / Repository
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.network.core.model.Infrastructure;

@Repository
public interface InfrastructureRepository extends JpaRepository<Infrastructure, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    List<Infrastructure> findByRegionId(Long regionId);
    
    List<Infrastructure> findByRegion_ActivityId(Long activityId);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT i FROM Infrastructure i WHERE "
         + "LOWER(i.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Infrastructure> searchByAnyField(@Param("search") String search, Pageable pageable);
}
