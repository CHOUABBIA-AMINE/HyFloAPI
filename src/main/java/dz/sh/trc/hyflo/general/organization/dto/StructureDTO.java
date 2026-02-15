/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StructureDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-15-2026 - Added comprehensive @Schema documentation
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.general.type.dto.StructureTypeDTO;
import dz.sh.trc.hyflo.general.type.model.StructureType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for SONATRACH organizational structure with hierarchical relationships and bilingual designation")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StructureDTO extends GenericDTO<Structure> {

    @Schema(
        description = "Structure code or identifier (required)",
        example = "DIR-001",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 10
    )
    @NotBlank(message = "Code is required")
    @Size(max = 10, message = "Code must not exceed 50 characters")
    private String code;

    @Schema(
        description = "Structure designation in Arabic script",
        example = "مديرية الموارد البشرية",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Structure designation in English",
        example = "Human Resources Directorate",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "Structure designation in French (required for SONATRACH operations)",
        example = "Direction des Ressources Humaines",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Schema(
        description = "ID of the parent structure in organizational hierarchy (null for top-level structures)",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long parentStructureId;

    @Schema(
        description = "ID of the structure type classification (required)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Structure type ID is required")
    private Long structureTypeId;

    @Schema(
        description = "Parent structure details (populated when fetching with hierarchy information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private StructureDTO parentStructure;

    @Schema(
        description = "Structure type details (populated when fetching with type information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private StructureTypeDTO structureType;

    @Override
    public Structure toEntity() {
        Structure entity = new Structure();
        entity.setId(this.getId());
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setCode(this.code);
        
        if (this.parentStructureId != null) {
            Structure structure = new Structure();
            structure.setId(this.parentStructureId);
            entity.setParentStructure(structure);
        }
        
        if (this.structureTypeId != null) {
        	StructureType structureType = new StructureType();
        	structureType.setId(this.structureTypeId);
            entity.setStructureType(structureType);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Structure entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        
        if (this.parentStructureId != null) {
            Structure structure = new Structure();
            structure.setId(this.parentStructureId);
            entity.setParentStructure(structure);
        }
        
        if (this.structureTypeId != null) {
        	StructureType structureType = new StructureType();
        	structureType.setId(this.structureTypeId);
            entity.setStructureType(structureType);
        }
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
                
                .parentStructure(entity.getParentStructure() != null ? StructureDTO.fromEntity(entity.getParentStructure()) : null)
                .structureType(entity.getStructureType() != null ? StructureTypeDTO.fromEntity(entity.getStructureType()) : null)
                .build();
    }
}