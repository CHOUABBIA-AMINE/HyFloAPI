/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CoordinateRepository
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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.general.localization.model.Coordinate;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {    
	
	// ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    List<Coordinate> findByInfrastructureId(Long infrastructureId);
    
    //List<Coordinate> findByLocalityId(Long localityId);

}
