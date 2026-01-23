/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ValidationStatusDTO
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
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Schema(description = "Validation status DTO for flow data verification tracking")
public class ValidationStatusDTO extends GenericDTO<ValidationStatus> {

    @NotBlank(message = "Validation status code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", 
             message = "Code must contain only uppercase letters, numbers, hyphens, and underscores")
    @Schema(description = "Unique code identifying the validation status", 
            example = "VALIDATED", 
            required = true,
            maxLength = 20)
    private String code;

    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    @Schema(description = "Validation status designation in Arabic", 
            example = "تم التحقق")
    private String designationAr;

    @Size(max = 100, message = "English designation must not exceed 100 characters")
    @Schema(description = "Validation status designation in English", 
            example = "Validated",
            maxLength = 100)
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    @Schema(description = "Validation status designation in French (required)", 
            example = "Validé", 
            required = true,
            maxLength = 100)
    private String designationFr;

    @Size(max = 255, message = "Arabic description must not exceed 255 characters")
    @Schema(description = "Detailed description in Arabic")
    private String descriptionAr;

    @Size(max = 255, message = "English description must not exceed 255 characters")
    @Schema(description = "Detailed description in English",
            maxLength = 255)
    private String descriptionEn;

    @Size(max = 255, message = "French description must not exceed 255 characters")
    @Schema(description = "Detailed description in French",
            maxLength = 255)
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
