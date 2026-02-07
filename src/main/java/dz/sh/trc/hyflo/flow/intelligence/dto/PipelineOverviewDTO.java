/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineOverviewDTO
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
 * DTO containing comprehensive overview of pipeline operational intelligence
 * Combines static asset specifications with dynamic operational status and KPIs
 */
@Schema(description = "Comprehensive pipeline overview with asset specifications and operational KPIs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineOverviewDTO {
    
    // Static asset information
    @Schema(description = "Static asset specifications")
    private PipelineAssetDTO asset;
    
    // Current operational status
    @Schema(description = "Current operational status", example = "ACTIVE", 
            allowableValues = {"ACTIVE", "MAINTENANCE", "SHUTDOWN"})
    private String operationalStatus;
    
    @Schema(description = "Current pressure reading in bar", example = "85.5")
    private BigDecimal currentPressure;
    
    @Schema(description = "Current temperature reading in Celsius", example = "45.2")
    private BigDecimal currentTemperature;
    
    @Schema(description = "Current flow rate in cubic meters per hour", example = "1875.0")
    private BigDecimal currentFlowRate;
    
    @Schema(description = "Timestamp of last recorded reading", example = "2026-02-07T14:30:00")
    private LocalDateTime lastReadingTime;
    
    // Today's slot monitoring KPIs
    @Schema(description = "Total slots expected today (always 12)", example = "12")
    private Integer totalSlotsToday;
    
    @Schema(description = "Number of slots with recorded readings", example = "10")
    private Integer recordedSlots;
    
    @Schema(description = "Number of slots with approved readings", example = "8")
    private Integer approvedSlots;
    
    @Schema(description = "Number of slots pending validation", example = "2")
    private Integer pendingValidationSlots;
    
    @Schema(description = "Number of slots past deadline without validation", example = "1")
    private Integer overdueSlots;
    
    @Schema(description = "Daily completion rate (approved/total) as percentage", example = "83.33")
    private Double completionRate;
    
    // Weekly summary
    @Schema(description = "Weekly average completion rate as percentage", example = "87.5")
    private Double weeklyCompletionRate;
    
    @Schema(description = "Number of active alerts requiring attention", example = "2")
    private Integer activeAlertsCount;
    
    @Schema(description = "Volume transported today in cubic meters", example = "42500.0")
    private BigDecimal volumeTransportedToday;
    
    @Schema(description = "Volume transported this week in cubic meters", example = "285000.0")
    private BigDecimal volumeTransportedWeek;
}
