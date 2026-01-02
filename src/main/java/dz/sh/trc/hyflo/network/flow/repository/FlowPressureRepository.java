/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FlowPressureRepository
 *	@CreatedOn	: 12-19-2025
 *	@UpdatedOn	: 12-27-2025
 *
 *	@Type		: Repository
 *	@Layer		: Network / Repository
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.flow.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.network.flow.model.FlowPressure;

@Repository
public interface FlowPressureRepository extends JpaRepository<FlowPressure, Long> {

    /**
     * Calculate average pressure for a specific date
     * Averages all pressure readings across all pipelines for the given date
     */
    @Query("SELECT COALESCE(AVG(fp.Pressure), 0.0) FROM FlowPressure fp WHERE fp.measurementDate = :date")
    Double avgPressureByDate(@Param("date") LocalDate date);
    
    /**
     * Get all pressure readings for a specific pipeline and date
     * Ordered by measurement hour
     */
    @Query("SELECT fp FROM FlowPressure fp " +
           "WHERE fp.pipeline.id = :pipelineId AND fp.measurementDate = :date " +
           "ORDER BY fp.measurementHour.code")
    List<FlowPressure> findByPipelineAndDate(
        @Param("pipelineId") Long pipelineId, 
        @Param("date") LocalDate date
    );
    
    /**
     * Get all pressure readings within a date range
     * Used for trend analysis
     */
    @Query("SELECT fp FROM FlowPressure fp " +
           "WHERE fp.measurementDate BETWEEN :startDate AND :endDate " +
           "ORDER BY fp.measurementDate, fp.measurementHour.code")
    List<FlowPressure> findByDateRange(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );
    
    /**
     * Calculate average pressure for a specific pipeline on a specific date
     */
    @Query("SELECT COALESCE(AVG(fp.Pressure), 0.0) FROM FlowPressure fp " +
           "WHERE fp.pipeline.id = :pipelineId AND fp.measurementDate = :date")
    Double avgPressureByPipelineAndDate(
        @Param("pipelineId") Long pipelineId, 
        @Param("date") LocalDate date
    );
    
    /**
     * Get minimum pressure for a specific pipeline on a specific date
     */
    @Query("SELECT COALESCE(MIN(fp.Pressure), 0.0) FROM FlowPressure fp " +
           "WHERE fp.pipeline.id = :pipelineId AND fp.measurementDate = :date")
    Double minPressureByPipelineAndDate(
        @Param("pipelineId") Long pipelineId, 
        @Param("date") LocalDate date
    );
    
    /**
     * Get maximum pressure for a specific pipeline on a specific date
     */
    @Query("SELECT COALESCE(MAX(fp.Pressure), 0.0) FROM FlowPressure fp " +
           "WHERE fp.pipeline.id = :pipelineId AND fp.measurementDate = :date")
    Double maxPressureByPipelineAndDate(
        @Param("pipelineId") Long pipelineId, 
        @Param("date") LocalDate date
    );

}
