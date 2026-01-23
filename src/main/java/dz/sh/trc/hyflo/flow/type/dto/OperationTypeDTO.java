/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: OperationTypeDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
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
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Flow operation type DTO for classifying PRODUCED, TRANSPORTED, and CONSUMED operations")
public class OperationTypeDTO extends GenericDTO<OperationType> {

    @NotBlank(message = "Operation type code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    @Pattern(regexp = "PRODUCED|TRANSPORTED|CONSUMED", 
             message = "Code must be one of: PRODUCED, TRANSPORTED, CONSUMED")
    @Schema(description = "Unique code for operation type", 
            example = "PRODUCED", 
            required = true, 
            allowableValues = {"PRODUCED", "TRANSPORTED", "CONSUMED"})
    private String code;

    @Size(max = 100, message = "Arabic name must not exceed 100 characters")
    @Schema(description = "Operation type name in Arabic", example = "منتج")
    private String nameAr;

    @Size(max = 100, message = "French name must not exceed 100 characters")
    @Schema(description = "Operation type name in French", example = "Produit")
    private String nameFr;

    @Size(max = 100, message = "English name must not exceed 100 characters")
    @Schema(description = "Operation type name in English", example = "Produced")
    private String nameEn;

    @Override
    public OperationType toEntity() {
        OperationType entity = new OperationType();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setNameAr(this.nameAr);
        entity.setNameFr(this.nameFr);
        entity.setNameEn(this.nameEn);
        return entity;
    }

    @Override
    public void updateEntity(OperationType entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.nameAr != null) entity.setNameAr(this.nameAr);
        if (this.nameFr != null) entity.setNameFr(this.nameFr);
        if (this.nameEn != null) entity.setNameEn(this.nameEn);
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
                .nameAr(entity.getNameAr())
                .nameFr(entity.getNameFr())
                .nameEn(entity.getNameEn())
                .build();
    }
}
