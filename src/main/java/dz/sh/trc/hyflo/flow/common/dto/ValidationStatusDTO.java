/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ValidationStatusDTO
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.dto;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import jakarta.validation.constraints.NotBlank;
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
public class ValidationStatusDTO extends GenericDTO<ValidationStatus> {
    
    @NotBlank(message = "Code is required")
    private String code;
    
    private String designationAr;
    private String designationEn;
    
    @NotBlank(message = "French designation is required")
    private String designationFr;
    
    private String descriptionAr;
    private String descriptionEn;
    private String descriptionFr;
    
    @Override
    public ValidationStatus toEntity() {
        ValidationStatus entity = new ValidationStatus();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setDescriptionAr(this.descriptionAr);
        entity.setDescriptionEn(this.descriptionEn);
        entity.setDescriptionFr(this.descriptionFr);
        return entity;
    }
    
    @Override
    public void updateEntity(ValidationStatus entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.descriptionAr != null) entity.setDescriptionAr(this.descriptionAr);
        if (this.descriptionEn != null) entity.setDescriptionEn(this.descriptionEn);
        if (this.descriptionFr != null) entity.setDescriptionFr(this.descriptionFr);
    }
    
    public static ValidationStatusDTO fromEntity(ValidationStatus entity) {
        if (entity == null) return null;
        return ValidationStatusDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .descriptionAr(entity.getDescriptionAr())
                .descriptionEn(entity.getDescriptionEn())
                .descriptionFr(entity.getDescriptionFr())
                .build();
    }
}
