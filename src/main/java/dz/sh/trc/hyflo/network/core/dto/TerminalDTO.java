/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: TerminalDTO
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
import dz.sh.trc.hyflo.network.core.model.Terminal;
import dz.sh.trc.hyflo.network.type.dto.TerminalTypeDTO;
import dz.sh.trc.hyflo.network.type.model.TerminalType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for storage and distribution terminals serving as pipeline origin or destination points")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerminalDTO extends GenericDTO<Terminal> {

    @Schema(
        description = "Unique identification code for the terminal",
        example = "TRM-SKD-001",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Schema(
        description = "Official name of the terminal",
        example = "Skikda Export Terminal",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    @Schema(
        description = "Date when the terminal was physically installed",
        example = "2020-05-15",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Installation date cannot be in the future")
    private LocalDate installationDate;

    @Schema(
        description = "Date when the terminal was officially commissioned for operational use",
        example = "2020-08-01",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Commissioning date cannot be in the future")
    private LocalDate commissioningDate;

    @Schema(
        description = "Date when the terminal was decommissioned or retired from service",
        example = "2045-12-31",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalDate decommissioningDate;

    @Schema(
        description = "ID of the current operational status (required)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operational status is required")
    private Long operationalStatusId;

    @Schema(
        description = "ID of the organizational structure owning this terminal (optional)",
        example = "5",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long ownerId;

    @Schema(
        description = "ID of the vendor or contractor who supplied/constructed the terminal (required)",
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
        description = "ID of the terminal type (export, import, storage, distribution, etc.) - required",
        example = "3",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Terminal type ID is required")
    private Long terminalTypeId;
    
    @Schema(description = "Current operational status of the terminal")
    private OperationalStatusDTO operationalStatus;
    
    @Schema(description = "Organizational structure owning this terminal")
    private StructureDTO owner;
    
    @Schema(description = "Vendor or contractor who supplied or constructed the terminal")
    private VendorDTO vendor;
    
    @Schema(description = "Geographic location with coordinates")
    private LocationDTO location;
    
    @Schema(description = "Type of terminal (export, import, storage, distribution)")
    private TerminalTypeDTO terminalType;

    @Schema(description = "Set of departing pipeline IDs originating from this terminal")
    @Builder.Default
    private Set<Long> departingPipelineIds = new HashSet<>();

    @Schema(description = "Set of arriving pipeline IDs terminating at this terminal")
    @Builder.Default
    private Set<Long> arrivingPipelineIds = new HashSet<>();

    @Schema(description = "Set of facility IDs connected to this terminal")
    @Builder.Default
    private Set<Long> facilityIds = new HashSet<>();

    @Override
    public Terminal toEntity() {
        Terminal entity = new Terminal();
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
        
        if (this.terminalTypeId != null) {
        	TerminalType terminalType = new TerminalType();
            terminalType.setId(this.terminalTypeId);
            entity.setTerminalType(terminalType);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Terminal entity) {
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
        
        if (this.terminalTypeId != null) {
        	TerminalType terminalType = new TerminalType();
            terminalType.setId(this.terminalTypeId);
            entity.setTerminalType(terminalType);
        }
    }

    public static TerminalDTO fromEntity(Terminal entity) {
        if (entity == null) return null;
        
        Set<Long> departingPipelineIds = new HashSet<>();
        if (entity.getDepartingPipelines() != null) {
            entity.getDepartingPipelines().forEach(p -> departingPipelineIds.add(p.getId()));
        }
        
        Set<Long> arrivingPipelineIds = new HashSet<>();
        if (entity.getArrivingPipelines() != null) {
            entity.getArrivingPipelines().forEach(p -> arrivingPipelineIds.add(p.getId()));
        }
        
        Set<Long> facilityIds = new HashSet<>();
        if (entity.getFacilities() != null) {
            entity.getFacilities().forEach(p -> facilityIds.add(p.getId()));
        }
        
        return TerminalDTO.builder()
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
                .terminalTypeId(entity.getTerminalType() != null ? entity.getTerminalType().getId() : null)
                .departingPipelineIds(departingPipelineIds)
                .arrivingPipelineIds(arrivingPipelineIds)
                .facilityIds(facilityIds)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .owner(entity.getOwner() != null ? StructureDTO.fromEntity(entity.getOwner()) : null)
                .vendor(entity.getVendor() != null ? VendorDTO.fromEntity(entity.getVendor()) : null)
                .location(entity.getLocation() != null ? LocationDTO.fromEntity(entity.getLocation()) : null)
                .terminalType(entity.getTerminalType() != null ? TerminalTypeDTO.fromEntity(entity.getTerminalType()) : null)
                .build();
    }
}
