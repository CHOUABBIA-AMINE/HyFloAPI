/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: StationRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.network.core.model.Station;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT s FROM Station s WHERE "
         + "LOWER(s.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Station> searchByAnyField(@Param("search") String search, Pageable pageable);
}
