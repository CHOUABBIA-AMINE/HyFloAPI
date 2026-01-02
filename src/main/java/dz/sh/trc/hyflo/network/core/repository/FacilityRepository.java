/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FacilityRepository
 *	@CreatedOn	: 12-11-2025
 *	@UpdatedOn	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Network / Repository
 *	@Package	: Network / Repository
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

import dz.sh.trc.hyflo.network.core.model.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    List<Facility> findByVendorId(Long vendorId);
    
    List<Facility> findByLocalityId(Long localityId);
    
    List<Facility> findByOperationalStatusId(Long operationalStatusId);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT f FROM Facility f WHERE "
         + "LOWER(f.name) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(f.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Facility> searchByAnyField(@Param("search") String search, Pageable pageable);
}
