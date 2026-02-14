/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineInfoDTO
 * 	@CreatedOn	: 02-14-2026
 * 	@UpdatedOn	: 02-14-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.dto.StationDTO;
import dz.sh.trc.hyflo.network.core.dto.ValveDTO;
import dz.sh.trc.hyflo.network.core.dto.SensorDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comprehensive Pipeline Information DTO for PipelineInfoPage
 * Aggregates static infrastructure data with optional dynamic operational status
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Comprehensive pipeline information including infrastructure and operational status")
public class PipelineInfoDTO {

    // Core Pipeline Information
    @Schema(description = "Pipeline ID", example = "1", required = true)
    private Long id;

    @Schema(description = "Pipeline name", example = "GT-2023-A", required = true)
    private String name;

    @Schema(description = "Pipeline code/reference", example = "PL-GT-2023-A")
    private String code;

    @Schema(description = "Current operational status", example = "OPERATIONAL", 
            allowableValues = {"OPERATIONAL", "MAINTENANCE", "SHUTDOWN", "EMERGENCY"})
    private String status;

    @Schema(description = "Pipeline length in kilometers", example = "45.2")
    private Double lengthKm;

    @Schema(description = "Pipeline diameter in millimeters", example = "914.0")
    private Double diameterMm;

    @Schema(description = "Pipeline material", example = "X70 Steel")
    private String material;

    @Schema(description = "Pipeline operator name", example = "Sonatrach")
    private String operatorName;

    @Schema(description = "Commissioning date", example = "2023-03-15")
    private LocalDate commissionDate;

    @Schema(description = "Pipeline route geometry (GeoJSON LineString)")
    private Object geometry;

    @Schema(description = "Pipeline description")
    private String description;

    @Schema(description = "Maximum operating pressure in bar", example = "90.0")
    private Double maxPressureBar;

    @Schema(description = "Design flow rate in m³/h", example = "1500.0")
    private Double designFlowRate;

    // Linked Entities (lazy-loaded)
    @Schema(description = "Connected stations along the pipeline")
    private List<StationDTO> stations;

    @Schema(description = "Valves installed on the pipeline")
    private List<ValveDTO> valves;

    @Schema(description = "Sensors monitoring the pipeline")
    private List<SensorDTO> sensors;

    // Current Health Status (optional - for lightweight calls can be null)
    @Schema(description = "Current health and operational metrics")
    private PipelineHealthDTO currentHealth;

    @Schema(description = "Last data update timestamp", example = "2026-02-14T14:30:00")
    private LocalDateTime lastUpdateTime;

    // Statistics
    @Schema(description = "Number of connected stations", example = "3")
    private Integer stationCount;

    @Schema(description = "Number of installed valves", example = "12")
    private Integer valveCount;

    @Schema(description = "Number of active sensors", example = "45")
    private Integer sensorCount;

    @Schema(description = "Pipeline full details (from Network module)")
    private PipelineDTO pipelineDetails;
}
