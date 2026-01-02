/**
 *	
 *	@author		: MEDJERAB Abir
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

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.organization.dto.RegionDTO;
import dz.sh.trc.hyflo.general.organization.model.Region;
import dz.sh.trc.hyflo.network.common.dto.AlloyDTO;
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.model.Alloy;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
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

    @NotNull(message = "Region is required")
    private Long regionId;
    
    @NotNull(message = "Construction material ID is required")
    private Long constructionMaterialId;

    @NotNull(message = "Exterior coating ID is required")
    private Long exteriorCoatingId;

    @NotNull(message = "Interior coating ID is required")
    private Long interiorCoatingId;

    @NotNull(message = "Pipeline is required")
    private Long pipelineId;
    
    private OperationalStatusDTO operationalStatus;
    
    private RegionDTO region;
    
    private AlloyDTO constructionMaterial;
    
    private AlloyDTO exteriorCoating;
    
    private AlloyDTO interiorCoating;

    private PipelineDTO pipeline;

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
        
        if (this.regionId != null) {
        	Region region = new Region();
        	region.setId(this.regionId);
        	entity.setRegion(region);
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
        
        if (this.regionId != null) {
        	Region region = new Region();
        	region.setId(this.regionId);
        	entity.setRegion(region);
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
    }

    public static PipelineSegmentDTO fromEntity(PipelineSegment entity) {
        if (entity == null) return null;
        
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
                
                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .regionId(entity.getRegion() != null ? entity.getRegion().getId() : null)
                .constructionMaterialId(entity.getConstructionMaterial() != null ? entity.getConstructionMaterial().getId() : null)
                .exteriorCoatingId(entity.getExteriorCoating() != null ? entity.getExteriorCoating().getId() : null)
                .interiorCoatingId(entity.getInteriorCoating() != null ? entity.getInteriorCoating().getId() : null)
                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .region(entity.getRegion() != null ? RegionDTO.fromEntity(entity.getRegion()) : null)
                .constructionMaterial(entity.getConstructionMaterial() != null ? AlloyDTO.fromEntity(entity.getConstructionMaterial()) : null)
                .exteriorCoating(entity.getExteriorCoating() != null ? AlloyDTO.fromEntity(entity.getExteriorCoating()) : null)
                .interiorCoating(entity.getInteriorCoating() != null ? AlloyDTO.fromEntity(entity.getInteriorCoating()) : null)
                .pipeline(entity.getPipeline() != null ? PipelineDTO.fromEntity(entity.getPipeline()) : null)                
                .build();
    }
}
