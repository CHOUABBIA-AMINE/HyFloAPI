/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: OperationalStatusDTO
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
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * OperationalStatus Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - code (F_01) - required
 * - designation (F_02) - required
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperationalStatusDTO extends GenericDTO<OperationalStatus> {

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
    public OperationalStatus toEntity() {
        OperationalStatus entity = new OperationalStatus();
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
    public void updateEntity(OperationalStatus entity) {
        if (this.code != null) { entity.setCode(this.code); }
        if (this.designationAr != null) { entity.setDesignationAr(this.designationAr); }
        if (this.designationEn != null) { entity.setDesignationEn(this.designationEn); }
        if (this.designationFr != null) { entity.setDesignationFr(this.designationFr); }
        if (this.descriptionAr != null) { entity.setDescriptionAr(this.descriptionAr); }
        if (this.descriptionEn != null) { entity.setDescriptionEn(this.descriptionEn); }
        if (this.descriptionFr != null) { entity.setDescriptionFr(this.descriptionFr); }
    }

    public static OperationalStatusDTO fromEntity(OperationalStatus entity) {
        if (entity == null) return null;
        
        return OperationalStatusDTO.builder()
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
