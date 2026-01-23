/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowForecastRepository
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
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowForecast;

@Repository
public interface FlowForecastRepository extends JpaRepository<FlowForecast, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    List<FlowForecast> findByForecastDate(LocalDate forecastDate);
    
    List<FlowForecast> findByForecastDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<FlowForecast> findByInfrastructureId(Long infrastructureId);
    
    List<FlowForecast> findByProductId(Long productId);
    
    List<FlowForecast> findByOperationTypeId(Long operationTypeId);
    
    List<FlowForecast> findBySupervisorId(Long supervisorId);
    
    List<FlowForecast> findByInfrastructureIdAndForecastDateBetween(Long infrastructureId, LocalDate startDate, LocalDate endDate);
    
    Optional<FlowForecast> findByForecastDateAndInfrastructureIdAndProductIdAndOperationTypeId(
        LocalDate forecastDate, Long infrastructureId, Long productId, Long operationTypeId);
    
    boolean existsByForecastDateAndInfrastructureIdAndProductIdAndOperationTypeId(
        LocalDate forecastDate, Long infrastructureId, Long productId, Long operationTypeId);
    
    boolean existsByForecastDateAndInfrastructureIdAndProductIdAndOperationTypeIdAndIdNot(
        LocalDate forecastDate, Long infrastructureId, Long productId, Long operationTypeId, Long id);

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    @Query("SELECT ff FROM FlowForecast ff WHERE "
         + "ff.infrastructure.id = :infrastructureId AND "
         + "ff.forecastDate BETWEEN :startDate AND :endDate "
         + "ORDER BY ff.forecastDate ASC")
    Page<FlowForecast> findByInfrastructureAndDateRange(
        @Param("infrastructureId") Long infrastructureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);
    
    @Query("SELECT ff FROM FlowForecast ff WHERE "
         + "ff.product.id = :productId AND "
         + "ff.operationType.id = :operationTypeId AND "
         + "ff.forecastDate BETWEEN :startDate AND :endDate "
         + "ORDER BY ff.forecastDate ASC")
    Page<FlowForecast> findByProductAndOperationTypeAndDateRange(
        @Param("productId") Long productId,
        @Param("operationTypeId") Long operationTypeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);
    
    @Query("SELECT ff FROM FlowForecast ff WHERE "
         + "ff.actualVolume IS NOT NULL AND "
         + "ff.forecastDate BETWEEN :startDate AND :endDate "
         + "ORDER BY ff.accuracy DESC")
    Page<FlowForecast> findCompletedByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable);
    
    @Query("SELECT ff FROM FlowForecast ff WHERE "
         + "ff.actualVolume IS NULL AND "
         + "ff.forecastDate >= :today "
         + "ORDER BY ff.forecastDate ASC")
    Page<FlowForecast> findPending(
        @Param("today") LocalDate today,
        Pageable pageable);
}
