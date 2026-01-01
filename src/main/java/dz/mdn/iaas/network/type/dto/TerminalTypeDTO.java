/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: TerminalTypeDTO
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
import dz.mdn.iaas.network.type.model.TerminalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * TerminalType Data Transfer Object - Extends GenericDTO
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
public class TerminalTypeDTO extends GenericDTO<TerminalType> {

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
    public TerminalType toEntity() {
        TerminalType type = new TerminalType();
        type.setId(getId());
        type.setCode(this.code);
        type.setDesignationAr(this.designationAr);
        type.setDesignationEn(this.designationEn);
        type.setDesignationFr(this.designationFr);
        return type;
    }

    @Override
    public void updateEntity(TerminalType type) {
        if (this.code != null) type.setCode(this.code);
        if (this.designationAr != null) type.setDesignationAr(this.designationAr);
        if (this.designationEn != null) type.setDesignationEn(this.designationEn);
        if (this.designationFr != null) type.setDesignationFr(this.designationFr);
    }

    public static TerminalTypeDTO fromEntity(TerminalType type) {
        if (type == null) return null;
        
        return TerminalTypeDTO.builder()
                .id(type.getId())
                .code(type.getCode())
                .designationAr(type.getDesignationAr())
                .designationEn(type.getDesignationEn())
                .designationFr(type.getDesignationFr())
                .build();
    }
}
