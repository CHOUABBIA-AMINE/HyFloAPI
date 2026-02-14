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

    @Schema(description = "Current pressure reading in bar", example = "45.2")
    private BigDecimal currentPressure;

    @Schema(description = "Current temperature reading in Celsius", example = "42.0")
    private BigDecimal currentTemperature;

    @Schema(description = "Current flow rate in m³/h", example = "1250.5")
    private BigDecimal currentFlowRate;

    @Schema(description = "Average pressure over last 24 hours in bar", example = "44.8")
    private BigDecimal avgPressureLast24h;

    @Schema(description = "Throughput over last 24 hours in m³", example = "30012.0")
    private BigDecimal throughputLast24h;

    @Schema(description = "Number of events in last 7 days", example = "15")
    private Integer eventsLast7Days;

    @Schema(description = "Last reading timestamp", example = "2026-02-14T14:25:00")
    private LocalDateTime lastReadingTime;

    @Schema(description = "Pressure status indicator", example = "NORMAL",
            allowableValues = {"NORMAL", "LOW", "HIGH", "CRITICAL"})
    private String pressureStatus;

    @Schema(description = "Temperature status indicator", example = "NORMAL",
            allowableValues = {"NORMAL", "LOW", "HIGH", "CRITICAL"})
    private String temperatureStatus;

    @Schema(description = "Flow rate status indicator", example = "NORMAL",
            allowableValues = {"NORMAL", "LOW", "HIGH", "CRITICAL"})
    private String flowRateStatus;

    @Schema(description = "System availability percentage", example = "99.5")
    private Double availabilityPercent;

    @Schema(description = "Sensor online percentage", example = "97.8")
    private Double sensorOnlinePercent;
}
