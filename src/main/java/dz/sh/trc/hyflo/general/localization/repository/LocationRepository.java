/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: LocationRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.general.localization.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    List<Location> findByLocalityId(Long localityId);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT l FROM Location l WHERE "
            + "LOWER(l.placeName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Location> searchByAnyField(@Param("search") String search, Pageable pageable);
}
