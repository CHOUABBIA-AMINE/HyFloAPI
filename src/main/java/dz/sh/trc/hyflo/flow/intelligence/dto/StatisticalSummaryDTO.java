/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: StatisticalSummaryDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Statistical summary of a dataset
 */
@Schema(description = "Statistical analysis of time-series data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalSummaryDTO {
    
    @Schema(description = "Minimum value in dataset", example = "68.5")
    private BigDecimal min;
    
    @Schema(description = "Maximum value in dataset", example = "82.3")
    private BigDecimal max;
    
    @Schema(description = "Average (mean) value", example = "75.4")
    private BigDecimal avg;
    
    @Schema(description = "Median value", example = "75.0")
    private BigDecimal median;
    
    @Schema(description = "Standard deviation", example = "3.2")
    private BigDecimal stdDev;
}
