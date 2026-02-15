/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CoordinateDTO
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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for GPS coordinates (latitude and longitude) for geographical positioning")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoordinateDTO extends GenericDTO<Coordinate> {

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

    @Override
    public Coordinate toEntity() {
        Coordinate entity = new Coordinate();
        entity.setId(this.getId());
        entity.setLatitude(this.latitude);
        entity.setLongitude(this.longitude);
        entity.setElevation(this.elevation);
        return entity;
    }

    @Override
    public void updateEntity(Coordinate entity) {
        if (this.latitude != null) entity.setLatitude(this.latitude);
        if (this.longitude != null) entity.setLongitude(this.longitude);
        if (this.elevation != null) entity.setElevation(this.elevation);
    }

    public static CoordinateDTO fromEntity(Coordinate entity) {
        if (entity == null) return null;
        return CoordinateDTO.builder()
                .id(entity.getId())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .elevation(entity.getElevation())
                .build();
    }
}