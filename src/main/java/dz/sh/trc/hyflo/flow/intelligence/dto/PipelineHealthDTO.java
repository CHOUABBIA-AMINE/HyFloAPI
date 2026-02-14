/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineHealthDTO
 * 	@CreatedOn	: 02-14-2026
 * 	@UpdatedOn	: 02-14-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Pipeline health status aggregating operational metrics.
 * 	              
 * 	              DATA SOURCES (indicated in @Schema descriptions):
 * 	              - [FlowReading] = Latest FlowReading entity
 * 	              - [FlowAlert] = Active/recent FlowAlert entities
 * 	              - [FlowThreshold] = FlowThreshold compared with readings
 * 	              - [Calculated] = Aggregated from multiple readings/alerts
 * 	              - [FlowEvent] = FlowEvent entities for event counts
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pipeline Health Status DTO
 * Represents the current operational health and key metrics of a pipeline
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Current pipeline health status and operational metrics")
public class PipelineHealthDTO {

    // ========== OVERALL HEALTH (Source: Calculated) ==========
    
    @Schema(description = "[Calculated] Overall health status derived from alerts, thresholds, and readings", 
            example = "HEALTHY",
            allowableValues = {"HEALTHY", "WARNING", "CRITICAL", "UNKNOWN"})
    private String overallHealth;

    @Schema(description = "[Calculated] Health score (0-100) based on multiple factors", 
            example = "92.5", minimum = "0", maximum = "100")
    private Double healthScore;

    // ========== ALERT METRICS (Source: FlowAlert) ==========
    
    @Schema(description = "[FlowAlert] Number of active (unresolved) alerts", example = "2")
    private Integer activeAlertsCount;

    @Schema(description = "[FlowAlert] Number of critical severity alerts", example = "0")
    private Integer criticalAlertsCount;

    @Schema(description = "[FlowAlert] Number of warning severity alerts", example = "2")
    private Integer warningAlertsCount;

    // ========== CURRENT READINGS (Source: FlowReading - latest) ==========
    
    @Schema(description = "[FlowReading.latest] Current pressure reading in bar", example = "45.2")
    private BigDecimal currentPressure;

    @Schema(description = "[FlowReading.latest] Current temperature reading in Celsius", example = "42.0")
    private BigDecimal currentTemperature;

    @Schema(description = "[FlowReading.latest] Current flow rate in m³/h", example = "1250.5")
    private BigDecimal currentFlowRate;

    // ========== 24-HOUR STATISTICS (Source: Calculated from FlowReading) ==========
    
    @Schema(description = "[Calculated] Average pressure over last 24 hours in bar", example = "44.8")
    private BigDecimal avgPressureLast24h;

    @Schema(description = "[Calculated] Total throughput over last 24 hours in m³", example = "30012.0")
    private BigDecimal throughputLast24h;

    // ========== EVENT METRICS (Source: FlowEvent) ==========
    
    @Schema(description = "[FlowEvent] Number of events recorded in last 7 days", example = "15")
    private Integer eventsLast7Days;

    // ========== TIMESTAMPS (Source: FlowReading) ==========
    
    @Schema(description = "[FlowReading.latest.recordedAt] Timestamp of last reading", 
            example = "2026-02-14T14:25:00")
    private LocalDateTime lastReadingTime;

    // ========== STATUS INDICATORS (Source: Calculated from FlowReading vs FlowThreshold) ==========
    
    @Schema(description = "[Calculated] Pressure status based on FlowThreshold comparison", 
            example = "NORMAL",
            allowableValues = {"NORMAL", "LOW", "HIGH", "CRITICAL"})
    private String pressureStatus;

    @Schema(description = "[Calculated] Temperature status based on FlowThreshold comparison", 
            example = "NORMAL",
            allowableValues = {"NORMAL", "LOW", "HIGH", "CRITICAL"})
    private String temperatureStatus;

    @Schema(description = "[Calculated] Flow rate status based on FlowThreshold comparison", 
            example = "NORMAL",
            allowableValues = {"NORMAL", "LOW", "HIGH", "CRITICAL"})
    private String flowRateStatus;

    // ========== AVAILABILITY METRICS (Source: Calculated) ==========
    
    @Schema(description = "[Calculated] System availability percentage over last 30 days", example = "99.5")
    private Double availabilityPercent;

    @Schema(description = "[Calculated] Percentage of sensors currently online/active", example = "97.8")
    private Double sensorOnlinePercent;
}
