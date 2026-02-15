/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProcessingPlantTypeDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-15-2026 - Added comprehensive @Schema documentation
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.type.model.ProcessingPlantType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for processing plant type classification (gas processing, oil refining, LNG liquefaction plants)")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessingPlantTypeDTO extends GenericDTO<ProcessingPlantType> {

    @Schema(
        description = "Processing plant type designation in Arabic script",
        example = "مصنع معالجة الغاز",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Processing plant type designation in English",
        example = "Gas Processing Plant",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "Processing plant type designation in French (required for SONATRACH operations)",
        example = "Usine de Traitement de Gaz",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Designation is required")
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationFr;

    @Override
    public ProcessingPlantType toEntity() {
        ProcessingPlantType type = new ProcessingPlantType();
        type.setId(getId());
        type.setDesignationAr(this.designationAr);
        type.setDesignationEn(this.designationEn);
        type.setDesignationFr(this.designationFr);
        return type;
    }

    @Override
    public void updateEntity(ProcessingPlantType type) {
        if (this.designationAr != null) type.setDesignationAr(this.designationAr);
        if (this.designationEn != null) type.setDesignationEn(this.designationEn);
        if (this.designationFr != null) type.setDesignationFr(this.designationFr);
    }

    public static ProcessingPlantTypeDTO fromEntity(ProcessingPlantType type) {
        if (type == null) return null;
        
        return ProcessingPlantTypeDTO.builder()
                .id(type.getId())
                .designationAr(type.getDesignationAr())
                .designationEn(type.getDesignationEn())
                .designationFr(type.getDesignationFr())
                .build();
    }
}
