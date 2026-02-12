/**
 *
 *	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ReadingSlotRepository
 * 	@CreatedOn	: 01-27-2026
 * 	@UpdatedOn	: 01-27-2026
 * 	@UpdatedOn	: 02-07-2026 - Added displayOrder query for intelligence service
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;

@Repository
public interface ReadingSlotRepository extends JpaRepository<ReadingSlot, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    boolean existsByDesignationFr(String designationFr);
    
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    Optional<ReadingSlot> findByCode(String code);
    
    Optional<ReadingSlot> findByDesignationFr(String designationFr);
    
    List<ReadingSlot> findAllByOrderByCodeAsc();
    
    /**
     * Find all slots ordered by display order
     * Used for intelligence service chronological display
     */
    List<ReadingSlot> findAllByOrderByDisplayOrder();

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT vs FROM ReadingSlot vs WHERE " +
           "LOWER(vs.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(vs.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(vs.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(vs.designationFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<ReadingSlot> searchByAnyField(@Param("search") String search, Pageable pageable);
}
