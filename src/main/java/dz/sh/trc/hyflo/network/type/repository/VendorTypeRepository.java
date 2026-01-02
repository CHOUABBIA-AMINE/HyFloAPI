/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: VendorTypeRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
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

import dz.sh.trc.hyflo.network.type.model.VendorType;

@Repository
public interface VendorTypeRepository extends JpaRepository<VendorType, Long> {

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT t FROM VendorType t WHERE "
         + "LOWER(t.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(t.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(t.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<VendorType> searchByAnyField(@Param("search") String search, Pageable pageable);
}
