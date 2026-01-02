/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: HydrocarbonFieldDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-19-2025
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
import dz.sh.trc.hyflo.general.localization.dto.LocalityDTO;
import dz.sh.trc.hyflo.general.localization.model.Locality;
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.dto.VendorDTO;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import dz.sh.trc.hyflo.network.core.model.HydrocarbonField;
import dz.sh.trc.hyflo.network.type.dto.HydrocarbonFieldTypeDTO;
import dz.sh.trc.hyflo.network.type.model.HydrocarbonFieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * HydrocarbonField Data Transfer Object - Extends GenericDTO
 * Maps HydrocarbonField entity which extends Facility which extends Infrastructure
 * 
 * Inherited from Infrastructure:
 * - code, name, installationDate, commissioningDate, decommissioningDate, operationalStatusId
 * 
 * Inherited from Facility:
 * - vendor, location (vendorId, locationId)
 * 
 * HydrocarbonField specific:
 * - stationType, pipelines
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HydrocarbonFieldDTO extends GenericDTO<HydrocarbonField> {

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
    
    @NotBlank(message = "Place name is required")
    @Size(max = 100, message = "PlaceName must not exceed 100 characters")
    private String placeName;
    
    @NotNull(message = "Latitude thickness is required")
    private Double latitude;
    
    @NotNull(message = "Longitude thickness is required")
    private Double longitude;
    
    @NotNull(message = "Elevation is required")
    private Double elevation;

    @NotNull(message = "Operational status ID is required")
    private Long operationalStatusId;

    @NotNull(message = "provider is required")
    private Long vendorId;

    @NotNull(message = "Locality is required")
    private Long localityId;

    @NotNull(message = "HydrocarbonFieldType type ID is required")
    private Long hydrocarbonFieldTypeId;
    
    private OperationalStatusDTO operationalStatus;
    private VendorDTO vendor;
    private LocalityDTO locality;
    private HydrocarbonFieldTypeDTO hydrocarbonFieldType;

    @Builder.Default
    private Set<Long> pipelineIds = new HashSet<>();
	
	@Builder.Default
    private Set<Long> partnerIds = new HashSet<>();
	
	@Builder.Default
    private Set<Long> productIds = new HashSet<>();

    @Override
    public HydrocarbonField toEntity() {
        HydrocarbonField entity = new HydrocarbonField();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setName(this.name);
        entity.setInstallationDate(this.installationDate);
        entity.setCommissioningDate(this.commissioningDate);
        entity.setDecommissioningDate(this.decommissioningDate);
        entity.setPlaceName(this.placeName);
        entity.setLatitude(this.latitude);
        entity.setLongitude(this.longitude);
        entity.setElevation(this.elevation);
        
        if (this.operationalStatusId != null) {
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.vendorId != null) {
        	Vendor vendor = new Vendor();
        	vendor.setId(this.vendorId);
        	entity.setVendor(vendor);
        }
        
        if (this.localityId != null) {
        	Locality locality = new Locality();
        	locality.setId(this.localityId);
        	entity.setLocality(locality);
        }
        
        if (this.hydrocarbonFieldTypeId != null) {
        	HydrocarbonFieldType hydrocarbonFieldType = new HydrocarbonFieldType();
            hydrocarbonFieldType.setId(this.hydrocarbonFieldTypeId);
            entity.setHydrocarbonFieldType(hydrocarbonFieldType);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(HydrocarbonField entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.name != null) entity.setName(this.name);
        if (this.installationDate != null) entity.setInstallationDate(this.installationDate);
        if (this.commissioningDate != null) entity.setCommissioningDate(this.commissioningDate);
        if (this.decommissioningDate != null) entity.setDecommissioningDate(this.decommissioningDate);
        if (this.placeName != null) entity.setPlaceName(this.placeName);
        if (this.latitude != null) entity.setLatitude(this.latitude);
        if (this.longitude != null) entity.setLongitude(this.longitude);
        if (this.elevation != null) entity.setElevation(this.elevation);
        
        if (this.operationalStatusId != null) {
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.vendorId != null) {
        	Vendor vendor = new Vendor();
        	vendor.setId(this.vendorId);
        	entity.setVendor(vendor);
        }     
        
        if (this.localityId != null) {
        	Locality locality = new Locality();
        	locality.setId(this.localityId);
        	entity.setLocality(locality);
        }
        
        if (this.hydrocarbonFieldTypeId != null) {
        	HydrocarbonFieldType hydrocarbonFieldType = new HydrocarbonFieldType();
            hydrocarbonFieldType.setId(this.hydrocarbonFieldTypeId);
            entity.setHydrocarbonFieldType(hydrocarbonFieldType);
        }
    }

    public static HydrocarbonFieldDTO fromEntity(HydrocarbonField entity) {
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
        
        return HydrocarbonFieldDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .installationDate(entity.getInstallationDate())
                .commissioningDate(entity.getCommissioningDate())
                .decommissioningDate(entity.getDecommissioningDate())
                .placeName(entity.getPlaceName())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .elevation(entity.getElevation())
                
                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .vendorId(entity.getVendor() != null ? entity.getVendor().getId() : null)
                .localityId(entity.getLocality() != null ? entity.getLocality().getId() : null)
                .hydrocarbonFieldTypeId(entity.getHydrocarbonFieldType() != null ? entity.getHydrocarbonFieldType().getId() : null)
                .pipelineIds(pipelineIds)
                .partnerIds(partnerIds)
                .productIds(productIds)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .vendor(entity.getVendor() != null ? VendorDTO.fromEntity(entity.getVendor()) : null)
                .locality(entity.getLocality() != null ? LocalityDTO.fromEntity(entity.getLocality()) : null)
                .hydrocarbonFieldType(entity.getHydrocarbonFieldType() != null ? HydrocarbonFieldTypeDTO.fromEntity(entity.getHydrocarbonFieldType()) : null)
                .build();
    }
}
