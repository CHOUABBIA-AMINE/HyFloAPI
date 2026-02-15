/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: EquipmentTypeDTO
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
import dz.sh.trc.hyflo.network.type.model.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for equipment and machinery type classification (pumps, compressors, valves, meters, heat exchangers, etc.)")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentTypeDTO extends GenericDTO<EquipmentType> {

    @Schema(
        description = "Unique code identifying the equipment type (required)",
        example = "PMP-CENT",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Schema(
        description = "Equipment type designation in Arabic script",
        example = "مضخة طرد مركزي",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Equipment type designation in English",
        example = "Centrifugal Pump",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "Equipment type designation in French (required for SONATRACH operations)",
        example = "Pompe Centrifuge",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Designation is required")
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationFr;

    @Override
    public EquipmentType toEntity() {
        EquipmentType type = new EquipmentType();
        type.setId(getId());
        type.setCode(this.code);
        type.setDesignationAr(this.designationAr);
        type.setDesignationEn(this.designationEn);
        type.setDesignationFr(this.designationFr);
        return type;
    }

    @Override
    public void updateEntity(EquipmentType type) {
        if (this.code != null) type.setCode(this.code);
        if (this.designationAr != null) type.setDesignationAr(this.designationAr);
        if (this.designationEn != null) type.setDesignationEn(this.designationEn);
        if (this.designationFr != null) type.setDesignationFr(this.designationFr);
    }

    public static EquipmentTypeDTO fromEntity(EquipmentType type) {
        if (type == null) return null;
        
        return EquipmentTypeDTO.builder()
                .id(type.getId())
                .code(type.getCode())
                .designationAr(type.getDesignationAr())
                .designationEn(type.getDesignationEn())
                .designationFr(type.getDesignationFr())
                .build();
    }
}
