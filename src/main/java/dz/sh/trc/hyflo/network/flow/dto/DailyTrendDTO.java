/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DailyTrendDTO
 *	@CreatedOn	: 12-27-2025
 *	@UpdatedOn	: 12-27-2025
 *
 *	@Type		: Record
 *	@Layer		: DTO
 *	@Package	: Network.Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Daily Trend DTO
 * Contains daily summary for trend charts (last 7/30 days)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyTrendDTO {
    
    private LocalDate date;
    private Double totalVolumeTransported;
    private Double totalVolumeEstimated;
    private Double variance;
    private Double variancePercent;
    private Double averagePressure;
    private Integer activePipelines;
    
}
