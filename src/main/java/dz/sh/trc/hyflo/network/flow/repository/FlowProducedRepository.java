/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowProducedRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.network.flow.model.FlowProduced;

@Repository
public interface FlowProducedRepository extends JpaRepository<FlowProduced, Long> {

    /**
     * Find transported flow record for a specific pipeline and date
     */
    Optional<FlowProduced> findByProcessingPlantIdAndMeasurementDate(Long pipelineId, LocalDate date);
    
    /**
     * Get all transported flow records for a specific date
     */
    List<FlowProduced> findByMeasurementDate(LocalDate date);
    
    /**
     * Calculate total transported volume for a specific date
     */
    @Query("SELECT COALESCE(SUM(fp.volumeProduced), 0.0) FROM FlowProduced fp " +
           "WHERE fp.measurementDate = :date")
    Double sumProducedByDate(@Param("date") LocalDate date);
    
    /**
     * Calculate total estimated volume for a specific date
     */
    @Query("SELECT COALESCE(SUM(fp.volumeEstimated), 0.0) FROM FlowProduced fp " +
           "WHERE fp.measurementDate = :date")
    Double sumEstimatedByDate(@Param("date") LocalDate date);
    
    /**
     * Get all transported flow records within a date range
     */
    List<FlowProduced> findByMeasurementDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Count pipelines on target for a specific date (variance within ±5%)
     */
    @Query("SELECT COUNT(fp) FROM FlowProduced fp " +
           "WHERE fp.measurementDate = :date " +
           "AND fp.volumeProduced IS NOT NULL " +
           "AND ABS((fp.volumeProduced - fp.volumeEstimated) / fp.volumeEstimated * 100) <= 5")
    Integer countProcessingPlantsOnTarget(@Param("date") LocalDate date);
    
    /**
     * Count pipelines below target for a specific date (variance < -5%)
     */
    @Query("SELECT COUNT(fp) FROM FlowProduced fp " +
           "WHERE fp.measurementDate = :date " +
           "AND fp.volumeProduced IS NOT NULL " +
           "AND ((fp.volumeProduced - fp.volumeEstimated) / fp.volumeEstimated * 100) < -5")
    Integer countProcessingPlantsBelowTarget(@Param("date") LocalDate date);
    
    /**
     * Count pipelines above target for a specific date (variance > +5%)
     */
    @Query("SELECT COUNT(fp) FROM FlowProduced fp " +
           "WHERE fp.measurementDate = :date " +
           "AND fp.volumeProduced IS NOT NULL " +
           "AND ((fp.volumeProduced - fp.volumeEstimated) / fp.volumeEstimated * 100) > 5")
    Integer countProcessingPlantsAboveTarget(@Param("date") LocalDate date);
    
    /**
     * Count pipelines offline for a specific date (no transported data)
     */
    @Query("SELECT COUNT(fp) FROM FlowProduced fp " +
           "WHERE fp.measurementDate = :date " +
           "AND fp.volumeProduced IS NULL")
    Integer countProcessingPlantsOffline(@Param("date") LocalDate date);

}
