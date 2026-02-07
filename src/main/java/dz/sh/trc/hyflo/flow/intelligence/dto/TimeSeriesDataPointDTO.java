/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: TimeSeriesDataPointDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a single data point in a time-series chart
 */
@Schema(description = "Single data point in a time-series of readings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSeriesDataPointDTO {
    
    @Schema(description = "Timestamp of the reading", example = "2026-02-07T08:00:00")
    private LocalDateTime timestamp;
    
    @Schema(description = "Measured value", example = "85.5")
    private BigDecimal value;
    
    @Schema(description = "Slot code for this reading", example = "SLOT-05")
    private String slotCode;
    
    @Schema(description = "Validation status of this reading", example = "APPROVED")
    private String validationStatus;
    
    @Schema(description = "Whether this reading has business warnings", example = "false")
    private Boolean hasWarning;
}
