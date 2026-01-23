/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: SeverityDTO
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
import dz.sh.trc.hyflo.flow.common.model.Severity;
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
@Schema(description = "Severity DTO for alert and event criticality classification")
public class SeverityDTO extends GenericDTO<Severity> {

    @Size(max = 50, message = "Arabic designation must not exceed 50 characters")
    @Schema(description = "Severity designation in Arabic", 
            example = "حرج")
    private String designationAr;

    @Size(max = 50, message = "English designation must not exceed 50 characters")
    @Schema(description = "Severity designation in English", 
            example = "Critical",
            maxLength = 50)
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 50, message = "French designation must not exceed 50 characters")
    @Schema(description = "Severity designation in French (required)", 
            example = "Critique", 
            required = true,
            maxLength = 50)
    private String designationFr;

    @Override
    public Severity toEntity() {
        Severity entity = new Severity();
        entity.setId(getId());
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        return entity;
    }

    @Override
    public void updateEntity(Severity entity) {
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
    }

    public static SeverityDTO fromEntity(Severity entity) {
        if (entity == null) return null;
        
        return SeverityDTO.builder()
                .id(entity.getId())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .build();
    }
}
