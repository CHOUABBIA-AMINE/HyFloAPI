/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProcessingPlantDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-15-2026 - Fixed validation mismatches and added comprehensive @Schema
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

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.localization.dto.LocationDTO;
import dz.sh.trc.hyflo.general.localization.model.Location;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.dto.VendorDTO;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import dz.sh.trc.hyflo.network.core.model.ProcessingPlant;
import dz.sh.trc.hyflo.network.type.dto.ProcessingPlantTypeDTO;
import dz.sh.trc.hyflo.network.type.model.ProcessingPlantType;
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

@Schema(description = "Data Transfer Object for hydrocarbon processing plants that refine or process raw materials")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessingPlantDTO extends GenericDTO<ProcessingPlant> {

    @Schema(
        description = "Unique identification code for the processing plant",
        example = "PPL-HAS-001",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Schema(
        description = "Official name of the processing plant",
        example = "Hassi Messaoud Gas Processing Plant",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(
        description = "Date when the processing plant was physically installed",
        example = "2020-05-15",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Installation date cannot be in the future")
    private LocalDate installationDate;
    
    @Schema(
        description = "Date when the processing plant was officially commissioned for operational use",
        example = "2020-08-01",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Commissioning date cannot be in the future")
    private LocalDate commissioningDate;
    
    @Schema(
        description = "Date when the processing plant was decommissioned or retired from service",
        example = "2045-12-31",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalDate decommissioningDate;
    
    @Schema(
        description = "Processing capacity of the plant in cubic meters per day",
        example = "50000.0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Positive(message = "Capacity must be positive")
    private double capacity;

    @Schema(
        description = "ID of the current operational status (required)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operational status ID is required")
    private Long operationalStatusId;

    @Schema(
        description = "ID of the organizational structure owning this processing plant (optional)",
        example = "5",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long ownerId;

    @Schema(
        description = "ID of the vendor or contractor who supplied/constructed the processing plant (required)",
        example = "12",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Vendor is required")
    private Long vendorId;
    
    @Schema(
        description = "ID of the geographic location (required)",
        example = "8",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Location is required")
    private Long locationId;

    @Schema(
        description = "ID of the processing plant type (refinery, gas processing, etc.) - required",
        example = "2",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Processing plant type ID is required")
    private Long processingPlantTypeId;
    
    @Schema(description = "Current operational status of the processing plant")
    private OperationalStatusDTO operationalStatus;
    
    @Schema(description = "Organizational structure owning this processing plant")
    private StructureDTO owner;
    
    @Schema(description = "Vendor or contractor who supplied or constructed the processing plant")
    private VendorDTO vendor;
    
    @Schema(description = "Geographic location with coordinates")
    private LocationDTO location;
    
    @Schema(description = "Type of processing plant (refinery, gas processing plant, separation unit)")
    private ProcessingPlantTypeDTO processingPlantType;

    @Schema(description = "Set of pipeline IDs connected to this processing plant")
    @Builder.Default
    private Set<Long> pipelineIds = new HashSet<>();
	
    @Schema(description = "Set of partner organization IDs involved in this processing plant")
	@Builder.Default
    private Set<Long> partnerIds = new HashSet<>();
	
    @Schema(description = "Set of product IDs processed by this plant")
	@Builder.Default
    private Set<Long> productIds = new HashSet<>();

    @Override
    public ProcessingPlant toEntity() {
        ProcessingPlant entity = new ProcessingPlant();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setName(this.name);
        entity.setInstallationDate(this.installationDate);
        entity.setCommissioningDate(this.commissioningDate);
        entity.setDecommissioningDate(this.decommissioningDate);
        entity.setCapacity(this.capacity);

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
        
        if (this.vendorId != null) {
        	Vendor vendor = new Vendor();
        	vendor.setId(this.vendorId);
        	entity.setVendor(vendor);
        }
        
        if (this.locationId != null) {
        	Location location = new Location();
        	location.setId(this.locationId);
        	entity.setLocation(location);
        }
        
        if (this.processingPlantTypeId != null) {
        	ProcessingPlantType processingPlantType = new ProcessingPlantType();
            processingPlantType.setId(this.processingPlantTypeId);
            entity.setProcessingPlantType(processingPlantType);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(ProcessingPlant entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.name != null) entity.setName(this.name);
        if (this.installationDate != null) entity.setInstallationDate(this.installationDate);
        if (this.commissioningDate != null) entity.setCommissioningDate(this.commissioningDate);
        if (this.decommissioningDate != null) entity.setDecommissioningDate(this.decommissioningDate);
        if (this.capacity != 0) entity.setCapacity(this.capacity);

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
        
        if (this.vendorId != null) {
        	Vendor vendor = new Vendor();
        	vendor.setId(this.vendorId);
        	entity.setVendor(vendor);
        }   
        
        if (this.locationId != null) {
        	Location location = new Location();
        	location.setId(this.locationId);
        	entity.setLocation(location);
        }
        
        if (this.processingPlantTypeId != null) {
        	ProcessingPlantType processingPlantType = new ProcessingPlantType();
            processingPlantType.setId(this.processingPlantTypeId);
            entity.setProcessingPlantType(processingPlantType);
        }
    }

    public static ProcessingPlantDTO fromEntity(ProcessingPlant entity) {
        if (entity == null) return null;
        
        Set<Long> pipelineIds = new HashSet<>();
        if (entity.getPipelines() != null) {
            entity.getPipelines().forEach(p -> pipelineIds.add(p.getId()));
        }
		
		Set<Long> partnerIds = new HashSet<>();
        if (entity.getPartners() != null) {
            entity.getPartners().forEach(p -> partnerIds.add(p.getId()));
        }
		
		Set<Long> productIds = new HashSet<>();
        if (entity.getProducts() != null) {
            entity.getProducts().forEach(p -> productIds.add(p.getId()));
        }
        
        return ProcessingPlantDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .installationDate(entity.getInstallationDate())
                .commissioningDate(entity.getCommissioningDate())
                .decommissioningDate(entity.getDecommissioningDate())
                .capacity(entity.getCapacity())

                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .ownerId(entity.getOwner() != null ? entity.getOwner().getId() : null)
                .vendorId(entity.getVendor() != null ? entity.getVendor().getId() : null)
                .locationId(entity.getLocation() != null ? entity.getLocation().getId() : null)
                .processingPlantTypeId(entity.getProcessingPlantType() != null ? entity.getProcessingPlantType().getId() : null)
                .pipelineIds(pipelineIds)
                .partnerIds(partnerIds)
                .productIds(productIds)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .owner(entity.getOwner() != null ? StructureDTO.fromEntity(entity.getOwner()) : null)
                .vendor(entity.getVendor() != null ? VendorDTO.fromEntity(entity.getVendor()) : null)
                .location(entity.getLocation() != null ? LocationDTO.fromEntity(entity.getLocation()) : null)
                .processingPlantType(entity.getProcessingPlantType() != null ? ProcessingPlantTypeDTO.fromEntity(entity.getProcessingPlantType()) : null)
                .build();
    }
}
