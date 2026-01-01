/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PartnerRepository
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Network / Repository
 *	@Package	: Network / Common
 *
 **/

package dz.mdn.iaas.network.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.mdn.iaas.network.common.model.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByShortName(String shortName);
    
    boolean existsByShortNameAndIdNot(String shortName, Long id);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT p FROM Partner p WHERE "
         + "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(p.shortName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Partner> searchByAnyField(@Param("search") String search, Pageable pageable);
}
