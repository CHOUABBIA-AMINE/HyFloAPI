/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineOverviewDTO
 * 	@CreatedOn	: 02-07-2026
 * 	@UpdatedOn	: 02-10-2026 - Phase 1: Use network PipelineDTO instead of PipelineAssetDTO
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 * 	@Refactoring: Phase 1 - Replaced PipelineAssetDTO with PipelineDTO from network module.
 * 	              Eliminates DTO redundancy and establishes single source of truth.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comprehensive overview combining asset information, current readings, and operational KPIs.
 * 
 * This DTO aggregates:
 * - Pipeline asset specifications (from network module via PipelineDTO)
 * - Real-time operational measurements
 * - Daily slot coverage statistics
 * - Weekly completion trends
 * - Alert summaries
 * 
 * Used by dashboard/overview endpoints to provide complete pipeline operational status.
 */
@Schema(description = "Complete operational dashboard for a pipeline")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineOverviewDTO {
    
    // ========== ASSET INFORMATION ==========
    
    /**
     * Pipeline asset specifications and configuration.
     * 
     * REFACTORED: Now uses PipelineDTO from network module instead of
     * the redundant PipelineAssetDTO that was previously defined in
     * intelligence module.
     * 
     * PipelineDTO provides:
     * - Basic identification (id, code, name)
     * - Physical characteristics (length, diameter, thickness)
     * - Pressure specifications (design/operational min/max)
     * - Capacity specifications (design/operational)
     * - Network connections (departure/arrival terminals)
     * - Material information (construction material, coatings)
     * - Organization references (owner, manager, pipeline system)
     */
    @Schema(description = "Pipeline asset specifications and configuration")
    private PipelineDTO asset;
    
    // ========== OPERATIONAL STATUS ==========
    
    @Schema(description = "Current operational status", example = "ACTIVE",
            allowableValues = {"ACTIVE", "STANDBY", "MAINTENANCE", "SHUTDOWN"})
    private String operationalStatus;
    
    // ========== CURRENT MEASUREMENTS (Latest Reading) ==========
    
    @Schema(description = "Current pressure reading in bar", example = "75.8")
    private BigDecimal currentPressure;
    
    @Schema(description = "Current temperature reading in °C", example = "22.5")
    private BigDecimal currentTemperature;
    
    @Schema(description = "Current flow rate in m³/h", example = "1875.20")
    private BigDecimal currentFlowRate;
    
    @Schema(description = "Timestamp of last recorded reading", example = "2026-02-07T15:45:00")
    private LocalDateTime lastReadingTime;
    
    // ========== SLOT COVERAGE KPIs (Today) ==========
    
    @Schema(description = "Total number of slots in a day (typically 12 for 2-hour intervals)", example = "12")
    private Integer totalSlotsToday;
    
    @Schema(description = "Number of slots with recordings", example = "9")
    private Integer recordedSlots;
    
    @Schema(description = "Number of approved slots", example = "7")
    private Integer approvedSlots;
    
    @Schema(description = "Number of slots pending validation", example = "2")
    private Integer pendingValidationSlots;
    
    @Schema(description = "Number of overdue slots (past deadline without approval)", example = "1")
    private Integer overdueSlots;
    
    @Schema(description = "Completion rate (approved/total) as percentage", example = "58.33")
    private Double completionRate;
    
    @Schema(description = "Average weekly completion rate (last 7 days) as percentage", example = "87.50")
    private Double weeklyCompletionRate;
    
    // ========== ALERT SUMMARY ==========
    
    @Schema(description = "Number of active alerts for this pipeline", example = "0")
    private Integer activeAlertsCount;
    
    // ========== VOLUME STATISTICS ==========
    
    @Schema(description = "Total volume transported today in m³", example = "45000.75")
    private BigDecimal volumeTransportedToday;
    
    @Schema(description = "Total volume transported this week in m³", example = "315000.50")
    private BigDecimal volumeTransportedWeek;
}
