/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowVolumeRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Measurement
 *
 **/

package dz.sh.trc.hyflo.core.flow.measurement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.core.flow.measurement.model.FlowVolumeReading;

@Repository
public interface FlowVolumeRepository extends JpaRepository<FlowVolumeReading, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    List<FlowVolumeReading> findByOperationDate(LocalDate operationDate);
    
    List<FlowVolumeReading> findByOperationDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<FlowVolumeReading> findByInfrastructureId(Long infrastructureId);
    
    List<FlowVolumeReading> findByProductId(Long productId);
    
    List<FlowVolumeReading> findByTypeId(Long typeId);
    
    List<FlowVolumeReading> findByValidationStatusId(Long validationStatusId);
    
    List<FlowVolumeReading> findByRecordedById(Long employeeId);
    
    List<FlowVolumeReading> findByValidatedById(Long employeeId);
    
    List<FlowVolumeReading> findByInfrastructureIdAndOperationDate(Long infrastructureId, LocalDate operationDate);
    
    List<FlowVolumeReading> findByInfrastructureIdAndProductIdAndTypeId(Long infrastructureId, Long productId, Long typeId);
    
    boolean existsByOperationDateAndInfrastructureIdAndProductIdAndTypeId(LocalDate operationDate, Long infrastructureId, Long productId, Long typeId);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT fo FROM FlowVolume fo WHERE "
         + "fo.infrastructure.id = :infrastructureId AND "
         + "fo.operationDate BETWEEN :startDate AND :endDate")
    Page<FlowVolumeReading> findByInfrastructureAndDateRange(
        @Param("infrastructureId") Long infrastructureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);
    
    @Query("SELECT fo FROM FlowVolume fo WHERE "
         + "fo.product.id = :productId AND "
         + "fo.type.id = :typeId AND "
         + "fo.operationDate BETWEEN :startDate AND :endDate")
    Page<FlowVolumeReading> findByProductAndTypeAndDateRange(
        @Param("productId") Long productId,
        @Param("typeId") Long typeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);
    
    @Query("SELECT fo FROM FlowVolume fo WHERE "
         + "fo.validationStatus.id = :statusId AND "
         + "fo.operationDate BETWEEN :startDate AND :endDate")
    Page<FlowVolumeReading> findByValidationStatusAndDateRange(
        @Param("statusId") Long statusId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);
}
