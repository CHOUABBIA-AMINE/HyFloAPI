/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: PipelineInfoDTO
 * 	@CreatedOn	: 02-14-2026
 * 	@UpdatedOn	: 02-14-2026 - Refactored to remove non-pipeline data
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 * 	@Refactoring: Removed operational metrics, thresholds, and forecast data.
 * 	              These belong to their respective modules:
 * 	              - Current readings -> FlowReading module
 * 	              - Thresholds -> Threshold module
 * 	              - Forecasts -> Forecast module
 * 	              - Operations -> Operation module
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
 * 
 * Contains ONLY pipeline-specific static infrastructure data and linked entities.
 * Does NOT contain operational metrics, thresholds, or forecasts.
 * 
 * For dynamic/operational data, use:
 * - PipelineDynamicDashboardDTO (real-time metrics)
 * - PipelineHealthDTO (health status)
 * - FlowReadingDTO (current readings)
 * - ThresholdDTO (threshold settings)
 * - ForecastDTO (forecast data)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Comprehensive pipeline infrastructure information")
public class PipelineInfoDTO {

    // ========== CORE PIPELINE INFORMATION ==========
    
    @Schema(description = "Pipeline ID", example = "1", required = true)
    private Long id;

    @Schema(description = "Pipeline name", example = "GT-2023-A", required = true)
    private String name;

    @Schema(description = "Pipeline code/reference", example = "PL-GT-2023-A")
    private String code;

    @Schema(description = "Pipeline description")
    private String description;

    // ========== PHYSICAL SPECIFICATIONS ==========
    
    @Schema(description = "Pipeline length in kilometers", example = "45.2")
    private Double lengthKm;

    @Schema(description = "Pipeline diameter in millimeters", example = "914.0")
    private Double diameterMm;

    @Schema(description = "Pipeline material", example = "X70 Steel")
    private String material;

    @Schema(description = "Pipeline wall thickness in mm", example = "12.7")
    private Double wallThicknessMm;

    @Schema(description = "Coating type", example = "3-Layer Polyethylene")
    private String coatingType;

    @Schema(description = "Burial depth in meters", example = "1.5")
    private Double burialDepthM;

    // ========== DESIGN SPECIFICATIONS ==========
    
    @Schema(description = "Maximum design pressure in bar", example = "90.0")
    private Double maxDesignPressureBar;

    @Schema(description = "Minimum design pressure in bar", example = "10.0")
    private Double minDesignPressureBar;

    @Schema(description = "Design temperature range (min) in Celsius", example = "-10.0")
    private Double minDesignTemperatureC;

    @Schema(description = "Design temperature range (max) in Celsius", example = "80.0")
    private Double maxDesignTemperatureC;

    @Schema(description = "Design flow rate in m³/h", example = "1500.0")
    private Double designFlowRate;

    @Schema(description = "Design standard/specification", example = "ASME B31.8")
    private String designStandard;

    // ========== OPERATIONAL INFO ==========
    
    @Schema(description = "Pipeline operator/manager name", example = "Sonatrach")
    private String operatorName;

    @Schema(description = "Commissioning date", example = "2023-03-15")
    private LocalDate commissionDate;

    @Schema(description = "Last inspection date", example = "2025-12-10")
    private LocalDate lastInspectionDate;

    @Schema(description = "Next scheduled inspection date", example = "2026-06-10")
    private LocalDate nextInspectionDate;

    @Schema(description = "Installation year", example = "2023")
    private Integer installationYear;

    @Schema(description = "Current operational status", example = "OPERATIONAL", 
            allowableValues = {"OPERATIONAL", "MAINTENANCE", "SHUTDOWN", "DECOMMISSIONED", "CONSTRUCTION"})
    private String status;

    // ========== GEOGRAPHIC DATA ==========
    
    @Schema(description = "Pipeline route geometry (GeoJSON LineString)")
    private Object geometry;

    @Schema(description = "Start location/station name", example = "Hassi R'Mel")
    private String startLocation;

    @Schema(description = "End location/station name", example = "Skikda")
    private String endLocation;

    @Schema(description = "Route description", example = "Crosses desert terrain via...")
    private String routeDescription;

    // ========== LINKED ENTITIES (lazy-loaded) ==========
    
    @Schema(description = "Connected stations along the pipeline")
    private List<StationDTO> stations;

    @Schema(description = "Valves installed on the pipeline")
    private List<ValveDTO> valves;

    @Schema(description = "Sensors monitoring the pipeline")
    private List<SensorDTO> sensors;

    // ========== ENTITY COUNTS (for quick display without loading full lists) ==========
    
    @Schema(description = "Number of connected stations", example = "3")
    private Integer stationCount;

    @Schema(description = "Number of installed valves", example = "12")
    private Integer valveCount;

    @Schema(description = "Number of active sensors", example = "45")
    private Integer sensorCount;

    @Schema(description = "Number of cathodic protection stations", example = "8")
    private Integer cpStationCount;

    // ========== ADDITIONAL TECHNICAL INFO ==========
    
    @Schema(description = "Fluid type being transported", example = "Natural Gas")
    private String fluidType;

    @Schema(description = "Cathodic protection type", example = "Impressed Current")
    private String cathodicProtectionType;

    @Schema(description = "SCADA system integration", example = "true")
    private Boolean scadaIntegrated;

    @Schema(description = "Pipeline class/category", example = "Class 3")
    private String pipelineClass;

    @Schema(description = "Regulatory compliance status", example = "Compliant")
    private String complianceStatus;

    @Schema(description = "Certifications", example = "ISO 9001, API 1160")
    private String certifications;

    // ========== FULL PIPELINE DETAILS (from Network module) ==========
    
    @Schema(description = "Complete pipeline details from Network Core module")
    private PipelineDTO pipelineDetails;

    // ========== METADATA ==========
    
    @Schema(description = "Last data update timestamp", example = "2026-02-14T14:30:00")
    private LocalDateTime lastUpdateTime;

    @Schema(description = "Data source/origin", example = "Network Module")
    private String dataSource;

    @Schema(description = "Active status", example = "true")
    private Boolean active;

    // ========== OPTIONAL: HEALTH STATUS (can be null for static-only calls) ==========
    
    @Schema(description = "Current health and operational metrics (optional - separate module)")
    private PipelineHealthDTO currentHealth;
}
