/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: ReadingsTimeSeriesDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO containing time-series data for a specific measurement type
 */
@Schema(description = "Time-series data for historical readings with statistical analysis")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingsTimeSeriesDTO {
    
    @Schema(description = "Type of measurement", example = "PRESSURE",
            allowableValues = {"PRESSURE", "TEMPERATURE", "FLOW_RATE", "CONTAINED_VOLUME"})
    private String measurementType;
    
    @Schema(description = "List of data points ordered chronologically")
    private List<TimeSeriesDataPointDTO> dataPoints;
    
    @Schema(description = "Statistical summary of the dataset")
    private StatisticalSummaryDTO statistics;
}
