/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowTransportedRepository
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

import dz.sh.trc.hyflo.network.flow.model.FlowTransported;

@Repository
public interface FlowTransportedRepository extends JpaRepository<FlowTransported, Long> {

    /**
     * Find transported flow record for a specific pipeline and date
     */
    Optional<FlowTransported> findByPipelineIdAndMeasurementDate(Long pipelineId, LocalDate date);
    
    /**
     * Get all transported flow records for a specific date
     */
    List<FlowTransported> findByMeasurementDate(LocalDate date);
    
    /**
     * Calculate total transported volume for a specific date
     */
    @Query("SELECT COALESCE(SUM(ft.volumeTransported), 0.0) FROM FlowTransported ft " +
           "WHERE ft.measurementDate = :date")
    Double sumTransportedByDate(@Param("date") LocalDate date);
    
    /**
     * Calculate total estimated volume for a specific date
     */
    @Query("SELECT COALESCE(SUM(ft.volumeEstimated), 0.0) FROM FlowTransported ft " +
           "WHERE ft.measurementDate = :date")
    Double sumEstimatedByDate(@Param("date") LocalDate date);
    
    /**
     * Get all transported flow records within a date range
     */
    List<FlowTransported> findByMeasurementDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Count pipelines on target for a specific date (variance within ±5%)
     */
    @Query("SELECT COUNT(ft) FROM FlowTransported ft " +
           "WHERE ft.measurementDate = :date " +
           "AND ft.volumeTransported IS NOT NULL " +
           "AND ABS((ft.volumeTransported - ft.volumeEstimated) / ft.volumeEstimated * 100) <= 5")
    Integer countPipelinesOnTarget(@Param("date") LocalDate date);
    
    /**
     * Count pipelines below target for a specific date (variance < -5%)
     */
    @Query("SELECT COUNT(ft) FROM FlowTransported ft " +
           "WHERE ft.measurementDate = :date " +
           "AND ft.volumeTransported IS NOT NULL " +
           "AND ((ft.volumeTransported - ft.volumeEstimated) / ft.volumeEstimated * 100) < -5")
    Integer countPipelinesBelowTarget(@Param("date") LocalDate date);
    
    /**
     * Count pipelines above target for a specific date (variance > +5%)
     */
    @Query("SELECT COUNT(ft) FROM FlowTransported ft " +
           "WHERE ft.measurementDate = :date " +
           "AND ft.volumeTransported IS NOT NULL " +
           "AND ((ft.volumeTransported - ft.volumeEstimated) / ft.volumeEstimated * 100) > 5")
    Integer countPipelinesAboveTarget(@Param("date") LocalDate date);
    
    /**
     * Count pipelines offline for a specific date (no transported data)
     */
    @Query("SELECT COUNT(ft) FROM FlowTransported ft " +
           "WHERE ft.measurementDate = :date " +
           "AND ft.volumeTransported IS NULL")
    Integer countPipelinesOffline(@Param("date") LocalDate date);

}
