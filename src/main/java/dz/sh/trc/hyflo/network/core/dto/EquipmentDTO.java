/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EquipmentDTO
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
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
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.core.model.Equipment;
import dz.sh.trc.hyflo.network.core.model.Facility;
import dz.sh.trc.hyflo.network.type.dto.EquipmentTypeDTO;
import dz.sh.trc.hyflo.network.type.model.EquipmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Equipment Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - name (F_01) - required
 * - code (F_02) - required
 * - manufacturer (F_03) - required
 * - modelNumber (F_04) - required
 * - serialNumber (F_05) - required
 * - manufacturingDate (F_06) - required
 * - installationDate (F_07) - required
 * - lastMaintenanceDate (F_08) - required
 * - operationalStatusId (F_09) - required
 * - equipmentTypeId (F_10) - required
 * - facilityId (F_11) - required
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentDTO extends GenericDTO<Equipment> {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @NotBlank(message = "Manufacturer is required")
    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    private String manufacturer;

    @NotBlank(message = "Model number is required")
    @Size(max = 50, message = "Model number must not exceed 50 characters")
    private String modelNumber;

    @NotBlank(message = "Serial number is required")
    @Size(max = 100, message = "Serial number must not exceed 100 characters")
    private String serialNumber;

    @NotNull(message = "Manufacturing date is required")
    private LocalDate manufacturingDate;

    @NotNull(message = "Installation date is required")
    private LocalDate installationDate;

    @NotNull(message = "Last maintenance date is required")
    private LocalDate lastMaintenanceDate;

    @NotNull(message = "Operational status is required")
    private Long operationalStatusId;

    @NotNull(message = "Equipment type is required")
    private Long equipmentTypeId;

    @NotNull(message = "Facility is required")
    private Long facilityId;

    // Nested DTOs
    private OperationalStatusDTO operationalStatus;
    private EquipmentTypeDTO equipmentType;
    private FacilityDTO facility;

    @Override
    public Equipment toEntity() {
        Equipment entity = new Equipment();
        entity.setId(getId());
        entity.setName(this.name);
        entity.setCode(this.code);
        entity.setManufacturer(this.manufacturer);
        entity.setModelNumber(this.modelNumber);
        entity.setSerialNumber(this.serialNumber);
        entity.setManufacturingDate(this.manufacturingDate);
        entity.setInstallationDate(this.installationDate);
        entity.setLastMaintenanceDate(this.lastMaintenanceDate);
        
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
        
        return entity;
    }

    @Override
    public void updateEntity(Equipment entity) {
        if (this.name != null) entity.setName(this.name);
        if (this.code != null) entity.setCode(this.code);
        if (this.manufacturer != null) entity.setManufacturer(this.manufacturer);
        if (this.modelNumber != null) entity.setModelNumber(this.modelNumber);
        if (this.serialNumber != null) entity.setSerialNumber(this.serialNumber);
        if (this.manufacturingDate != null) entity.setManufacturingDate(this.manufacturingDate);
        if (this.installationDate != null) entity.setInstallationDate(this.installationDate);
        if (this.lastMaintenanceDate != null) entity.setLastMaintenanceDate(this.lastMaintenanceDate);
        
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
    }

    public static EquipmentDTO fromEntity(Equipment entity) {
        if (entity == null) return null;
        
        return EquipmentDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .manufacturer(entity.getManufacturer())
                .modelNumber(entity.getModelNumber())
                .serialNumber(entity.getSerialNumber())
                .manufacturingDate(entity.getManufacturingDate())
                .installationDate(entity.getInstallationDate())
                .lastMaintenanceDate(entity.getLastMaintenanceDate())
                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .equipmentTypeId(entity.getEquipmentType() != null ? entity.getEquipmentType().getId() : null)
                .facilityId(entity.getFacility() != null ? entity.getFacility().getId() : null)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .equipmentType(entity.getEquipmentType() != null ? EquipmentTypeDTO.fromEntity(entity.getEquipmentType()) : null)
                .facility(entity.getFacility() != null ? FacilityDTO.fromEntity(entity.getFacility()) : null)
                .build();
    }
}
