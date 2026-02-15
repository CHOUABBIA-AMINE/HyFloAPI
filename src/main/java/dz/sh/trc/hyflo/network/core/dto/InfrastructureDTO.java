/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: InfrastructureDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-15-2026 - Fixed validation mismatches and added comprehensive @Schema documentation
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
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
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

@Schema(description = "Data Transfer Object for base infrastructure assets in the hydrocarbon transportation network")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfrastructureDTO extends GenericDTO<Infrastructure> {

    @Schema(
        description = "Unique identification code for the infrastructure asset",
        example = "OLZ-PL-001",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Schema(
        description = "Official name of the infrastructure asset",
        example = "Hassi Messaoud - Skikda Pipeline",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(
        description = "Date when the infrastructure was physically installed",
        example = "2020-05-15",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Installation date cannot be in the future")
    private LocalDate installationDate;

    @Schema(
        description = "Date when the infrastructure was officially commissioned for operational use",
        example = "2020-08-01",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @PastOrPresent(message = "Commissioning date cannot be in the future")
    private LocalDate commissioningDate;

    @Schema(
        description = "Date when the infrastructure was decommissioned or retired from service",
        example = "2045-12-31",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalDate decommissioningDate;

    @Schema(
        description = "ID of the current operational status",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operational status ID is required")
    private Long operationalStatusId;

    @Schema(
        description = "ID of the organizational structure owning this infrastructure",
        example = "5",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long ownerId;
    
    @Schema(description = "Current operational status of the infrastructure")
    private OperationalStatusDTO operationalStatus;
    
    @Schema(description = "Organizational structure owning this infrastructure")
    private StructureDTO owner;

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
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.ownerId != null) {
        	Structure owner = new Structure();
        	owner.setId(this.ownerId);
        	entity.setOwner(owner);
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
            OperationalStatus status = new OperationalStatus();
            status.setId(this.operationalStatusId);
            entity.setOperationalStatus(status);
        }
        
        if (this.ownerId != null) {
        	Structure owner = new Structure();
        	owner.setId(this.ownerId);
        	entity.setOwner(owner);
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
                .ownerId(entity.getOwner() != null ? entity.getOwner().getId() : null)
                
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .owner(entity.getOwner() != null ? StructureDTO.fromEntity(entity.getOwner()) : null)
                .build();
    }
}
