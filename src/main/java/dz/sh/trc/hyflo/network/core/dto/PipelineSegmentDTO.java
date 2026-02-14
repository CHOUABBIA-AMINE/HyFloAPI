/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSegmentDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2025
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineSegmentDTO extends GenericDTO<PipelineSegment> {

    @NotBlank(message = "Code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    private String code;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    private LocalDate installationDate;

    private LocalDate commissioningDate;

    private LocalDate decommissioningDate;

    @NotNull(message = "Diameter is required")
    @PositiveOrZero(message = "Diameter must be positive")
    private Double diameter;

    @NotNull(message = "Length is required")
    @PositiveOrZero(message = "Length must be positive")
    private Double length;

    @NotNull(message = "Thickness is required")
    @PositiveOrZero(message = "Thickness must be positive")
    private Double thickness;

    @NotNull(message = "Roughness is required")
    @PositiveOrZero(message = "Roughness must be positive")
    private Double roughness;

    @NotNull(message = "Start Point is required")
    @PositiveOrZero(message = "Start Point must be positive")
    private Double startPoint;

    @NotNull(message = "End Point is required")
    @PositiveOrZero(message = "End Point must be positive")
    private Double endPoint;
    
    @NotNull(message = "Operational status ID is required")
    private Long operationalStatusId;

    @NotNull(message = "Structure is required")
    private Long ownerId;
    
    @NotNull(message = "Construction material ID is required")
    private Long constructionMaterialId;

    @NotNull(message = "Exterior coating ID is required")
    private Long exteriorCoatingId;

    @NotNull(message = "Interior coating ID is required")
    private Long interiorCoatingId;

    @NotNull(message = "Pipeline is required")
    private Long pipelineId;

    @NotNull(message = "Departure Facility is required")
    private Long departureFacilityId;

    @NotNull(message = "Arrival Facility is required")
    private Long arrivalFacilityId;
    
    @Builder.Default
    private Set<Long> coordinateIds = new HashSet<>();
    
    private OperationalStatusDTO operationalStatus;
    
    private StructureDTO owner;
    
    private AlloyDTO constructionMaterial;
    
    private AlloyDTO exteriorCoating;
    
    private AlloyDTO interiorCoating;

    private PipelineDTO pipeline;

    private FacilityDTO departureFacility;

    private FacilityDTO arrivalFacility;

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
