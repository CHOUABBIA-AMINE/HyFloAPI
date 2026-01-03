/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DailyTrendDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Flow
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
