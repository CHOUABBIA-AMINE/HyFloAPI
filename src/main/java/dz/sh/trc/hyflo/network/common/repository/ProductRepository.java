/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProductRepository
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

import dz.sh.trc.hyflo.network.common.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT p FROM Product p WHERE "
            + "LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(p.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(p.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(p.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> searchByAnyField(@Param("search") String search, Pageable pageable);
}
