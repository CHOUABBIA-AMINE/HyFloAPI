/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProcessingPlantDTO
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProcessingPlantDTO extends GenericDTO<ProcessingPlant> {

    // Infrastructure fields
    @NotBlank(message = "Code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    private String code;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    private LocalDate installationDate;
    
    private LocalDate commissioningDate;
    
    private LocalDate decommissioningDate;

    @NotNull(message = "Operational status ID is required")
    private Long operationalStatusId;

    @NotNull(message = "Structure is required")
    private Long structureId;

    @NotNull(message = "provider is required")
    private Long vendorId;
    
    @NotNull(message = "Location is required")
    private Long locationId;

    @NotNull(message = "ProcessingPlantType type ID is required")
    private Long processingPlantTypeId;
    
    private OperationalStatusDTO operationalStatus;
    
    private StructureDTO structure;
    
    private VendorDTO vendor;
    
    private LocationDTO location;
    
    private ProcessingPlantTypeDTO processingPlantType;

    @Builder.Default
    private Set<Long> pipelineIds = new HashSet<>();
	
	@Builder.Default
    private Set<Long> partnerIds = new HashSet<>();
	
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
        
        if (this.operationalStatusId != null) {
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.structureId != null) {
        	Structure structure = new Structure();
        	structure.setId(this.structureId);
        	entity.setStructure(structure);
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
        
        if (this.operationalStatusId != null) {
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.structureId != null) {
        	Structure structure = new Structure();
        	structure.setId(this.structureId);
        	entity.setStructure(structure);
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
                
                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .structureId(entity.getStructure() != null ? entity.getStructure().getId() : null)
                .vendorId(entity.getVendor() != null ? entity.getVendor().getId() : null)
                .locationId(entity.getLocation() != null ? entity.getLocation().getId() : null)
                .processingPlantTypeId(entity.getProcessingPlantType() != null ? entity.getProcessingPlantType().getId() : null)
                .pipelineIds(pipelineIds)
                .partnerIds(partnerIds)
                .productIds(productIds)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .structure(entity.getStructure() != null ? StructureDTO.fromEntity(entity.getStructure()) : null)
                .vendor(entity.getVendor() != null ? VendorDTO.fromEntity(entity.getVendor()) : null)
                .location(entity.getLocation() != null ? LocationDTO.fromEntity(entity.getLocation()) : null)
                .processingPlantType(entity.getProcessingPlantType() != null ? ProcessingPlantTypeDTO.fromEntity(entity.getProcessingPlantType()) : null)
                .build();
    }
}
