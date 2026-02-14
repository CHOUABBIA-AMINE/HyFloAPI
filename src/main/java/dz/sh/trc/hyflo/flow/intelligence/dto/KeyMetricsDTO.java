/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: KeyMetricsDTO
 * 	@CreatedOn	: 02-14-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Key operational metrics from latest pipeline reading.
 * 	              Replaces Map<String, BigDecimal> for type safety and clarity.
 * 	              
 * 	              DATA SOURCE: FlowReading (latest reading)
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Key Metrics DTO
 * Contains the most important real-time operational metrics from latest reading
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Key operational metrics from latest pipeline reading")
public class KeyMetricsDTO {

    @Schema(
        description = "[FlowReading.latest] Current pressure reading in bar",
        example = "45.2",
        nullable = true
    )
    private BigDecimal pressure;

    @Schema(
        description = "[FlowReading.latest] Current temperature reading in Celsius",
        example = "42.0",
        nullable = true
    )
    private BigDecimal temperature;

    @Schema(
        description = "[FlowReading.latest] Current flow rate in cubic meters per hour (m³/h)",
        example = "1250.5",
        nullable = true
    )
    private BigDecimal flowRate;

    @Schema(
        description = "[FlowReading.latest] Current contained volume in cubic meters (m³)",
        example = "520.8",
        nullable = true
    )
    private BigDecimal containedVolume;

    /**
     * Check if any metric is available
     * @return true if at least one metric is non-null
     */
    public boolean hasAnyMetric() {
        return pressure != null || temperature != null || flowRate != null || containedVolume != null;
    }

    /**
     * Check if all metrics are available
     * @return true if all metrics are non-null
     */
    public boolean hasAllMetrics() {
        return pressure != null && temperature != null && flowRate != null && containedVolume != null;
    }
}
