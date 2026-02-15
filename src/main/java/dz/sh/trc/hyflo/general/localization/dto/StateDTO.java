/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StateDTO
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
import dz.sh.trc.hyflo.general.localization.model.Country;
import dz.sh.trc.hyflo.general.localization.model.State;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for state/wilaya (first-level administrative division in Algeria) with country reference")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateDTO extends GenericDTO<State> {

    @Schema(
        description = "State code or wilaya number (required, e.g., 16 for Algiers)",
        example = "16",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 10
    )
    @NotBlank(message = "Code is required")
    @Size(max = 10, message = "Code must not exceed 10 characters")
    private String code;

    @Schema(
        description = "State/Wilaya name in Arabic script",
        example = "ولاية الجزائر",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "State/Wilaya name in English",
        example = "Algiers",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "State/Wilaya name in French (required for SONATRACH operations)",
        example = "Alger",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Schema(
        description = "ID of the country this state belongs to (required)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Country ID is required")
    private Long countryId;

    @Schema(
        description = "Country details (populated when fetching with country information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private CountryDTO country;

    @Override
    public State toEntity() {
        State entity = new State();
        entity.setId(this.getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);

        if (this.countryId != null) {
            Country country = new Country();
            country.setId(this.countryId);
            entity.setCountry(country);
        }

        return entity;
    }

    @Override
    public void updateEntity(State entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);

        if (this.countryId != null) {
            Country country = new Country();
            country.setId(this.countryId);
            entity.setCountry(country);
        }
    }

    public static StateDTO fromEntity(State entity) {
        if (entity == null) return null;
        return StateDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
                .country(entity.getCountry() != null ? CountryDTO.fromEntity(entity.getCountry()) : null)
                .build();
    }
}