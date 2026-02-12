/**
 *
 * 	@Author		: MEDJERAB Abir
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
 * Individual data point in a time series
 */
@Schema(description = "Single data point with timestamp and value")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSeriesDataPointDTO {
    
    @Schema(description = "Timestamp of the reading", example = "2026-02-07T08:30:00")
    private LocalDateTime timestamp;
    
    @Schema(description = "Measured value", example = "75.5")
    private BigDecimal value;
    
    @Schema(description = "Reading slot code", example = "SLOT_02")
    private String slotCode;
    
    @Schema(description = "Validation status", example = "APPROVED",
            allowableValues = {"DRAFT", "SUBMITTED", "APPROVED", "REJECTED"})
    private String validationStatus;
    
    @Schema(description = "Indicates if this reading has warnings or notes", example = "false")
    private Boolean hasWarning;
}
