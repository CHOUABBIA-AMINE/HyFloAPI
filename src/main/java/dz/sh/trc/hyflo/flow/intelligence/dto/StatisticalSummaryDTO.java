/**
 *
 * 	@Author		: CHOUABBIA Amine
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
 * DTO containing statistical summary for a dataset
 */
@Schema(description = "Statistical summary of measurement values")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalSummaryDTO {
    
    @Schema(description = "Minimum value", example = "75.2")
    private BigDecimal min;
    
    @Schema(description = "Maximum value", example = "92.8")
    private BigDecimal max;
    
    @Schema(description = "Average (mean) value", example = "85.5")
    private BigDecimal avg;
    
    @Schema(description = "Median value", example = "84.7")
    private BigDecimal median;
    
    @Schema(description = "Standard deviation", example = "4.3")
    private BigDecimal stdDev;
}
