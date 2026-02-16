/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ValidationStatusDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026 - CRITICAL: Fixed code size and added comprehensive @Schema
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

/**
 * Data Transfer Object for ValidationStatus entity.
 * Used for API requests and responses related to data validation tracking.
 */
@Schema(description = "Validation status DTO for flow data verification and approval workflow tracking")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationStatusDTO extends GenericDTO<ValidationStatus> {

    @Schema(
        description = "Unique code identifying the validation status (uppercase letters, numbers, hyphens, underscores)",
        example = "PENDING",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 50
    )
    @NotBlank(message = "Validation status code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", 
             message = "Code must contain only uppercase letters, numbers, hyphens, and underscores")
    private String code;

    @Schema(
        description = "Validation status designation in Arabic",
        example = "قيد الانتظار",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Validation status designation in English",
        example = "Pending Validation",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "Validation status designation in French (required for system use)",
        example = "En Attente de Validation",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Schema(
        description = "Detailed description in Arabic",
        example = "في انتظار التحقق من صحة البيانات",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 255
    )
    @Size(max = 255, message = "Arabic description must not exceed 255 characters")
    private String descriptionAr;

    @Schema(
        description = "Detailed description in English",
        example = "Awaiting data validation and approval",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 255
    )
    @Size(max = 255, message = "English description must not exceed 255 characters")
    private String descriptionEn;

    @Schema(
        description = "Detailed description in French",
        example = "En attente de validation et d'approbation des données",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 255
    )
    @Size(max = 255, message = "French description must not exceed 255 characters")
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

    /**
     * Converts a ValidationStatus entity to its DTO representation.
     *
     * @param entity the ValidationStatus entity to convert
     * @return ValidationStatusDTO or null if entity is null
     */
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
