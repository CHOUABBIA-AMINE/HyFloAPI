/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: HydrocarbonFieldTypeDTO
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Data Transfer Object
 *	@Layer		: Network / DTO
 *	@Package	: Network / DTO
 *
 **/

package dz.sh.trc.hyflo.network.type.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.type.model.HydrocarbonFieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * HydrocarbonFieldType Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - designation (F_01) - type designation (unique, required)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HydrocarbonFieldTypeDTO extends GenericDTO<HydrocarbonFieldType> {

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

    @Override
    public HydrocarbonFieldType toEntity() {
        HydrocarbonFieldType type = new HydrocarbonFieldType();
        type.setId(getId());
        type.setCode(this.code);
        type.setDesignationAr(this.designationAr);
        type.setDesignationEn(this.designationEn);
        type.setDesignationFr(this.designationFr);
        return type;
    }

    @Override
    public void updateEntity(HydrocarbonFieldType type) {
        if (this.code != null) type.setCode(this.code);
        if (this.designationAr != null) type.setDesignationAr(this.designationAr);
        if (this.designationEn != null) type.setDesignationEn(this.designationEn);
        if (this.designationFr != null) type.setDesignationFr(this.designationFr);
    }

    public static HydrocarbonFieldTypeDTO fromEntity(HydrocarbonFieldType type) {
        if (type == null) return null;
        
        return HydrocarbonFieldTypeDTO.builder()
                .id(type.getId())
                .code(type.getCode())
                .designationAr(type.getDesignationAr())
                .designationEn(type.getDesignationEn())
                .designationFr(type.getDesignationFr())
                .build();
    }
}
