/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EquipmentRepository
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Network / Repository
 *	@Package	: Network / Repository
 *
 **/

package dz.mdn.iaas.network.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.mdn.iaas.network.core.model.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    boolean existsBySerialNumber(String serialNumber);
    
    boolean existsBySerialNumberAndIdNot(String serialNumber, Long id);
    
    List<Equipment> findByFacilityId(Long facilityId);
    
    List<Equipment> findByEquipmentTypeId(Long equipmentTypeId);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT e FROM Equipment e WHERE "
         + "LOWER(e.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
         + "LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Equipment> searchByAnyField(@Param("search") String search, Pageable pageable);
}
