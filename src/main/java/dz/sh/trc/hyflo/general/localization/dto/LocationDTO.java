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
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        description = "Latitude in decimal degrees (required, range: -90 to 90)",
        example = "36.753768",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minimum = "-90",
        maximum = "90"
    )
    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;

    @Schema(
        description = "Longitude in decimal degrees (required, range: -180 to 180)",
        example = "3.058756",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minimum = "-180",
        maximum = "180"
    )
    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;

    @Schema(
        description = "Elevation/elevation in meters above sea level",
        example = "25.0",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Double elevation;

    @Schema(
        description = "ID of the locality/commune where this location is situated",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long localityId;

    @Schema(
        description = "Locality/Commune details (populated when fetching with locality information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalityDTO locality;

    @Override
    public Location toEntity() {
        Location entity = new Location();
        entity.setId(this.getId());
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setLatitude(this.latitude);
        entity.setLongitude(this.longitude);
        entity.setElevation(this.elevation);

        if (this.localityId != null) {
            Locality locality = new Locality();
            locality.setId(this.localityId);
            entity.setLocality(locality);
        }

        return entity;
    }

    @Override
    public void updateEntity(Location entity) {
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.latitude != null) entity.setLatitude(this.latitude);
        if (this.longitude != null) entity.setLongitude(this.longitude);
        if (this.elevation != null) entity.setElevation(this.elevation);

        if (this.localityId != null) {
            Locality locality = new Locality();
            locality.setId(this.localityId);
            entity.setLocality(locality);
        }
    }

    public static LocationDTO fromEntity(Location entity) {
        if (entity == null) return null;
        return LocationDTO.builder()
                .id(entity.getId())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .elevation(entity.getElevation())
                .localityId(entity.getLocality() != null ? entity.getLocality().getId() : null)
                .locality(entity.getLocality() != null ? LocalityDTO.fromEntity(entity.getLocality()) : null)
                .build();
    }
}