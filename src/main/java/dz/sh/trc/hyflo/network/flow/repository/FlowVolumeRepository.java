/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FlowVolumeRepository
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-27-2025
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

import dz.sh.trc.hyflo.network.flow.model.FlowVolume;

@Repository
public interface FlowVolumeRepository extends JpaRepository<FlowVolume, Long> {

    /**
     * Calculate total volume for a specific date
     * Sums all volume readings across all pipelines for the given date
     */
    @Query("SELECT COALESCE(SUM(fv.volume), 0.0) FROM FlowVolume fv WHERE fv.measurementDate = :date")
    Double sumVolumeByDate(@Param("date") LocalDate date);
    
    /**
     * Get all volume readings for a specific pipeline and date
     * Ordered by measurement hour
     */
    @Query("SELECT fv FROM FlowVolume fv " +
           "WHERE fv.pipeline.id = :pipelineId AND fv.measurementDate = :date " +
           "ORDER BY fv.measurementHour.code")
    List<FlowVolume> findByPipelineAndDate(
        @Param("pipelineId") Long pipelineId, 
        @Param("date") LocalDate date
    );
    
    /**
     * Get all volume readings within a date range
     * Used for trend analysis
     */
    @Query("SELECT fv FROM FlowVolume fv " +
           "WHERE fv.measurementDate BETWEEN :startDate AND :endDate " +
           "ORDER BY fv.measurementDate, fv.measurementHour.code")
    List<FlowVolume> findByDateRange(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );
    
    /**
     * Calculate total volume for a specific pipeline on a specific date
     */
    @Query("SELECT COALESCE(SUM(fv.volume), 0.0) FROM FlowVolume fv " +
           "WHERE fv.pipeline.id = :pipelineId AND fv.measurementDate = :date")
    Double sumVolumeByPipelineAndDate(
        @Param("pipelineId") Long pipelineId, 
        @Param("date") LocalDate date
    );
    
    /**
     * Get count of readings for a specific pipeline and date
     * Should be 6 readings per day (00:00, 04:00, 08:00, 12:00, 16:00, 20:00)
     */
    @Query("SELECT COUNT(fv) FROM FlowVolume fv " +
           "WHERE fv.pipeline.id = :pipelineId AND fv.measurementDate = :date")
    Integer countReadingsByPipelineAndDate(
        @Param("pipelineId") Long pipelineId, 
        @Param("date") LocalDate date
    );
    
    /**
     * Get total count of readings for a specific date (all pipelines)
     */
    @Query("SELECT COUNT(fv) FROM FlowVolume fv WHERE fv.measurementDate = :date")
    Integer countReadingsByDate(@Param("date") LocalDate date);

}
