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
 * Comprehensive overview combining asset information, current readings, and operational KPIs
 */
@Schema(description = "Complete operational dashboard for a pipeline")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineOverviewDTO {
    
    // Asset Information
    @Schema(description = "Pipeline asset specifications and configuration")
    private PipelineAssetDTO asset;
    
    // Operational Status
    @Schema(description = "Current operational status", example = "ACTIVE",
            allowableValues = {"ACTIVE", "STANDBY", "MAINTENANCE", "SHUTDOWN"})
    private String operationalStatus;
    
    // Current Measurements (Latest Reading)
    @Schema(description = "Current pressure reading in bar", example = "75.8")
    private BigDecimal currentPressure;
    
    @Schema(description = "Current temperature reading in °C", example = "22.5")
    private BigDecimal currentTemperature;
    
    @Schema(description = "Current flow rate in m³/h", example = "1875.20")
    private BigDecimal currentFlowRate;
    
    @Schema(description = "Timestamp of last recorded reading", example = "2026-02-07T15:45:00")
    private LocalDateTime lastReadingTime;
    
    // Slot Coverage KPIs (Today)
    @Schema(description = "Total number of slots in a day", example = "12")
    private Integer totalSlotsToday;
    
    @Schema(description = "Number of slots with recordings", example = "9")
    private Integer recordedSlots;
    
    @Schema(description = "Number of approved slots", example = "7")
    private Integer approvedSlots;
    
    @Schema(description = "Number of slots pending validation", example = "2")
    private Integer pendingValidationSlots;
    
    @Schema(description = "Number of overdue slots", example = "1")
    private Integer overdueSlots;
    
    @Schema(description = "Completion rate (approved/total)", example = "58.33")
    private Double completionRate;
    
    @Schema(description = "Average weekly completion rate (last 7 days)", example = "87.50")
    private Double weeklyCompletionRate;
    
    // Alert Summary
    @Schema(description = "Number of active alerts", example = "0")
    private Integer activeAlertsCount;
    
    // Volume Statistics
    @Schema(description = "Total volume transported today in m³", example = "45000.75")
    private BigDecimal volumeTransportedToday;
    
    @Schema(description = "Total volume transported this week in m³", example = "315000.50")
    private BigDecimal volumeTransportedWeek;
}
