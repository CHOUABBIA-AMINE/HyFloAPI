/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FacilityTypeDTO
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
import dz.sh.trc.hyflo.network.type.model.FacilityType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for facility type classification in oil & gas network (operational buildings, storage, utilities)")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityTypeDTO extends GenericDTO<FacilityType> {

    @Schema(
        description = "Unique code identifying the facility type (required)",
        example = "WHS-001",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Schema(
        description = "Facility type designation in Arabic script",
        example = "مستودع",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Facility type designation in English",
        example = "Warehouse",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "Facility type designation in French (required for SONATRACH operations)",
        example = "Entrepôt",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Designation is required")
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designationFr;

    @Override
    public FacilityType toEntity() {
        FacilityType type = new FacilityType();
        type.setId(getId());
        type.setCode(this.code);
        type.setDesignationAr(this.designationAr);
        type.setDesignationEn(this.designationEn);
        type.setDesignationFr(this.designationFr);
        return type;
    }

    @Override
    public void updateEntity(FacilityType type) {
        if (this.code != null) type.setCode(this.code);
        if (this.designationAr != null) type.setDesignationAr(this.designationAr);
        if (this.designationEn != null) type.setDesignationEn(this.designationEn);
        if (this.designationFr != null) type.setDesignationFr(this.designationFr);
    }

    public static FacilityTypeDTO fromEntity(FacilityType type) {
        if (type == null) return null;
        
        return FacilityTypeDTO.builder()
                .id(type.getId())
                .code(type.getCode())
                .designationAr(type.getDesignationAr())
                .designationEn(type.getDesignationEn())
                .designationFr(type.getDesignationFr())
                .build();
    }
}
