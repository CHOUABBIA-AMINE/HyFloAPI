/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSegmentDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-15-2026 - CRITICAL: Fixed validation mismatches and added comprehensive @Schema
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.localization.dto.CoordinateDTO;
import dz.sh.trc.hyflo.general.localization.model.Coordinate;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.network.common.dto.AlloyDTO;
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.model.Alloy;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.core.model.Facility;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for individual pipeline segments with specific physical and material properties for detailed asset management")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineSegmentDTO extends GenericDTO<PipelineSegment> {

    @Schema(
        description = "Unique identification code for the pipeline segment",
        example = "SEG-OLZ-001-KM150",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Schema(
        description = "Descriptive name of the pipeline segment",
        example = "Hassi Messaoud Pipeline Segment KM 150-175",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(
        description = "Date when the pipeline segment was physically installed",
        example = "2020-05-15",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Installation date cannot be in the future")
    private LocalDate installationDate;

    @Schema(
        description = "Date when the pipeline segment was officially commissioned for operational use",
        example = "2020-08-01",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Commissioning date cannot be in the future")
    private LocalDate commissioningDate;

    @Schema(
        description = "Date when the pipeline segment was decommissioned or retired from service",
        example = "2045-12-31",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalDate decommissioningDate;

    @Schema(
        description = "Internal diameter of this pipeline segment in millimeters",
        example = "1219.2",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Diameter is required")
    @Positive(message = "Diameter must be positive")
    private Double diameter;

    @Schema(
        description = "Length of this pipeline segment in kilometers",
        example = "25.5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Length is required")
    @Positive(message = "Length must be positive")
    private Double length;

    @Schema(
        description = "Wall thickness of this pipeline segment in millimeters",
        example = "12.7",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Thickness is required")
    @Positive(message = "Thickness must be positive")
    private Double thickness;

    @Schema(
        description = "Internal surface roughness of this pipeline segment in millimeters",
        example = "0.045",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Roughness is required")
    @PositiveOrZero(message = "Roughness must be zero or positive")
    private Double roughness;

    @Schema(
        description = "Chainage or kilometer post marking the start point of this segment along the pipeline route",
        example = "150.0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Start point is required")
    @PositiveOrZero(message = "Start point must be zero or positive")
    private Double startPoint;

    @Schema(
        description = "Chainage or kilometer post marking the end point of this segment along the pipeline route",
        example = "175.5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "End point is required")
    @PositiveOrZero(message = "End point must be zero or positive")
    private Double endPoint;
    
    @Schema(
        description = "ID of the current operational status (required)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operational status ID is required")
    private Long operationalStatusId;

    @Schema(
        description = "ID of the organizational structure owning this pipeline segment (optional)",
        example = "5",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long ownerId;
    
    @Schema(
        description = "ID of the alloy or material used for constructing this segment (optional, may differ from nominal pipeline material)",
        example = "3",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long constructionMaterialId;

    @Schema(
        description = "ID of the alloy or material used for exterior coating of this segment (optional)",
        example = "7",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long exteriorCoatingId;

    @Schema(
        description = "ID of the alloy or material used for interior coating of this segment (optional)",
        example = "9",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long interiorCoatingId;

    @Schema(
        description = "ID of the parent pipeline to which this segment belongs (required)",
        example = "2",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Pipeline is required")
    private Long pipelineId;

    @Schema(
        description = "ID of the facility where this pipeline segment originates (required)",
        example = "10",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Departure facility is required")
    private Long departureFacilityId;

    @Schema(
        description = "ID of the facility where this pipeline segment terminates (required)",
        example = "15",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Arrival facility is required")
    private Long arrivalFacilityId;
    
    @Schema(description = "Set of coordinate IDs defining the geographic path of this segment")
    @Builder.Default
    private Set<Long> coordinateIds = new HashSet<>();
    
    @Schema(description = "Current operational status of the pipeline segment")
    private OperationalStatusDTO operationalStatus;
    
    @Schema(description = "Organizational structure owning this pipeline segment")
    private StructureDTO owner;
    
    @Schema(description = "Alloy or material used for constructing this specific segment")
    private AlloyDTO constructionMaterial;
    
    @Schema(description = "Alloy or material used for exterior coating of this segment")
    private AlloyDTO exteriorCoating;
    
    @Schema(description = "Alloy or material used for interior coating of this segment")
    private AlloyDTO interiorCoating;

    @Schema(description = "Parent pipeline to which this segment belongs")
    private PipelineDTO pipeline;

    @Schema(description = "Facility where this pipeline segment originates")
    private FacilityDTO departureFacility;

    @Schema(description = "Facility where this pipeline segment terminates")
    private FacilityDTO arrivalFacility;

    @Schema(description = "Collection of coordinates defining the geographic path of this segment")
    private Set<CoordinateDTO> coordinates;

    @Override
    public PipelineSegment toEntity() {
        PipelineSegment entity = new PipelineSegment();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setName(this.name);
        entity.setInstallationDate(this.installationDate);
        entity.setCommissioningDate(this.commissioningDate);
        entity.setDecommissioningDate(this.decommissioningDate);
        entity.setDiameter(this.diameter);
        entity.setLength(this.length);
        entity.setThickness(this.thickness);
        entity.setRoughness(this.roughness);
        entity.setStartPoint(this.startPoint);
        entity.setEndPoint(this.endPoint);
        
        if (this.operationalStatusId != null) {
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.ownerId != null) {
        	Structure structure = new Structure();
        	structure.setId(this.ownerId);
        	entity.setOwner(structure);
        }
        
        if (this.constructionMaterialId != null) {
            Alloy material = new Alloy();
            material.setId(this.constructionMaterialId);
            entity.setConstructionMaterial(material);
        }
        
        if (this.exteriorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.exteriorCoatingId);
            entity.setExteriorCoating(coating);
        }
        
        if (this.interiorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.interiorCoatingId);
            entity.setInteriorCoating(coating);
        }
        
        if (this.pipelineId != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(this.pipelineId);
            entity.setPipeline(pipeline);
        }
        
        if (this.departureFacilityId != null) {
        	Facility facility = new Facility();
        	facility.setId(this.departureFacilityId);
            entity.setDepartureFacility(facility);
        }
        
        if (this.arrivalFacilityId != null) {
        	Facility facility = new Facility();
        	facility.setId(this.arrivalFacilityId);
            entity.setArrivalFacility(facility);
        }
        
        if (this.coordinates != null && !this.coordinates.isEmpty()) {
            Set<Coordinate> coordinates = this.coordinates.stream()
                    						  		  .map(CoordinateDTO::toEntity)
                    						  		  .collect(Collectors.toSet());
            entity.setCoordinates(coordinates);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(PipelineSegment entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.name != null) entity.setName(this.name);
        if (this.installationDate != null) entity.setInstallationDate(this.installationDate);
        if (this.commissioningDate != null) entity.setCommissioningDate(this.commissioningDate);
        if (this.decommissioningDate != null) entity.setDecommissioningDate(this.decommissioningDate);
        if (this.diameter != null) entity.setDiameter(this.diameter);
        if (this.length != null) entity.setLength(this.length);
        if (this.thickness != null) entity.setThickness(this.thickness);
        if (this.roughness != null) entity.setRoughness(this.roughness);
        if (this.startPoint != null) entity.setStartPoint(this.startPoint);
        if (this.endPoint != null) entity.setEndPoint(this.endPoint);
        
        if (this.operationalStatusId != null) {
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.ownerId != null) {
        	Structure structure = new Structure();
        	structure.setId(this.ownerId);
        	entity.setOwner(structure);
        }
        
        if (this.constructionMaterialId != null) {
            Alloy material = new Alloy();
            material.setId(this.constructionMaterialId);
            entity.setConstructionMaterial(material);
        }
        
        if (this.exteriorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.exteriorCoatingId);
            entity.setExteriorCoating(coating);
        }
        
        if (this.interiorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.interiorCoatingId);
            entity.setInteriorCoating(coating);
        }
        
        if (this.pipelineId != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(this.pipelineId);
            entity.setPipeline(pipeline);
        }
        
        if (this.departureFacilityId != null) {
        	Facility facility = new Facility();
        	facility.setId(this.departureFacilityId);
            entity.setDepartureFacility(facility);
        }
        
        if (this.arrivalFacilityId != null) {
        	Facility facility = new Facility();
        	facility.setId(this.arrivalFacilityId);
            entity.setArrivalFacility(facility);
        }
        
        if (this.coordinates != null && !this.coordinates.isEmpty()) {
            Set<Coordinate> coordinates = this.coordinates.stream()
                    						  		  .map(CoordinateDTO::toEntity)
                    						  		  .collect(Collectors.toSet());
            entity.setCoordinates(coordinates);
        }
    }

    public static PipelineSegmentDTO fromEntity(PipelineSegment entity) {
        if (entity == null) return null;
        
        Set<Long> coordinateIds = new HashSet<>();
        if (entity.getCoordinates() != null) {
            entity.getCoordinates().forEach(l -> coordinateIds.add(l.getId()));
        }
        
        return PipelineSegmentDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .installationDate(entity.getInstallationDate())
                .commissioningDate(entity.getCommissioningDate())
                .decommissioningDate(entity.getDecommissioningDate())
                .diameter(entity.getDiameter())
                .length(entity.getLength())
                .thickness(entity.getThickness())
                .roughness(entity.getRoughness())
                .startPoint(entity.getStartPoint())
                .endPoint(entity.getEndPoint())
                
                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .ownerId(entity.getOwner() != null ? entity.getOwner().getId() : null)
                .constructionMaterialId(entity.getConstructionMaterial() != null ? entity.getConstructionMaterial().getId() : null)
                .exteriorCoatingId(entity.getExteriorCoating() != null ? entity.getExteriorCoating().getId() : null)
                .interiorCoatingId(entity.getInteriorCoating() != null ? entity.getInteriorCoating().getId() : null)
                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)
                .departureFacilityId(entity.getDepartureFacility() != null ? entity.getDepartureFacility().getId() : null)
                .arrivalFacilityId(entity.getArrivalFacility() != null ? entity.getArrivalFacility().getId() : null)
                .coordinateIds(coordinateIds)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .owner(entity.getOwner() != null ? StructureDTO.fromEntity(entity.getOwner()) : null)
                .constructionMaterial(entity.getConstructionMaterial() != null ? AlloyDTO.fromEntity(entity.getConstructionMaterial()) : null)
                .exteriorCoating(entity.getExteriorCoating() != null ? AlloyDTO.fromEntity(entity.getExteriorCoating()) : null)
                .interiorCoating(entity.getInteriorCoating() != null ? AlloyDTO.fromEntity(entity.getInteriorCoating()) : null)
                .pipeline(entity.getPipeline() != null ? PipelineDTO.fromEntity(entity.getPipeline()) : null)                          
                .departureFacility(entity.getDepartureFacility() != null ? FacilityDTO.fromEntity(entity.getDepartureFacility()) : null)                          
                .arrivalFacility(entity.getArrivalFacility() != null ? FacilityDTO.fromEntity(entity.getArrivalFacility()) : null)                          
                .build();
    }
}
