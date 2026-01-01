/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AlloyDTO
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.network.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.common.model.Alloy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Alloy Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - name (F_01) - required
 * - code (F_02) - unique, required
 * - density (F_03) - required
 * - viscosity (F_04) - required
 * - flashPoint (F_05) - required
 * - sulfurContent (F_06) - required
 * - isHazardous (F_07) - required
 * - description (F_08) - required
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlloyDTO extends GenericDTO<Alloy> {

    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationAr;

    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationEn;

    @NotBlank(message = "Designation is required")
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationFr;
    
    @Size(max = 100, message = "Arabic decsription must not exceed 100 characters")
    private String descriptionAr;

    @Size(max = 100, message = "English decsription must not exceed 100 characters")
    private String descriptionEn;

    @NotBlank(message = "French decsription is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String descriptionFr;

    @Override
    public Alloy toEntity() {
        Alloy entity = new Alloy();
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
    public void updateEntity(Alloy entity) {
        if (this.code != null) { entity.setCode(this.code); }
        if (this.designationAr != null) { entity.setDesignationAr(this.designationAr); }
        if (this.designationEn != null) { entity.setDesignationEn(this.designationEn); }
        if (this.designationFr != null) { entity.setDesignationFr(this.designationFr); }
        if (this.descriptionAr != null) { entity.setDescriptionAr(this.descriptionAr); }
        if (this.descriptionEn != null) { entity.setDescriptionEn(this.descriptionEn); }
        if (this.descriptionFr != null) { entity.setDescriptionFr(this.descriptionFr); }
    }

    public static AlloyDTO fromEntity(Alloy entity) {
        if (entity == null) return null;
        
        return AlloyDTO.builder()
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
