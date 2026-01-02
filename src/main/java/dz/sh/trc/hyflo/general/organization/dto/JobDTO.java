/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: JobDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.organization.model.Job;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class JobDTO extends GenericDTO<Job> {

    @NotBlank(message = "Code is required")
    @Size(max = 10, message = "Code must not exceed 10 characters")
    private String code;

    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;
    
    @NotNull(message = "Structure ID is required")
    private Long structureId;
    
    private StructureDTO structure;

    @Override
    public Job toEntity() {
        Job entity = new Job();
        entity.setId(this.getId());
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setCode(this.code);
        
        if (this.structureId != null) {
            Structure structure = new Structure();
            structure.setId(this.structureId);
            entity.setStructure(structure);
        }
		
        return entity;
    }

    @Override
    public void updateEntity(Job entity) {
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.code != null) entity.setCode(this.code);
        
        if (this.structureId != null) {
            Structure structure = new Structure();
            structure.setId(this.structureId);
            entity.setStructure(structure);
        }
    }

    public static JobDTO fromEntity(Job entity) {
        if (entity == null) return null;
        return JobDTO.builder()
                .id(entity.getId())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .code(entity.getCode())
                .structureId(entity.getStructure() != null ? entity.getStructure().getId() : null)
                
                .structure(entity.getStructure() != null ? StructureDTO.fromEntity(entity.getStructure()) : null)
                .build();
    }
}