/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DashboardSummaryDTO
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dashboard Summary DTO
 * Contains aggregated data for dashboard display
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    
    // Infrastructure counts
    private Long totalStations;
    private Long totalTerminals;
    private Long totalFields;
    private Long totalPipelines;
    
    // Today's summary
    private LocalDate currentDate;
    private Double totalVolumeToday;
    private Double averagePressureToday;
    private Integer activePipelines;
    private Integer totalReadingsToday;
    private Integer expectedReadingsToday;
    
    // Daily transported vs estimated
    private Double totalTransportedToday;
    private Double totalEstimatedToday;
    private Double varianceToday;
    private Double variancePercentToday;
    
    // Status breakdown
    private Integer pipelinesOnTarget;  // Within ±5%
    private Integer pipelinesBelowTarget;  // < -5%
    private Integer pipelinesAboveTarget;  // > +5%
    private Integer pipelinesOffline;
    
    // Recent readings
    private String lastReadingTime;  // e.g., "20:00"
    private String nextReadingTime;  // e.g., "00:00"
    
    // Monthly summary
    private Integer currentDayOfMonth;
    private Double monthlyTotalTransported;
    private Double monthlyTotalEstimated;
    private Double monthlyVariance;
    private Double monthlyVariancePercent;
    private Integer daysOnTargetThisMonth;
    
}
