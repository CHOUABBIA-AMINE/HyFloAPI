/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineDynamicDashboardDTO
 * 	@CreatedOn	: 02-14-2026
 * 	@UpdatedOn	: 02-14-2026 - Replace Map with KeyMetricsDTO
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Real-time operational dashboard for pipeline monitoring.
 * 	              
 * 	              DATA SOURCES (indicated in @Schema descriptions):
 * 	              - [Pipeline] = Network.Core.Pipeline entity
 * 	              - [FlowReading] = FlowReading entity (latest or aggregated)
 * 	              - [FlowAlert] = FlowAlert entities (active alerts)
 * 	              - [FlowEvent] = FlowEvent entities for event counts
 * 	              - [FlowOperation] = FlowOperation entities for operation counts
 * 	              - [FlowThreshold] = Threshold comparisons for status
 * 	              - [Calculated] = Aggregated/derived metrics
 * 	              - [Sensor] = Network.Sensor entities for sensor status
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pipeline Dynamic Dashboard DTO
 * Real-time operational dashboard data optimized for frequent updates (30-second cache)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Real-time operational dashboard metrics for pipeline monitoring")
public class PipelineDynamicDashboardDTO {

    // ========== IDENTIFICATION (Source: Pipeline) ==========
    
    @Schema(description = "[Pipeline] Pipeline ID", example = "1", required = true)
    private Long pipelineId;

    @Schema(description = "[Pipeline] Pipeline name", example = "GT-2023-A")
    private String pipelineName;

    // ========== CURRENT READING (Source: FlowReading - latest) ==========
    
    @Schema(description = "[FlowReading.latest] Latest sensor reading with full measurement details")
    private FlowReadingDTO latestReading;

    // ========== KEY METRICS (Source: FlowReading - latest) ==========
    
    @Schema(description = "[FlowReading.latest] Key operational metrics extracted from latest reading for quick display")
    private KeyMetricsDTO keyMetrics;

    // ========== HEALTH INDICATORS (Source: Calculated from FlowAlert + FlowReading) ==========
    
    @Schema(description = "[Calculated] Overall health status from alerts and threshold violations", 
            example = "HEALTHY",
            allowableValues = {"HEALTHY", "WARNING", "CRITICAL", "UNKNOWN"})
    private String overallHealth;

    @Schema(description = "[Calculated] Health score (0-100) based on alerts, thresholds, and data quality", 
            example = "92.5", minimum = "0", maximum = "100")
    private Double healthScore;

    @Schema(description = "[FlowAlert] Number of active (unresolved) alerts", example = "2")
    private Integer activeAlertsCount;

    @Schema(description = "[FlowAlert] Number of critical severity alerts", example = "0")
    private Integer criticalAlertsCount;

    @Schema(description = "[FlowAlert] Number of warning severity alerts", example = "2")
    private Integer warningAlertsCount;

    // ========== 24-HOUR STATISTICS (Source: Calculated from FlowReading) ==========
    
    @Schema(description = "[Calculated] Average pressure over last 24 hours in bar", example = "44.8")
    private BigDecimal avgPressureLast24h;

    @Schema(description = "[Calculated] Average temperature over last 24 hours in Celsius", example = "41.5")
    private BigDecimal avgTemperatureLast24h;

    @Schema(description = "[Calculated] Average flow rate over last 24 hours in m³/h", example = "1248.2")
    private BigDecimal avgFlowRateLast24h;

    @Schema(description = "[Calculated] Total throughput (sum of contained volumes) over last 24 hours in m³", example = "30012.0")
    private BigDecimal throughputLast24h;

    // ========== RECENT ACTIVITY COUNTS (Source: FlowEvent + FlowOperation) ==========
    
    @Schema(description = "[FlowEvent] Number of events recorded in last 7 days", example = "15")
    private Integer eventsLast7Days;

    @Schema(description = "[FlowOperation] Number of operations performed in last 7 days", example = "8")
    private Integer operationsLast7Days;

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

    // ========== SENSOR COVERAGE (Source: Sensor entities + Calculated) ==========
    
    @Schema(description = "[Calculated] Percentage of sensors currently online/reporting", example = "97.8")
    private Double sensorOnlinePercent;

    @Schema(description = "[Sensor] Number of sensors currently online", example = "44")
    private Integer onlineSensors;

    @Schema(description = "[Sensor] Total number of sensors installed on pipeline", example = "45")
    private Integer totalSensors;

    // ========== DATA QUALITY (Source: Calculated from FlowReading today) ==========
    
    @Schema(description = "[Calculated] Data completeness percentage for today (recorded slots / total slots * 100)", 
            example = "95.5")
    private Double dataCompletenessPercent;

    @Schema(description = "[FlowReading] Number of readings validated today", example = "8")
    private Integer validatedReadingsToday;

    @Schema(description = "[FlowReading] Number of readings pending validation today", example = "4")
    private Integer pendingReadingsToday;
}
