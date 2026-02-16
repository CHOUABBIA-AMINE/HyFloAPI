/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: OperationTypeDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026 - CRITICAL: Fixed pattern validation and missing @NotBlank
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.type.model.OperationType;
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
 * Data Transfer Object for OperationType entity.
 * Used for API requests and responses related to flow operation type classification.
 */
@Schema(description = "Flow operation type DTO for classifying hydrocarbon operations (production, transport, consumption)")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperationTypeDTO extends GenericDTO<OperationType> {

    @Schema(
        description = "Unique code for operation type (uppercase letters, numbers, hyphens, underscores)",
        example = "PRODUCTION",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
    @NotBlank(message = "Operation type code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", 
             message = "Code must contain only uppercase letters, numbers, hyphens, and underscores")
    private String code;

    @Schema(
        description = "Operation type designation in Arabic",
        example = "إنتاج",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Operation type designation in French (required for system use)",
        example = "Production",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Schema(
        description = "Operation type designation in English",
        example = "Production",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Override
    public OperationType toEntity() {
        OperationType entity = new OperationType();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationFr(this.designationFr);
        entity.setDesignationEn(this.designationEn);
        return entity;
    }

    @Override
    public void updateEntity(OperationType entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
    }

    /**
     * Converts an OperationType entity to its DTO representation.
     *
     * @param entity the OperationType entity to convert
     * @return OperationTypeDTO or null if entity is null
     */
    public static OperationTypeDTO fromEntity(OperationType entity) {
        if (entity == null) return null;
        
        return OperationTypeDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationFr(entity.getDesignationFr())
                .designationEn(entity.getDesignationEn())
                .build();
    }
}
