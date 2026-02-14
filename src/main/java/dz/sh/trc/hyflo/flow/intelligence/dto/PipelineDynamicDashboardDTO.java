/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineDynamicDashboardDTO
 * 	@CreatedOn	: 02-14-2026
 * 	@UpdatedOn	: 02-14-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pipeline Dynamic Dashboard DTO
 * Real-time operational dashboard data optimized for frequent updates
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Real-time operational dashboard metrics for pipeline")
public class PipelineDynamicDashboardDTO {

    @Schema(description = "Pipeline ID", example = "1", required = true)
    private Long pipelineId;

    @Schema(description = "Pipeline name", example = "GT-2023-A")
    private String pipelineName;

    // Current Real-time Reading
    @Schema(description = "Latest sensor reading with all measurements")
    private FlowReadingDTO latestReading;

    // Key Metrics (for quick display)
    @Schema(description = "Key operational metrics map", example = "{\"pressure\": 45.2, \"temperature\": 42.0, \"flowRate\": 1250.5}")
    private Map<String, BigDecimal> keyMetrics;

    // Health Indicators
    @Schema(description = "Overall health status", example = "HEALTHY",
            allowableValues = {"HEALTHY", "WARNING", "CRITICAL", "UNKNOWN"})
    private String overallHealth;

    @Schema(description = "Health score (0-100)", example = "92.5", minimum = "0", maximum = "100")
    private Double healthScore;

    @Schema(description = "Number of active alerts", example = "2")
    private Integer activeAlertsCount;

    @Schema(description = "Number of critical alerts", example = "0")
    private Integer criticalAlertsCount;

    @Schema(description = "Number of warning alerts", example = "2")
    private Integer warningAlertsCount;

    // Quick Statistics
    @Schema(description = "Average pressure over last 24 hours in bar", example = "44.8")
    private BigDecimal avgPressureLast24h;

    @Schema(description = "Average temperature over last 24 hours in Celsius", example = "41.5")
    private BigDecimal avgTemperatureLast24h;

    @Schema(description = "Average flow rate over last 24 hours in m³/h", example = "1248.2")
    private BigDecimal avgFlowRateLast24h;

    @Schema(description = "Total throughput over last 24 hours in m³", example = "30012.0")
    private BigDecimal throughputLast24h;

    @Schema(description = "Number of events in last 7 days", example = "15")
    private Integer eventsLast7Days;

    @Schema(description = "Number of operations in last 7 days", example = "8")
    private Integer operationsLast7Days;

    // Status Indicators
    @Schema(description = "Pressure status", example = "NORMAL",
            allowableValues = {"NORMAL", "LOW", "HIGH", "CRITICAL"})
    private String pressureStatus;

    @Schema(description = "Temperature status", example = "NORMAL",
            allowableValues = {"NORMAL", "LOW", "HIGH", "CRITICAL"})
    private String temperatureStatus;

    @Schema(description = "Flow rate status", example = "NORMAL",
            allowableValues = {"NORMAL", "LOW", "HIGH", "CRITICAL"})
    private String flowRateStatus;

    // Sensor Coverage
    @Schema(description = "Percentage of sensors online", example = "97.8")
    private Double sensorOnlinePercent;

    @Schema(description = "Number of online sensors", example = "44")
    private Integer onlineSensors;

    @Schema(description = "Total number of sensors", example = "45")
    private Integer totalSensors;

    // Data Quality
    @Schema(description = "Data completeness percentage for today", example = "95.5")
    private Double dataCompletenessPercent;

    @Schema(description = "Number of readings validated today", example = "8")
    private Integer validatedReadingsToday;

    @Schema(description = "Number of pending readings today", example = "4")
    private Integer pendingReadingsToday;
}
