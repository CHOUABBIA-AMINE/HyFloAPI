/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: AlertStatusDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026 - CRITICAL: Added missing code field and fixed validations
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.common.model.AlertStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for AlertStatus entity.
 * Used for API requests and responses related to alert lifecycle tracking.
 */
@Schema(description = "Alert status DTO for flow alert lifecycle tracking from creation to resolution")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertStatusDTO extends GenericDTO<AlertStatus> {

    @Schema(
        description = "Unique alert status code",
        example = "ACTIVE",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
    @NotBlank(message = "Alert status code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Schema(
        description = "Alert status designation in Arabic",
        example = "نشط",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Alert status designation in English",
        example = "Active",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "Alert status designation in French (required for system use)",
        example = "Actif",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Override
    public AlertStatus toEntity() {
        AlertStatus entity = new AlertStatus();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        return entity;
    }

    @Override
    public void updateEntity(AlertStatus entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
    }

    /**
     * Converts an AlertStatus entity to its DTO representation.
     *
     * @param entity the AlertStatus entity to convert
     * @return AlertStatusDTO or null if entity is null
     */
    public static AlertStatusDTO fromEntity(AlertStatus entity) {
        if (entity == null) return null;
        
        return AlertStatusDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .build();
    }
}
