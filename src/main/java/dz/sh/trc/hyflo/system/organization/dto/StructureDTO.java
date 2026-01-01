/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: StructureDTO
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Organization
 *
 **/

package dz.sh.trc.hyflo.system.organization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.organization.model.Structure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Structure Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StructureDTO extends GenericDTO<Structure> {

    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    private Long parentStructureId;

    @NotNull(message = "Structure type ID is required")
    private Long structureTypeId;

    @NotBlank(message = "Code is required")
    @Size(max = 10, message = "Code must not exceed 50 characters")
    private String code;

    @Override
    public Structure toEntity() {
        Structure entity = new Structure();
        entity.setId(this.getId());
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setCode(this.code);
        return entity;
    }

    @Override
    public void updateEntity(Structure entity) {
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.code != null) entity.setCode(this.code);
    }

    public static StructureDTO fromEntity(Structure entity) {
        if (entity == null) return null;
        return StructureDTO.builder()
                .id(entity.getId())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .code(entity.getCode())
                .parentStructureId(entity.getParentStructure() != null ? entity.getParentStructure().getId() : null)
                .structureTypeId(entity.getStructureType() != null ? entity.getStructureType().getId() : null)
                .build();
    }
}