/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MeasurementHourRepository
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Network / Flow
 *
 **/

package dz.mdn.iaas.network.flow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.mdn.iaas.network.flow.model.MeasurementHour;

@Repository
public interface MeasurementHourRepository extends JpaRepository<MeasurementHour, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT t FROM MeasurementHour t WHERE LOWER(t.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<MeasurementHour> searchByAnyField(@Param("search") String search, Pageable pageable);
}
