/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowConsumedRepository
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

import dz.sh.trc.hyflo.network.flow.model.FlowConsumed;

@Repository
public interface FlowConsumedRepository extends JpaRepository<FlowConsumed, Long> {

    /**
     * Find transported flow record for a specific pipeline and date
     */
    Optional<FlowConsumed> findByTerminalIdAndMeasurementDate(Long pipelineId, LocalDate date);
    
    /**
     * Get all transported flow records for a specific date
     */
    List<FlowConsumed> findByMeasurementDate(LocalDate date);
    
    /**
     * Calculate total transported volume for a specific date
     */
    @Query("SELECT COALESCE(SUM(fc.volumeConsumed), 0.0) FROM FlowConsumed fc " +
           "WHERE fc.measurementDate = :date")
    Double sumConsumedByDate(@Param("date") LocalDate date);
    
    /**
     * Calculate total estimated volume for a specific date
     */
    @Query("SELECT COALESCE(SUM(fc.volumeEstimated), 0.0) FROM FlowConsumed fc " +
           "WHERE fc.measurementDate = :date")
    Double sumEstimatedByDate(@Param("date") LocalDate date);
    
    /**
     * Get all transported flow records within a date range
     */
    List<FlowConsumed> findByMeasurementDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Count pipelines on target for a specific date (variance within ±5%)
     */
    @Query("SELECT COUNT(ft) FROM FlowConsumed ft " +
           "WHERE ft.measurementDate = :date " +
           "AND ft.volumeConsumed IS NOT NULL " +
           "AND ABS((ft.volumeConsumed - ft.volumeEstimated) / ft.volumeEstimated * 100) <= 5")
    Integer countTerminalsOnTarget(@Param("date") LocalDate date);
    
    /**
     * Count pipelines below target for a specific date (variance < -5%)
     */
    @Query("SELECT COUNT(ft) FROM FlowConsumed ft " +
           "WHERE ft.measurementDate = :date " +
           "AND ft.volumeConsumed IS NOT NULL " +
           "AND ((ft.volumeConsumed - ft.volumeEstimated) / ft.volumeEstimated * 100) < -5")
    Integer countTerminalsBelowTarget(@Param("date") LocalDate date);
    
    /**
     * Count pipelines above target for a specific date (variance > +5%)
     */
    @Query("SELECT COUNT(ft) FROM FlowConsumed ft " +
           "WHERE ft.measurementDate = :date " +
           "AND ft.volumeConsumed IS NOT NULL " +
           "AND ((ft.volumeConsumed - ft.volumeEstimated) / ft.volumeEstimated * 100) > 5")
    Integer countTerminalsAboveTarget(@Param("date") LocalDate date);
    
    /**
     * Count pipelines offline for a specific date (no transported data)
     */
    @Query("SELECT COUNT(ft) FROM FlowConsumed ft " +
           "WHERE ft.measurementDate = :date " +
           "AND ft.volumeConsumed IS NULL")
    Integer countTerminalsOffline(@Param("date") LocalDate date);

}
