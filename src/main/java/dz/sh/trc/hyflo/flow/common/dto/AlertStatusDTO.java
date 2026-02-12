/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: AlertStatusDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
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

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Alert status DTO for flow alert lifecycle tracking")
public class AlertStatusDTO extends GenericDTO<AlertStatus> {

    @Size(max = 100, message = "Arabic designation must not exceed 50 characters")
    @Schema(description = "Alert status designation in Arabic", 
            example = "نشط")
    private String designationAr;

    @Size(max = 100, message = "English designation must not exceed 50 characters")
    @Schema(description = "Alert status designation in English", 
            example = "Active",
            maxLength = 100)
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 50 characters")
    @Schema(description = "Alert status designation in French (required)", 
            example = "Actif", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100)
    private String designationFr;

    @Override
    public AlertStatus toEntity() {
        AlertStatus entity = new AlertStatus();
        entity.setId(getId());
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        return entity;
    }

    @Override
    public void updateEntity(AlertStatus entity) {
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
    }

    public static AlertStatusDTO fromEntity(AlertStatus entity) {
        if (entity == null) return null;
        
        return AlertStatusDTO.builder()
                .id(entity.getId())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .build();
    }
}
