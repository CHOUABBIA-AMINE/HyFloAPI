/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: LocalityDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-15-2026 - Added comprehensive @Schema documentation
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.localization.model.District;
import dz.sh.trc.hyflo.general.localization.model.Locality;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for locality/commune (third-level administrative division in Algeria) with district hierarchy")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocalityDTO extends GenericDTO<Locality> {

    @Schema(
        description = "Locality/Commune code or identifier (required)",
        example = "160101",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 10
    )
    @NotBlank(message = "Code is required")
    @Size(max = 10, message = "Code must not exceed 10 characters")
    private String code;

    @Schema(
        description = "Locality/Commune name in Arabic script",
        example = "بلدية باب الواد",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Locality/Commune name in English",
        example = "Bab El Oued",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "Locality/Commune name in French (required for SONATRACH operations)",
        example = "Bab El Oued",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Schema(
        description = "ID of the district/daïra this locality belongs to (required)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "District ID is required")
    private Long districtId;

    @Schema(
        description = "District/Daïra details (populated when fetching with district information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private DistrictDTO district;

    @Override
    public Locality toEntity() {
        Locality entity = new Locality();
        entity.setId(this.getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);

        if (this.districtId != null) {
            District district = new District();
            district.setId(this.districtId);
            entity.setDistrict(district);
        }

        return entity;
    }

    @Override
    public void updateEntity(Locality entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);

        if (this.districtId != null) {
            District district = new District();
            district.setId(this.districtId);
            entity.setDistrict(district);
        }
    }

    public static LocalityDTO fromEntity(Locality entity) {
        if (entity == null) return null;
        return LocalityDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .districtId(entity.getDistrict() != null ? entity.getDistrict().getId() : null)
                .district(entity.getDistrict() != null ? DistrictDTO.fromEntity(entity.getDistrict()) : null)
                .build();
    }
}