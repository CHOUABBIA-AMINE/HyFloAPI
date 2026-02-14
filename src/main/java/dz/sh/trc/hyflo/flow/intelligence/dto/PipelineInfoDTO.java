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
 * 	@Description: Comprehensive pipeline information for PipelineInfoPage.
 * 	              
 * 	              DATA SOURCES (indicated in @Schema descriptions):
 * 	              - [Pipeline] = Network.Core.Pipeline entity fields
 * 	              - [Pipeline.X] = Related entity accessed via Pipeline (e.g., manager, terminals)
 * 	              - [Calculated] = Aggregated/derived from multiple sources
 * 	              - [Network] = Fetched separately from Network module entities
 * 	              
 * 	              This DTO contains ONLY static infrastructure data.
 * 	              For operational metrics, use PipelineDynamicDashboardDTO.
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
 * Aggregates static infrastructure data with optional linked entities
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Comprehensive pipeline infrastructure information aggregating data from Network and Flow modules")
public class PipelineInfoDTO {

    // ========== CORE IDENTIFICATION (Source: Pipeline) ==========
    
    @Schema(description = "[Pipeline] Pipeline ID", example = "1", required = true)
    private Long id;

    @Schema(description = "[Pipeline] Pipeline name", example = "GT-2023-A", required = true)
    private String name;

    @Schema(description = "[Pipeline] Pipeline code/reference", example = "PL-GT-2023-A")
    private String code;

    @Schema(description = "[Pipeline] Pipeline description")
    private String description;

    @Schema(description = "[Pipeline.operationalStatus] Current operational status from Pipeline entity", 
            example = "OPERATIONAL")
    private String operationalStatus;

    // ========== PHYSICAL SPECIFICATIONS (Source: Pipeline) ==========
    
    @Schema(description = "[Pipeline.length] Pipeline length in kilometers", example = "850.5")
    private Double lengthKm;

    @Schema(description = "[Pipeline.nominalDiameter] Nominal internal diameter", example = "48 inches")
    private String nominalDiameter;

    @Schema(description = "[Pipeline.nominalThickness] Nominal wall thickness", example = "12.7 mm")
    private String nominalThickness;

    @Schema(description = "[Pipeline.nominalRoughness] Internal surface roughness", example = "0.045")
    private Double nominalRoughness;

    @Schema(description = "[Pipeline.nominalConstructionMaterial] Construction material/alloy", example = "X70 Steel")
    private String materialName;

    @Schema(description = "[Pipeline.nominalExteriorCoating] Exterior coating material", example = "3LPE")
    private String exteriorCoating;

    @Schema(description = "[Pipeline.nominalInteriorCoating] Interior coating material", example = "Epoxy")
    private String interiorCoating;

    // ========== PRESSURE SPECIFICATIONS (Source: Pipeline) ==========
    
    @Schema(description = "[Pipeline.designMaxServicePressure] Maximum design service pressure in bar", example = "120.5")
    private Double designMaxPressureBar;

    @Schema(description = "[Pipeline.operationalMaxServicePressure] Maximum operational service pressure in bar", example = "100.0")
    private Double operationalMaxPressureBar;

    @Schema(description = "[Pipeline.designMinServicePressure] Minimum design service pressure in bar", example = "10.0")
    private Double designMinPressureBar;

    @Schema(description = "[Pipeline.operationalMinServicePressure] Minimum operational service pressure in bar", example = "15.0")
    private Double operationalMinPressureBar;

    // ========== CAPACITY SPECIFICATIONS (Source: Pipeline) ==========
    
    @Schema(description = "[Pipeline.designCapacity] Design capacity in m³/day", example = "50000.0")
    private Double designCapacityM3PerDay;

    @Schema(description = "[Pipeline.operationalCapacity] Operational capacity in m³/day", example = "45000.0")
    private Double operationalCapacityM3PerDay;

    // ========== ADMINISTRATIVE (Source: Pipeline) ==========
    
    @Schema(description = "[Pipeline.manager.designationLt] Managing organization name", example = "Sonatrach - Region Centre")
    private String managerName;

    @Schema(description = "[Pipeline.owner.designationLt] Owner organization name", example = "Sonatrach")
    private String ownerName;

    @Schema(description = "[Pipeline.installationDate] Installation date", example = "2023-01-15")
    private LocalDate installationDate;

    @Schema(description = "[Pipeline.commissioningDate] Commissioning date", example = "2023-03-15")
    private LocalDate commissionDate;

    @Schema(description = "[Pipeline.decommissioningDate] Decommissioning date (if applicable)", example = "2050-12-31")
    private LocalDate decommissionDate;

    // ========== TERMINALS (Source: Pipeline.terminals) ==========
    
    @Schema(description = "[Pipeline.departureTerminal.name] Departure terminal name", example = "Hassi R'Mel")
    private String departureTerminalName;

    @Schema(description = "[Pipeline.arrivalTerminal.name] Arrival terminal name", example = "Skikda")
    private String arrivalTerminalName;

    // ========== SYSTEM (Source: Pipeline.pipelineSystem) ==========
    
    @Schema(description = "[Pipeline.pipelineSystem.name] Pipeline system name", example = "GZ1 System")
    private String pipelineSystemName;

    // ========== GEOGRAPHIC DATA (Source: Pipeline) ==========
    
    @Schema(description = "[Pipeline.geometry] Pipeline route geometry (GeoJSON LineString)")
    private Object geometry;

    // ========== LINKED ENTITIES (Source: Network module - lazy-loaded) ==========
    
    @Schema(description = "[Network] Connected stations along the pipeline (lazy-loaded)")
    private List<StationDTO> stations;

    @Schema(description = "[Network] Valves installed on the pipeline (lazy-loaded)")
    private List<ValveDTO> valves;

    @Schema(description = "[Network] Sensors monitoring the pipeline (lazy-loaded)")
    private List<SensorDTO> sensors;

    // ========== COUNTS (Source: Calculated/Aggregated) ==========
    
    @Schema(description = "[Calculated] Number of connected stations", example = "3")
    private Integer stationCount;

    @Schema(description = "[Calculated] Number of installed valves", example = "12")
    private Integer valveCount;

    @Schema(description = "[Calculated] Number of active sensors", example = "45")
    private Integer sensorCount;

    // ========== FULL PIPELINE DETAILS (Source: Pipeline) ==========
    
    @Schema(description = "[Pipeline] Complete pipeline entity with all nested relationships")
    private PipelineDTO pipelineDetails;

    // ========== HEALTH STATUS (Source: Calculated - optional) ==========
    
    @Schema(description = "[Calculated] Current health metrics aggregated from FlowReadings and FlowAlerts (optional)")
    private PipelineHealthDTO currentHealth;

    // ========== METADATA (Source: Calculated) ==========
    
    @Schema(description = "[Calculated] Last data update timestamp", example = "2026-02-14T14:30:00")
    private LocalDateTime lastUpdateTime;
}
