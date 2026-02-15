/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineDTO
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
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.network.common.dto.AlloyDTO;
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.dto.VendorDTO;
import dz.sh.trc.hyflo.network.common.model.Alloy;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSystem;
import dz.sh.trc.hyflo.network.core.model.Terminal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for hydrocarbon transportation pipelines with comprehensive technical and operational specifications")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineDTO extends GenericDTO<Pipeline> {

    @Schema(
        description = "Unique identification code for the pipeline",
        example = "OLZ-PL-001",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
	@NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Schema(
        description = "Official name of the pipeline",
        example = "Hassi Messaoud - Skikda Crude Oil Pipeline",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(
        description = "Date when the pipeline was physically installed",
        example = "2020-05-15",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Installation date cannot be in the future")
    private LocalDate installationDate;

    @Schema(
        description = "Date when the pipeline was officially commissioned for operational use",
        example = "2020-08-01",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Commissioning date cannot be in the future")
    private LocalDate commissioningDate;

    @Schema(
        description = "Date when the pipeline was decommissioned or retired from service",
        example = "2045-12-31",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalDate decommissioningDate;

    @Schema(
        description = "Nominal internal diameter of the pipeline (e.g., '48 inches', '1200 mm')",
        example = "48 inches",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 255
    )
    @NotBlank(message = "Nominal diameter is required")
    @Size(max = 255, message = "Nominal diameter must not exceed 255 characters")
    private String nominalDiameter;

    @Schema(
        description = "Total length of the pipeline in kilometers",
        example = "850.5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Length is required")
    @Positive(message = "Length must be positive")
    private Double length;

    @Schema(
        description = "Nominal wall thickness of the pipeline (e.g., '12.7 mm', '0.5 inch')",
        example = "12.7 mm",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 255
    )
    @NotBlank(message = "Nominal thickness is required")
    @Size(max = 255, message = "Nominal thickness must not exceed 255 characters")
    private String nominalThickness;

    @Schema(
        description = "Internal surface roughness of the pipeline material in millimeters",
        example = "0.045",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Nominal roughness is required")
    @Positive(message = "Nominal roughness must be positive")
    private Double nominalRoughness;

    @Schema(
        description = "Maximum design service pressure in bar",
        example = "120.5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Design maximum service pressure is required")
    @Positive(message = "Design maximum service pressure must be positive")
    private Double designMaxServicePressure;

    @Schema(
        description = "Maximum operational service pressure in bar (typically lower than design pressure)",
        example = "100.0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operational maximum service pressure is required")
    @Positive(message = "Operational maximum service pressure must be positive")
    private Double operationalMaxServicePressure;

    @Schema(
        description = "Minimum design service pressure in bar",
        example = "10.0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Design minimum service pressure is required")
    @Positive(message = "Design minimum service pressure must be positive")
    private Double designMinServicePressure;

    @Schema(
        description = "Minimum operational service pressure in bar",
        example = "15.0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operational minimum service pressure is required")
    @Positive(message = "Operational minimum service pressure must be positive")
    private Double operationalMinServicePressure;

    @Schema(
        description = "Design capacity of the pipeline in cubic meters per day",
        example = "50000.0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Design capacity is required")
    @Positive(message = "Design capacity must be positive")
    private Double designCapacity;

    @Schema(
        description = "Operational capacity of the pipeline in cubic meters per day (typically lower than design capacity)",
        example = "45000.0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operational capacity is required")
    @Positive(message = "Operational capacity must be positive")
    private Double operationalCapacity;

    @Schema(
        description = "ID of the current operational status (required)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operational status ID is required")
    private Long operationalStatusId;

    @Schema(
        description = "ID of the organizational structure owning this pipeline",
        example = "5",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long ownerId;

    @Schema(
        description = "ID of the alloy or material used for pipeline construction",
        example = "3",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long nominalConstructionMaterialId;

    @Schema(
        description = "ID of the alloy or material used for exterior coating/protection",
        example = "7",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long nominalExteriorCoatingId;

    @Schema(
        description = "ID of the alloy or material used for interior coating/lining",
        example = "9",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long nominalInteriorCoatingId;

    @Schema(
        description = "ID of the pipeline system to which this pipeline belongs (required)",
        example = "2",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Pipeline system ID is required")
    private Long pipelineSystemId;

    @Schema(
        description = "ID of the terminal where the pipeline originates (required)",
        example = "10",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Departure terminal ID is required")
    private Long departureTerminalId;

    @Schema(
        description = "ID of the terminal where the pipeline terminates (required)",
        example = "15",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Arrival terminal ID is required")
    private Long arrivalTerminalId;

    @Schema(
        description = "ID of the organizational structure responsible for managing this pipeline (required)",
        example = "8",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Manager structure is required")
    private Long managerId;

    @Schema(
        description = "Set of vendor IDs who supplied or constructed the pipeline",
        example = "[12, 14, 18]"
    )
    @Builder.Default
    private Set<Long> vendorIds = new HashSet<>();
    
    @Schema(description = "Current operational status of the pipeline")
    private OperationalStatusDTO operationalStatus;
    
    @Schema(description = "Organizational structure owning this pipeline")
    private StructureDTO owner;
    
    @Schema(description = "Alloy or material used for pipeline construction")
    private AlloyDTO nominalConstructionMaterial;
    
    @Schema(description = "Alloy or material used for exterior coating/protection")
    private AlloyDTO nominalExteriorCoating;
    
    @Schema(description = "Alloy or material used for interior coating/lining")
    private AlloyDTO nominalInteriorCoating;
    
    @Schema(description = "Pipeline system to which this pipeline belongs")
    private PipelineSystemDTO pipelineSystem;
    
    @Schema(description = "Terminal where the pipeline originates")
    private TerminalDTO departureTerminal;
    
    @Schema(description = "Terminal where the pipeline terminates")
    private TerminalDTO arrivalTerminal;
    
    @Schema(description = "Organizational structure managing this pipeline")
    private StructureDTO manager;

    @Schema(description = "Vendors who supplied or constructed the pipeline")
    private Set<VendorDTO> vendors;

    @Override
    public Pipeline toEntity() {
        Pipeline entity = new Pipeline();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setName(this.name);
        entity.setInstallationDate(this.installationDate);
        entity.setCommissioningDate(this.commissioningDate);
        entity.setDecommissioningDate(this.decommissioningDate);
        entity.setNominalDiameter(this.nominalDiameter);
        entity.setLength(this.length);
        entity.setNominalThickness(this.nominalThickness);
        entity.setNominalRoughness(this.nominalRoughness);
        entity.setDesignMaxServicePressure(this.designMaxServicePressure);
        entity.setOperationalMaxServicePressure(this.operationalMaxServicePressure);
        entity.setDesignMinServicePressure(this.designMinServicePressure);
        entity.setOperationalMinServicePressure(this.operationalMinServicePressure);
        entity.setDesignCapacity(this.designCapacity);
        entity.setOperationalCapacity(this.operationalCapacity);
        
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
        
        if (this.nominalConstructionMaterialId != null) {
            Alloy material = new Alloy();
            material.setId(this.nominalConstructionMaterialId);
            entity.setNominalConstructionMaterial(material);
        }
        
        if (this.nominalExteriorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.nominalExteriorCoatingId);
            entity.setNominalExteriorCoating(coating);
        }
        
        if (this.nominalInteriorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.nominalInteriorCoatingId);
            entity.setNominalInteriorCoating(coating);
        }
        
        if (this.pipelineSystemId != null) {
            PipelineSystem system = new PipelineSystem();
            system.setId(this.pipelineSystemId);
            entity.setPipelineSystem(system);
        }
        
        if (this.departureTerminalId != null) {
        	Terminal terminal = new Terminal();
        	terminal.setId(this.departureTerminalId);
            entity.setDepartureTerminal(terminal);
        }
        
        if (this.arrivalTerminalId != null) {
        	Terminal terminal = new Terminal();
        	terminal.setId(this.arrivalTerminalId);
            entity.setArrivalTerminal(terminal);
        }
        
        if (this.managerId != null) {
        	Structure structure = new Structure();
        	structure.setId(this.managerId);
        	entity.setManager(structure);
        }
        
        if (this.vendors != null && !this.vendors.isEmpty()) {
            Set<Vendor> vendors = this.vendors.stream()
                    						  .map(VendorDTO::toEntity)
                    						  .collect(Collectors.toSet());
            entity.setVendors(vendors);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Pipeline entity) {

    	if (this.code != null) entity.setCode(this.code);
    	if (this.name != null) entity.setName(this.name);
    	if (this.installationDate != null) entity.setInstallationDate(this.installationDate);
    	if (this.commissioningDate != null) entity.setCommissioningDate(this.commissioningDate);
    	if (this.decommissioningDate != null) entity.setDecommissioningDate(this.decommissioningDate);
        if (this.nominalDiameter != null) entity.setNominalDiameter(this.nominalDiameter);
        if (this.length != null) entity.setLength(this.length);
        if (this.nominalThickness != null) entity.setNominalThickness(this.nominalThickness);
        if (this.nominalRoughness != null) entity.setNominalRoughness(this.nominalRoughness);
        if (this.designMaxServicePressure != null) entity.setDesignMaxServicePressure(this.designMaxServicePressure);
        if (this.operationalMaxServicePressure != null) entity.setOperationalMaxServicePressure(this.operationalMaxServicePressure);
        if (this.designMinServicePressure != null) entity.setDesignMinServicePressure(this.designMinServicePressure);
        if (this.operationalMinServicePressure != null) entity.setOperationalMinServicePressure(this.operationalMinServicePressure);
        if (this.designCapacity != null) entity.setDesignCapacity(this.designCapacity);
        if (this.operationalCapacity != null) entity.setOperationalCapacity(this.operationalCapacity);
        
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
        
        if (this.nominalConstructionMaterialId != null) {
            Alloy material = new Alloy();
            material.setId(this.nominalConstructionMaterialId);
            entity.setNominalConstructionMaterial(material);
        }
        
        if (this.nominalExteriorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.nominalExteriorCoatingId);
            entity.setNominalExteriorCoating(coating);
        }
        
        if (this.nominalInteriorCoatingId != null) {
            Alloy coating = new Alloy();
            coating.setId(this.nominalInteriorCoatingId);
            entity.setNominalInteriorCoating(coating);
        }
        
        if (this.pipelineSystemId != null) {
            PipelineSystem system = new PipelineSystem();
            system.setId(this.pipelineSystemId);
            entity.setPipelineSystem(system);
        }
        
        if (this.departureTerminalId != null) {
        	Terminal terminal = new Terminal();
        	terminal.setId(this.departureTerminalId);
            entity.setDepartureTerminal(terminal);
        }
        
        if (this.arrivalTerminalId != null) {
        	Terminal terminal = new Terminal();
        	terminal.setId(this.arrivalTerminalId);
            entity.setArrivalTerminal(terminal);
        }
        
        if (this.managerId != null) {
        	Structure structure = new Structure();
        	structure.setId(this.managerId);
        	entity.setManager(structure);
        }
        
        if (this.vendors != null && !this.vendors.isEmpty()) {
            Set<Vendor> vendors = this.vendors.stream()
                    						  .map(VendorDTO::toEntity)
                    						  .collect(Collectors.toSet());
            entity.setVendors(vendors);
        }
    }

    public static PipelineDTO fromEntity(Pipeline entity) {
        if (entity == null) return null;
        
        Set<Long> vendorIds = new HashSet<>();
        if (entity.getVendors() != null) {
            entity.getVendors().forEach(l -> vendorIds.add(l.getId()));
        }
        
        return PipelineDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .installationDate(entity.getInstallationDate())
                .commissioningDate(entity.getCommissioningDate())
                .decommissioningDate(entity.getDecommissioningDate())
                .nominalDiameter(entity.getNominalDiameter())
                .length(entity.getLength())
                .nominalThickness(entity.getNominalThickness())
                .nominalRoughness(entity.getNominalRoughness())
                .designMaxServicePressure(entity.getDesignMaxServicePressure())
                .operationalMaxServicePressure(entity.getOperationalMaxServicePressure())
                .designMinServicePressure(entity.getDesignMinServicePressure())
                .operationalMinServicePressure(entity.getOperationalMinServicePressure())
                .designCapacity(entity.getDesignCapacity())
                .operationalCapacity(entity.getOperationalCapacity())
                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .ownerId(entity.getOwner() != null ? entity.getOwner().getId() : null)
                .nominalConstructionMaterialId(entity.getNominalConstructionMaterial() != null ? entity.getNominalConstructionMaterial().getId() : null)
                .nominalExteriorCoatingId(entity.getNominalExteriorCoating() != null ? entity.getNominalExteriorCoating().getId() : null)
                .nominalInteriorCoatingId(entity.getNominalInteriorCoating() != null ? entity.getNominalInteriorCoating().getId() : null)
                .pipelineSystemId(entity.getPipelineSystem() != null ? entity.getPipelineSystem().getId() : null)
                .departureTerminalId(entity.getDepartureTerminal() != null ? entity.getDepartureTerminal().getId() : null)
                .arrivalTerminalId(entity.getArrivalTerminal() != null ? entity.getArrivalTerminal().getId() : null)
                .managerId(entity.getManager() != null ? entity.getManager().getId() : null)
                .vendorIds(vendorIds)

                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .owner(entity.getOwner() != null ? StructureDTO.fromEntity(entity.getOwner()) : null)
                .nominalConstructionMaterial(entity.getNominalConstructionMaterial() != null ? AlloyDTO.fromEntity(entity.getNominalConstructionMaterial()) : null)
                .nominalExteriorCoating(entity.getNominalExteriorCoating() != null ? AlloyDTO.fromEntity(entity.getNominalExteriorCoating()) : null)
                .nominalInteriorCoating(entity.getNominalInteriorCoating() != null ? AlloyDTO.fromEntity(entity.getNominalInteriorCoating()) : null)
                .pipelineSystem(entity.getPipelineSystem() != null ? PipelineSystemDTO.fromEntity(entity.getPipelineSystem()) : null)
                .departureTerminal(entity.getDepartureTerminal() != null ? TerminalDTO.fromEntity(entity.getDepartureTerminal()) : null)
                .arrivalTerminal(entity.getArrivalTerminal() != null ? TerminalDTO.fromEntity(entity.getArrivalTerminal()) : null)
                .manager(entity.getManager() != null ? StructureDTO.fromEntity(entity.getManager()) : null)
                .build();
    }
}
