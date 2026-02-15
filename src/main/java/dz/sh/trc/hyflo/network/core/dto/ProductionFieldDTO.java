/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProductionFieldDTO
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
import dz.sh.trc.hyflo.network.core.model.ProductionField;
import dz.sh.trc.hyflo.network.core.model.ProcessingPlant;
import dz.sh.trc.hyflo.network.type.dto.ProductionFieldTypeDTO;
import dz.sh.trc.hyflo.network.type.model.ProductionFieldType;
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

@Schema(description = "Data Transfer Object for hydrocarbon production fields where oil and gas extraction occurs")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductionFieldDTO extends GenericDTO<ProductionField> {

    @Schema(
        description = "Unique identification code for the production field",
        example = "PFD-HAS-001",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Schema(
        description = "Official name of the production field",
        example = "Hassi Messaoud Oil Field",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(
        description = "Date when the production field infrastructure was installed",
        example = "2020-05-15",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Installation date cannot be in the future")
    private LocalDate installationDate;
    
    @Schema(
        description = "Date when the production field was officially commissioned for operational use",
        example = "2020-08-01",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Commissioning date cannot be in the future")
    private LocalDate commissioningDate;
    
    @Schema(
        description = "Date when the production field was decommissioned or retired from service",
        example = "2045-12-31",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalDate decommissioningDate;
    
    @Schema(
        description = "Production capacity of the field in cubic meters per day",
        example = "75000.0",
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
        description = "ID of the organizational structure owning this production field (optional)",
        example = "5",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long ownerId;

    @Schema(
        description = "ID of the vendor or contractor who developed the production field (required)",
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
        description = "ID of the production field type (oil field, gas field, etc.) - required",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Production field type ID is required")
    private Long productionFieldTypeId;

    @Schema(
        description = "ID of the processing plant where production from this field is processed (optional)",
        example = "3",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long processingPlantId;
    
    @Schema(description = "Current operational status of the production field")
    private OperationalStatusDTO operationalStatus;
    
    @Schema(description = "Organizational structure owning this production field")
    private StructureDTO owner;
    
    @Schema(description = "Vendor or contractor who developed the production field")
    private VendorDTO vendor;
    
    @Schema(description = "Geographic location with coordinates")
    private LocationDTO location;
    
    @Schema(description = "Type of production field (oil field, gas field, condensate field)")
    private ProductionFieldTypeDTO productionFieldType;
    
    @Schema(description = "Processing plant where production from this field is processed")
    private ProcessingPlantDTO processingPlant;
	
    @Schema(description = "Set of partner organization IDs involved in this production field")
	@Builder.Default
    private Set<Long> partnerIds = new HashSet<>();
	
    @Schema(description = "Set of product IDs extracted from this field")
	@Builder.Default
    private Set<Long> productIds = new HashSet<>();

    @Override
    public ProductionField toEntity() {
        ProductionField entity = new ProductionField();
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
        
        if (this.productionFieldTypeId != null) {
        	ProductionFieldType productionFieldType = new ProductionFieldType();
            productionFieldType.setId(this.productionFieldTypeId);
            entity.setProductionFieldType(productionFieldType);
        }
        
        if (this.processingPlantId != null) {
        	ProcessingPlant processingPlant = new ProcessingPlant();
        	processingPlant.setId(this.processingPlantId);
            entity.setProcessingPlant(processingPlant);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(ProductionField entity) {
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
        
        if (this.productionFieldTypeId != null) {
        	ProductionFieldType productionFieldType = new ProductionFieldType();
            productionFieldType.setId(this.productionFieldTypeId);
            entity.setProductionFieldType(productionFieldType);
        }
        
        if (this.processingPlantId != null) {
        	ProcessingPlant processingPlant = new ProcessingPlant();
        	processingPlant.setId(this.processingPlantId);
            entity.setProcessingPlant(processingPlant);
        }
    }

    public static ProductionFieldDTO fromEntity(ProductionField entity) {
        if (entity == null) return null;
		
		Set<Long> partnerIds = new HashSet<>();
        if (entity.getPartners() != null) {
            entity.getPartners().forEach(p -> partnerIds.add(p.getId()));
        }
		
		Set<Long> productIds = new HashSet<>();
        if (entity.getProducts() != null) {
            entity.getProducts().forEach(p -> productIds.add(p.getId()));
        }
        
        return ProductionFieldDTO.builder()
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
                .productionFieldTypeId(entity.getProductionFieldType() != null ? entity.getProductionFieldType().getId() : null)
                .processingPlantId(entity.getProcessingPlant() != null ? entity.getProcessingPlant().getId() : null)
                .partnerIds(partnerIds)
                .productIds(productIds)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .owner(entity.getOwner() != null ? StructureDTO.fromEntity(entity.getOwner()) : null)
                .vendor(entity.getVendor() != null ? VendorDTO.fromEntity(entity.getVendor()) : null)
                .location(entity.getLocation() != null ? LocationDTO.fromEntity(entity.getLocation()) : null)
                .productionFieldType(entity.getProductionFieldType() != null ? ProductionFieldTypeDTO.fromEntity(entity.getProductionFieldType()) : null)
                .processingPlant(entity.getProcessingPlant() != null ? ProcessingPlantDTO.fromEntity(entity.getProcessingPlant()) : null)
                .build();
    }
}
