/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DistrictDTO
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

@Schema(description = "Data Transfer Object for district/daïra (second-level administrative division in Algeria) with state hierarchy")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistrictDTO extends GenericDTO<District> {

    @Schema(
        description = "District/Daïra code or identifier (required)",
        example = "1601",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 10
    )
    @NotBlank(message = "Code is required")
    @Size(max = 10, message = "Code must not exceed 10 characters")
    private String code;

    @Schema(
        description = "District/Daïra name in Arabic script",
        example = "دائرة باب الواد",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "District/Daïra name in English",
        example = "Bab El Oued District",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "District/Daïra name in French (required for SONATRACH operations)",
        example = "Daïra de Bab El Oued",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Schema(
        description = "ID of the state/wilaya this district belongs to (required)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "State ID is required")
    private Long stateId;

    @Schema(
        description = "State/Wilaya details (populated when fetching with state information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private StateDTO state;

    @Override
    public District toEntity() {
        District entity = new District();
        entity.setId(this.getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);

        if (this.stateId != null) {
            State state = new State();
            state.setId(this.stateId);
            entity.setState(state);
        }

        return entity;
    }

    @Override
    public void updateEntity(District entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);

        if (this.stateId != null) {
            State state = new State();
            state.setId(this.stateId);
            entity.setState(state);
        }
    }

    public static DistrictDTO fromEntity(District entity) {
        if (entity == null) return null;
        return DistrictDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .stateId(entity.getState() != null ? entity.getState().getId() : null)
                .state(entity.getState() != null ? StateDTO.fromEntity(entity.getState()) : null)
                .build();
    }
}