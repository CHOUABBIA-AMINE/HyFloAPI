/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: CompanyTypeDTO
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Data Transfer Object
 *	@Layer		: Network / DTO
 *	@Package	: Network / DTO
 *
 **/

package dz.mdn.iaas.network.type.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.mdn.iaas.configuration.template.GenericDTO;
import dz.mdn.iaas.network.type.model.CompanyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * CompanyType Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - designationAr (F_01) - Arabic designation
 * - designationEn (F_02) - English designation
 * - designationFr (F_03) - French designation (unique, required)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyTypeDTO extends GenericDTO<CompanyType> {

    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(min = 2, max = 100, message = "French designation must be between 2 and 100 characters")
    private String designationFr;

    @Override
    public CompanyType toEntity() {
        CompanyType type = new CompanyType();
        type.setId(getId());
        type.setDesignationAr(this.designationAr);
        type.setDesignationEn(this.designationEn);
        type.setDesignationFr(this.designationFr);
        return type;
    }

    @Override
    public void updateEntity(CompanyType type) {
        if (this.designationAr != null) type.setDesignationAr(this.designationAr);
        if (this.designationEn != null) type.setDesignationEn(this.designationEn);
        if (this.designationFr != null) type.setDesignationFr(this.designationFr);
    }

    public static CompanyTypeDTO fromEntity(CompanyType type) {
        if (type == null) return null;
        
        return CompanyTypeDTO.builder()
                .id(type.getId())
                .designationAr(type.getDesignationAr())
                .designationEn(type.getDesignationEn())
                .designationFr(type.getDesignationFr())
                .build();
    }
}
