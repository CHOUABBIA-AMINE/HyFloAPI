/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: InfrastructureDTO
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
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
import dz.sh.trc.hyflo.network.common.dto.RegionDTO;
import dz.sh.trc.hyflo.network.common.model.Region;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Infrastructure Data Transfer Object - Extends GenericDTO
 * Base infrastructure class with operational status tracking
 * This is the base class for Facility hierarchy
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - code (F_01) - unique, required identifier
 * - name (F_02) - required
 * - installationDate (F_03) - optional
 * - commissioningDate (F_04) - optional
 * - decommissioningDate (F_05) - optional
 * - operationalStatusId (F_06) - required foreign key
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfrastructureDTO extends GenericDTO<Infrastructure> {

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

    @NotNull(message = "Region is required")
    private Long regionId;
    
    private OperationalStatusDTO operationalStatus;
    private RegionDTO region;

    @Override
    public Infrastructure toEntity() {
        Infrastructure entity = new Infrastructure();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setName(this.name);
        entity.setInstallationDate(this.installationDate);
        entity.setCommissioningDate(this.commissioningDate);
        entity.setDecommissioningDate(this.decommissioningDate);
        
        if (this.operationalStatusId != null) {
            dz.sh.trc.hyflo.network.common.model.OperationalStatus status = 
                new dz.sh.trc.hyflo.network.common.model.OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.regionId != null) {
        	Region region = new Region();
        	region.setId(this.regionId);
        	entity.setRegion(region);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Infrastructure entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.name != null) entity.setName(this.name);
        if (this.installationDate != null) entity.setInstallationDate(this.installationDate);
        if (this.commissioningDate != null) entity.setCommissioningDate(this.commissioningDate);
        if (this.decommissioningDate != null) entity.setDecommissioningDate(this.decommissioningDate);
        
        if (this.operationalStatusId != null) {
            dz.sh.trc.hyflo.network.common.model.OperationalStatus status = 
                new dz.sh.trc.hyflo.network.common.model.OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.regionId != null) {
        	Region region = new Region();
        	region.setId(this.regionId);
        	entity.setRegion(region);
        }
    }

    public static InfrastructureDTO fromEntity(Infrastructure entity) {
        if (entity == null) return null;
        
        return InfrastructureDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .installationDate(entity.getInstallationDate())
                .commissioningDate(entity.getCommissioningDate())
                .decommissioningDate(entity.getDecommissioningDate())
                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .regionId(entity.getRegion() != null ? entity.getRegion().getId() : null)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .region(entity.getRegion() != null ? RegionDTO.fromEntity(entity.getRegion()) : null)
                .build();
    }
}
