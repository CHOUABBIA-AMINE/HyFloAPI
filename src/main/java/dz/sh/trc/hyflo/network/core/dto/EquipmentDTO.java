/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: EquipmentDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-15-2026 - CRITICAL: Fixed 5 validation mismatches and added missing fields + @Schema
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
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.dto.VendorDTO;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import dz.sh.trc.hyflo.network.core.model.Equipment;
import dz.sh.trc.hyflo.network.core.model.Facility;
import dz.sh.trc.hyflo.network.type.dto.EquipmentTypeDTO;
import dz.sh.trc.hyflo.network.type.model.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for equipment and machinery installed at hydrocarbon facilities")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentDTO extends GenericDTO<Equipment> {

    @Schema(
        description = "Unique identification code for the equipment",
        example = "PMP-HM-001",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 50
    )
    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @Schema(
        description = "Name or designation of the equipment",
        example = "Centrifugal Pump Unit A1",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(
        description = "Manufacturer's model number (optional)",
        example = "BB5-200-350",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50
    )
    @Size(max = 50, message = "Model number must not exceed 50 characters")
    private String modelNumber;

    @Schema(
        description = "Manufacturer's serial number for traceability (optional)",
        example = "SN-2020-HM-12345",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Serial number must not exceed 100 characters")
    private String serialNumber;

    @Schema(
        description = "Date when the equipment was manufactured (optional)",
        example = "2020-03-15",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Manufacturing date cannot be in the future")
    private LocalDate manufacturingDate;

    @Schema(
        description = "Date when the equipment was physically installed at the facility (optional)",
        example = "2020-06-20",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Installation date cannot be in the future")
    private LocalDate installationDate;

    @Schema(
        description = "Date when the equipment was commissioned for operational use (optional)",
        example = "2020-07-01",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Commissioning date cannot be in the future")
    private LocalDate commissioningDate;

    @Schema(
        description = "Date of the most recent maintenance service (optional)",
        example = "2025-12-15",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Last maintenance date cannot be in the future")
    private LocalDate lastMaintenanceDate;

    @Schema(
        description = "Date when the equipment was decommissioned or retired from service (optional)",
        example = "2040-12-31",
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
        description = "ID of the equipment type or category (required)",
        example = "3",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Equipment type is required")
    private Long equipmentTypeId;

    @Schema(
        description = "ID of the facility where this equipment is installed (required)",
        example = "10",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Facility is required")
    private Long facilityId;

    @Schema(
        description = "ID of the equipment manufacturer (required)",
        example = "7",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Manufacturer is required")
    private Long manufacturerId;

    @Schema(description = "Current operational status of the equipment")
    private OperationalStatusDTO operationalStatus;
    
    @Schema(description = "Type or category of the equipment")
    private EquipmentTypeDTO equipmentType;
    
    @Schema(description = "Facility where this equipment is installed")
    private FacilityDTO facility;
    
    @Schema(description = "Manufacturer of the equipment")
    private VendorDTO manufacturer;

    @Override
    public Equipment toEntity() {
        Equipment entity = new Equipment();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setName(this.name);
        entity.setModelNumber(this.modelNumber);
        entity.setSerialNumber(this.serialNumber);
        entity.setManufacturingDate(this.manufacturingDate);
        entity.setInstallationDate(this.installationDate);
        entity.setCommissioningDate(this.commissioningDate);
        entity.setLastMaintenanceDate(this.lastMaintenanceDate);
        entity.setDecommissioningDate(this.decommissioningDate);
        
        if (this.operationalStatusId != null) {
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.equipmentTypeId != null) {
            EquipmentType type = new EquipmentType();
            type.setId(this.equipmentTypeId);
            entity.setEquipmentType(type);
        }
        
        if (this.facilityId != null) {
            Facility facility = new Facility();
            facility.setId(this.facilityId);
            entity.setFacility(facility);
        }
        
        if (this.manufacturerId != null) {
            Vendor manufacturer = new Vendor();
            manufacturer.setId(this.manufacturerId);
            entity.setManufacturer(manufacturer);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Equipment entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.name != null) entity.setName(this.name);
        if (this.modelNumber != null) entity.setModelNumber(this.modelNumber);
        if (this.serialNumber != null) entity.setSerialNumber(this.serialNumber);
        if (this.manufacturingDate != null) entity.setManufacturingDate(this.manufacturingDate);
        if (this.installationDate != null) entity.setInstallationDate(this.installationDate);
        if (this.commissioningDate != null) entity.setCommissioningDate(this.commissioningDate);
        if (this.lastMaintenanceDate != null) entity.setLastMaintenanceDate(this.lastMaintenanceDate);
        if (this.decommissioningDate != null) entity.setDecommissioningDate(this.decommissioningDate);
        
        if (this.operationalStatusId != null) {
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.equipmentTypeId != null) {
            EquipmentType type = new EquipmentType();
            type.setId(this.equipmentTypeId);
            entity.setEquipmentType(type);
        }
        
        if (this.facilityId != null) {
            Facility facility = new Facility();
            facility.setId(this.facilityId);
            entity.setFacility(facility);
        }
        
        if (this.manufacturerId != null) {
            Vendor manufacturer = new Vendor();
            manufacturer.setId(this.manufacturerId);
            entity.setManufacturer(manufacturer);
        }
    }

    public static EquipmentDTO fromEntity(Equipment entity) {
        if (entity == null) return null;
        
        return EquipmentDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .modelNumber(entity.getModelNumber())
                .serialNumber(entity.getSerialNumber())
                .manufacturingDate(entity.getManufacturingDate())
                .installationDate(entity.getInstallationDate())
                .commissioningDate(entity.getCommissioningDate())
                .lastMaintenanceDate(entity.getLastMaintenanceDate())
                .decommissioningDate(entity.getDecommissioningDate())
                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .equipmentTypeId(entity.getEquipmentType() != null ? entity.getEquipmentType().getId() : null)
                .facilityId(entity.getFacility() != null ? entity.getFacility().getId() : null)
                .manufacturerId(entity.getManufacturer() != null ? entity.getManufacturer().getId() : null)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .equipmentType(entity.getEquipmentType() != null ? EquipmentTypeDTO.fromEntity(entity.getEquipmentType()) : null)
                .facility(entity.getFacility() != null ? FacilityDTO.fromEntity(entity.getFacility()) : null)
                .manufacturer(entity.getManufacturer() != null ? VendorDTO.fromEntity(entity.getManufacturer()) : null)
                .build();
    }
}
