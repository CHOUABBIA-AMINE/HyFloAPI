/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: LocationDTO
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
import dz.sh.trc.hyflo.general.localization.model.Coordinate;
import dz.sh.trc.hyflo.general.localization.model.Locality;
import dz.sh.trc.hyflo.general.localization.model.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for specific location/place with address, locality reference, and optional GPS coordinates")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationDTO extends GenericDTO<Location> {

    @Schema(
        description = "Location name or designation in Arabic script",
        example = "مقر سوناطراك الرئيسي",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Location name or designation in English",
        example = "SONATRACH Headquarters",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "Location name or designation in French (required for SONATRACH operations)",
        example = "Siège Social SONATRACH",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Schema(
        description = "Physical address in Arabic script",
        example = "الطريق الوطني رقم 11، درقانة، الجزائر العاصمة",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Arabic address must not exceed 200 characters")
    private String addressAr;

    @Schema(
        description = "Physical address in Latin script",
        example = "Route Nationale N°11, Dergana, Alger",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Latin address must not exceed 200 characters")
    private String addressLt;

    @Schema(
        description = "ID of the locality/commune where this location is situated",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long localityId;

    @Schema(
        description = "ID of the GPS coordinate for this location",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long coordinateId;

    @Schema(
        description = "Locality/Commune details (populated when fetching with locality information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalityDTO locality;

    @Schema(
        description = "GPS coordinate details (populated when fetching with coordinate information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private CoordinateDTO coordinate;

    @Override
    public Location toEntity() {
        Location entity = new Location();
        entity.setId(this.getId());
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setAddressAr(this.addressAr);
        entity.setAddressLt(this.addressLt);

        if (this.localityId != null) {
            Locality locality = new Locality();
            locality.setId(this.localityId);
            entity.setLocality(locality);
        }

        if (this.coordinateId != null) {
            Coordinate coordinate = new Coordinate();
            coordinate.setId(this.coordinateId);
            entity.setCoordinate(coordinate);
        }

        return entity;
    }

    @Override
    public void updateEntity(Location entity) {
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.addressAr != null) entity.setAddressAr(this.addressAr);
        if (this.addressLt != null) entity.setAddressLt(this.addressLt);

        if (this.localityId != null) {
            Locality locality = new Locality();
            locality.setId(this.localityId);
            entity.setLocality(locality);
        }

        if (this.coordinateId != null) {
            Coordinate coordinate = new Coordinate();
            coordinate.setId(this.coordinateId);
            entity.setCoordinate(coordinate);
        }
    }

    public static LocationDTO fromEntity(Location entity) {
        if (entity == null) return null;
        return LocationDTO.builder()
                .id(entity.getId())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .addressAr(entity.getAddressAr())
                .addressLt(entity.getAddressLt())
                .localityId(entity.getLocality() != null ? entity.getLocality().getId() : null)
                .coordinateId(entity.getCoordinate() != null ? entity.getCoordinate().getId() : null)
                .locality(entity.getLocality() != null ? LocalityDTO.fromEntity(entity.getLocality()) : null)
                .coordinate(entity.getCoordinate() != null ? CoordinateDTO.fromEntity(entity.getCoordinate()) : null)
                .build();
    }
}