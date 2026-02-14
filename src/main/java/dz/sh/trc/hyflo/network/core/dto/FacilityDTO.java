/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FacilityDTO
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
import dz.sh.trc.hyflo.general.localization.dto.LocationDTO;
import dz.sh.trc.hyflo.general.localization.model.Location;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.dto.VendorDTO;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import dz.sh.trc.hyflo.network.core.model.Facility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class FacilityDTO extends GenericDTO<Facility> {

    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    private LocalDate installationDate;

    private LocalDate commissioningDate;

    private LocalDate decommissioningDate;

    @NotNull(message = "Operational status is required")
    private Long operationalStatusId;

    private Long ownerId;

    @NotNull(message = "provider is required")
    private Long vendorId;
    
    @NotNull(message = "Location is required")
    private Long locationId;

    private OperationalStatusDTO operationalStatus;
    
    private StructureDTO owner;
    
    private VendorDTO vendor;
    
    private LocationDTO location;

    @Override
    public Facility toEntity() {
        Facility entity = new Facility();
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
        
        if (this.ownerId != null) {
        	Structure owner = new Structure();
        	owner.setId(this.ownerId);
        	entity.setOwner(owner);
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
        
        return entity;
    }

    @Override
    public void updateEntity(Facility entity) {
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
        
        if (this.ownerId != null) {
        	Structure owner = new Structure();
        	owner.setId(this.ownerId);
        	entity.setOwner(owner);
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
    }

    public static FacilityDTO fromEntity(Facility entity) {
        if (entity == null) return null;
        
        return FacilityDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .installationDate(entity.getInstallationDate())
                .commissioningDate(entity.getCommissioningDate())
                .decommissioningDate(entity.getDecommissioningDate())
                
                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .ownerId(entity.getOwner() != null ? entity.getOwner().getId() : null)
                .vendorId(entity.getVendor() != null ? entity.getVendor().getId() : null)
                .locationId(entity.getLocation() != null ? entity.getLocation().getId() : null)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .owner(entity.getOwner() != null ? StructureDTO.fromEntity(entity.getOwner()) : null)
                .vendor(entity.getVendor() != null ? VendorDTO.fromEntity(entity.getVendor()) : null)
                .location(entity.getLocation() != null ? LocationDTO.fromEntity(entity.getLocation()) : null)
                .build();
    }
}
