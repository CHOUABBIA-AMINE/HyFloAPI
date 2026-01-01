/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: CompanyTypeRepository
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Network / Repository
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.network.type.model.CompanyType;

@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long> {

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT t FROM CompanyType t WHERE "
         + "LOWER(t.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(t.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(t.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<CompanyType> searchByAnyField(@Param("search") String search, Pageable pageable);
}
