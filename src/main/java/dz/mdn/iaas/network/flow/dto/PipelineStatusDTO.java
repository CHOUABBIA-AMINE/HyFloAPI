/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PipelineStatusDTO
 *	@CreatedOn	: 12-27-2025
 *	@Updated	: 12-27-2025
 *
 *	@Type		: Record
 *	@Layer		: DTO
 *	@Package	: Network.Flow
 *
 **/

package dz.mdn.iaas.network.flow.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pipeline Status DTO
 * Contains current status and metrics for a single pipeline
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineStatusDTO {
    
    // Pipeline info
    private Long pipelineId;
    private String pipelineCode;
    private String pipelineName;
    
    // Latest reading
    private LocalDate measurementDate;
    private LocalTime lastReadingTime;
    private Double lastVolume;  // m³ from last 4-hour period
    private Double lastPressure;  // bar from last 4-hour period
    
    // Daily accumulated
    private Double dailyVolumeTransported;  // Sum of all readings today
    private Double dailyVolumeEstimated;  // Daily target
    private Double dailyVariance;  // Transported - Estimated
    private Double dailyVariancePercent;  // (Variance / Estimated) * 100
    private Double dailyProgress;  // (Transported / Estimated) * 100
    
    // Pressure stats
    private Double averagePressureToday;
    private Double minPressureToday;
    private Double maxPressureToday;
    
    // Status
    private String volumeStatus;  // "ON_TARGET", "BELOW_TARGET", "ABOVE_TARGET", "OFFLINE"
    private String pressureStatus;  // "NORMAL", "LOW", "HIGH", "OFFLINE"
    
    // Reading count
    private Integer readingsCompletedToday;
    private Integer readingsExpectedToday;  // Usually 6 (00:00, 04:00, 08:00, 12:00, 16:00, 20:00)
    
}
