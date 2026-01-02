/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: VendorRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.network.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.network.common.model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    @Query("SELECT v FROM Vendor v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(v.shortName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Vendor> searchByAnyField(@Param("search") String search, Pageable pageable);
}
