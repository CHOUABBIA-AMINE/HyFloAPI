/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowOperationRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowOperation;

@Repository
public interface FlowOperationRepository extends JpaRepository<FlowOperation, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    List<FlowOperation> findByDate(LocalDate date);
    
    List<FlowOperation> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<FlowOperation> findByInfrastructureId(Long infrastructureId);
    
    List<FlowOperation> findByProductId(Long productId);
    
    List<FlowOperation> findByTypeId(Long typeId);
    
    List<FlowOperation> findByValidationStatusId(Long validationStatusId);
    
    List<FlowOperation> findByRecordedById(Long employeeId);
    
    List<FlowOperation> findByValidatedById(Long employeeId);
    
    List<FlowOperation> findByInfrastructureIdAndDate(Long infrastructureId, LocalDate date);
    
    List<FlowOperation> findByInfrastructureIdAndProductIdAndTypeId(Long infrastructureId, Long productId, Long typeId);
    
    boolean existsByDateAndInfrastructureIdAndProductIdAndTypeId(LocalDate date, Long infrastructureId, Long productId, Long typeId);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT fo FROM FlowOperation fo WHERE "
         + "fo.infrastructure.id = :infrastructureId AND "
         + "fo.date BETWEEN :startDate AND :endDate")
    Page<FlowOperation> findByInfrastructureAndDateRange(
        @Param("infrastructureId") Long infrastructureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);
    
    @Query("SELECT fo FROM FlowOperation fo WHERE "
         + "fo.product.id = :productId AND "
         + "fo.type.id = :typeId AND "
         + "fo.date BETWEEN :startDate AND :endDate")
    Page<FlowOperation> findByProductAndTypeAndDateRange(
        @Param("productId") Long productId,
        @Param("typeId") Long typeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);
    
    @Query("SELECT fo FROM FlowOperation fo WHERE "
         + "fo.validationStatus.id = :statusId AND "
         + "fo.date BETWEEN :startDate AND :endDate")
    Page<FlowOperation> findByValidationStatusAndDateRange(
        @Param("statusId") Long statusId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);
}
